package payment

import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import kuorum.KuorumFile
import kuorum.files.FileService
import kuorum.post.PostService
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserService
import kuorum.web.commands.payment.contact.ContactFilterCommand
import kuorum.web.commands.payment.massMailing.MassMailingCommand
import kuorum.web.commands.profile.TimeZoneCommand
import org.kuorum.rest.model.communication.debate.DebateRSDTO
import org.kuorum.rest.model.communication.post.PostRSDTO
import org.kuorum.rest.model.contact.ContactPageRSDTO
import org.kuorum.rest.model.contact.filter.ExtendedFilterRSDTO
import org.kuorum.rest.model.contact.filter.FilterRDTO
import org.kuorum.rest.model.notification.campaign.CampaignRQDTO
import org.kuorum.rest.model.notification.campaign.CampaignRSDTO
import org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO
import org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatsByCampaignPageRSDTO
import payment.campaign.DebateService
import payment.campaign.MassMailingService
import payment.contact.ContactService

@Secured(['IS_AUTHENTICATED_REMEMBERED'])
class MassMailingController {

    SpringSecurityService springSecurityService

    ContactService contactService

    KuorumUserService kuorumUserService

    FileService fileService

    MassMailingService massMailingService

    DebateService debateService
    PostService postService

    def index() {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        List<CampaignRSDTO> campaigns = massMailingService.findCampaigns(user)
        List<DebateRSDTO> debates = debateService.findAllDebates(user)
        List<PostRSDTO> posts = postService.findAllPosts(user)

        [campaigns: campaigns, debates: debates, posts: posts, user: user]
    }

    def newCampaign(){

    }

    def createMassMailing() {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        def returnModels = modelMassMailing(user, new MassMailingCommand(), params.testFilter)

        return returnModels
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def saveTimeZone(TimeZoneCommand timeZoneCommand) {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)

        if (timeZoneCommand.hasErrors()) {
            flash.error="There was a problem with your data."
            redirect(mapping: "politicianCampaignsNew")
        } else {
            user.timeZone = TimeZone.getTimeZone(timeZoneCommand.timeZoneId)
            kuorumUserService.updateUser(user)
            if (timeZoneCommand.timeZoneRedirect){
                redirect url: timeZoneCommand.timeZoneRedirect
            }else{
                redirect(mapping: "politicianCampaignsNew")
            }
        }
    }

    private def modelMassMailing(KuorumUser user, MassMailingCommand command, def testFilterParam = false){
        List<ExtendedFilterRSDTO> filters = contactService.getUserFilters(user)
        ContactPageRSDTO contactPageRSDTO = contactService.getUsers(user)
        [filters:filters, command:command, totalContacts:contactPageRSDTO.total, hightLigthTestButtons: testFilterParam]
    }

    def saveMassMailing(MassMailingCommand command){
        KuorumUser loggedUser = KuorumUser.get(springSecurityService.principal.id)
        if (command.hasErrors()){
            if (command.errors.allErrors.findAll{it.field == "scheduled"}){
                flash.error=g.message(code:'kuorum.web.commands.payment.massMailing.MassMailingCommand.scheduled.min.warn')
            }
            render view: 'createMassMailing', model: modelMassMailing(loggedUser, command, command.filterId<0)
            return;
        }
        Long campaignId = params.campaignId?Long.parseLong(params.campaignId):null // if the user has sent a test, it was saved as draft but the url hasn't changed
        FilterRDTO anonymousFilter = recoverAnonymousFilter(params, command)
        def dataSend = saveAndSendCampaign(loggedUser, command, campaignId, anonymousFilter)
//        flash.message = dataSend.msg
        redirect mapping:'politicianMassMailing'
    }

    def showCampaign(Long campaignId){
        KuorumUser loggedUser = KuorumUser.get(springSecurityService.principal.id)
        CampaignRSDTO campaignRSDTO = massMailingService.findCampaign(loggedUser, campaignId)
        if (campaignRSDTO.status == CampaignStatusRSDTO.DRAFT || campaignRSDTO.status == CampaignStatusRSDTO.SCHEDULED ){
            MassMailingCommand command = new MassMailingCommand()
            command.filterId = campaignRSDTO.filter?.id ?: null
            if (campaignRSDTO.imageUrl){
                KuorumFile kuorumFile = KuorumFile.findByUrl(campaignRSDTO.imageUrl)
                command.headerPictureId = kuorumFile?.id
            }
            command.scheduled = campaignRSDTO.sentOn
            command.subject = campaignRSDTO.subject
            command.text = campaignRSDTO.body
            if ( campaignRSDTO.triggeredTags){
                command.tags = campaignRSDTO.triggeredTags
            }

            def model = modelMassMailing(loggedUser, command, false);
            if (campaignRSDTO.filter && !model.filters.find{it.id==campaignRSDTO.filter.id}){
                // Not found campaign filter. That means that the filter is custom filter for the campaign
                ExtendedFilterRSDTO anonymousFilter = contactService.getFilter(loggedUser, campaignRSDTO.filter.id)
                model.put("anonymousFilter", anonymousFilter)
            }
            model.put("campaignId", campaignId)
            render view: 'createMassMailing', model: model
        }else{
            TrackingMailStatsByCampaignPageRSDTO trackingPage = massMailingService.findTrackingMails(loggedUser, campaignId)
            render view: 'showCampaign', model: [campaign: campaignRSDTO, trackingPage:trackingPage]
        }
    }

