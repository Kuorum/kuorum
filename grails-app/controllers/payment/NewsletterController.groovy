package payment

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import kuorum.KuorumFile
import kuorum.dashboard.DashboardService
import kuorum.politician.CampaignController
import kuorum.register.KuorumUserSession
import kuorum.users.KuorumUser
import kuorum.web.commands.payment.CampaignContentCommand
import kuorum.web.commands.payment.CampaignSettingsCommand
import kuorum.web.commands.payment.contact.ContactFilterCommand
import kuorum.web.commands.payment.massMailing.MassMailingCommand
import kuorum.web.commands.payment.massMailing.MassMailingContentCommand
import kuorum.web.commands.payment.massMailing.MassMailingSettingsCommand
import kuorum.web.commands.payment.massMailing.MassMailingTemplateCommand
import kuorum.web.commands.profile.TimeZoneCommand
import org.kuorum.rest.model.communication.CampaignLightPageRSDTO
import org.kuorum.rest.model.communication.CampaignRSDTO
import org.kuorum.rest.model.communication.CampaignTypeRSDTO
import org.kuorum.rest.model.communication.bulletin.BulletinRSDTO
import org.kuorum.rest.model.communication.search.SearchCampaignRDTO
import org.kuorum.rest.model.contact.ContactPageRSDTO
import org.kuorum.rest.model.contact.filter.ExtendedFilterRSDTO
import org.kuorum.rest.model.contact.filter.FilterRDTO
import org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO
import org.kuorum.rest.model.notification.campaign.NewsletterRQDTO
import org.kuorum.rest.model.notification.campaign.NewsletterRSDTO
import org.kuorum.rest.model.notification.campaign.NewsletterTemplateDTO
import org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatsByCampaignPageRSDTO
import org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO
import payment.campaign.BulletinService
import payment.campaign.NewsletterService

@Secured(['IS_AUTHENTICATED_REMEMBERED'])
class NewsletterController extends CampaignController{


    NewsletterService newsletterService

    BulletinService bulletinService;

    DashboardService dashboardService

    def index() {
        KuorumUserSession user = springSecurityService.principal
        // TODO: Bad trick -> Recover all campaigns without pagination
        SearchCampaignRDTO searchCampaignRDTO = new SearchCampaignRDTO(
                page:0,
                size: 100,
                attachNotPublished: true,
                onlyPublications: false
        )
        CampaignLightPageRSDTO campaignsPage = campaignService.findAllCampaigns(user, searchCampaignRDTO); // BAD TRICK
        [campaigns: campaignsPage.getData(), user: user, "tour":params.tour?Boolean.parseBoolean(params.tour):false]
    }

    def newCampaign() {

    }

    @Secured(['ROLE_CAMPAIGN_NEWSLETTER'])
    def createNewsletter() {
        return bulletinModelSettings(new CampaignSettingsCommand(debatable:false), null)
    }

    @Secured(['ROLE_CAMPAIGN_NEWSLETTER'])
    def editSettingsStep() {
        KuorumUserSession userSession = springSecurityService.principal
        BulletinRSDTO bulletinRSDTO = bulletinService.find(userSession, Long.parseLong(params.campaignId))
        return bulletinModelSettings(new CampaignSettingsCommand(debatable:false), bulletinRSDTO)
    }

    private def bulletinModelSettings(CampaignSettingsCommand command, BulletinRSDTO bulletinRSDTO) {
        def model = modelSettings(command, bulletinRSDTO)
        command.debatable=false
        model.options =[debatable:false, showCampaignDateLimits:false]
        return model
    }

    @Secured(['ROLE_CAMPAIGN_NEWSLETTER'])
    def editTemplateStep() {
        Long campaignId = Long.parseLong(params.campaignId)
        BulletinRSDTO bulletinRSDTO = setCampaignAsDraft(campaignId, bulletinService)
        MassMailingTemplateCommand command = new MassMailingTemplateCommand()
        command.contentType = bulletinRSDTO.getNewsletter().template
        [command: command, campaign: bulletinRSDTO]
    }

