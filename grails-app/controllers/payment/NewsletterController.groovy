package payment

import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import kuorum.KuorumFile
import kuorum.dashboard.DashboardService
import kuorum.files.FileService
import payment.campaign.PostService
import kuorum.users.CookieUUIDService
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserService
import kuorum.web.commands.payment.contact.ContactFilterCommand
import kuorum.web.commands.payment.massMailing.MassMailingCommand
import kuorum.web.commands.payment.massMailing.MassMailingContentCommand
import kuorum.web.commands.payment.massMailing.MassMailingSettingsCommand
import kuorum.web.commands.payment.massMailing.MassMailingTemplateCommand
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
import org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO
import payment.campaign.DebateService
import payment.campaign.MassMailingService
import payment.contact.ContactService

@Secured(['IS_AUTHENTICATED_REMEMBERED'])
class NewsletterController {

    SpringSecurityService springSecurityService

    ContactService contactService

    KuorumUserService kuorumUserService

    FileService fileService

    MassMailingService massMailingService

    DebateService debateService
    PostService postService

    DashboardService dashboardService;

    CustomerService customerService;

    CookieUUIDService cookieUUIDService;

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

    def createNewsletter() {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        def returnModels = modelMassMailingSettings(user, new MassMailingSettingsCommand(), null)

        return returnModels
    }

    def editSettingsStep(){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        Long campaignId = Long.parseLong(params.campaignId)
        def returnModels = modelMassMailingSettings(user, new MassMailingSettingsCommand(), campaignId)

        return returnModels
    }

    def editTemplateStep(){
        MassMailingTemplateCommand command = new MassMailingTemplateCommand()
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        Long campaignId = Long.parseLong(params.campaignId)
        CampaignRSDTO campaignRSDTO = massMailingService.findCampaign(user, campaignId)
        command.contentType = campaignRSDTO.template
        [command: command, campaign: campaignRSDTO]
    }

    def editContentStep(){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        Long campaignId = Long.parseLong(params.campaignId)
        CampaignRSDTO campaignRSDTO = massMailingService.findCampaign(user, campaignId)
        if (campaignRSDTO == null){
            flash.error="Campaign not found"
            redirect(mapping:"politicianCampaigns");
            return;
        }
        def model = contentModel(user, new MassMailingContentCommand(), campaignRSDTO)
        model
    }

    private def contentModel(KuorumUser user, MassMailingContentCommand command, CampaignRSDTO campaignRSDTO = null){

        campaignRSDTO = campaignRSDTO?:massMailingService.findCampaign(user, command.campaignId)

        CampaignTemplateDTO template = campaignRSDTO.template?:CampaignTemplateDTO.NEWSLETTER

        command.text= command.text?:campaignRSDTO.body;
        command.subject = command.subject?:campaignRSDTO.subject;
        command.setContentType(template)

        if(CampaignTemplateDTO.NEWSLETTER.equals(CampaignTemplateDTO.NEWSLETTER) && !command.headerPictureId){
            KuorumFile kuorumFile = KuorumFile.findByUrl(campaignRSDTO.imageUrl);
            command.headerPictureId = kuorumFile?.id;
        }

        def numberRecipients = getNumberRecipients(campaignRSDTO, user);
        Boolean validSubscription = customerService.validSubscription(user)

        [
                command: command,
                contentType: template,
                campaign: campaignRSDTO,
                validSubscription:validSubscription,
                numberRecipients: numberRecipients
        ]
    }

    private Long getNumberRecipients(CampaignRSDTO campaignRSDTO, KuorumUser user){
        campaignRSDTO.filter?.amountOfContacts!=null?campaignRSDTO.filter?.amountOfContacts:contactService.getUsers(user, null).total
    };

    /*** SAVE FIRST STEP ***/

    def saveMassMailingSettings(MassMailingSettingsCommand command){
        KuorumUser loggedUser = KuorumUser.get(springSecurityService.principal.id)
        Long campaignId = params.campaignId?Long.parseLong(params.campaignId):null // if the user has sent a test, it was saved as draft but the url hasn't changed
        if (command.hasErrors()){
            render view: 'createNewsletter', model: modelMassMailingSettings(loggedUser, command, campaignId)
            return;
        }
        String nextStep = params.redirectLink
        FilterRDTO anonymousFilter = recoverAnonymousFilter(params, command)
        def dataSend = saveSettings(loggedUser, command, campaignId, anonymousFilter)
//        flash.message = dataSend.msg
        redirect(mapping: nextStep, params: [campaignId: dataSend.campaign.id])
    }

