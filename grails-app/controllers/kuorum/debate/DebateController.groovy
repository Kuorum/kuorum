package kuorum.debate

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import kuorum.KuorumFile
import kuorum.core.FileType
import kuorum.core.exception.KuorumException
import kuorum.files.FileService
import kuorum.users.CookieUUIDService
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserService
import kuorum.web.commands.payment.contact.ContactFilterCommand
import kuorum.web.commands.payment.debate.DebateCommand
import kuorum.web.commands.payment.debate.DebateContentCommand
import kuorum.web.commands.payment.debate.DebateSettingsCommand
import org.kuorum.rest.model.communication.debate.DebateRDTO
import org.kuorum.rest.model.communication.debate.DebateRSDTO
import org.kuorum.rest.model.communication.debate.search.ProposalPageRSDTO
import org.kuorum.rest.model.communication.debate.search.SearchProposalRSDTO
import org.kuorum.rest.model.communication.debate.search.SortProposalRDTO
import org.kuorum.rest.model.contact.ContactPageRSDTO
import org.kuorum.rest.model.contact.filter.ExtendedFilterRSDTO
import org.kuorum.rest.model.contact.filter.FilterRDTO
import org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO
import org.springframework.security.access.AccessDeniedException
import payment.campaign.DebateService
import payment.campaign.ProposalService
import payment.contact.ContactService

import javax.servlet.http.HttpServletResponse

class DebateController {

    def springSecurityService

    KuorumUserService kuorumUserService
    FileService fileService
    ContactService contactService
    DebateService debateService
    ProposalService proposalService
    CookieUUIDService cookieUUIDService

