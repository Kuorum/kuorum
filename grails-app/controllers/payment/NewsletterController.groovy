package payment

import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import kuorum.KuorumFile
import kuorum.dashboard.DashboardService
import kuorum.files.FileService
import kuorum.register.KuorumUserSession
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserService
import kuorum.web.commands.payment.contact.ContactFilterCommand
import kuorum.web.commands.payment.massMailing.MassMailingCommand
import kuorum.web.commands.payment.massMailing.MassMailingContentCommand
import kuorum.web.commands.payment.massMailing.MassMailingSettingsCommand
import kuorum.web.commands.payment.massMailing.MassMailingTemplateCommand
import kuorum.web.commands.profile.TimeZoneCommand
import org.kuorum.rest.model.communication.CampaignRSDTO
import org.kuorum.rest.model.contact.ContactPageRSDTO
import org.kuorum.rest.model.contact.filter.ExtendedFilterRSDTO
import org.kuorum.rest.model.contact.filter.FilterRDTO
import org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO
import org.kuorum.rest.model.notification.campaign.NewsletterRQDTO
import org.kuorum.rest.model.notification.campaign.NewsletterRSDTO
import org.kuorum.rest.model.notification.campaign.NewsletterTemplateDTO
import org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatsByCampaignPageRSDTO
import org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO
import payment.campaign.CampaignService
import payment.campaign.NewsletterService
import payment.campaign.PostService
import payment.contact.ContactService

@Secured(['IS_AUTHENTICATED_REMEMBERED'])
class NewsletterController {

    SpringSecurityService springSecurityService

    ContactService contactService

    KuorumUserService kuorumUserService

    FileService fileService

    NewsletterService newsletterService

    CampaignService campaignService
    PostService postService

    DashboardService dashboardService

    def index() {

        if (dashboardService.forceUploadContacts()){
            render view: "/dashboard/payment/paymentNoContactsDashboard", model: [:]
            return
        }

        KuorumUserSession user = springSecurityService.principal
        List<NewsletterRSDTO> newsletters = newsletterService.findCampaigns(user)
        List<CampaignRSDTO> campaigns = campaignService.findAllCampaigns(user, true)

        [newsletters: newsletters, campaigns: campaigns, user:user]
    }

    def newCampaign(){

    }

    @Secured(['ROLE_CAMPAIGN_NEWSLETTER'])
    def createNewsletter() {
        KuorumUserSession user = springSecurityService.principal
        def returnModels = modelMassMailingSettings(user, new MassMailingSettingsCommand(), null)

        return returnModels
    }

    @Secured(['ROLE_CAMPAIGN_NEWSLETTER'])
    def editSettingsStep(){
        KuorumUserSession user = springSecurityService.principal
        Long campaignId = Long.parseLong(params.campaignId)
        def returnModels = modelMassMailingSettings(user, new MassMailingSettingsCommand(), campaignId)

        return returnModels
    }

    @Secured(['ROLE_CAMPAIGN_NEWSLETTER'])
    def editTemplateStep(){
        MassMailingTemplateCommand command = new MassMailingTemplateCommand()
        KuorumUserSession user = springSecurityService.principal
        Long campaignId = Long.parseLong(params.campaignId)
        NewsletterRSDTO newsletterRSDTO = newsletterService.findCampaign(user, campaignId)
        command.contentType = newsletterRSDTO.template
        [command: command, campaign: newsletterRSDTO]
    }

    @Secured(['ROLE_CAMPAIGN_NEWSLETTER'])
    def editContentStep(){
        KuorumUserSession user = springSecurityService.principal
        Long campaignId = Long.parseLong(params.campaignId)
        NewsletterRSDTO newsletterRSDTO = newsletterService.findCampaign(user, campaignId)
        if (newsletterRSDTO == null){
            flash.error="Campaign not found"
            redirect(mapping:"politicianCampaigns")
            return
        }
        updateScheduledCampaignToDraft(user, newsletterRSDTO)
        def model = contentModel(user, new MassMailingContentCommand(), newsletterRSDTO)
        model
    }

