package payment

import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import kuorum.KuorumFile
import kuorum.dashboard.DashboardService
import kuorum.files.FileService
import kuorum.post.PostService
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserService
import kuorum.web.commands.payment.contact.ContactFilterCommand
import kuorum.web.commands.payment.massMailing.*
import kuorum.web.commands.profile.TimeZoneCommand
import org.kuorum.rest.model.communication.debate.DebateRSDTO
import org.kuorum.rest.model.communication.post.PostRSDTO
import org.kuorum.rest.model.contact.ContactPageRSDTO
import org.kuorum.rest.model.contact.filter.ExtendedFilterRSDTO
import org.kuorum.rest.model.contact.filter.FilterRDTO
import org.kuorum.rest.model.notification.campaign.CampaignRQDTO
import org.kuorum.rest.model.notification.campaign.CampaignRSDTO
import org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO
import org.kuorum.rest.model.notification.campaign.CampaignTemplateDTO
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

    DashboardService dashboardService;

    def index() {

        if (dashboardService.forceUploadContacts()){
            render view: "/dashboard/payment/paymentNoContactsDashboard", model: [:]
            return;
        }

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
        def returnModels = modelMassMailingSettings(user, new MassMailingSettingsCommand(), params.testFilter, null)

        return returnModels
    }

    def editSettingsStep(){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        Long campaignId = Long.parseLong(params.campaignId)
        def returnModels = modelMassMailingSettings(user, new MassMailingSettingsCommand(), params.testFilter, campaignId)

        return returnModels
    }

    def editTemplateStep(){
        MassMailingTemplateCommand command = new MassMailingTemplateCommand()
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        Long campaignId = Long.parseLong(params.campaignId)
        CampaignRSDTO campaignRSDTO = massMailingService.findCampaign(user, campaignId)
        command.contentType = campaignRSDTO.template
        [command: command]
    }

    def editContentStep(){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        Long campaignId = Long.parseLong(params.campaignId)
        CampaignRSDTO campaignRSDTO = massMailingService.findCampaign(user, campaignId)
        if (campaignRSDTO.template == CampaignTemplateDTO.HTML){
            editContentStepText()
            return;
        }
        else{
            editContentStepTemplate()
            return;
        }
    }

    def editContentStepText(){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        Long campaignId = Long.parseLong(params.campaignId)
        CampaignRSDTO campaignRSDTO = massMailingService.findCampaign(user, campaignId)

        MassMailingContentTextCommand command = new MassMailingContentTextCommand()

        command.text = campaignRSDTO.body
        command.subject = campaignRSDTO.subject

        render view: 'editContentStep', model: [command: command, contentType: 'HTML', campaignId: campaignId, contacts: campaignRSDTO.numberRecipients]
    }

    def editContentStepTemplate(){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        Long campaignId = Long.parseLong(params.campaignId)
        CampaignRSDTO campaignRSDTO = massMailingService.findCampaign(user, campaignId)

        MassMailingContentTemplateCommand command = new MassMailingContentTemplateCommand()

        command.text = campaignRSDTO.body
        command.subject = campaignRSDTO.subject

        if (campaignRSDTO.imageUrl){
            KuorumFile kuorumFile = KuorumFile.findByUrl(campaignRSDTO.imageUrl)
            command.headerPictureId = kuorumFile?.id
        }

        render view: 'editContentStep', model: [command: command, contentType: 'NEWSLETTER', campaignId: campaignId, contacts: campaignRSDTO.numberRecipients]
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

    /*** SAVE FIRST STEP ***/

    private def modelMassMailingSettings(KuorumUser user, MassMailingSettingsCommand command, def testFilterParam = false, Long campaignId){
        List<ExtendedFilterRSDTO> filters = contactService.getUserFilters(user)
        ContactPageRSDTO contactPageRSDTO = contactService.getUsers(user)
        if(campaignId){
            CampaignRSDTO campaignRSDTO = massMailingService.findCampaign(user, campaignId)
            command.campaignName = campaignRSDTO.name
            command.filterId = campaignRSDTO.filter?.id
            command.tags = campaignRSDTO.triggeredTags
        }
        [filters:filters, command:command, totalContacts:contactPageRSDTO.total, hightLigthTestButtons: testFilterParam]
    }

    def saveMassMailingSettings(MassMailingSettingsCommand command){
        KuorumUser loggedUser = KuorumUser.get(springSecurityService.principal.id)
        if (command.hasErrors()){
            render view: 'createMassMailing', model: modelMassMailingSettings(loggedUser, command, command.filterId<0)
            return;
        }
        String nextStep = params.redirectLink
        Long campaignId = params.campaignId?Long.parseLong(params.campaignId):null // if the user has sent a test, it was saved as draft but the url hasn't changed
        FilterRDTO anonymousFilter = recoverAnonymousFilter(params, command)
        def dataSend = saveAndSendSettings(loggedUser, command, campaignId, anonymousFilter)
//        flash.message = dataSend.msg
        redirect(mapping: nextStep, params: [campaignId: dataSend.campaign.id])
    }

    private def saveAndSendSettings(KuorumUser user, MassMailingSettingsCommand command, Long campaignId = null, FilterRDTO anonymousFilter = null){
        CampaignRQDTO campaignRQDTO = convertSettingsToCampaign(command, user, anonymousFilter, campaignId)
        String msg = ""
        campaignRQDTO.status = CampaignStatusRSDTO.DRAFT;
        CampaignRSDTO savedCampaign = massMailingService.campaignDraft(user, campaignRQDTO, campaignId)
        msg = g.message(code:'tools.massMailing.saveDraft.advise', args: [savedCampaign.subject])

        [msg:msg, campaign:savedCampaign]
    }

    private CampaignRQDTO convertSettingsToCampaign(MassMailingSettingsCommand command, KuorumUser user, FilterRDTO anonymousFilter, Long campaignId){
        CampaignRQDTO campaignRQDTO = new CampaignRQDTO();
        campaignRQDTO.setName(command.campaignName)
        campaignRQDTO.setTriggerTags(command.tags)

        if(campaignId){
            CampaignRSDTO campaignRSDTO = massMailingService.findCampaign(user, campaignId)
            campaignRQDTO.body = campaignRSDTO.body
            campaignRQDTO.subject = campaignRSDTO.subject
            campaignRQDTO.template = campaignRSDTO.template
            campaignRQDTO.imageUrl = campaignRSDTO.imageUrl
        }

        if (command.filterEdited){
//            anonymousFilter.setName(g.message(code:'tools.contact.filter.anonymousName', args: anonymousFilter.getName()))
            campaignRQDTO.setAnonymousFilter(anonymousFilter)
        }else {
            campaignRQDTO.setFilterId(command.filterId)
        }
        campaignRQDTO
    }

    /*** SAVE TEMPLATE STEP ***/

    def saveMassMailingTemplate(MassMailingTemplateCommand command){
        KuorumUser loggedUser = KuorumUser.get(springSecurityService.principal.id)
        if (command.hasErrors()){
            render view: 'politicianMassMailingTemplate', model: [command: command]
            return;
        }
        String nextStep = params.redirectLink
        Long campaignId = params.campaignId?Long.parseLong(params.campaignId):null // if the user has sent a test, it was saved as draft but the url hasn't changed
        def dataSend = saveAndSendTemplate(loggedUser, command, campaignId)
//        flash.message = dataSend.msg
        redirect(mapping: nextStep, params: [campaignId: dataSend.campaign.id])
    }

    private def saveAndSendTemplate(KuorumUser user, MassMailingTemplateCommand command, Long campaignId = null){
        CampaignRQDTO campaignRQDTO = convertTemplateToCampaign(command, user, campaignId)
        CampaignRSDTO savedCampaign = massMailingService.campaignDraft(user, campaignRQDTO, campaignId)
        String msg = g.message(code:'tools.massMailing.saveDraft.advise', args: [savedCampaign.name])
        [msg:msg, campaign:savedCampaign]
    }

    private CampaignRQDTO convertTemplateToCampaign(MassMailingTemplateCommand command, KuorumUser user, Long campaignId){
        CampaignRQDTO campaignRQDTO = new CampaignRQDTO();
        campaignRQDTO.setTemplate(command.contentType)

        CampaignRSDTO campaignRSDTO = massMailingService.findCampaign(user, campaignId)
        campaignRQDTO.name = campaignRSDTO.name
        campaignRQDTO.anonymousFilter = campaignRSDTO.filter
        campaignRQDTO.body = campaignRSDTO.body
        campaignRQDTO.subject = campaignRSDTO.subject
        campaignRQDTO.triggerTags = campaignRSDTO.triggeredTags
        campaignRQDTO.imageUrl = campaignRSDTO.imageUrl

        campaignRQDTO
    }

    /*** SAVE THIRD STEP HTML ***/

    def saveMassMailingContentText(MassMailingContentTextCommand command){
        KuorumUser loggedUser = KuorumUser.get(springSecurityService.principal.id)
        if (command.hasErrors()){
            render view: 'politicianMassMailingContent', model: [command: command]
            return;
        }
        String nextStep = params.redirectLink
        Long campaignId = params.campaignId?Long.parseLong(params.campaignId):null // if the user has sent a test, it was saved as draft but the url hasn't changed
        def dataSend = saveAndSendContentText(loggedUser, command, campaignId)
//        flash.message = dataSend.msg
        redirect(mapping: nextStep, params: [campaignId: dataSend.campaign.id])
    }

    private def saveAndSendContentText(KuorumUser user, MassMailingContentTextCommand command, Long campaignId = null){
        CampaignRQDTO campaignRQDTO = convertContentToTextCampaign(command, user, campaignId)

        String msg = ""
        CampaignRSDTO savedCampaign = null;
        if (command.getSendType()=="SEND"){
            savedCampaign = massMailingService.campaignSend(user, campaignRQDTO, campaignId)
            msg = g.message(code:'tools.massMailing.send.advise', args: [savedCampaign.name])
        }else if(command.sendType == "SCHEDULED") {
            savedCampaign = massMailingService.campaignSchedule(user, campaignRQDTO, command.getScheduled(), campaignId)
            msg = g.message(code: 'tools.massMailing.schedule.advise', args: [savedCampaign.name, g.formatDate(date: savedCampaign.sentOn, type: "datetime", style: "SHORT")])
        }else{
            // IS A DRAFT
            campaignRQDTO.status = CampaignStatusRSDTO.DRAFT;
            savedCampaign = massMailingService.campaignDraft(user, campaignRQDTO, campaignId)
            msg = g.message(code:'tools.massMailing.saveDraft.advise', args: [savedCampaign.name])
        }

        if(command.sendType == "SEND_TEST"){
            msg = g.message(code:'tools.massMailing.saveDraft.adviseTest', args: [savedCampaign.name])
            massMailingService.campaignTest(user, savedCampaign.getId())
        }
        [msg:msg, campaign:savedCampaign]
    }

    private CampaignRQDTO convertContentToTextCampaign(MassMailingContentTextCommand command, KuorumUser user, Long campaignId){
        CampaignRQDTO campaignRQDTO = new CampaignRQDTO();
        campaignRQDTO.setSubject(command.subject)
        campaignRQDTO.setBody(command.text)

        CampaignRSDTO campaignRSDTO = massMailingService.findCampaign(user, campaignId)
        campaignRQDTO.name = campaignRSDTO.name
        campaignRQDTO.anonymousFilter = campaignRSDTO.filter
        campaignRQDTO.template = campaignRSDTO.template
        campaignRQDTO.triggerTags = campaignRSDTO.triggeredTags

        campaignRQDTO
    }

    /*** SAVE THIRD STEP TEMPLATE ***/

    def saveMassMailingContentTemplate(MassMailingContentTemplateCommand command){
        KuorumUser loggedUser = KuorumUser.get(springSecurityService.principal.id)
        if (command.hasErrors()){
            render view: 'politicianMassMailingContent', model: [command: command]
            return;
        }
        String nextStep = params.redirectLink
        Long campaignId = params.campaignId?Long.parseLong(params.campaignId):null // if the user has sent a test, it was saved as draft but the url hasn't changed
        def dataSend = saveAndSendContentTemplate(loggedUser, command, campaignId)
//        flash.message = dataSend.msg
        redirect(mapping: nextStep, params: [campaignId: dataSend.campaign.id])
    }

    private def saveAndSendContentTemplate(KuorumUser user, MassMailingContentTemplateCommand command, Long campaignId = null){
        CampaignRQDTO campaignRQDTO = convertContentToTemplateCampaign(command, user, campaignId)

        String msg = ""
        CampaignRSDTO savedCampaign = null;
        if (command.getSendType()=="SEND"){
            savedCampaign = massMailingService.campaignSend(user, campaignRQDTO, campaignId)
            msg = g.message(code:'tools.massMailing.send.advise', args: [savedCampaign.name])
        }else if(command.sendType == "SCHEDULED") {
            savedCampaign = massMailingService.campaignSchedule(user, campaignRQDTO, command.getScheduled(), campaignId)
            msg = g.message(code: 'tools.massMailing.schedule.advise', args: [savedCampaign.name, g.formatDate(date: savedCampaign.sentOn, type: "datetime", style: "SHORT")])
        }else{
            // IS A DRAFT
            campaignRQDTO.status = CampaignStatusRSDTO.DRAFT;
            savedCampaign = massMailingService.campaignDraft(user, campaignRQDTO, campaignId)
            msg = g.message(code:'tools.massMailing.saveDraft.advise', args: [savedCampaign.name])
        }

        if(command.sendType == "SEND_TEST"){
            msg = g.message(code:'tools.massMailing.saveDraft.adviseTest', args: [savedCampaign.name])
            massMailingService.campaignTest(user, savedCampaign.getId())
        }
        [msg:msg, campaign:savedCampaign]
    }

    private CampaignRQDTO convertContentToTemplateCampaign(MassMailingContentTemplateCommand command, KuorumUser user, campaignId){
        CampaignRQDTO campaignRQDTO = new CampaignRQDTO();
        campaignRQDTO.setSubject(command.subject)
        campaignRQDTO.setBody(command.text)

        CampaignRSDTO campaignRSDTO = massMailingService.findCampaign(user, campaignId)
        campaignRQDTO.name = campaignRSDTO.name
        campaignRQDTO.anonymousFilter = campaignRSDTO.filter
        campaignRQDTO.template = campaignRSDTO.template
        campaignRQDTO.triggerTags = campaignRSDTO.triggeredTags

        if (command.headerPictureId){
            KuorumFile picture = KuorumFile.get(command.headerPictureId);
            picture = fileService.convertTemporalToFinalFile(picture)
            fileService.deleteTemporalFiles(user)
            campaignRQDTO.setImageUrl(picture.getUrl())
        }
        campaignRQDTO
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

            def model = modelMassMailingSettings(loggedUser, command, false);
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

    /** END STEPS **/

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

    def updateCampaign(MassMailingSettingsCommand command){
        KuorumUser loggedUser = KuorumUser.get(springSecurityService.principal.id)
        if (command.hasErrors()){
            if (command.errors.allErrors.findAll{it.field == "scheduled"}){
                flash.error=g.message(code:'kuorum.web.commands.payment.massMailing.MassMailingCommand.scheduled.min.warn')
            }
            render view: 'createMassMailing', model: modelMassMailingSettings(loggedUser, command, command.filterId<0)
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

    private FilterRDTO recoverAnonymousFilter(def params, MassMailingSettingsCommand command){
        ContactFilterCommand filterCommand = bindData(new ContactFilterCommand(), params)
        if (!filterCommand.filterName){
            filterCommand.filterName = "Custom filter for ${command.campaignName}"
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

    def sendMassMailingTest(MassMailingContentTextCommand command, KuorumUser user){
        KuorumUser loggedUser = KuorumUser.get(springSecurityService.principal.id)
        if (command.hasErrors()){
            String msg = "error"
            ([msg:msg] as JSON)
            return;
        }
        command.sendType = "SEND_TEST"
        Long campaignId = params.campaignId?Long.parseLong(params.campaignId):null
        def dataSend = saveAndSendContentText(loggedUser, command, campaignId)
        render ([msg:dataSend.msg, campaing: dataSend.campaign] as JSON)

    }

    private def saveAndSendCampaign(KuorumUser user, MassMailingCommand command, Long campaignId = null, FilterRDTO anonymousFilter = null){
        CampaignRQDTO campaignRQDTO = convertCommandToCampaign(command, user, anonymousFilter)
        String msg = ""
        CampaignRSDTO savedCampaign = null;
        if (command.getSendType()=="SEND"){
            savedCampaign = massMailingService.campaignSend(user, campaignRQDTO, campaignId)
            msg = g.message(code:'tools.massMailing.send.advise', args: [savedCampaign.name])
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
