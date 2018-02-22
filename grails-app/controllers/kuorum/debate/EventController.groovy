package kuorum.debate

import grails.plugin.springsecurity.annotation.Secured
import kuorum.politician.CampaignController
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserService
import kuorum.web.commands.payment.CampaignSettingsCommand
import kuorum.web.commands.payment.event.EventCommand
import net.sf.json.JSON
import org.kuorum.rest.model.communication.CampaignRDTO
import org.kuorum.rest.model.communication.CampaignRSDTO
import org.kuorum.rest.model.communication.debate.DebateRSDTO
import org.kuorum.rest.model.communication.event.EventRDTO
import org.kuorum.rest.model.communication.event.EventRegistrationRSDTO
import org.kuorum.rest.model.communication.post.PostRSDTO
import org.kuorum.rest.model.contact.ContactRSDTO
import org.kuorum.rest.model.contact.filter.FilterRDTO
import payment.campaign.CampaignCreatorService
import payment.campaign.event.EventService

class EventController extends CampaignController{

    EventService eventService

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def create() {
        return eventModelSettings(new CampaignSettingsCommand(), null)
    }

    private def eventModelSettings(CampaignSettingsCommand command, DebateRSDTO debateRSDTO) {
        def model = modelSettings(command, debateRSDTO)
        model.options =[debatable:true, endDate:false]
        return model
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def saveSettings(CampaignSettingsCommand command){
        if (command.hasErrors()) {
            render view: 'create', model: eventModelSettings(command, null)
            return
        }
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        FilterRDTO anonymousFilter = recoverAnonymousFilterSettings(params, command)

        CampaignCreatorService campaignService = null;
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
            flash.error = g.message(code:'tools.massMailing.event.location.error')
            render view: 'editEvent', model: modelEditEvent(params, command)
            return
        }
        String nextStep = params.redirectLink
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        CampaignRSDTO campaignRSDTO = findCampaign(params)
        CampaignCreatorService campaignService = null;
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
        campaignRDTO.event.capacity = eventCommand.capacity
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
                command.capacity = campaignRSDTO.event.capacity
            }
        }
        [
                campaign:campaignRSDTO,
                command:command
        ]
    }

    private CampaignRSDTO findCampaign(def params){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        Long campaignId = Long.parseLong(params.campaignId)
        campaignService.find(user, campaignId)
    }


    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def bookTicket(Long eventId){
        KuorumUser assistant = springSecurityService.currentUser
        EventRegistrationRSDTO eventRegistration = eventService.addAssistant(params.userAlias, eventId, assistant)
        if (eventRegistration){
            render ([success:true, error:"", eventRegistration:eventRegistration]) as JSON
        }else{
            render ([success:false, error:"Error saving registration"]) as JSON
        }
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def checkIn(Long eventId){
        KuorumUser user = springSecurityService.currentUser
        String hash = params.hash
        Long contactId = Long.parseLong(params.contactId)
        EventRegistrationRSDTO eventRegistration = eventService.checkIn(contactId, eventId, user, hash)
        if (eventRegistration){
            ContactRSDTO contact = contactService.getContact(user, contactId)
            [event:eventRegistration.event, eventRegistration:eventRegistration, contact:contact]
        }else{
            render view: "/error/notFound"
        }
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def sendReport(Long eventId) {
        KuorumUser user = springSecurityService.currentUser
        Boolean checkList = params.checkList?Boolean.parseBoolean(params.checkList):false
        eventService.sendReport(user, eventId,checkList)
        render ([success:"success"] as grails.converters.JSON)
    }

}
