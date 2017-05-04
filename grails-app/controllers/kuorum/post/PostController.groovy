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
import kuorum.web.commands.payment.contact.ContactFilterCommand
import kuorum.web.commands.payment.massMailing.post.LikePostCommand
import kuorum.web.commands.payment.post.PostCommand
import kuorum.web.commands.payment.post.PostContentCommand
import kuorum.web.commands.payment.post.PostSettingsCommand
import org.kuorum.rest.model.communication.post.PostRDTO
import org.kuorum.rest.model.communication.post.PostRSDTO
import org.kuorum.rest.model.contact.ContactPageRSDTO
import org.kuorum.rest.model.contact.filter.ExtendedFilterRSDTO
import org.kuorum.rest.model.contact.filter.FilterRDTO
import org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO
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
        return postModelSettings(new PostSettingsCommand(), null)
    }

    def editSettingsStep(){
        String viewerUid = cookieUUIDService.buildUserUUID()
        KuorumUser postUser = KuorumUser.get(springSecurityService.principal.id)
        PostRSDTO postRSDTO = postService.findPost(postUser, Long.parseLong(params.postId), viewerUid)

        return postModelSettings(new PostSettingsCommand(), postRSDTO)

    }

    def editContentStep(){
        return postModelContent(Long.parseLong(params.postId))
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

    def saveSettings(PostSettingsCommand command) {
        if (command.hasErrors()) {
            render view: 'create', model: postModelSettings(command, null)
            return
        }
        String nextStep = params.redirectLink
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        FilterRDTO anonymousFilter = recoverAnonymousFilterSettings(params, command)
        Long postId = params.postId?Long.parseLong(params.postId):null
        Map<String, Object> resultPost = saveAndSendPostSettings(user, command, postId, anonymousFilter)

        //flash.message = resultPost.msg.toString()

        redirect mapping: nextStep, params: [postId: resultPost.post.id]
    }

    def saveContent(PostContentCommand command) {
        if (command.hasErrors()) {
            render view: 'editContentStep', model: postModelContent(Long.parseLong(params.postId), command)
            return
        }
        String nextStep = params.redirectLink
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        Long postId = params.postId?Long.parseLong(params.postId):null
        Map<String, Object> resultPost = saveAndSendPostContent(user, command, postId)

        //flash.message = resultPost.msg.toString()
        if (CampaignStatusRSDTO.DRAFT.equals(((PostRSDTO)resultPost.post).newsletter.status)){
            redirect mapping: nextStep, params: [postId: resultPost.post.id]
        }else{
            redirect mapping: "postShow",  params:resultPost.post.encodeAsLinkProperties()
        }
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

        redirect mapping: "postShow", params: [postId: resultPost.post.id]
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

    private def postModelSettings(PostSettingsCommand command, PostRSDTO postRSDTO) {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        List<ExtendedFilterRSDTO> filters = contactService.getUserFilters(user)
        ContactPageRSDTO contactPageRSDTO = contactService.getUsers(user)

        if(postRSDTO){
            command.campaignName = postRSDTO.name
            command.tags = postRSDTO.triggeredTags
            command.filterId = postRSDTO.newsletter?.filter?.id
            if(postRSDTO.newsletter.filter && !filters.find{it.id==postRSDTO.newsletter.filter.id}){
                ExtendedFilterRSDTO anonymousFilter = contactService.getFilter(user, postRSDTO.newsletter.filter.id)
                filters.add(anonymousFilter)
            }
        }


        [
                filters: filters,
                command: command,
                totalContacts: contactPageRSDTO.total,
                post: postRSDTO
        ]
    }

    private def postModelContent(Long postId, PostContentCommand command = null) {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        PostRSDTO postRSDTO = postService.findPost(user, postId)
        def numberRecipients = 0;
        if(!command) {
            command = new PostContentCommand()
            command.title = postRSDTO.title
            command.body = postRSDTO.body
            command.videoPost = postRSDTO.videoUrl

            if (postRSDTO.photoUrl) {
                KuorumFile kuorumFile = KuorumFile.findByUrl(postRSDTO.photoUrl)
                command.headerPictureId = kuorumFile?.id
            }
            numberRecipients = postRSDTO.newsletter?.filter?.amountOfContacts?:contactService.getUsers(user, null).total;
        }
        [
                command: command,
                post: postRSDTO,
                numberRecipients:numberRecipients
        ]
    }

    private FilterRDTO recoverAnonymousFilter(params, PostCommand command) {
        ContactFilterCommand filterCommand = (ContactFilterCommand) bindData(new ContactFilterCommand(), params)
        contactService.transformCommand(filterCommand, "Custom filter for ${command.title}")
    }

    private FilterRDTO recoverAnonymousFilterSettings(params, PostSettingsCommand command) {
        ContactFilterCommand filterCommand = (ContactFilterCommand) bindData(new ContactFilterCommand(), params)
        contactService.transformCommand(filterCommand, "Custom filter for ${command.campaignName}")
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

            if(command.publishOn > after5minutes){
                // Shceduled over 5 minutes
                msg = g.message(code: 'tools.massMailing.schedule.advise', args: [
                        savedPost.title,
                        g.formatDate(date: command.publishOn, type: "datetime", style: "SHORT")
                ])
            }
            else {
                // Published or scheduled within 5 minutes
                msg = g.message(code: 'tools.massMailing.saved.advise', args: [
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
    private Map<String, Object> saveAndSendPostSettings(KuorumUser user, PostSettingsCommand command,
                                                        Long postId = null, FilterRDTO anonymousFilter = null) {
        String msg
        PostRDTO postRDTO = convertCommandSettingsToPost(command, user, anonymousFilter, postId)

        PostRSDTO savedPost = postService.savePost(user, postRDTO, postId)
        msg = g.message(code: 'tools.massMailing.saveDraft.advise', args: [savedPost.title])

        [msg: msg, post: savedPost]

    }
    private Map<String, Object> saveAndSendPostContent(KuorumUser user, PostContentCommand command,
                                                       Long postId) {
        String msg
        PostRDTO postRDTO = convertCommandContentToPost(command, user, postId)

        PostRSDTO savedPost = null
        if(command.publishOn){
            // Published or Scheduled
            savedPost = postService.savePost(user, postRDTO, postId)

            Date date = new Date()
            Date after5minutes = new Date()

            // If Scheduled in the next 5 minutes, consider published
            use (TimeCategory){
                after5minutes = date + 5.minutes
            }

            if(command.publishOn > after5minutes){
                // Shceduled over 5 minutes
                msg = g.message(code: 'tools.massMailing.schedule.advise', args: [
                        savedPost.title,
                        g.formatDate(date: command.publishOn, type: "datetime", style: "SHORT")
                ])
            }
            else {
                // Published or scheduled within 5 minutes
                msg = g.message(code: 'tools.massMailing.saved.advise', args: [
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

    private PostRDTO convertCommandSettingsToPost(PostSettingsCommand command, KuorumUser user, FilterRDTO anonymousFilter, Long postId) {
        PostRDTO postRDTO = new PostRDTO()
        postRDTO.name = command.campaignName
        postRDTO.triggeredTags = command.tags

        if (command.filterEdited) {
            postRDTO.setAnonymousFilter(anonymousFilter)
        } else {
            postRDTO.setFilterId(command.filterId)
        }

        if(postId){
            PostRSDTO postRSDTO = postService.findPost(user, postId)
            postRDTO.title = postRSDTO.title
            postRDTO.body = postRSDTO.body
            postRDTO.photoUrl = postRSDTO.photoUrl
            postRDTO.videoUrl = postRSDTO.videoUrl
        }

        postRDTO
    }

    private PostRDTO convertCommandContentToPost(PostContentCommand command, KuorumUser user, Long postId) {
        PostRDTO postRDTO = new PostRDTO()
        postRDTO.title = command.title
        postRDTO.body = command.body
        postRDTO.publishOn = command.publishOn

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

        if(postId){
            PostRSDTO postRSDTO = postService.findPost(user, postId)
            postRDTO.name = postRSDTO.name
            postRDTO.triggeredTags = postRSDTO.triggeredTags
            postRDTO.filterId = postRSDTO.newsletter?.filter?.id
            postRDTO.anonymousFilter = postRSDTO.anonymousFilter
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
        else if (postRSDTO.videoUrl){
            postCommand.videoPost = postRSDTO.videoUrl
            postCommand.fileType = FileType.YOUTUBE.toString();
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