    private def contentModel(KuorumUserSession user, MassMailingContentCommand command, NewsletterRSDTO NewsletterRSDTO = null){

        NewsletterRSDTO = NewsletterRSDTO?:newsletterService.findCampaign(user, command.campaignId)

        NewsletterTemplateDTO template = NewsletterRSDTO.template?:NewsletterTemplateDTO.NEWSLETTER

        command.text= command.text?:NewsletterRSDTO.body
        command.subject = command.subject?:NewsletterRSDTO.subject
        command.setContentType(template)

        if(NewsletterTemplateDTO.NEWSLETTER.equals(NewsletterTemplateDTO.NEWSLETTER) && !command.headerPictureId){
            KuorumFile kuorumFile = KuorumFile.findByUrl(NewsletterRSDTO.imageUrl)
            command.headerPictureId = kuorumFile?.id
        }

        def numberRecipients = getNumberRecipients(NewsletterRSDTO, user)

        [
                command: command,
                contentType: template,
                campaign: NewsletterRSDTO,
                numberRecipients: numberRecipients
        ]
    }

    private Long getNumberRecipients(NewsletterRSDTO NewsletterRSDTO, KuorumUserSession user){
        NewsletterRSDTO.filter?.amountOfContacts!=null?NewsletterRSDTO.filter?.amountOfContacts:contactService.getUsers(user, null).total
    }

    /*** SAVE FIRST STEP ***/

    @Secured(['ROLE_CAMPAIGN_NEWSLETTER'])
    def saveMassMailingSettings(MassMailingSettingsCommand command){
        KuorumUserSession loggedUser = springSecurityService.principal
        Long campaignId = params.campaignId?Long.parseLong(params.campaignId):null // if the user has sent a test, it was saved as draft but the url hasn't changed
        if (command.hasErrors()){
            render view: 'createNewsletter', model: modelMassMailingSettings(loggedUser, command, campaignId)
            return
        }
        String nextStep = params.redirectLink
        FilterRDTO anonymousFilter = recoverAnonymousFilter(params, command)
        def dataSend = saveSettings(loggedUser, command, campaignId, anonymousFilter)
//        flash.message = dataSend.msg
        redirect(mapping: nextStep, params: [campaignId: dataSend.campaign.id])
    }

    private def modelMassMailingSettings(KuorumUserSession user, MassMailingSettingsCommand command, Long campaignId){
        List<ExtendedFilterRSDTO> filters = contactService.getUserFilters(user)
        ContactPageRSDTO contactPageRSDTO = contactService.getUsers(user)
        if(campaignId){
            NewsletterRSDTO newsletterRSDTO = newsletterService.findCampaign(user, campaignId)
            updateScheduledCampaignToDraft(user, newsletterRSDTO)
            command.campaignName = newsletterRSDTO.name
            command.filterId = newsletterRSDTO.filter?.id
            command.tags = newsletterRSDTO.triggeredTags
            if (newsletterRSDTO.filter && !filters.find{it.id == newsletterRSDTO.filter.id}){
                ExtendedFilterRSDTO anonymousFilter = contactService.getFilter(user, newsletterRSDTO.filter.id)
                filters.add(anonymousFilter)
            }
        }
        [filters:filters, command:command, totalContacts:contactPageRSDTO.total]
    }

    private def saveSettings(KuorumUserSession user, MassMailingSettingsCommand command, Long campaignId = null, FilterRDTO anonymousFilter = null){
        NewsletterRQDTO newsletterRQDTO = mapSettingsToCampaign(command, user, anonymousFilter, campaignId)
        newsletterRQDTO.status = CampaignStatusRSDTO.DRAFT
        NewsletterRSDTO savedCampaign = newsletterService.campaignDraft(user, newsletterRQDTO, campaignId)
        String msg = g.message(code:'tools.massMailing.saveDraft.advise', args: [savedCampaign.subject])
        [msg:msg, campaign:savedCampaign]
    }

