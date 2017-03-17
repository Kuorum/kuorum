package kuorum.post

import grails.plugin.springsecurity.annotation.Secured
import kuorum.KuorumFile
import kuorum.core.FileType
import kuorum.files.FileService
import kuorum.users.CookieUUIDService
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserService
import kuorum.web.commands.PostCommand
import kuorum.web.commands.payment.contact.ContactFilterCommand
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
        KuorumUser postUser = kuorumUserService.findByAlias(params.userAlias)
        PostRSDTO postRSDTO = postService.findPost(postUser, Long.parseLong(params.postId))

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

    private def postModel(PostCommand command, Long postId) {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        List<ExtendedFilterRSDTO> filters = contactService.getUserFilters(user)
        ContactPageRSDTO contactPageRSDTO = contactService.getUsers(user)

        [
                filters: filters,
                command: command,
                totalContacts: contactPageRSDTO.total,
                postId: postId
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

        PostRSDTO savedPost = postService.savePost(user, postRDTO)
        msg = 'Posted'

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

}