    @Secured(['ROLE_CAMPAIGN_NEWSLETTER'])
    def editContentStep() {
        KuorumUserSession user = springSecurityService.principal
        Long campaignId = Long.parseLong(params.campaignId)
        BulletinRSDTO bulletinRSDTO = bulletinService.find(user, campaignId)
        if (bulletinRSDTO == null) {
            flash.error = "Campaign not found"
            redirect(mapping: "politicianCampaigns")
            return
        }
        updateScheduledCampaignToDraft(user, bulletinRSDTO)
        def model = contentModel(user, new MassMailingContentCommand(), bulletinRSDTO)
        model
    }

    private def contentModel(KuorumUserSession user, MassMailingContentCommand command, BulletinRSDTO bulletinRSDTO = null) {

        bulletinRSDTO = bulletinRSDTO ?: bulletinService.find(user, command.campaignId)
        NewsletterRSDTO newsletterRSDTO = bulletinRSDTO.getNewsletter()

        NewsletterTemplateDTO template = newsletterRSDTO.template ?: NewsletterTemplateDTO.NEWSLETTER

        command.text = command.text ?: newsletterRSDTO.body
        command.subject = command.subject ?: newsletterRSDTO.subject
        command.setContentType(template)

        if (NewsletterTemplateDTO.NEWSLETTER.equals(NewsletterTemplateDTO.NEWSLETTER) && !command.headerPictureId) {
            KuorumFile kuorumFile = KuorumFile.findByUrl(newsletterRSDTO.imageUrl)
            command.headerPictureId = kuorumFile?.id
        }

        def numberRecipients = getNumberRecipients(newsletterRSDTO, user)

        [
                command         : command,
                contentType     : template,
                campaign        : bulletinRSDTO,
                numberRecipients: numberRecipients
        ]
    }

    private Long getNumberRecipients(NewsletterRSDTO NewsletterRSDTO, KuorumUserSession user) {
        NewsletterRSDTO.filter?.amountOfContacts != null ? NewsletterRSDTO.filter?.amountOfContacts : contactService.getUsers(user, null).total
    }

    /*** SAVE FIRST STEP ***/

    @Secured(['ROLE_CAMPAIGN_NEWSLETTER'])
    def saveMassMailingSettings(MassMailingSettingsCommand command) {
        KuorumUserSession loggedUser = springSecurityService.principal
        Long campaignId = params.campaignId ? Long.parseLong(params.campaignId) : null
        // if the user has sent a test, it was saved as draft but the url hasn't changed
        if (command.hasErrors()) {
            render view: 'createNewsletter', model: modelMassMailingSettings(loggedUser, command, campaignId)
            return
        }
        String nextStep = params.redirectLink
        FilterRDTO anonymousFilter = recoverAnonymousFilter(params, command)
        def dataSend = saveSettings(loggedUser, command, campaignId, anonymousFilter)
//        flash.message = dataSend.msg
        redirect(mapping: nextStep, params: [campaignId: dataSend.campaign.id])
    }

    private def modelMassMailingSettings(KuorumUserSession user, MassMailingSettingsCommand command, Long campaignId) {
        List<ExtendedFilterRSDTO> filters = contactService.getUserFilters(user)
        ContactPageRSDTO contactPageRSDTO = contactService.getUsers(user)
        if (campaignId) {
            BulletinRSDTO bulletinRSDTO = bulletinService.find(user, campaignId);
            NewsletterRSDTO newsletterRSDTO = bulletinRSDTO.newsletter
            updateScheduledCampaignToDraft(user, bulletinRSDTO)
            command.campaignName = newsletterRSDTO.name
            command.filterId = newsletterRSDTO.filter?.id
            command.tags = newsletterRSDTO.triggeredTags
            if (newsletterRSDTO.filter && !filters.find { it.id == newsletterRSDTO.filter.id }) {
                ExtendedFilterRSDTO anonymousFilter = contactService.getFilter(user, newsletterRSDTO.filter.id)
                filters.add(anonymousFilter)
            }
        }
        [filters: filters, command: command, totalContacts: contactPageRSDTO.total]
    }

