package kuorum.post

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import kuorum.politician.CampaignController
import kuorum.users.KuorumUser
import kuorum.web.commands.payment.CampaignContentCommand
import kuorum.web.commands.payment.CampaignSettingsCommand
import kuorum.web.commands.payment.massMailing.post.LikePostCommand
import org.kuorum.rest.model.communication.post.PostRSDTO
import org.kuorum.rest.model.contact.filter.FilterRDTO

class PostController extends CampaignController{

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def create() {
        return postModelSettings(new CampaignSettingsCommand(debatable:false), null)
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def remove(Long campaignId) {
        removeCampaign(campaignId)
        render ([msg: "Post deleted"] as JSON)
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def editSettingsStep(){
        KuorumUser postUser = KuorumUser.get(springSecurityService.principal.id)
        PostRSDTO postRSDTO = postService.find(postUser, Long.parseLong(params.campaignId))
        return postModelSettings(new CampaignSettingsCommand(debatable:false), postRSDTO)

    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def editContentStep(){
        Long campaignId = Long.parseLong(params.campaignId)
        PostRSDTO postRSDTO = setCampaignAsDraft(campaignId, postService)
        return campaignModelContent(campaignId, postRSDTO, null, postService)
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def saveSettings(CampaignSettingsCommand command) {
        if (command.hasErrors()) {
            render view: 'create', model: postModelSettings(command, null)
            return
        }
        command.eventAttached=false
        Map<String, Object> result = saveCampaignSettings(command, params, postService)
        redirect mapping: result.nextStep.mapping, params: result.nextStep.params
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
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
        KuorumUser currentUser= springSecurityService.currentUser;
        PostRSDTO post = postService.likePost(command.postId, currentUser, command.like, command.userAlias);
        render post as JSON;
    }
}
