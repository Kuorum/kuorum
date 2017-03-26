package kuorum.post

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import groovy.time.TimeCategory
import kuorum.KuorumFile
import kuorum.core.FileType
import kuorum.files.FileService
import kuorum.users.CookieUUIDService
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserService
import kuorum.web.commands.PostCommand
import kuorum.web.commands.payment.contact.ContactFilterCommand
import kuorum.web.commands.payment.massMailing.post.LikePostCommand
import org.kuorum.rest.model.communication.post.PostRDTO
import org.kuorum.rest.model.communication.post.PostRSDTO
import org.kuorum.rest.model.contact.ContactPageRSDTO
import org.kuorum.rest.model.contact.filter.ExtendedFilterRSDTO
import org.kuorum.rest.model.contact.filter.FilterRDTO
import payment.contact.ContactService

class PostController {

    def springSecurityService

    KuorumUserService kuorumUserService
    FileService fileService
    ContactService contactService
    PostService postService
    CookieUUIDService cookieUUIDService

    def show() {
        String viewerUid = cookieUUIDService.buildUserUUID()
        KuorumUser postUser = kuorumUserService.findByAlias(params.userAlias)
        PostRSDTO postRSDTO = postService.findPost(postUser, Long.parseLong(params.postId),viewerUid)

        return  [post: postRSDTO, postUser: postUser]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def create() {
        return postModel(new PostCommand(), null)
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def save(PostCommand command) {
        if (command.hasErrors()) {
            render view: 'create', model: postModel(command, null)
            return
        }

        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        FilterRDTO anonymousFilter = recoverAnonymousFilter(params, command)
        Map<String, Object> resultPost = saveAndSendPost(user, command, null, anonymousFilter)

        flash.message = resultPost.msg.toString()

        redirect mapping: "postShow", params: resultPost.post.encodeAsLinkProperties()
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def edit(Long postId) {
        KuorumUser loggedUser = KuorumUser.get(springSecurityService.principal.id)
        PostRSDTO postRSDTO = postService.findPost(loggedUser, postId)

        PostCommand postCommand = convertPostToCommand(postRSDTO, loggedUser)
        def model = postModel(postCommand, postRSDTO)
        postCommand.filterId = postRSDTO.anonymousFilter?.id ?: null
        if (postRSDTO.anonymousFilter && !model.filters.find{it.id == postRSDTO.anonymousFilter.id}) {
            // Not found debate filter. That means that the filter is custom filter for the campaign
            ExtendedFilterRSDTO anonymousFilter = contactService.getFilter(loggedUser, postRSDTO.anonymousFilter.id)
            model.put("anonymousFilter", anonymousFilter)
        }

        render view: 'edit', model: model

    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def update(PostCommand command, Long postId) {

        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        PostRSDTO postRSDTO = postService.findPost(user, postId)

        if(command.hasErrors()){
            render view: 'edit', model: postModel(command, postRSDTO)
            return;
        }

        FilterRDTO anonymousFilter = recoverAnonymousFilter(params, command)
        Map<String, Object> resultPost = saveAndSendPost(user, command, postId, anonymousFilter)

        flash.message = resultPost.msg.toString()

        redirect mapping: "postShow", params: resultPost.post.encodeAsLinkProperties()
    }

    private def postModel(PostCommand command, PostRSDTO postRSDTO) {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        List<ExtendedFilterRSDTO> filters = contactService.getUserFilters(user)
        ContactPageRSDTO contactPageRSDTO = contactService.getUsers(user)

        [
                filters: filters,
                command: command,
                totalContacts: contactPageRSDTO.total,
                post: postRSDTO
        ]
    }

    private FilterRDTO recoverAnonymousFilter(params, PostCommand command) {
        ContactFilterCommand filterCommand = (ContactFilterCommand) bindData(new ContactFilterCommand(), params)
        contactService.transformCommand(filterCommand, "Custom filter for ${command.title}")
    }

    private Map<String, Object> saveAndSendPost(KuorumUser user, PostCommand command,
                                                  Long postId = null, FilterRDTO anonymousFilter = null) {
        String msg
        PostRDTO postRDTO = convertCommandToPost(command, user, anonymousFilter)

        PostRSDTO savedPost = new PostRSDTO()
        if(command.publishOn){
            // Published or Scheduled
            savedPost = postService.savePost(user, postRDTO, postId)

            Date date = new Date()
            Date after5minutes = new Date()

            // If Scheduled in the next 5 minutes, consider published
            use (TimeCategory){
                after5minutes = date + 5.minutes
            }

            if(command.publishOn < after5minutes){
                // Shceduled over 5 minutes
                msg = g.message(code: 'tools.massMailing.schedule.advise', args: [
                        savedPost.title,
                        g.formatDate(date: command.publishOn, type: "datetime", style: "SHORT")
                ])
            }
            else {
                // Published or scheduled within 5 minutes
                msg = g.message(code: 'tools.massMailing.schedule.advise', args: [
                        savedPost.title,
                        g.formatDate(date: command.publishOn, type: "datetime", style: "SHORT")
                ])
            }
        }
        else {
            // Draft
            savedPost = postService.savePost(user, postRDTO, postId)
            msg = g.message(code: 'tools.massMailing.saveDraft.advise', args: [
                    savedPost.title
            ])
        }


        [msg: msg, post: savedPost]

    }

    private PostRDTO convertCommandToPost(PostCommand command, KuorumUser user, FilterRDTO anonymousFilter) {
        PostRDTO postRDTO = new PostRDTO()
        postRDTO.body = command.body
        postRDTO.title = command.title
        postRDTO.publishOn = command.publishOn

        // TAGS
        postRDTO.triggeredTags = command.tags

        // FILTERS
        if (command.filterEdited) {
            postRDTO.setAnonymousFilter(anonymousFilter)
        } else {
            postRDTO.setFilterId(command.filterId)
        }

        // Multimedia URL
        if (command.fileType == FileType.IMAGE.toString() && command.headerPictureId) {
            // Save image
            KuorumFile picture = KuorumFile.get(command.headerPictureId)
            picture = fileService.convertTemporalToFinalFile(picture)
            fileService.deleteTemporalFiles(user)
            postRDTO.setPhotoUrl(picture.getUrl())

            // Remove video
            postRDTO.setVideoUrl(null)
        } else if (command.fileType == FileType.YOUTUBE.toString() && command.videoPost) {
            // Save video
            KuorumFile urlYoutubeFile = fileService.createYoutubeKuorumFile(command.videoPost, user)
            postRDTO.setVideoUrl(urlYoutubeFile?.url)

            // Remove image
            if (command.headerPictureId) {
                KuorumFile picture = KuorumFile.get(command.headerPictureId)
                fileService.deleteKuorumFile(picture)
                command.setHeaderPictureId(null)
            }
        }

        postRDTO
    }

    private PostCommand convertPostToCommand(PostRSDTO postRSDTO, KuorumUser kuorumUser){

        PostCommand postCommand = new PostCommand()
        postCommand.body = postRSDTO.body
        postCommand.title = postRSDTO.title
        postCommand.publishOn = postRSDTO.datePublished

        // Tags
        if(postRSDTO.triggeredTags){
            postCommand.setTags(postRSDTO.triggeredTags)
        }

        // Multimedia
        if (postRSDTO.photoUrl){
            KuorumFile kuorumFile = KuorumFile.findByUrl(postRSDTO.photoUrl)
            postCommand.headerPictureId = kuorumFile.id
        }
        else if (postRSDTO.videoUrl?.contains("youtube")){
            postCommand.videoPost = postRSDTO.videoUrl
        }

        postCommand
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def likePost(LikePostCommand command){
        KuorumUser currentUser= springSecurityService.currentUser;
        PostRSDTO post = postService.likePost(command.postId, currentUser, command.like, command.userAlias);
        render post as JSON;
    }

}