    private def saveSettings(KuorumUserSession user, MassMailingSettingsCommand command, Long campaignId = null, FilterRDTO anonymousFilter = null) {
        NewsletterRQDTO newsletterRQDTO = mapSettingsToCampaign(command, user, anonymousFilter, campaignId)
        newsletterRQDTO.status = CampaignStatusRSDTO.DRAFT
        BulletinRSDTO savedCampaign = newsletterService.campaignDraft(user, newsletterRQDTO, campaignId)
        String msg = g.message(code: 'tools.massMailing.saveDraft.advise', args: [savedCampaign.title])
        [msg: msg, campaign: savedCampaign]
    }

    /*** SAVE TEMPLATE STEP ***/

    @Secured(['ROLE_CAMPAIGN_NEWSLETTER'])
    def saveMassMailingTemplate(MassMailingTemplateCommand command) {
        KuorumUserSession loggedUser = springSecurityService.principal
        if (command.hasErrors()) {
            render view: 'politicianMassMailingTemplate', model: [command: command]
            return
        }
        String nextStep = params.redirectLink
        Long campaignId = params.campaignId ? Long.parseLong(params.campaignId) : null
        // if the user has sent a test, it was saved as draft but the url hasn't changed
        def dataSend = saveAndSendTemplate(loggedUser, command, campaignId)
//        flash.message = dataSend.msg
        redirect(mapping: nextStep, params: [campaignId: dataSend.campaign.id])
    }

    private def saveAndSendTemplate(KuorumUserSession user, MassMailingTemplateCommand command, Long campaignId = null) {
        NewsletterRQDTO newsletterRQDTO = convertTemplateToCampaign(command, user, campaignId)
        BulletinRSDTO savedCampaign = newsletterService.campaignDraft(user, newsletterRQDTO, campaignId)
        String msg = g.message(code: 'tools.massMailing.saveDraft.advise', args: [savedCampaign.name])
        [msg: msg, campaign: savedCampaign]
    }

    private NewsletterRQDTO convertTemplateToCampaign(MassMailingTemplateCommand command, KuorumUserSession user, Long campaignId) {
        BulletinRSDTO bulletinRSDTO = bulletinService.find(user, campaignId)
        NewsletterRQDTO newsletterRQDTO = transformRStoRQ(bulletinRSDTO.getNewsletter())
        newsletterRQDTO.setTemplate(command.contentType)

        if (bulletinRSDTO.getNewsletter().getTemplate() != command.contentType) {
            // The template has been changed, so the body will be new
            newsletterRQDTO.body = ""
        }
        newsletterRQDTO
    }

    /*** SAVE THIRD STEP ***/

    def saveMassMailingContent(MassMailingContentCommand command) {
        KuorumUserSession loggedUser = springSecurityService.principal
        if (command.hasErrors()) {
            if (command.errors.getFieldError().arguments.first() == "scheduled") {
                flash.error = message(code: "kuorum.web.commands.payment.massMailing.MassMailingCommand.scheduled.min.warn")
            }
            render view: 'editContentStep', model: contentModel(loggedUser, command)
            return
        }
        String nextStep = params.redirectLink
        // if the user has sent a test, it was saved as draft but the url hasn't changed
        def dataSend = saveAndSendContent(loggedUser, command, command.campaignId)
//        flash.message = dataSend.msg
        redirect(mapping: nextStep, params: [campaignId: dataSend.campaign.id])
    }