    private def modelMassMailingSettings(KuorumUser user, MassMailingSettingsCommand command, Long campaignId){
        List<ExtendedFilterRSDTO> filters = contactService.getUserFilters(user)
        ContactPageRSDTO contactPageRSDTO = contactService.getUsers(user)
        if(campaignId){
            CampaignRSDTO campaignRSDTO = massMailingService.findCampaign(user, campaignId)
            command.campaignName = campaignRSDTO.name
            command.filterId = campaignRSDTO.filter?.id
            command.tags = campaignRSDTO.triggeredTags
            if (campaignRSDTO.filter && !filters.find{it.id == campaignRSDTO.filter.id}){
                ExtendedFilterRSDTO anonymousFilter = contactService.getFilter(user, campaignRSDTO.filter.id)
                filters.add(anonymousFilter)
            }
        }
        [filters:filters, command:command, totalContacts:contactPageRSDTO.total]
    }

    private def saveSettings(KuorumUser user, MassMailingSettingsCommand command, Long campaignId = null, FilterRDTO anonymousFilter = null){
        CampaignRQDTO campaignRQDTO = mapSettingsToCampaign(command, user, anonymousFilter, campaignId)
        campaignRQDTO.status = CampaignStatusRSDTO.DRAFT;
        CampaignRSDTO savedCampaign = massMailingService.campaignDraft(user, campaignRQDTO, campaignId)
        String msg = g.message(code:'tools.massMailing.saveDraft.advise', args: [savedCampaign.subject])
        [msg:msg, campaign:savedCampaign]
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
        CampaignRSDTO campaignRSDTO = massMailingService.findCampaign(user, campaignId)
        CampaignRQDTO campaignRQDTO = transformRStoRQ(campaignRSDTO)
        campaignRQDTO.setTemplate(command.contentType)

        if (campaignRSDTO.template != command.contentType){
            // The template has been changed, so the body will be new
            campaignRQDTO.body = "";
        }

        campaignRQDTO
    }

    /*** SAVE THIRD STEP ***/

    def saveMassMailingContent(MassMailingContentCommand command){
        KuorumUser loggedUser = KuorumUser.get(springSecurityService.principal.id)
        if (command.hasErrors()){
            if(command.errors.getFieldError().arguments.first() == "scheduled"){
                flash.error = message(code: "kuorum.web.commands.payment.massMailing.MassMailingCommand.scheduled.min.warn")
            }
            render view: 'editContentStep', model: contentModel(loggedUser, command)
            return;
        }
        String nextStep = params.redirectLink // if the user has sent a test, it was saved as draft but the url hasn't changed
        def dataSend = saveAndSendContent(loggedUser, command, command.campaignId)
//        flash.message = dataSend.msg
        if (dataSend.goToPaymentProcess){
            String paymentRedirect = g.createLink(mapping:"politicianMassMailingContent", params:[campaignId: dataSend.campaign.id] )
            cookieUUIDService.setPaymentRedirect(paymentRedirect)
            redirect(mapping: "paymentStart")
        }else{
            redirect(mapping: nextStep, params: [campaignId: dataSend.campaign.id])
        }
    }

    private def saveAndSendContent(KuorumUser user, MassMailingContentCommand command, Long campaignId = null){
        CampaignRQDTO campaignRQDTO = mapContentCampaign(command, user, campaignId)
        Boolean validSubscription = customerService.validSubscription(user);
        Boolean goToPaymentProcess = !validSubscription && (command.getSendType()=="SEND" || command.sendType == "SCHEDULED");
        String msg = ""
        CampaignRSDTO savedCampaign = null;
        if (validSubscription && command.getSendType()=="SEND"){
            savedCampaign = massMailingService.campaignSend(user, campaignRQDTO, campaignId)
            msg = g.message(code:'tools.massMailing.send.advise', args: [savedCampaign.name])
        }else if(validSubscription && command.sendType == "SCHEDULED") {
            savedCampaign = massMailingService.campaignSchedule(user, campaignRQDTO, command.getScheduled(), campaignId)
            msg = g.message(code: 'tools.massMailing.schedule.advise', args: [savedCampaign.name, g.formatDate(date: savedCampaign.sentOn, type: "datetime", style: "SHORT")])
        }else{
            // IS A DRAFT OR TEST
            campaignRQDTO.status = CampaignStatusRSDTO.DRAFT;
            savedCampaign = massMailingService.campaignDraft(user, campaignRQDTO, campaignId)
            msg = g.message(code:'tools.massMailing.saveDraft.advise', args: [savedCampaign.name])
        }

        if(command.sendType == "SEND_TEST"){
            msg = g.message(code:'tools.massMailing.saveDraft.adviseTest', args: [savedCampaign.name])
            massMailingService.campaignTest(user, savedCampaign.getId())
        }
        [msg:msg, campaign:savedCampaign, goToPaymentProcess:goToPaymentProcess]
    }

