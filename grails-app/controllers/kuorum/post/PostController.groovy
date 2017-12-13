package kuorum.post

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import groovy.time.TimeCategory
import kuorum.KuorumFile
import kuorum.core.FileType
import kuorum.core.exception.KuorumException
import kuorum.files.FileService
import kuorum.politician.CampaignController
import kuorum.users.CookieUUIDService
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserService
import kuorum.util.TimeZoneUtil
import kuorum.web.commands.payment.CampaignSettingsCommand
import kuorum.web.commands.payment.massMailing.post.LikePostCommand
import kuorum.web.commands.payment.post.PostContentCommand
import org.kuorum.rest.model.communication.CampaignRDTO
import org.kuorum.rest.model.communication.post.PostRDTO
import org.kuorum.rest.model.communication.post.PostRSDTO
import org.kuorum.rest.model.contact.filter.FilterRDTO
import payment.CustomerService

import javax.servlet.http.HttpServletResponse

class PostController extends CampaignController{

    KuorumUserService kuorumUserService
    FileService fileService
    CookieUUIDService cookieUUIDService
    CustomerService customerService

    def show() {
        String viewerUid = cookieUUIDService.buildUserUUID()
        KuorumUser postUser = kuorumUserService.findByAlias(params.userAlias)
        try{
            PostRSDTO postRSDTO = postService.find(postUser, Long.parseLong(params.postId),viewerUid)
            if (!postRSDTO) {
                throw new KuorumException(message(code: "post.notFound") as String)
            }
            return  [post: postRSDTO, postUser: postUser]
        }catch (Exception ignored){
            flash.error = message(code: "post.notFound")
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return false
        }
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def create() {
        return postModelSettings(new CampaignSettingsCommand(debatable:false), null)
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def remove(Long postId) {
        KuorumUser loggedUser = KuorumUser.get(springSecurityService.principal.id)
        postService.removePost(loggedUser, postId)
        render ([msg: "Post deleted"] as JSON)
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def editSettingsStep(){
        String viewerUid = cookieUUIDService.buildUserUUID()
        KuorumUser postUser = KuorumUser.get(springSecurityService.principal.id)
        PostRSDTO postRSDTO = postService.find(postUser, Long.parseLong(params.postId), viewerUid)
        return postModelSettings(new CampaignSettingsCommand(debatable:false), postRSDTO)

    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def editContentStep(){
        Long postId = Long.parseLong(params.postId)
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        def model =  postModelContent(postId)
        setPostAsDraft(user,postId)
        model
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def saveSettings(CampaignSettingsCommand command) {
        if (command.hasErrors()) {
            render view: 'create', model: postModelSettings(command, null)
            return
        }
        String nextStep = params.redirectLink
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        FilterRDTO anonymousFilter = recoverAnonymousFilterSettings(params, command)
        Long postId = params.postId?Long.parseLong(params.postId):null
        command.eventAttached=false
        Map<String, Object> result = saveCampaignSettings(user, command, postId, anonymousFilter, postService)

        //flash.message = resultPost.msg.toString()

        redirect mapping: nextStep, params: result.campaign.encodeAsLinkProperties()
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def saveContent(PostContentCommand command) {
        if (command.hasErrors()) {
            if(command.errors.getFieldError().arguments.first() == "publishOn"){
                flash.error = message(code: "post.scheduleError")
            }
            render view: 'editContentStep', model: postModelContent(Long.parseLong(params.postId), command)
            return
        }
        String nextStep = params.redirectLink
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        Long postId = params.postId?Long.parseLong(params.postId):null
        Map<String, Object> resultPost = saveAndSendPostContent(user, command, postId)
        if (resultPost.goToPaymentProcess){
            String paymentRedirect = g.createLink(mapping:"postEditContent", params:resultPost.post.encodeAsLinkProperties() )
            cookieUUIDService.setPaymentRedirect(paymentRedirect)
            redirect(mapping: "paymentStart")
        }else {
//            flash.message = resultPost.msg.toString()
            redirect mapping: nextStep, params: resultPost.post.encodeAsLinkProperties()
        }
    }

    private def postModelSettings(CampaignSettingsCommand command, PostRSDTO postRSDTO) {
        def model = modelSettings(command, postRSDTO)
        command.debatable=false
        return model
    }

    private def postModelContent(Long postId, PostContentCommand command = null) {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        PostRSDTO postRSDTO = postService.find(user, postId)
        def numberRecipients = 0;
        if(!command) {
            command = new PostContentCommand()
            command.title = postRSDTO.title
            command.body = postRSDTO.body
            command.videoPost = postRSDTO.videoUrl
            if(postRSDTO.datePublished){
                command.publishOn = postRSDTO.datePublished
            }

            if (postRSDTO.photoUrl) {
                KuorumFile kuorumFile = KuorumFile.findByUrl(postRSDTO.photoUrl)
                command.headerPictureId = kuorumFile?.id
            }
            numberRecipients = postRSDTO.newsletter?.filter?.amountOfContacts!=null?
                    postRSDTO.newsletter?.filter?.amountOfContacts:
                    contactService.getUsers(user, null).total;
        }
        [
                command: command,
                post: postRSDTO,
                numberRecipients:numberRecipients,
                status: postRSDTO.campaignStatusRSDTO
        ]
    }

    private Map<String, Object> saveAndSendPostContent(KuorumUser user, PostContentCommand command,
                                                       Long postId) {
        String msg
        PostRDTO postRDTO = convertCommandContentToPost(command, user, postId)

        PostRSDTO savedPost = null
        Boolean validSubscription = customerService.validSubscription(user);
        Boolean goToPaymentProcess = !validSubscription && command.publishOn;
        if(command.publishOn){
            // Published or Scheduled
            savedPost = postService.save(user, postRDTO, postId)

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
            savedPost = postService.save(user, postRDTO, postId)
            msg = g.message(code: 'tools.massMailing.saveDraft.advise', args: [
                    savedPost.title
            ])
        }

        [msg: msg, post: savedPost, goToPaymentProcess:goToPaymentProcess]

    }

    private PostRDTO convertCommandContentToPost(PostContentCommand command, KuorumUser user, Long postId) {
        PostRDTO postRDTO = createRDTO(user, postId, postService)
        postRDTO.title = command.title
        postRDTO.body = command.body
        if(command.sendType == 'SEND'){
            postRDTO.publishOn = Calendar.getInstance(user.getTimeZone()).time;
        }
        else {
            postRDTO.publishOn = TimeZoneUtil.convertToUserTimeZone(command.publishOn, user.timeZone)
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
                postRDTO.setPhotoUrl(null)
            }
        }
        postRDTO
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def likePost(LikePostCommand command){
        KuorumUser currentUser= springSecurityService.currentUser;
        PostRSDTO post = postService.likePost(command.postId, currentUser, command.like, command.userAlias);
        render post as JSON;
    }


    private void setPostAsDraft(KuorumUser user, Long postId){
        PostRDTO postRDTO = createRDTO(user, postId, postService)
        postRDTO.publishOn = null
        postService.save(user, postRDTO, postId)
    }
}