    private def saveAndSendContent(KuorumUserSession user, MassMailingContentCommand command, Long campaignId = null) {
        NewsletterRQDTO newsletterRQDTO = mapContentCampaign(command, user, campaignId)
        String msg = ""
        BulletinRSDTO savedBulletin = null
        if (command.getSendType() == CampaignContentCommand.CAMPAIGN_SEND_TYPE_SEND) {
            savedBulletin = newsletterService.campaignSend(user, newsletterRQDTO, campaignId)
            msg = g.message(code: 'tools.massMailing.send.advise', args: [savedBulletin.name])
        } else if (command.sendType == CampaignContentCommand.CAMPAIGN_SEND_TYPE_SCHEDULED) {
            savedBulletin = newsletterService.campaignSchedule(user, newsletterRQDTO, command.getPublishOn(), campaignId)
            msg = g.message(code: 'tools.massMailing.schedule.advise', args: [savedBulletin.name, g.formatDate(date: savedBulletin.newsletter.sentOn, type: "datetime", style: "SHORT")])
        } else {
            // IS A DRAFT OR TEST
            newsletterRQDTO.status = CampaignStatusRSDTO.DRAFT
            savedBulletin = newsletterService.campaignDraft(user, newsletterRQDTO, campaignId)
            msg = g.message(code: 'tools.massMailing.saveDraft.advise', args: [savedBulletin.name])
        }

        if (command.sendType == CampaignContentCommand.CAMPAIGN_SEND_TYPE_SEND_TEST) {
            msg = g.message(code: 'tools.massMailing.saveDraft.adviseTest', args: [savedBulletin.name])
            newsletterService.campaignTest(user, savedBulletin.newsletter.getId())
        }
        [msg: msg, campaign: savedBulletin]
    }

    /** END STEPS **/

    @Secured(['ROLE_CAMPAIGN_NEWSLETTER'])
    def copyCampaign(Long campaignId) {
        KuorumUserSession loggedUser = springSecurityService.principal
        NewsletterRSDTO newsletterCopy = newsletterService.copyNewsletter(loggedUser, campaignId)
        redirect(mapping: 'politicianCampaigns')
    }

//    @Secured(['ROLE_CAMPAIGN_NEWSLETTER'])
//    def resendEmail(Long newsletterId, Long tackingMailId) {
//        KuorumUserSession loggedUser = springSecurityService.principal
//        newsletterService.resendEmail(loggedUser, newsletterId, tackingMailId)
//        render "OK"
//    }

    @Secured(['ROLE_CAMPAIGN_NEWSLETTER'])
    def resendEmail(Long campaignId, Long tackingMailId) {
        KuorumUserSession loggedUser = springSecurityService.principal
        newsletterService.resendEmail(loggedUser, campaignId, tackingMailId)
        render "OK"
    }

    @Secured(['ROLE_CAMPAIGN_NEWSLETTER'])
    def showCampaign(Long campaignId) {
        KuorumUserSession loggedUser = springSecurityService.principal
        BulletinRSDTO bulletin = bulletinService.find(loggedUser, campaignId)
        NewsletterRSDTO newsletterRSDTO = bulletin.newsletter
        if (newsletterRSDTO.status == CampaignStatusRSDTO.DRAFT || newsletterRSDTO.status == CampaignStatusRSDTO.SCHEDULED) {
            redirect(mapping: 'politicianMassMailingContent', params: [campaignId: campaignId])
        } else {
            TrackingMailStatsByCampaignPageRSDTO trackingPage = newsletterService.findTrackingMails(loggedUser, newsletterRSDTO.getId())
            List<String> reportsList = printableNewsletterReportList(loggedUser, bulletin.getId())
            render view: 'showCampaign', model: [
                    campaign             : bulletin,
                    campaignStatusesValid: getValidCampaignMailStatus(bulletin),
                    newsletter           : newsletterRSDTO,
                    trackingPage         : trackingPage,
                    reportsList          : reportsList]
        }
    }