    /*** SAVE TEMPLATE STEP ***/

    @Secured(['ROLE_CAMPAIGN_NEWSLETTER'])
    def saveMassMailingTemplate(MassMailingTemplateCommand command){
        KuorumUserSession loggedUser = springSecurityService.principal
        if (command.hasErrors()){
            render view: 'politicianMassMailingTemplate', model: [command: command]
            return
        }
        String nextStep = params.redirectLink
        Long campaignId = params.campaignId?Long.parseLong(params.campaignId):null // if the user has sent a test, it was saved as draft but the url hasn't changed
        def dataSend = saveAndSendTemplate(loggedUser, command, campaignId)
//        flash.message = dataSend.msg
        redirect(mapping: nextStep, params: [campaignId: dataSend.campaign.id])
    }

    private def saveAndSendTemplate(KuorumUserSession user, MassMailingTemplateCommand command, Long campaignId = null){
        NewsletterRQDTO newsletterRQDTO = convertTemplateToCampaign(command, user, campaignId)
        NewsletterRSDTO savedCampaign = newsletterService.campaignDraft(user, newsletterRQDTO, campaignId)
        String msg = g.message(code:'tools.massMailing.saveDraft.advise', args: [savedCampaign.name])
        [msg:msg, campaign:savedCampaign]
    }

    private NewsletterRQDTO convertTemplateToCampaign(MassMailingTemplateCommand command, KuorumUserSession user, Long campaignId){
        NewsletterRSDTO NewsletterRSDTO = newsletterService.findCampaign(user, campaignId)
        NewsletterRQDTO newsletterRQDTO = transformRStoRQ(NewsletterRSDTO)
        newsletterRQDTO.setTemplate(command.contentType)

        if (NewsletterRSDTO.template != command.contentType){
            // The template has been changed, so the body will be new
            newsletterRQDTO.body = ""
        }

        newsletterRQDTO
    }

    /*** SAVE THIRD STEP ***/

    def saveMassMailingContent(MassMailingContentCommand command){
        KuorumUserSession loggedUser = springSecurityService.principal
        if (command.hasErrors()){
            if(command.errors.getFieldError().arguments.first() == "scheduled"){
                flash.error = message(code: "kuorum.web.commands.payment.massMailing.MassMailingCommand.scheduled.min.warn")
            }
            render view: 'editContentStep', model: contentModel(loggedUser, command)
            return
        }
        String nextStep = params.redirectLink // if the user has sent a test, it was saved as draft but the url hasn't changed
        def dataSend = saveAndSendContent(loggedUser, command, command.campaignId)
//        flash.message = dataSend.msg
        redirect(mapping: nextStep, params: [campaignId: dataSend.campaign.id])
    }

    private def saveAndSendContent(KuorumUserSession user, MassMailingContentCommand command, Long campaignId = null){
        NewsletterRQDTO newsletterRQDTO = mapContentCampaign(command, user, campaignId)
        String msg = ""
        NewsletterRSDTO savedCampaign = null
        if (command.getSendType()=="SEND"){
            savedCampaign = newsletterService.campaignSend(user, newsletterRQDTO, campaignId)
            msg = g.message(code:'tools.massMailing.send.advise', args: [savedCampaign.name])
        }else if(command.sendType == "SCHEDULED") {
            savedCampaign = newsletterService.campaignSchedule(user, newsletterRQDTO, command.getScheduled(), campaignId)
            msg = g.message(code: 'tools.massMailing.schedule.advise', args: [savedCampaign.name, g.formatDate(date: savedCampaign.sentOn, type: "datetime", style: "SHORT")])
        }else{
            // IS A DRAFT OR TEST
            newsletterRQDTO.status = CampaignStatusRSDTO.DRAFT
            savedCampaign = newsletterService.campaignDraft(user, newsletterRQDTO, campaignId)
            msg = g.message(code:'tools.massMailing.saveDraft.advise', args: [savedCampaign.name])
        }

        if(command.sendType == "SEND_TEST"){
            msg = g.message(code:'tools.massMailing.saveDraft.adviseTest', args: [savedCampaign.name])
            newsletterService.campaignTest(user, savedCampaign.getId())
        }
        [msg:msg, campaign:savedCampaign]
    }

