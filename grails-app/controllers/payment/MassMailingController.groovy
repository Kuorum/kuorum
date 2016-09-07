package payment

import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import kuorum.KuorumFile
import kuorum.files.FileService
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserService
import kuorum.web.commands.payment.massMailing.MassMailingCommand
import org.kuorum.rest.model.contact.ContactPageRSDTO
import org.kuorum.rest.model.contact.ContactRDTO
import org.kuorum.rest.model.contact.ContactRSDTO
import org.kuorum.rest.model.contact.filter.ConditionFieldTypeRDTO
import org.kuorum.rest.model.contact.filter.ConditionOperatorTypeRDTO
import org.kuorum.rest.model.contact.filter.ConditionRDTO
import org.kuorum.rest.model.contact.filter.ExtendedFilterRSDTO
import org.kuorum.rest.model.contact.filter.FilterRDTO
import org.kuorum.rest.model.contact.filter.OperatorTypeRDTO
import org.kuorum.rest.model.notification.campaign.CampaignRQDTO
import org.kuorum.rest.model.notification.campaign.CampaignRSDTO
import org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO
import org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatsByCampaignPageRSDTO
import payment.campaign.MassMailingService
import payment.contact.ContactService

@Secured("ROLE_POLITICIAN")
class MassMailingController {

    SpringSecurityService springSecurityService

    ContactService contactService;

    KuorumUserService kuorumUserService;

    FileService fileService;

    MassMailingService massMailingService;

    def index(){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
//        ContactPageRSDTO concatPage = contactService.getUsers(user)
//        if (concatPage.total <= 0) {
//            render view: "/dashboard/payment/paymentNoContactsDashboard", model: [:]
//            return;
//        }
        List<CampaignRSDTO> campaigns = massMailingService.findCampaigns(user)

        [campaigns:campaigns]
    }

    def createMassMailing(){
        KuorumUser loggedUser = springSecurityService.currentUser
        modelMassMailing(loggedUser, new MassMailingCommand(), params.testFilter)
    }

    private def modelMassMailing(KuorumUser user, MassMailingCommand command, def testFilterParam){
        List<ExtendedFilterRSDTO> filters = contactService.getUserFilters(user)
        ExtendedFilterRSDTO testFilter = fakeTestFilter(filters, user)
        if (testFilterParam){
//            ExtendedFilterRSDTO testFilter = createTestFilter(filters, loggedUser)
            command.filterId = testFilter.id
        }
        ContactPageRSDTO contactPageRSDTO = contactService.getUsers(user)

        [filters:filters, command:command, totalContacts:contactPageRSDTO.total]
    }

    private ExtendedFilterRSDTO fakeTestFilter(List<ExtendedFilterRSDTO> filters , KuorumUser user){
        FilterRDTO filter = createTestFilter(user)
        ExtendedFilterRSDTO filterTest = new ExtendedFilterRSDTO(filter.class.declaredFields.findAll { !it.synthetic }.collectEntries {
            [ (it.name):filter."$it.name" ]
        })
        filterTest.id = -1L;
        filterTest.amountOfContacts=1;
        filters.add(filterTest)
        filterTest
    }

    private ExtendedFilterRSDTO createTestFilter(List<ExtendedFilterRSDTO> filters , KuorumUser user){
        ExtendedFilterRSDTO filterTest = filters.find{it.name=~ "-TEST-"}
        if (!filterTest){
            FilterRDTO filter = new FilterRDTO();
            filter.setOperator(OperatorTypeRDTO.AND)
            filter.setName(g.message(code: 'tools.massMailing.fields.filter.to.testFilterName'))
            filter.filterConditions = []
            filter.filterConditions.add(new ConditionRDTO([
                    field:ConditionFieldTypeRDTO.EMAIL,
                    operator: ConditionOperatorTypeRDTO.EQUALS,
                    value:user.email
            ]))
            filterTest = contactService.createFilter(user, filter)
            filters.add(filterTest)
        }
        if (filterTest.amountOfContacts==0){
            List<ContactRDTO> contactRSDTOs = []
            contactRSDTOs.add(new ContactRDTO(name:user.name, email: user.email))
            contactService.addBulkContacts(user, contactRSDTOs)
            filterTest.amountOfContacts++ //Chapu por asincronia y por no volver a llamar
        }
        return filterTest
    }

    private FilterRDTO createTestFilter(KuorumUser user){
        FilterRDTO filter = new FilterRDTO();
        filter.setOperator(OperatorTypeRDTO.AND)
        filter.setName(g.message(code: 'tools.massMailing.fields.filter.to.testFilterName'))
        filter.filterConditions = []
        filter.filterConditions.add(new ConditionRDTO([
                field:ConditionFieldTypeRDTO.EMAIL,
                operator: ConditionOperatorTypeRDTO.EQUALS,
                value:user.email
        ]))
        return filter
    }