    private List<TrackingMailStatusRSDTO> getValidCampaignMailStatus(CampaignRSDTO campaign) {
        switch (campaign.getCampaignType()) {
            case CampaignTypeRSDTO.POST: return [TrackingMailStatusRSDTO.POST_LIKE];
            case CampaignTypeRSDTO.DEBATE: return [TrackingMailStatusRSDTO.DEBATE_PROPOSAL_COMMENT, TrackingMailStatusRSDTO.DEBATE_PROPOSAL_LIKE, TrackingMailStatusRSDTO.DEBATE_PROPOSAL_NEW];
            case CampaignTypeRSDTO.SURVEY: return [TrackingMailStatusRSDTO.SURVEY_ANSWER, TrackingMailStatusRSDTO.SURVEY_FINISHED];
            case CampaignTypeRSDTO.PARTICIPATORY_BUDGET: return [TrackingMailStatusRSDTO.DISTRICT_PROPOSAL_ADD, TrackingMailStatusRSDTO.DISTRICT_PROPOSAL_SUPPORT, TrackingMailStatusRSDTO.DISTRICT_PROPOSAL_VOTE];
            case CampaignTypeRSDTO.DISTRICT_PROPOSAL: return [];
            case CampaignTypeRSDTO.PETITION: return [TrackingMailStatusRSDTO.PETITION_SIGN];
            case CampaignTypeRSDTO.BULLETIN: return [TrackingMailStatusRSDTO.QUEUED, TrackingMailStatusRSDTO.SENT, TrackingMailStatusRSDTO.RESENT, TrackingMailStatusRSDTO.BOUNCED, TrackingMailStatusRSDTO.HARD_BOUNCED, TrackingMailStatusRSDTO.REJECT, TrackingMailStatusRSDTO.SPAM, TrackingMailStatusRSDTO.NOT_SENT, TrackingMailStatusRSDTO.OPEN, TrackingMailStatusRSDTO.CLICK, TrackingMailStatusRSDTO.TRACK_LINK, TrackingMailStatusRSDTO.ANSWERED, TrackingMailStatusRSDTO.UNSUBSCRIBE];
            case CampaignTypeRSDTO.CONTEST: return [TrackingMailStatusRSDTO.CONTEST_APPLICATION_ADD, TrackingMailStatusRSDTO.CONTEST_APPLICATION_APPROVED];
            case CampaignTypeRSDTO.CONTEST_APPLICATION: return [];
            default:
                return [];
        }
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def showCampaignStats(Long campaignId) {
        KuorumUserSession loggedUser = springSecurityService.principal
        CampaignRSDTO campaign = campaignService.find(loggedUser, campaignId, loggedUser.getId().toString())
        Long newsletterId = campaign.newsletter.id
        if (campaign.campaignStatusRSDTO == CampaignStatusRSDTO.DRAFT || campaign.campaignStatusRSDTO == CampaignStatusRSDTO.SCHEDULED) {
            redirect(mapping: 'politicianMassMailingContent', params: [campaignId: campaignId])
        } else {
            TrackingMailStatsByCampaignPageRSDTO trackingPage = newsletterService.findTrackingMails(loggedUser, newsletterId)
            def reportsList = printableCampaignReportList(loggedUser, campaign.getId())
            render view: 'showCampaign',
                    model: [
                            newsletter           : campaign.newsletter,
                            campaignStatusesValid: getValidCampaignMailStatus(campaign),
                            trackingPage         : trackingPage,
                            campaign             : campaign,
                            reportsList          : reportsList]
        }
    }

    private def printableCampaignReportList(KuorumUserSession loggedUser, Long campaignId) {
        return campaignService.getReports(loggedUser, campaignId)
                .collect {
                    g.createLink(
                            mapping: 'politicianCampaignDownloadReport',
                            params: [campaignId: campaignId, fileName: it.split("/").last().split("\\?").first()])
                }.collect { it -> it.encodeAsS3File() }
    }
    //TODO: JOIN BOTH PRINTABLE REPORTS LISTS
    private def printableNewsletterReportList(KuorumUserSession loggedUser, Long campaignId) {
        return newsletterService.getReports(loggedUser, campaignId)
                .collect {
                    g.createLink(
                            mapping: 'politicianNewsletterDownloadReport',
                            params: [campaignId: campaignId, fileName: it.split("/").last().split("\\?").first()])
                }.collect { it -> it.encodeAsS3File() }
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def saveTimeZone(TimeZoneCommand timeZoneCommand) {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)

        if (timeZoneCommand.hasErrors()) {
            flash.error = "There was a problem with your data."
            redirect(mapping: "politicianCampaignsNew")
        } else {
            user.timeZone = TimeZone.getTimeZone(timeZoneCommand.timeZoneId)
            kuorumUserService.updateUser(user)
            if (timeZoneCommand.timeZoneRedirect) {
                redirect url: timeZoneCommand.timeZoneRedirect
            } else {
                redirect(mapping: "politicianCampaignsNew")
            }
        }
    }


    def showMailCampaign(Long campaignId) {
        KuorumUserSession loggedUser = springSecurityService.principal
        CampaignRSDTO bulletinRSDTO = campaignService.find(loggedUser, campaignId);
        NewsletterRSDTO newsletterRSDTO = bulletinRSDTO.newsletter
        render newsletterRSDTO.htmlBody ?: "Not sent"
    }

    def showTrackingMails(Long campaignId) {
        KuorumUserSession loggedUser = springSecurityService.principal
        Integer page = params.page ? Integer.parseInt(params.page) : 0
        Integer size = params.size ? Integer.parseInt(params.size) : 10
        TrackingMailStatusRSDTO status
        try {
            status = TrackingMailStatusRSDTO.valueOf(params.status)
        } catch (Exception e) {
            status = null
        }
        CampaignRSDTO campaign = campaignService.find(loggedUser, campaignId, loggedUser.getId().toString());
        TrackingMailStatsByCampaignPageRSDTO trackingPage = newsletterService.findTrackingMails(loggedUser, campaign.newsletter.id, status, page, size)
        render template: '/newsletter/campaignTabs/campaignRecipeints',
                model: [
                        trackingPage         : trackingPage,
                        campaignId           : campaignId,
                        campaignStatusesValid: getValidCampaignMailStatus(campaign),
                        status               : status
                ]
    }

    def sendReport(Long campaignId) {
        KuorumUserSession loggedUser = springSecurityService.principal
        newsletterService.generateTrackingMailsReport(loggedUser, campaignId)
        Boolean isAjax = request.xhr
        if (isAjax) {
            render([success: "success"] as JSON)
        } else {
            flash.message = g.message(code: 'modal.exportedTrackingEvents.title')
            redirect(mapping: 'politicianMassMailingShow', params: [campaignId: campaignId])
        }
    }

    @Secured(['ROLE_CAMPAIGN_NEWSLETTER'])
    def updateCampaign(MassMailingSettingsCommand command) {
        KuorumUserSession loggedUser = springSecurityService.principal
        Long campaignId = Long.parseLong(params.campaignId)
        if (command.hasErrors()) {
            if (command.errors.allErrors.findAll { it.field == "scheduled" }) {
                flash.error = g.message(code: 'kuorum.web.commands.payment.massMailing.MassMailingCommand.scheduled.min.warn')
            }
            render view: 'createNewsletter', model: modelMassMailingSettings(loggedUser, command, campaignId)
            return
        }
        FilterRDTO anonymousFilter = recoverAnonymousFilter(params, command)
        def dataSend = saveAndSendCampaign(loggedUser, command, campaignId, anonymousFilter)
//        flash.message = dataSend.msg
        redirect mapping: 'politicianMassMailing'
    }

    @Secured(['ROLE_CAMPAIGN_NEWSLETTER'])
    def removeCampaign(Long campaignId) {
        KuorumUserSession loggedUser = springSecurityService.principal
        newsletterService.removeCampaign(loggedUser, campaignId)
        render([msg: "Campaing deleted"] as JSON)
    }

    private FilterRDTO recoverAnonymousFilter(def params, MassMailingSettingsCommand command) {
        ContactFilterCommand filterCommand = bindData(new ContactFilterCommand(), params)
        if (!filterCommand.filterName) {
            filterCommand.filterName = "Custom filter for ${command.campaignName}"
        }
        FilterRDTO filterRDTO = filterCommand.buildFilter()
        if (!filterRDTO?.filterConditions) {
            return null
        }
        return filterRDTO
    }

    private NewsletterRQDTO convertCommandToCampaign(MassMailingCommand command, KuorumUserSession user, FilterRDTO anonymousFilter) {
        NewsletterRQDTO newsletterRQDTO = new NewsletterRQDTO()
        newsletterRQDTO.setName(command.subject)
        newsletterRQDTO.setSubject(command.subject)
        newsletterRQDTO.setBody(command.text)
        newsletterRQDTO.setTriggerTags(command.tags)
        if (command.filterEdited) {
//            anonymousFilter.setName(g.message(code:'tools.contact.filter.anonymousName', args: anonymousFilter.getName()))
            newsletterRQDTO.setAnonymousFilter(anonymousFilter)
        } else {
            newsletterRQDTO.setFilterId(command.filterId)
        }

        if (command.headerPictureId) {
            KuorumFile picture = KuorumFile.get(command.headerPictureId)
            picture = fileService.convertTemporalToFinalFile(picture)
            fileService.deleteTemporalFiles(user)
            newsletterRQDTO.setImageUrl(picture.getUrl())
        }
        newsletterRQDTO
    }

    def sendMassMailingTest(MassMailingContentCommand command) {
        KuorumUserSession loggedUser = springSecurityService.principal
        if (command.hasErrors()) {
            String msg = "error"
            ([msg: msg] as JSON)
            return
        }
        command.sendType = CampaignContentCommand.CAMPAIGN_SEND_TYPE_SEND_TEST

        Long campaignId = params.campaignId ? Long.parseLong(params.campaignId) : null
        def dataSend = saveAndSendContent(loggedUser, command, campaignId)
        render([msg: dataSend.msg, campaing: dataSend.campaign] as JSON)

    }

    private def saveAndSendCampaign(KuorumUserSession user, MassMailingCommand command, Long campaignId = null, FilterRDTO anonymousFilter = null) {
        NewsletterRQDTO newsletterRQDTO = convertCommandToCampaign(command, user, anonymousFilter)
        String msg = ""
        BulletinRSDTO savedCampaign = null
        if (command.getSendType() == CampaignContentCommand.CAMPAIGN_SEND_TYPE_SEND) {
            savedCampaign = newsletterService.campaignSend(user, newsletterRQDTO, campaignId)
            msg = g.message(code: 'tools.massMailing.send.advise', args: [savedCampaign.name])
        } else if (command.sendType == CampaignContentCommand.CAMPAIGN_SEND_TYPE_SCHEDULED) {
            savedCampaign = newsletterService.campaignSchedule(user, newsletterRQDTO, command.getScheduled(), campaignId)
            msg = g.message(code: 'tools.massMailing.schedule.advise', args: [savedCampaign.title, g.formatDate(date: savedCampaign.newsletter.sentOn, type: "datetime", style: "SHORT")])
        } else {
            // IS A DRAFT
            newsletterRQDTO.status = CampaignStatusRSDTO.DRAFT
            savedCampaign = newsletterService.campaignDraft(user, newsletterRQDTO, campaignId)
            msg = g.message(code: 'tools.massMailing.saveDraft.advise', args: [savedCampaign.title])
        }

        if (command.sendType == CampaignContentCommand.CAMPAIGN_SEND_TYPE_SEND_TEST) {
            msg = g.message(code: 'tools.massMailing.saveDraft.adviseTest', args: [savedCampaign.title])
            newsletterService.campaignTest(user, savedCampaign.getNewsletter().getId())
        }
        [msg: msg, campaign: savedCampaign]
    }

    private NewsletterRQDTO transformRStoRQ(KuorumUserSession loggedUser, Long campaignId) {
        if (campaignId) {
            BulletinRSDTO bulletinRSDTO = bulletinService.find(loggedUser, campaignId)
            return transformRStoRQ(bulletinRSDTO.getNewsletter())
        } else {
            return new NewsletterRQDTO()
        }
    }

    private NewsletterRQDTO transformRStoRQ(NewsletterRSDTO rsdto) {
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

    private void updateScheduledCampaignToDraft(KuorumUserSession user, CampaignRSDTO campaignRSDTO) {
        NewsletterRSDTO newsletterRSDTO = campaignRSDTO.newsletter
        if (newsletterRSDTO.status == CampaignStatusRSDTO.SCHEDULED) {
            NewsletterRQDTO newsletterRQDTO = transformRStoRQ(newsletterRSDTO)
            newsletterRQDTO.setSentOn(null)
            newsletterRQDTO.setStatus(CampaignStatusRSDTO.DRAFT)
            newsletterService.campaignDraft(user, newsletterRQDTO, campaignRSDTO.getId())
        }
    }

    def exportCampaigns() {
        KuorumUserSession user = springSecurityService.principal
        newsletterService.findCampaignsCollectionReport(user)
        render([msg: ""] as JSON)
    }


    private NewsletterRQDTO mapSettingsToCampaign(MassMailingSettingsCommand command, KuorumUserSession user, FilterRDTO anonymousFilter, Long campaignId) {
        NewsletterRQDTO newsletterRQDTO = transformRStoRQ(user, campaignId)
        newsletterRQDTO.setName(command.campaignName)
        if (!newsletterRQDTO.getSubject()) {
            newsletterRQDTO.setSubject(command.campaignName)
        }
        newsletterRQDTO.setTriggerTags(command.tags)


        if (command.filterEdited) {
//            anonymousFilter.setName(g.message(code:'tools.contact.filter.anonymousName', args: anonymousFilter.getName()))
            newsletterRQDTO.setAnonymousFilter(anonymousFilter)
            newsletterRQDTO.setFilterId(null)
        } else {
            newsletterRQDTO.setFilterId(command.filterId)
        }
        newsletterRQDTO
    }

    private NewsletterRQDTO mapContentCampaign(MassMailingContentCommand command, KuorumUserSession user, campaignId) {
        NewsletterRQDTO newsletterRQDTO = transformRStoRQ(user, campaignId)

        newsletterRQDTO.body = command.text
        newsletterRQDTO.subject = command.subject

        if (command.headerPictureId) {
            KuorumFile picture = KuorumFile.get(command.headerPictureId)
            picture.setCampaignId(campaignId)
            picture = fileService.convertTemporalToFinalFile(picture)
            fileService.deleteTemporalFiles(user)
            newsletterRQDTO.setImageUrl(picture.getUrl())
        }
        newsletterRQDTO
    }

    //TODO: This action is only for campaigns not for newsletter. Refactor it and move it to campaign controller
    def pauseCampaign(Long campaignId, boolean activeOn) {
        KuorumUserSession loggedUser = springSecurityService.principal
        campaignService.pauseCampaign(loggedUser, campaignId, activeOn)
        render([success: true, msg: "Paused", paused: activeOn] as JSON)
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def refreshReports(Long campaignId) {
        KuorumUserSession loggedUser = springSecurityService.principal
        def reportsList = printableCampaignReportList(loggedUser, campaignId)
        render template: '/newsletter/campaignTabs/campaignReportsList', model: [reportsList: reportsList]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def downloadReport(Long campaignId, String fileName) {
        KuorumUserSession loggedUser = springSecurityService.principal
        campaignService.getReport(loggedUser, campaignId, fileName, response.outputStream)
        response.setContentType("application/octet-stream")
        response.setHeader("Content-disposition", "filename=${fileName}")
        response.outputStream.flush()
        return
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def deleteReport(Long campaignId, String fileName) {
        KuorumUserSession loggedUser = springSecurityService.principal
        campaignService.deleteReport(loggedUser, campaignId, fileName);
        def reportsList = printableCampaignReportList(loggedUser, campaignId)
        render template: '/newsletter/campaignTabs/campaignReportsList', model: [reportsList: reportsList]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def downloadReportNewsletter(Long campaignId, String fileName) {
        KuorumUserSession loggedUser = springSecurityService.principal
        newsletterService.getReport(loggedUser, campaignId, fileName, response.outputStream)
        response.setContentType("application/octet-stream")
        response.setHeader("Content-disposition", "filename=${fileName}")
        response.outputStream.flush()
        return
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def deleteReportNewsletter(Long campaignId, String fileName) {
        KuorumUserSession loggedUser = springSecurityService.principal
        newsletterService.deleteReport(loggedUser, campaignId, fileName);
        def reportsList = printableNewsletterReportList(loggedUser, campaignId)
        render template: '/newsletter/campaignTabs/campaignReportsList', model: [reportsList: reportsList]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def refreshReportsNewsletter(Long campaignId) {
        KuorumUserSession loggedUser = springSecurityService.principal
        def reportsList = printableNewsletterReportList(loggedUser, campaignId)
        render template: '/newsletter/campaignTabs/campaignReportsList', model: [reportsList: reportsList]
    }
}