    /** END STEPS **/

    @Secured(['ROLE_CAMPAIGN_NEWSLETTER'])
    def showCampaign(Long campaignId){
        KuorumUserSession loggedUser = springSecurityService.principal
        NewsletterRSDTO NewsletterRSDTO = newsletterService.findCampaign(loggedUser, campaignId)
        if (NewsletterRSDTO.status == CampaignStatusRSDTO.DRAFT || NewsletterRSDTO.status == CampaignStatusRSDTO.SCHEDULED ){
            redirect (mapping:'politicianMassMailingContent', params: [campaignId: campaignId])
        }else{
            TrackingMailStatsByCampaignPageRSDTO trackingPage = newsletterService.findTrackingMails(loggedUser, campaignId)
            render view: 'showCampaign', model: [newsletter: NewsletterRSDTO, trackingPage:trackingPage]
        }
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def showCampaignStats(Long campaignId){
        KuorumUserSession loggedUser = springSecurityService.principal
        CampaignRSDTO campaign = campaignService.find(loggedUser, campaignId)
        Long newsletterId = campaign.newsletter.id
        if (campaign.campaignStatusRSDTO == CampaignStatusRSDTO.DRAFT || campaign.campaignStatusRSDTO == CampaignStatusRSDTO.SCHEDULED ){
            redirect (mapping:'politicianMassMailingContent', params: [campaignId: campaignId])
        }else{
            TrackingMailStatsByCampaignPageRSDTO trackingPage = newsletterService.findTrackingMails(loggedUser, newsletterId)
            render view: 'showCampaign', model: [newsletter: campaign.newsletter, trackingPage:trackingPage, campaign:campaign]
        }
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


    def showMailCampaign(Long campaignId){
        KuorumUserSession loggedUser = springSecurityService.principal
        NewsletterRSDTO NewsletterRSDTO = newsletterService.findCampaign(loggedUser, campaignId)
        render NewsletterRSDTO.htmlBody?:"Not sent"
    }

    def showTrackingMails(Long newsletterId){
        KuorumUserSession loggedUser = springSecurityService.principal
        Integer page = params.page?Integer.parseInt(params.page):0
        Integer size = params.size?Integer.parseInt(params.size):10
        TrackingMailStatusRSDTO status
        try {
            status = TrackingMailStatusRSDTO.valueOf(params.status)
        }catch (Exception e){
            status = null
        }
        TrackingMailStatsByCampaignPageRSDTO trackingPage = newsletterService.findTrackingMails(loggedUser, newsletterId, status, page, size)
        render template: '/newsletter/campaignTabs/campaignRecipeints',
                model: [
                        trackingPage:trackingPage,
                        newsletterId:newsletterId,
                        status:status
                ]
    }

    def sendReport(Long newsletterId){
        KuorumUserSession loggedUser = springSecurityService.principal
        newsletterService.findTrackingMailsReport(loggedUser, newsletterId)
        Boolean isAjax = request.xhr
        if(isAjax){
            render ([success:"success"] as JSON)
        } else{
            flash.message = g.message(code: 'modal.exportedTrackingEvents.title')
            redirect (mapping: 'politicianMassMailingShow', params:[campaignId: newsletterId])
        }
    }

    @Secured(['ROLE_CAMPAIGN_NEWSLETTER'])
    def updateCampaign(MassMailingSettingsCommand command){
        KuorumUserSession loggedUser = springSecurityService.principal
        Long campaignId = Long.parseLong(params.campaignId)
        if (command.hasErrors()){
            if (command.errors.allErrors.findAll{it.field == "scheduled"}){
                flash.error=g.message(code:'kuorum.web.commands.payment.massMailing.MassMailingCommand.scheduled.min.warn')
            }
            render view: 'createNewsletter', model: modelMassMailingSettings(loggedUser, command, campaignId)
            return
        }
        FilterRDTO anonymousFilter = recoverAnonymousFilter(params, command)
        def dataSend = saveAndSendCampaign(loggedUser, command, campaignId, anonymousFilter)
//        flash.message = dataSend.msg
        redirect mapping:'politicianMassMailing'
    }

    @Secured(['ROLE_CAMPAIGN_NEWSLETTER'])
    def removeCampaign(Long campaignId){
        KuorumUserSession loggedUser = springSecurityService.principal
        newsletterService.removeCampaign(loggedUser, campaignId)
        render ([msg:"Campaing deleted"] as JSON)
    }

    private FilterRDTO recoverAnonymousFilter(def params, MassMailingSettingsCommand command){
        ContactFilterCommand filterCommand = bindData(new ContactFilterCommand(), params)
        if (!filterCommand.filterName){
            filterCommand.filterName = "Custom filter for ${command.campaignName}"
        }
        FilterRDTO filterRDTO = filterCommand.buildFilter()
        if (!filterRDTO?.filterConditions){
            return null
        }
        return filterRDTO
    }

    private NewsletterRQDTO convertCommandToCampaign(MassMailingCommand command, KuorumUserSession user,FilterRDTO anonymousFilter){
        NewsletterRQDTO newsletterRQDTO = new NewsletterRQDTO()
        newsletterRQDTO.setName(command.subject)
        newsletterRQDTO.setSubject(command.subject)
        newsletterRQDTO.setBody(command.text)
        newsletterRQDTO.setTriggerTags(command.tags)
        if (command.filterEdited){
//            anonymousFilter.setName(g.message(code:'tools.contact.filter.anonymousName', args: anonymousFilter.getName()))
            newsletterRQDTO.setAnonymousFilter(anonymousFilter)
        }else {
            newsletterRQDTO.setFilterId(command.filterId)
        }

        if (command.headerPictureId){
            KuorumFile picture = KuorumFile.get(command.headerPictureId)
            picture = fileService.convertTemporalToFinalFile(picture)
            fileService.deleteTemporalFiles(user)
            newsletterRQDTO.setImageUrl(picture.getUrl())
        }
        newsletterRQDTO
    }

    def sendMassMailingTest(MassMailingContentCommand command){
        KuorumUserSession loggedUser = springSecurityService.principal
        if (command.hasErrors()){
            String msg = "error"
            ([msg:msg] as JSON)
            return
        }
        command.sendType = "SEND_TEST"

        Long campaignId = params.campaignId?Long.parseLong(params.campaignId):null
        def dataSend = saveAndSendContent(loggedUser, command, campaignId)
        render ([msg:dataSend.msg, campaing: dataSend.campaign] as JSON)

    }

    private def saveAndSendCampaign(KuorumUserSession user, MassMailingCommand command, Long campaignId = null, FilterRDTO anonymousFilter = null){
        NewsletterRQDTO newsletterRQDTO = convertCommandToCampaign(command, user, anonymousFilter)
        String msg = ""
        NewsletterRSDTO savedCampaign = null
        if (command.getSendType()=="SEND"){
            savedCampaign = newsletterService.campaignSend(user, newsletterRQDTO, campaignId)
            msg = g.message(code:'tools.massMailing.send.advise', args: [savedCampaign.name])
        }else if(command.sendType == "SCHEDULED") {
            savedCampaign = newsletterService.campaignSchedule(user, newsletterRQDTO, command.getScheduled(), campaignId)
            msg = g.message(code: 'tools.massMailing.schedule.advise', args: [savedCampaign.subject, g.formatDate(date: savedCampaign.sentOn, type: "datetime", style: "SHORT")])
        }else{
            // IS A DRAFT
            newsletterRQDTO.status = CampaignStatusRSDTO.DRAFT
            savedCampaign = newsletterService.campaignDraft(user, newsletterRQDTO, campaignId)
            msg = g.message(code:'tools.massMailing.saveDraft.advise', args: [savedCampaign.subject])
        }

        if(command.sendType == "SEND_TEST"){
            msg = g.message(code:'tools.massMailing.saveDraft.adviseTest', args: [savedCampaign.subject])
            newsletterService.campaignTest(user, savedCampaign.getId())
        }
        [msg:msg, campaign:savedCampaign]
    }

    private NewsletterRQDTO transformRStoRQ(KuorumUserSession loggedUser, Long campaignId) {
        NewsletterRSDTO NewsletterRSDTO = newsletterService.findCampaign(loggedUser, campaignId)
        transformRStoRQ(NewsletterRSDTO)
    }
    private NewsletterRQDTO transformRStoRQ(NewsletterRSDTO rsdto){
        NewsletterRQDTO newsletterRQDTO = new NewsletterRQDTO()
        newsletterRQDTO.setName(rsdto.name)
        newsletterRQDTO.setSubject(rsdto.subject)
        newsletterRQDTO.setBody(rsdto.body)
        newsletterRQDTO.setTriggerTags(rsdto.triggeredTags)
        newsletterRQDTO.setFilterId(rsdto.filter?.id)
        newsletterRQDTO.setImageUrl(rsdto.imageUrl)
        newsletterRQDTO.setTemplate(rsdto.template)
        newsletterRQDTO
    }

    private void updateScheduledCampaignToDraft(KuorumUserSession user, NewsletterRSDTO newsletterRSDTO){
        if(newsletterRSDTO.status == CampaignStatusRSDTO.SCHEDULED){
            NewsletterRQDTO newsletterRQDTO = transformRStoRQ(newsletterRSDTO)
            newsletterRQDTO.setSentOn(null)
            newsletterRQDTO.setStatus(CampaignStatusRSDTO.DRAFT)
            newsletterService.campaignDraft(user, newsletterRQDTO, newsletterRSDTO.getId())
        }
    }

    def exportCampaigns(){
        KuorumUserSession user = springSecurityService.principal
        newsletterService.findCampaignsCollectionReport(user)
        render ([msg:""] as JSON)
    }


    private NewsletterRQDTO mapSettingsToCampaign(MassMailingSettingsCommand command, KuorumUserSession user, FilterRDTO anonymousFilter, Long campaignId){
        NewsletterRQDTO newsletterRQDTO = null
        if(campaignId){
            newsletterRQDTO = transformRStoRQ(user, campaignId)
        }else{

            newsletterRQDTO = new NewsletterRQDTO()
        }
        newsletterRQDTO.setName(command.campaignName)
        newsletterRQDTO.setTriggerTags(command.tags)


        if (command.filterEdited){
//            anonymousFilter.setName(g.message(code:'tools.contact.filter.anonymousName', args: anonymousFilter.getName()))
            newsletterRQDTO.setAnonymousFilter(anonymousFilter)
            newsletterRQDTO.setFilterId(null)
        }else {
            newsletterRQDTO.setFilterId(command.filterId)
        }
        newsletterRQDTO
    }

    private NewsletterRQDTO mapContentCampaign(MassMailingContentCommand command, KuorumUserSession user, campaignId){
        NewsletterRQDTO newsletterRQDTO = transformRStoRQ(user, campaignId)

        newsletterRQDTO.body = command.text
        newsletterRQDTO.subject = command.subject

        if (command.headerPictureId){
            KuorumFile picture = KuorumFile.get(command.headerPictureId)
            picture = fileService.convertTemporalToFinalFile(picture)
            fileService.deleteTemporalFiles(user)
            newsletterRQDTO.setImageUrl(picture.getUrl())
        }
        newsletterRQDTO
    }
}
