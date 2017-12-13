package kuorum.debate

import grails.plugin.springsecurity.annotation.Secured
import kuorum.campaign.Event
import kuorum.campaign.EventRegistration
import kuorum.files.FileService
import kuorum.users.CookieUUIDService
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserService
import kuorum.web.commands.payment.CampaignSettingsCommand
import net.sf.json.JSON
import org.kuorum.rest.model.communication.CampaignRDTO
import org.kuorum.rest.model.communication.CampaignRSDTO
import org.kuorum.rest.model.communication.debate.DebateRSDTO
import org.kuorum.rest.model.communication.event.EventRDTO
import org.kuorum.rest.model.communication.post.PostRSDTO
import org.kuorum.rest.model.contact.ContactPageRSDTO
import org.kuorum.rest.model.contact.filter.ExtendedFilterRSDTO
import org.kuorum.rest.model.contact.filter.FilterRDTO
import payment.CustomerService
import payment.campaign.CampaignService
import payment.campaign.DebateService
import payment.campaign.ProposalService
import payment.contact.ContactService
import kuorum.politician.CampaignController
import  kuorum.web.commands.payment.event.EventCommand

class EventController extends CampaignController{

    KuorumUserService kuorumUserService
    FileService fileService
    ProposalService proposalService
    CookieUUIDService cookieUUIDService
    CustomerService customerService

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def create() {
        return modelSettings(new CampaignSettingsCommand())
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def saveSettings(CampaignSettingsCommand command){
        if (command.hasErrors()) {
            render view: 'create', model: modelSettings(command, null)
            return
        }
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        FilterRDTO anonymousFilter = recoverAnonymousFilterSettings(params, command)

        CampaignService campaignService = null;
        if (command.debatable){
            campaignService = debateService
        }else{
            campaignService = postService
        }
        command.eventAttached=true
        Map<String, Object> result = saveCampaignSettings(user, command, null, anonymousFilter, campaignService)

        //flash.message = resultPost.msg.toString()

        // Al crear por primera vez un evento, no tiene sentido saltarse el paso de definir el evento.
        String nextStep = ''
        if (result.campaign instanceof DebateRSDTO){
            nextStep = 'debateEditEvent'
        }else if (result.campaign instanceof PostRSDTO){
            nextStep = 'postEditEvent'
        }
        redirect mapping: nextStep, params: result.campaign.encodeAsLinkProperties()
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def editEvent(){
        modelEditEvent(params)
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def updateEvent(EventCommand command ){
        if (command.hasErrors()) {
            render view: 'editEvent', model: modelEditEvent(params, command)
            return
        }
        String nextStep = params.redirectLink
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        CampaignRSDTO campaignRSDTO = findCampaign(params)
        CampaignService campaignService = null;
        CampaignRDTO campaignRDTO = null;
        if (campaignRSDTO instanceof DebateRSDTO){
            campaignService = debateService
        }else{
            campaignService = postService
        }
        campaignRDTO = campaignService.map(campaignRSDTO)
        updateEventRDTO(campaignRDTO, command)
        campaignRSDTO = campaignService.save(user,campaignRDTO,campaignRSDTO.id)

        //flash.message = resultPost.msg.toString()

        redirect mapping: nextStep, params: campaignRSDTO.encodeAsLinkProperties()
    }

    private def updateEventRDTO(CampaignRDTO campaignRDTO, EventCommand eventCommand) {
        if (!campaignRDTO.event){
            campaignRDTO.event = new EventRDTO()
        }
        campaignRDTO.event.address = eventCommand.address
        campaignRDTO.event.eventDate = eventCommand.eventDate
        campaignRDTO.event.latitude = eventCommand.latitude
        campaignRDTO.event.longitude = eventCommand.longitude
        campaignRDTO.event.localName = eventCommand.localName
        campaignRDTO.event.zoom = eventCommand.zoom
    }

    private def modelEditEvent(def params, EventCommand command = null ){
        CampaignRSDTO campaignRSDTO = findCampaign(params)
        if (!command){
            command = new EventCommand()
            if (campaignRSDTO.event){
                command.address = campaignRSDTO.event.address
                command.localName = campaignRSDTO.event.localName
                command.longitude = campaignRSDTO.event.longitude
                command.latitude = campaignRSDTO.event.latitude
                command.zoom = campaignRSDTO.event.zoom
                command.eventDate = campaignRSDTO.event.eventDate
            }
        }
        [
                campaign:campaignRSDTO,
                command:command
        ]
    }

    private CampaignRSDTO findCampaign(def params){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        CampaignService campaignService
        Long campaignId;
        if (params.postId){
            campaignId = Long.parseLong(params.postId)
            campaignService = postService
        }else if (params.debateId){
            campaignId = Long.parseLong(params.debateId)
            campaignService = debateService

        }
        campaignService.find(user, campaignId)
    }


    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def confirmAssistance(Long debateId){
        KuorumUser user = springSecurityService.currentUser
        Event event = Event.findByDebateId(debateId)
        if (!event){
            render ([success:false, error:"Debate not exists"]) as JSON
            return;
        }
        EventRegistration eventRegistration = EventRegistration.findByDebateIdAndUserId(debateId, user.getId())
        if (eventRegistration){
            render ([success:true, error:"Already register"]) as JSON
        }else{
            eventRegistration = new EventRegistration()
            eventRegistration.debateId = debateId
            eventRegistration.postId = 0
            eventRegistration.alias = user.alias
            eventRegistration.dateCreated = new Date();
            eventRegistration.userId = user.id
            eventRegistration.name = user.fullName
            eventRegistration.email = user.email
            eventRegistration.language = user.language
            if (eventRegistration.save()){
                render ([success:true, error:""]) as JSON
            }else{
                render ([success:false, error:"Error saving registration"]) as JSON
            }


        }
    }
}