    def show() {
        String viewerId = cookieUUIDService.buildUserUUID()
        try {
            DebateRSDTO debate = debateService.findDebate( params.userAlias, Long.parseLong((String) params.debateId),viewerId)
            if (!debate) {
                throw new KuorumException(message(code: "debate.notFound") as String)
            }

            KuorumUser debateUser = kuorumUserService.findByAlias(debate.userAlias)
            SearchProposalRSDTO searchProposalRSDTO = new SearchProposalRSDTO()
            searchProposalRSDTO.sort = new SortProposalRDTO()
            searchProposalRSDTO.sort.direction = SortProposalRDTO.Direction.DESC
            searchProposalRSDTO.sort.field = SortProposalRDTO.Field.LIKES
            searchProposalRSDTO.size = Integer.MAX_VALUE // Sorting and filtering will be done using JS. We expect maximum 100 proposals

            ProposalPageRSDTO proposalPage = proposalService.findProposal(debate, searchProposalRSDTO,viewerId)

            return [debate: debate, debateUser: debateUser, proposalPage:proposalPage]
        } catch (Exception ignored) {
            flash.error = message(code: "debate.notFound")
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return false
        }
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def create() {
        return debateModelSettings(new DebateSettingsCommand(), null)
    }

    def editSettingsStep(){
        String viewerUid = cookieUUIDService.buildUserUUID()
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        DebateRSDTO debateRSDTO = debateService.findDebate( user, Long.parseLong((String) params.debateId), viewerUid)

        return debateModelSettings(new DebateSettingsCommand(), debateRSDTO)

    }

    def editContentStep(){
        String viewerUid = cookieUUIDService.buildUserUUID()
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        DebateRSDTO debateRSDTO = debateService.findDebate( user, Long.parseLong((String) params.debateId), viewerUid)

        return debateModelContent(new DebateContentCommand(), debateRSDTO)

    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def save(DebateCommand command) {
        if (command.hasErrors()) {
            render view: 'create', model: debateModel(command, null)
            return
        }

        // Send debate
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        FilterRDTO anonymousFilter = recoverAnonymousFilter(params, command)
        Map<String, Object> resultDebate = saveAndSendDebate(user, command, null, anonymousFilter)

        flash.message = resultDebate.msg.toString()

        redirect mapping: "debateShow", params: resultDebate.debate.encodeAsLinkProperties()
    }

    def saveSettings(DebateSettingsCommand command) {
        if (command.hasErrors()) {
            render view: 'create', model: debateModelSettings(command, null)
            return
        }

        String nextStep = params.redirectLink
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        FilterRDTO anonymousFilter = recoverAnonymousFilterSettings(params, command)
        Map<String, Object> resultDebate = saveAndSendDebateSettings(user, command, null, anonymousFilter)

        //flash.message = resultDebate.msg.toString()

        redirect mapping: nextStep, params: resultDebate.debate.encodeAsLinkProperties()
    }

    def saveContent(DebateContentCommand command) {
        if (command.hasErrors()) {
            render view: 'create', model: debateModelContent(command, null)
            return
        }

        String nextStep = params.redirectLink
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        Map<String, Object> resultDebate = saveAndSendDebateContent(user, command, null)

        flash.message = resultDebate.msg.toString()

        redirect mapping: nextStep, params: resultDebate.debate.encodeAsLinkProperties()
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def edit(Long debateId) {
        KuorumUser loggedUser = KuorumUser.get(springSecurityService.principal.id)
        DebateRSDTO debateRSDTO = debateService.findDebate(loggedUser, debateId)

        if (!debateRSDTO) {
            flash.message = message(code: 'admin.createDebateUpdate.debate.notFound', args: [])
            redirect mapping: "home"
            return
        }
        if (debateRSDTO.userAlias != loggedUser.alias){
            throw new AccessDeniedException("This debate is not yours");
        }

        // Convert debate to command
        DebateCommand debateCommand = new DebateCommand()
        debateCommand.title = debateRSDTO.title
        debateCommand.body = debateRSDTO.body
        debateCommand.publishOn = debateRSDTO.datePublished

        // Tags
        if (debateRSDTO.triggeredTags) {
            debateCommand.setTags(debateRSDTO.triggeredTags)
        }

        // Multimedia URL
        if (debateRSDTO.videoUrl) {
            debateCommand.fileType = FileType.YOUTUBE.toString()
            debateCommand.videoPost = debateRSDTO.videoUrl
        } else if (debateRSDTO.photoUrl) {
            debateCommand.fileType = FileType.IMAGE.toString()
            KuorumFile kuorumFile = KuorumFile.findByUrl(debateRSDTO.photoUrl)
            debateCommand.headerPictureId = kuorumFile?.id
        }

        // Filter
        def model = debateModel(debateCommand, debateId)
        debateCommand.filterId = debateRSDTO.anonymousFilter?.id ?: null
        if (debateRSDTO.anonymousFilter && !model.filters.find{it.id == debateRSDTO.anonymousFilter.id}) {
            // Not found debate filter. That means that the filter is custom filter for the campaign
            ExtendedFilterRSDTO anonymousFilter = contactService.getFilter(loggedUser, debateRSDTO.anonymousFilter.id)
            model.put("anonymousFilter", anonymousFilter)
        }

        // Edit while sent or while draft
        if (debateRSDTO.campaignStatusRSDTO == CampaignStatusRSDTO.SENT) {
            render view: 'edit', model: model
        } else {
            render view: 'create', model: model
        }
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def update(DebateCommand command, Long debateId) {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        DebateRSDTO debateRSDTO = debateService.findDebate(user, debateId)

        if (debateRSDTO.userAlias != user.alias){
            throw new AccessDeniedException("This debate is not yours");
        }

        if (!debateRSDTO) {
            flash.message = message(code: 'admin.createProjectUpdate.project.not.found', args: [])
            redirect mapping: "home"
            return
        }
        if (!command.validate()) {
            // Edit while sent or while draft
            if (debateRSDTO.campaignStatusRSDTO == CampaignStatusRSDTO.SENT) {
                render view: 'edit', model: debateModel(command, null)
            } else {
                render view: 'create', model: debateModel(command, null)
            }
            return
        }
        if (debateRSDTO.campaignStatusRSDTO == CampaignStatusRSDTO.SENT) {
            // TAGS CAN NOT BE OVERWRITTEN
            command.setTags(debateRSDTO.getTriggeredTags())
        }
        // Save debate
        FilterRDTO anonymousFilter = recoverAnonymousFilter(params, command)
        Map<String, Object> resultDebate = saveAndSendDebate(user, command, debateId, anonymousFilter)

        flash.message = resultDebate.msg.toString()

        redirect mapping: "debateShow", params: resultDebate.debate.encodeAsLinkProperties()
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def remove(Long debateId) {
        // TODO: This function is incomplete in other places
        KuorumUser loggedUser = KuorumUser.get(springSecurityService.principal.id)
        debateService.removeDebate(loggedUser, debateId)
        render ([msg: "Debate deleted"] as JSON)
    }

    private def debateModel(DebateCommand command, Long debateId) {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        List<ExtendedFilterRSDTO> filters = contactService.getUserFilters(user)
        ContactPageRSDTO contactPageRSDTO = contactService.getUsers(user)

        [
                filters: filters,
                command: command,
                totalContacts: contactPageRSDTO.total,
                debateId: debateId
        ]
    }

    private def debateModelSettings(DebateSettingsCommand command, DebateRSDTO debateRSDTO) {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        List<ExtendedFilterRSDTO> filters = contactService.getUserFilters(user)
        ContactPageRSDTO contactPageRSDTO = contactService.getUsers(user)

        [
                filters: filters,
                command: command,
                totalContacts: contactPageRSDTO.total,
                debate: debateRSDTO
        ]
    }

    private def debateModelContent(DebateContentCommand command, DebateRSDTO debateRSDTO) {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        List<ExtendedFilterRSDTO> filters = contactService.getUserFilters(user)
        ContactPageRSDTO contactPageRSDTO = contactService.getUsers(user)

        if(debateRSDTO) {
            command.title = debateRSDTO.title
            command.body = debateRSDTO.body

            if (debateRSDTO.photoUrl) {
                KuorumFile kuorumFile = KuorumFile.findByUrl(debateRSDTO.photoUrl)
                command.headerPictureId = kuorumFile?.id
            }

            if (debateRSDTO.videoUrl) {
                KuorumFile kuorumFile = KuorumFile.findByUrl(debateRSDTO.videoUrl)
                command.videoPost = kuorumFile?.id
            }
        }

        [
                filters: filters,
                command: command,
                totalContacts: contactPageRSDTO.total,
                debate: debateRSDTO
        ]
    }

    private FilterRDTO recoverAnonymousFilter(params, DebateCommand command) {
        ContactFilterCommand filterCommand = (ContactFilterCommand) bindData(new ContactFilterCommand(), params)
        contactService.transformCommand(filterCommand, "Custom filter for ${command.title}")
    }

    private FilterRDTO recoverAnonymousFilterSettings(params, DebateSettingsCommand command) {
        ContactFilterCommand filterCommand = (ContactFilterCommand) bindData(new ContactFilterCommand(), params)
        contactService.transformCommand(filterCommand, "Custom filter for ${command.campaignName}")
    }

    private Map<String, Object> saveAndSendDebate(KuorumUser user, DebateCommand command,
                  Long debateId = null, FilterRDTO anonymousFilter = null) {
        DebateRDTO debateRDTO = convertCommandToDebate(command, user, anonymousFilter)

        String msg
        DebateRSDTO savedDebate
        if (command.publishOn) {
            savedDebate = debateService.saveDebate(user, debateRDTO, debateId)
            if (command.publishOn < new Date()) {
                msg = g.message(code: 'tools.massMailing.saved.advise', args: [
                        savedDebate.title
                ])
            } else {
                msg = g.message(code: 'tools.massMailing.schedule.advise', args: [
                        savedDebate.title,
                        g.formatDate(date: command.publishOn, type: "datetime", style: "SHORT")
                ])
            }
        } else {
            // IS A DRAFT
            savedDebate = debateService.saveDebate(user, debateRDTO, debateId)
            msg = g.message(code:'tools.massMailing.saveDraft.advise', args: [savedDebate.title])
        }

        [msg: msg, debate: savedDebate]
    }

    private Map<String, Object> saveAndSendDebateSettings(KuorumUser user, DebateSettingsCommand command,
                                                  Long debateId = null, FilterRDTO anonymousFilter = null) {

        DebateRDTO debateRDTO = convertCommandSettingsToDebate(command, user, anonymousFilter, debateId)
        DebateRSDTO savedDebate = debateService.saveDebate(user, debateRDTO, debateId)
        String msg = g.message(code:'tools.massMailing.saveDraft.advise', args: [savedDebate.title])

        [msg: msg, debate: savedDebate]
    }

    private Map<String, Object> saveAndSendDebateContent(KuorumUser user, DebateContentCommand command,
                                                  Long debateId = null) {
        DebateRDTO debateRDTO = convertCommandContentToDebate(command, user, debateId)

        String msg
        DebateRSDTO savedDebate
        if (command.publishOn) {
            savedDebate = debateService.saveDebate(user, debateRDTO, debateId)
            if (command.publishOn < new Date()) {
                msg = g.message(code: 'tools.massMailing.saved.advise', args: [
                        savedDebate.title
                ])
            } else {
                msg = g.message(code: 'tools.massMailing.schedule.advise', args: [
                        savedDebate.title,
                        g.formatDate(date: command.publishOn, type: "datetime", style: "SHORT")
                ])
            }
        } else {
            // IS A DRAFT
            savedDebate = debateService.saveDebate(user, debateRDTO, debateId)
            msg = g.message(code:'tools.massMailing.saveDraft.advise', args: [savedDebate.title])
        }

        [msg: msg, debate: savedDebate]
    }

    private DebateRDTO convertCommandToDebate(DebateCommand command, KuorumUser user, FilterRDTO anonymousFilter) {
        DebateRDTO debateRDTO = new DebateRDTO()
        debateRDTO.setTitle(command.title)
        debateRDTO.setBody(command.body)
        debateRDTO.setPublishOn(command.publishOn)

        // Tags
        debateRDTO.setTriggeredTags(command.tags)

        // Filter
        if (command.filterEdited) {
            //anonymousFilter.setName(g.message(code:'tools.contact.filter.anonymousName', args: anonymousFilter.getName()))
            debateRDTO.setAnonymousFilter(anonymousFilter)
        } else {
            debateRDTO.setFilterId(command.filterId)
        }

        // Multimedia URL
        if (command.fileType == FileType.IMAGE.toString() && command.headerPictureId) {
            // Save image
            KuorumFile picture = KuorumFile.get(command.headerPictureId)
            picture = fileService.convertTemporalToFinalFile(picture)
            fileService.deleteTemporalFiles(user)
            debateRDTO.setPhotoUrl(picture.getUrl())

            // Remove video
            debateRDTO.setVideoUrl(null)
        } else if (command.fileType == FileType.YOUTUBE.toString() && command.videoPost) {
            // Save video
            KuorumFile urlYoutubeFile = fileService.createYoutubeKuorumFile(command.videoPost, user)
            debateRDTO.setVideoUrl(urlYoutubeFile?.url)

            // Remove image
            if (command.headerPictureId) {
                KuorumFile picture = KuorumFile.get(command.headerPictureId)
                fileService.deleteKuorumFile(picture)
                command.setHeaderPictureId(null)
            }
        }

        debateRDTO
    }

    private DebateRDTO convertCommandSettingsToDebate(DebateSettingsCommand command, KuorumUser user, FilterRDTO anonymousFilter, Long debateId) {
        DebateRDTO debateRDTO = new DebateRDTO()
        debateRDTO.title = command.campaignName
        debateRDTO.setTriggeredTags(command.tags)
        if (command.filterEdited) {
            //anonymousFilter.setName(g.message(code:'tools.contact.filter.anonymousName', args: anonymousFilter.getName()))
            debateRDTO.setAnonymousFilter(anonymousFilter)
        } else {
            debateRDTO.setFilterId(command.filterId)
        }

        if(debateId){
            DebateRSDTO debateRSDTO = debateService.findDebate(user, debateId)
            debateRDTO.photoUrl = debateRSDTO.photoUrl
            debateRDTO.videoUrl = debateRSDTO.videoUrl
            debateRDTO.title = debateRSDTO.title
            debateRDTO.body = debateRDTO.body
        }

        debateRDTO
    }

    private DebateRDTO convertCommandContentToDebate(DebateContentCommand command, KuorumUser user, Long debateId) {
        DebateRDTO debateRDTO = new DebateRDTO()
        debateRDTO.setTitle(command.title)
        debateRDTO.setBody(command.body)
        debateRDTO.setPublishOn(command.publishOn)

        // Multimedia URL
        if (command.fileType == FileType.IMAGE.toString() && command.headerPictureId) {
            // Save image
            KuorumFile picture = KuorumFile.get(command.headerPictureId)
            picture = fileService.convertTemporalToFinalFile(picture)
            fileService.deleteTemporalFiles(user)
            debateRDTO.setPhotoUrl(picture.getUrl())

            // Remove video
            debateRDTO.setVideoUrl(null)
        } else if (command.fileType == FileType.YOUTUBE.toString() && command.videoPost) {
            // Save video
            KuorumFile urlYoutubeFile = fileService.createYoutubeKuorumFile(command.videoPost, user)
            debateRDTO.setVideoUrl(urlYoutubeFile?.url)

            // Remove image
            if (command.headerPictureId) {
                KuorumFile picture = KuorumFile.get(command.headerPictureId)
                fileService.deleteKuorumFile(picture)
                command.setHeaderPictureId(null)
            }
        }

        if(debateId){
            DebateRSDTO debateRSDTO = debateService.findDebate(user, debateId)
            debateRDTO.title = debateRSDTO.title
            debateRDTO.triggeredTags = debateRSDTO.triggeredTags
        }

        debateRDTO
    }
}
