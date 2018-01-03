package kuorum.post

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import kuorum.core.exception.KuorumException
import kuorum.politician.CampaignController
import kuorum.users.CookieUUIDService
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserService
import kuorum.web.commands.payment.CampaignContentCommand
import kuorum.web.commands.payment.CampaignSettingsCommand
import kuorum.web.commands.payment.massMailing.post.LikePostCommand
import org.kuorum.rest.model.communication.event.EventRegistrationRSDTO
import org.kuorum.rest.model.communication.post.PostRSDTO
import org.kuorum.rest.model.contact.filter.FilterRDTO
import payment.campaign.event.EventService

import javax.servlet.http.HttpServletResponse

class PostController extends CampaignController{

    KuorumUserService kuorumUserService
    CookieUUIDService cookieUUIDService
    EventService eventService

    def show() {
        String viewerUid = cookieUUIDService.buildUserUUID()
        KuorumUser postUser = kuorumUserService.findByAlias(params.userAlias)
        try{
            PostRSDTO postRSDTO = postService.find(postUser, Long.parseLong(params.postId),viewerUid)
            if (!postRSDTO) {
                throw new KuorumException(message(code: "post.notFound") as String)
            }
            def model = [post: postRSDTO, postUser: postUser]
            if (postRSDTO.event && springSecurityService.isLoggedIn()){
                KuorumUser userLogged = springSecurityService.currentUser
                EventRegistrationRSDTO eventRegistration = eventService.findAssistant(postUser.id.toString(),postRSDTO.event.id, userLogged)
                model.put("eventRegistration", eventRegistration)
            }
            return  model
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
        PostRSDTO postRSDTO = setCampaignAsDraft(user,postId, postService)
        return campaignModelContent(postId, postRSDTO, null, postService)
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
    def saveContent(CampaignContentCommand command) {
        if (command.hasErrors()) {
            if(command.errors.getFieldError().arguments.first() == "publishOn"){
                flash.error = message(code: "post.scheduleError")
            }
            render view: 'editContentStep', model: campaignModelContent(Long.parseLong(params.postId), null, command, postService)
            return
        }
        String nextStep = params.redirectLink
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        Long postId = params.postId?Long.parseLong(params.postId):null
        Map<String, Object> resultPost = saveAndSendCampaignContent(user, command, postId, postService)
        if (resultPost.goToPaymentProcess){
            String paymentRedirect = g.createLink(mapping:"postEditContent", params:resultPost.campaign.encodeAsLinkProperties() )
            cookieUUIDService.setPaymentRedirect(paymentRedirect)
            redirect(mapping: "paymentStart")
        }else {
//            flash.message = resultPost.msg.toString()
            redirect mapping: nextStep, params: resultPost.campaign.encodeAsLinkProperties()
        }
    }

    private def postModelSettings(CampaignSettingsCommand command, PostRSDTO postRSDTO) {
        def model = modelSettings(command, postRSDTO)
        command.debatable=false
        return model
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def likePost(LikePostCommand command){
        KuorumUser currentUser= springSecurityService.currentUser;
        PostRSDTO post = postService.likePost(command.postId, currentUser, command.like, command.userAlias);
        render post as JSON;
    }
}