    def saveMassMailing(MassMailingCommand command){
        KuorumUser loggedUser = springSecurityService.currentUser
        if (command.hasErrors()){
            render view: 'createMassMailing', model: modelMassMailing(loggedUser, command, command.filterId<0)
            return;
        }
        flash.message = saveAndSendCampaign(loggedUser, command)
        redirect mapping:'politicianMassMailing'
    }

    def showCampaign(Long campaignId){
        KuorumUser loggedUser = springSecurityService.currentUser
        CampaignRSDTO campaignRSDTO = massMailingService.findCampaign(loggedUser, campaignId)
        if (campaignRSDTO.status == CampaignStatusRSDTO.DRAFT || campaignRSDTO.status == CampaignStatusRSDTO.SCHEDULED ){
            MassMailingCommand command = new MassMailingCommand()
            command.filterId = campaignRSDTO.filter?.id ?: null
            KuorumFile kuorumFile = KuorumFile.findByUrl(campaignRSDTO.imageUrl)
            command.headerPictureId = kuorumFile.id
            command.scheduled = campaignRSDTO.sentOn
            command.subject = campaignRSDTO.subject
            command.text = campaignRSDTO.body

            render view: 'createMassMailing', model: modelMassMailing(loggedUser, command, false)
        }else{
            TrackingMailStatsByCampaignPageRSDTO trackingPage = massMailingService.findTrackingMails(loggedUser, campaignId)
            render view: 'showCampaign', model: [campaign: campaignRSDTO, trackingPage:trackingPage]
        }
    }

    def showTrackingMails(Long campaignId){
        KuorumUser loggedUser = springSecurityService.currentUser
        Integer page = params.page?Integer.parseInt(params.page):0;
        Integer size = params.size?Integer.parseInt(params.size):10;
        TrackingMailStatsByCampaignPageRSDTO trackingPage = massMailingService.findTrackingMails(loggedUser, campaignId, page, size)
        render template: '/massMailing/campaignTabs/campaignRecipeints', model: [trackingPage:trackingPage, campaignId:campaignId]
    }

    def updateCampaign(MassMailingCommand command){
        KuorumUser loggedUser = springSecurityService.currentUser
        if (command.hasErrors()){
            render view: 'createMassMailing', model: modelMassMailing(loggedUser, command, command.filterId<0)
            return;
        }
        Long campaignId = Long.parseLong(params.campaignId)
        flash.message = saveAndSendCampaign(loggedUser, command, campaignId)
        redirect mapping:'politicianMassMailing'
    }

    def removeCampaign(Long campaignId){
        KuorumUser loggedUser = springSecurityService.currentUser
        massMailingService.removeCampaign(loggedUser, campaignId)
        render ([msg:"Campaing deleted"] as JSON)
    }

    private CampaignRQDTO convertCommandToCampaign(MassMailingCommand command, KuorumUser user){
        CampaignRQDTO campaignRQDTO = new CampaignRQDTO();
        campaignRQDTO.setName(command.getSubject())
        campaignRQDTO.setSubject(command.getSubject())
        campaignRQDTO.setBody(command.getText())
        campaignRQDTO.setFilterId(command.filterId)

        KuorumFile picture = KuorumFile.get(command.headerPictureId);
        picture = fileService.convertTemporalToFinalFile(picture)
        fileService.deleteTemporalFiles(user)
        campaignRQDTO.setImageUrl(picture.getUrl())
        campaignRQDTO
    }

    private String saveAndSendCampaign(KuorumUser user, MassMailingCommand command, Long campaignId = null){
        CampaignRQDTO campaignRQDTO = convertCommandToCampaign(command, user)
        String msg = ""
        CampaignRSDTO savedCampaign = null;
        if (command.getSendType()=="SEND" && command.filterId >= 0){
            savedCampaign = massMailingService.campaignSend(user, campaignRQDTO, campaignId)
            msg = g.message(code:'tools.massMailing.send.advise', args: [savedCampaign.subject])
        }else if(command.sendType == "SCHEDULED" && command.filterId >= 0){
            savedCampaign = massMailingService.campaignSchedule(user, campaignRQDTO,command.getScheduled(),campaignId)
            msg = g.message(code:'tools.massMailing.schedule.advise', args: [savedCampaign.subject, g.formatDate(date:  savedCampaign.sentOn, type:"datetime", style:"SHORT")])
        }else{
            // IS A DRAFT
            savedCampaign = massMailingService.campaignDraft(user, campaignRQDTO, campaignId)
            msg = g.message(code:'tools.massMailing.saveDraft.advise', args: [savedCampaign.subject])
        }
        if (command.filterId <0){
            //Is a test
            msg = g.message(code:'tools.massMailing.saveDraft.adviseTest', args: [savedCampaign.subject])
            massMailingService.campaignTest(user, savedCampaign.getId())
        }
        msg
    }
}