    /** END STEPS **/

    def showCampaign(Long campaignId){
        KuorumUser loggedUser = KuorumUser.get(springSecurityService.principal.id)
        CampaignRSDTO campaignRSDTO = massMailingService.findCampaign(loggedUser, campaignId)
        if (campaignRSDTO.status == CampaignStatusRSDTO.DRAFT || campaignRSDTO.status == CampaignStatusRSDTO.SCHEDULED ){
            redirect (mapping:'politicianMassMailingContent', params: [campaignId: campaignId])
        }else{
            TrackingMailStatsByCampaignPageRSDTO trackingPage = massMailingService.findTrackingMails(loggedUser, campaignId)
            render view: 'showCampaign', model: [newsletter: campaignRSDTO, trackingPage:trackingPage]
        }
    }

    def showDebateStats(Long debateId){
        KuorumUser loggedUser = KuorumUser.get(springSecurityService.principal.id)
        org.kuorum.rest.model.communication.CampaignRSDTO debate = debateService.find(loggedUser, debateId)
        Long campaignId = debate.newsletter.id
        if (debate.campaignStatusRSDTO == CampaignStatusRSDTO.DRAFT || debate.campaignStatusRSDTO == CampaignStatusRSDTO.SCHEDULED ){
            redirect (mapping:'politicianMassMailingContent', params: [campaignId: campaignId])
        }else{
            TrackingMailStatsByCampaignPageRSDTO trackingPage = massMailingService.findTrackingMails(loggedUser, campaignId)
            render view: 'showCampaign', model: [newsletter: debate.newsletter, trackingPage:trackingPage, campaign:debate]
        }
    }
    def showPostStats(Long postId){
        KuorumUser loggedUser = KuorumUser.get(springSecurityService.principal.id)
        org.kuorum.rest.model.communication.CampaignRSDTO post = postService.find(loggedUser, postId)
        Long campaignId = post.newsletter.id
        if (post.campaignStatusRSDTO == CampaignStatusRSDTO.DRAFT || post.campaignStatusRSDTO == CampaignStatusRSDTO.SCHEDULED ){
            redirect (mapping:'politicianMassMailingContent', params: [campaignId: campaignId])
        }else{
            TrackingMailStatsByCampaignPageRSDTO trackingPage = massMailingService.findTrackingMails(loggedUser, campaignId)
            render view: 'showCampaign', model: [newsletter: post.newsletter, trackingPage:trackingPage, campaign:post]
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
        KuorumUser loggedUser = KuorumUser.get(springSecurityService.principal.id)
        CampaignRSDTO campaignRSDTO = massMailingService.findCampaign(loggedUser, campaignId)
        render campaignRSDTO.htmlBody?:"Not sent"
    }

    def showTrackingMails(Long campaignId){
        KuorumUser loggedUser = KuorumUser.get(springSecurityService.principal.id)
        Integer page = params.page?Integer.parseInt(params.page):0;
        Integer size = params.size?Integer.parseInt(params.size):10;
        TrackingMailStatusRSDTO status
        try {
            status = TrackingMailStatusRSDTO.valueOf(params.status);
        }catch (Exception e){
            status = null;
        }
        TrackingMailStatsByCampaignPageRSDTO trackingPage = massMailingService.findTrackingMails(loggedUser, campaignId, status, page, size)
        render template: '/newsletter/campaignTabs/campaignRecipeints',
                model: [
                        trackingPage:trackingPage,
                        campaignId:campaignId,
                        status:status
                ]
    }

    def sendReport(Long campaignId){
        KuorumUser loggedUser = KuorumUser.get(springSecurityService.principal.id)
        massMailingService.findTrackingMailsReport(loggedUser, campaignId)
        Boolean isAjax = request.xhr
        if(isAjax){
            render ([success:"success"] as JSON)
        } else{
            flash.message = g.message(code: 'modal.exportedTrackingEvents.title')
            redirect (mapping: 'politicianMassMailingShow', params:[campaignId: campaignId])
        }
    }

    def updateCampaign(MassMailingSettingsCommand command){
        KuorumUser loggedUser = KuorumUser.get(springSecurityService.principal.id)
        Long campaignId = Long.parseLong(params.campaignId)
        if (command.hasErrors()){
            if (command.errors.allErrors.findAll{it.field == "scheduled"}){
                flash.error=g.message(code:'kuorum.web.commands.payment.massMailing.MassMailingCommand.scheduled.min.warn')
            }
            render view: 'createNewsletter', model: modelMassMailingSettings(loggedUser, command, campaignId)
            return;
        }
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

    def sendMassMailingTest(MassMailingContentCommand command, KuorumUser user){
        KuorumUser loggedUser = KuorumUser.get(springSecurityService.principal.id)
        if (command.hasErrors()){
            String msg = "error"
            ([msg:msg] as JSON)
            return;
        }
        command.sendType = "SEND_TEST"

        Long campaignId = params.campaignId?Long.parseLong(params.campaignId):null
        def dataSend = saveAndSendContent(loggedUser, command, campaignId)
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

    private CampaignRQDTO transformRStoRQ(KuorumUser loggedUser, Long campaignId) {
        CampaignRSDTO campaignRSDTO = massMailingService.findCampaign(loggedUser, campaignId)
        transformRStoRQ(campaignRSDTO)
    }
    private CampaignRQDTO transformRStoRQ(CampaignRSDTO rsdto){
        CampaignRQDTO campaignRQDTO = new CampaignRQDTO();
        campaignRQDTO.setName(rsdto.name)
        campaignRQDTO.setSubject(rsdto.subject)
        campaignRQDTO.setBody(rsdto.body)
        campaignRQDTO.setTriggerTags(rsdto.triggeredTags)
        campaignRQDTO.setFilterId(rsdto.filter?.id)
        campaignRQDTO.setImageUrl(rsdto.imageUrl)
        campaignRQDTO.setTemplate(rsdto.template)
        campaignRQDTO
    }

    private void updateScheduledCampaignToDraft(KuorumUser user, CampaignRSDTO campaignRSDTO){
        if(campaignRSDTO.status == CampaignStatusRSDTO.SCHEDULED){
            CampaignRQDTO campaignRQDTO = transformRStoRQ(campaignRSDTO)
            campaignRQDTO.setSentOn(null)
            campaignRQDTO.setStatus(CampaignStatusRSDTO.DRAFT)
            massMailingService.campaignDraft(user, campaignRQDTO, campaignRSDTO.getId())
        }
    }

    def exportCampaigns(){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        massMailingService.findCampaignsCollectionReport(user);
        render ([msg:""] as JSON)
    }


    private CampaignRQDTO mapSettingsToCampaign(MassMailingSettingsCommand command, KuorumUser user, FilterRDTO anonymousFilter, Long campaignId){
        CampaignRQDTO campaignRQDTO = null;
        if(campaignId){
            campaignRQDTO = transformRStoRQ(user, campaignId)
        }else{

            campaignRQDTO = new CampaignRQDTO();
        }
        campaignRQDTO.setName(command.campaignName)
        campaignRQDTO.setTriggerTags(command.tags)


        if (command.filterEdited){
//            anonymousFilter.setName(g.message(code:'tools.contact.filter.anonymousName', args: anonymousFilter.getName()))
            campaignRQDTO.setAnonymousFilter(anonymousFilter)
            campaignRQDTO.setFilterId(null)
        }else {
            campaignRQDTO.setFilterId(command.filterId)
        }
        campaignRQDTO
    }

    private CampaignRQDTO mapContentCampaign(MassMailingContentCommand command, KuorumUser user, campaignId){
        CampaignRQDTO campaignRQDTO = transformRStoRQ(user, campaignId)

        campaignRQDTO.body = command.text
        campaignRQDTO.subject = command.subject

        if (command.headerPictureId){
            KuorumFile picture = KuorumFile.get(command.headerPictureId);
            picture = fileService.convertTemporalToFinalFile(picture)
            fileService.deleteTemporalFiles(user)
            campaignRQDTO.setImageUrl(picture.getUrl())
        }
        campaignRQDTO
    }
}