    def showMailCampaign(Long campaignId){
        KuorumUser loggedUser = KuorumUser.get(springSecurityService.principal.id)
        CampaignRSDTO campaignRSDTO = massMailingService.findCampaign(loggedUser, campaignId)
        render campaignRSDTO.htmlBody?:"Not sent"
    }

    def showTrackingMails(Long campaignId){
        KuorumUser loggedUser = KuorumUser.get(springSecurityService.principal.id)
        Integer page = params.page?Integer.parseInt(params.page):0;
        Integer size = params.size?Integer.parseInt(params.size):10;
        TrackingMailStatsByCampaignPageRSDTO trackingPage = massMailingService.findTrackingMails(loggedUser, campaignId, page, size)
        render template: '/massMailing/campaignTabs/campaignRecipeints', model: [trackingPage:trackingPage, campaignId:campaignId]
    }

    def updateCampaign(MassMailingCommand command){
        KuorumUser loggedUser = KuorumUser.get(springSecurityService.principal.id)
        if (command.hasErrors()){
            if (command.errors.allErrors.findAll{it.field == "scheduled"}){
                flash.error=g.message(code:'kuorum.web.commands.payment.massMailing.MassMailingCommand.scheduled.min.warn')
            }
            render view: 'createMassMailing', model: modelMassMailing(loggedUser, command, command.filterId<0)
            return;
        }
        Long campaignId = Long.parseLong(params.campaignId)
        FilterRDTO anonymousFilter = recoverAnonymousFilter(params, command)
        def dataSend = saveAndSendCampaign(loggedUser, command, campaignId, anonymousFilter)
//        flash.message = dataSend.msg
        redirect mapping:'politicianMassMailing'
    }

    def removeCampaign(Long campaignId){
        KuorumUser loggedUser = KuorumUser.get(springSecurityService.principal.id)
        massMailingService.removeCampaign(loggedUser, campaignId)
        render ([msg:"Campaing deleted"] as JSON)
    }

    private FilterRDTO recoverAnonymousFilter(def params, MassMailingCommand command){
        ContactFilterCommand filterCommand = bindData(new ContactFilterCommand(), params)
        if (!filterCommand.filterName){
            filterCommand.filterName = "Custom filter for ${command.subject}"
        }
        FilterRDTO filterRDTO = filterCommand.buildFilter();
        if (!filterRDTO?.filterConditions){
            return null;
        }
        return filterRDTO
    }

    private CampaignRQDTO convertCommandToCampaign(MassMailingCommand command, KuorumUser user,FilterRDTO anonymousFilter){
        CampaignRQDTO campaignRQDTO = new CampaignRQDTO();
        campaignRQDTO.setName(command.subject)
        campaignRQDTO.setSubject(command.subject)
        campaignRQDTO.setBody(command.text)
        campaignRQDTO.setTriggerTags(command.tags)
        if (command.filterEdited){
//            anonymousFilter.setName(g.message(code:'tools.contact.filter.anonymousName', args: anonymousFilter.getName()))
            campaignRQDTO.setAnonymousFilter(anonymousFilter)
        }else {
            campaignRQDTO.setFilterId(command.filterId)
        }

        if (command.headerPictureId){
            KuorumFile picture = KuorumFile.get(command.headerPictureId);
            picture = fileService.convertTemporalToFinalFile(picture)
            fileService.deleteTemporalFiles(user)
            campaignRQDTO.setImageUrl(picture.getUrl())
        }
        campaignRQDTO
    }

    def sendMassMailingTest(MassMailingCommand command, KuorumUser user){
        KuorumUser loggedUser = KuorumUser.get(springSecurityService.principal.id)
        if (command.hasErrors()){
            String msg = "error"
            ([msg:msg] as JSON)
            return;
        }
        command.sendType = "SEND_TEST"
        Long campaignId = params.campaignId?Long.parseLong(params.campaignId):null
        FilterRDTO anonymousFilter = recoverAnonymousFilter(params, command)
        def dataSend = saveAndSendCampaign(loggedUser, command, campaignId, anonymousFilter)
        render ([msg:dataSend.msg, campaing: dataSend.campaign] as JSON)

    }

    private def saveAndSendCampaign(KuorumUser user, MassMailingCommand command, Long campaignId = null, FilterRDTO anonymousFilter = null){
        CampaignRQDTO campaignRQDTO = convertCommandToCampaign(command, user, anonymousFilter)
        String msg = ""
        CampaignRSDTO savedCampaign = null;
        if (command.getSendType()=="SEND"){
            savedCampaign = massMailingService.campaignSend(user, campaignRQDTO, campaignId)
            msg = g.message(code:'tools.massMailing.send.advise', args: [savedCampaign.subject])
        }else if(command.sendType == "SCHEDULED") {
            savedCampaign = massMailingService.campaignSchedule(user, campaignRQDTO, command.getScheduled(), campaignId)
            msg = g.message(code: 'tools.massMailing.schedule.advise', args: [savedCampaign.subject, g.formatDate(date: savedCampaign.sentOn, type: "datetime", style: "SHORT")])
        }else{
            // IS A DRAFT
            campaignRQDTO.status = CampaignStatusRSDTO.DRAFT;
            savedCampaign = massMailingService.campaignDraft(user, campaignRQDTO, campaignId)
            msg = g.message(code:'tools.massMailing.saveDraft.advise', args: [savedCampaign.subject])
        }

        if(command.sendType == "SEND_TEST"){
            msg = g.message(code:'tools.massMailing.saveDraft.adviseTest', args: [savedCampaign.subject])
            massMailingService.campaignTest(user, savedCampaign.getId())
        }
        [msg:msg, campaign:savedCampaign]
    }
}
