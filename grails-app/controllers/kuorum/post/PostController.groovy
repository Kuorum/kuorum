package kuorum.post

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import kuorum.politician.CampaignController
import kuorum.users.KuorumUser
import kuorum.web.commands.payment.CampaignContentCommand
import kuorum.web.commands.payment.CampaignSettingsCommand
import kuorum.web.commands.payment.massMailing.post.LikePostCommand
import org.kuorum.rest.model.communication.post.PostRSDTO

class PostController extends CampaignController{

    @Secured(['ROLE_CAMPAIGN_POST','ROLE_ADMIN'])
    def create() {
        return postModelSettings(new CampaignSettingsCommand(debatable:false), null)
    }

    @Secured(['ROLE_CAMPAIGN_POST','ROLE_CAMPAIGN_EVENT','ROLE_ADMIN'])
    def remove(Long campaignId) {
        removeCampaign(campaignId)
        render ([msg: "Post deleted"] as JSON)
    }

    @Secured(['ROLE_CAMPAIGN_POST','ROLE_CAMPAIGN_EVENT','ROLE_ADMIN'])
    def editSettingsStep(){
        KuorumUser postUser = KuorumUser.get(springSecurityService.principal.id)
        PostRSDTO postRSDTO = postService.find(postUser, Long.parseLong(params.campaignId))
        return postModelSettings(new CampaignSettingsCommand(debatable:false), postRSDTO)

    }

    @Secured(['ROLE_CAMPAIGN_POST','ROLE_CAMPAIGN_EVENT','ROLE_ADMIN'])
    def editContentStep(){
        Long campaignId = Long.parseLong(params.campaignId)
        PostRSDTO postRSDTO = setCampaignAsDraft(campaignId, postService)
        return campaignModelContent(campaignId, postRSDTO, null, postService)
    }

    @Secured(['ROLE_CAMPAIGN_POST','ROLE_CAMPAIGN_EVENT','ROLE_ADMIN'])
    def saveSettings(CampaignSettingsCommand command) {
        if (command.hasErrors()) {
            render view: 'create', model: postModelSettings(command, null)
            return
        }
        command.eventAttached=false
        Map<String, Object> result = saveCampaignSettings(command, params, postService)
        redirect mapping: result.nextStep.mapping, params: result.nextStep.params
    }

    @Secured(['ROLE_CAMPAIGN_POST','ROLE_CAMPAIGN_EVENT','ROLE_ADMIN'])
    def saveContent(CampaignContentCommand command) {
        if (command.hasErrors()) {
            if(command.errors.getFieldError().arguments.first() == "publishOn"){
                flash.error = message(code: "post.scheduleError")
            }
            render view: 'editContentStep', model: campaignModelContent(Long.parseLong(params.campaignId), null, command, postService)
            return
        }
        Long campaignId = params.campaignId?Long.parseLong(params.campaignId):null
        Map<String, Object> result = saveAndSendCampaignContent(command, campaignId, postService)
        redirect mapping: result.nextStep.mapping, params: result.nextStep.params
    }

    private def postModelSettings(CampaignSettingsCommand command, PostRSDTO postRSDTO) {
        def model = modelSettings(command, postRSDTO)
        command.debatable=false
        model.options =[debatable:postRSDTO?.event?true:false, endDate:false]
        return model
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def likePost(LikePostCommand command){
        if (command.hasErrors()){
            render "No correct data"
        }
        KuorumUser currentUser= springSecurityService.currentUser;
        PostRSDTO post = postService.likePost(command.postId, currentUser, command.like, command.postUserId);
        render post as JSON;
    }
}
