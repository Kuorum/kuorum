package kuorum.debate

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import kuorum.KuorumFile
import kuorum.files.FileService
import kuorum.users.KuorumUser
import kuorum.web.commands.payment.contact.ContactFilterCommand
import kuorum.web.commands.payment.massMailing.DebateCommand
import org.kuorum.rest.model.communication.debate.DebateRDTO
import org.kuorum.rest.model.communication.debate.DebateRSDTO
import org.kuorum.rest.model.contact.ContactPageRSDTO
import org.kuorum.rest.model.contact.filter.ExtendedFilterRSDTO
import org.kuorum.rest.model.contact.filter.FilterRDTO
import org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO
import org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO
import payment.campaign.DebateService
import payment.contact.ContactService

class DebateController {

    def springSecurityService

    FileService fileService
    ContactService contactService
    DebateService debateService

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def create() {
        return debateModel(new DebateCommand(), null)
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def save(DebateCommand command) {
        if (command.hasErrors()) {
            render view: '/debate/create', model: debateModel(command, null)
            return
        }

        // Send debate
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        FilterRDTO anonymousFilter = recoverAnonymousFilter(params, command)
        Map<String, Object> resultDebate = saveAndSendDebate(user, command, null, anonymousFilter)

        flash.message = resultDebate.msg.toString()

        redirect mapping: "politicianMassMailing", params: []
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

        // Convert debate to command
        DebateCommand debateCommand = new DebateCommand()
        debateCommand.title = debateRSDTO.title
        debateCommand.body = debateRSDTO.body
        debateCommand.publishOn = debateRSDTO.publishOn

        // Tags
        if (debateRSDTO.triggeredTags) {
            debateCommand.setEventsWithTag(new ArrayList<TrackingMailStatusRSDTO>())
            debateCommand.setEventsWithTag(debateRSDTO.triggeredTags.keySet() as List)
            debateCommand.setTags(debateRSDTO.triggeredTags.values().flatten())
        }

        // Multimedia URL
        if (debateRSDTO.videoUrl?.contains("youtube")) {
            debateCommand.videoPost = debateRSDTO.videoUrl
        } else if (debateRSDTO.photoUrl) {
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

        // Save debate
        FilterRDTO anonymousFilter = recoverAnonymousFilter(params, command)
        Map<String, Object> resultDebate = saveAndSendDebate(user, command, debateId, anonymousFilter)

        flash.message = resultDebate.msg.toString()

        redirect mapping: "politicianMassMailing", params: []
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

    private FilterRDTO recoverAnonymousFilter(params, DebateCommand command) {
        ContactFilterCommand filterCommand = (ContactFilterCommand) bindData(new ContactFilterCommand(), params)
        if (!filterCommand.filterName) {
            filterCommand.filterName = "Custom filter for ${command.title}"
        }
        FilterRDTO filterRDTO = filterCommand.buildFilter()
        if (!filterRDTO?.filterConditions) {
            return null
        }
        return filterRDTO
    }

    private Map<String, Object> saveAndSendDebate(KuorumUser user, DebateCommand command,
                  Long debateId = null, FilterRDTO anonymousFilter = null) {
        DebateRDTO debateRDTO = convertCommandToDebate(command, user, anonymousFilter)

        String msg
        DebateRSDTO savedDebate
        if (command.publishOn != null) {
            savedDebate = debateService.saveDebate(user, debateRDTO, debateId)
            msg = g.message(code: 'tools.massMailing.schedule.advise', args: [
                    savedDebate.title,
                    g.formatDate(date: command.publishOn, type: "datetime", style: "SHORT")
            ])
        } else {
            // IS A DRAFT
            savedDebate = debateService.saveDebate(user, debateRDTO, debateId)
            msg = g.message(code:'tools.massMailing.saveDraft.advise', args: [savedDebate.title])
        }

        [msg: msg, debate: savedDebate]
    }

    private DebateRDTO convertCommandToDebate(DebateCommand command, KuorumUser user, FilterRDTO anonymousFilter) {
        DebateRDTO debateRDTO = new DebateRDTO()
        debateRDTO.setTitle(command.getTitle())
        debateRDTO.setBody(command.getBody())
        debateRDTO.setPublishOn(command.getPublishOn())

        // Tags
        debateRDTO.setTriggeredTags(new HashMap<TrackingMailStatusRSDTO, List<String>>())
        for (TrackingMailStatusRSDTO trackingMailStatusRSDTO : command.eventsWithTag) {
            debateRDTO.getTriggeredTags().put(trackingMailStatusRSDTO, command.tags)
        }

        // Filter
        if (command.filterEdited) {
            //anonymousFilter.setName(g.message(code:'tools.contact.filter.anonymousName', args: anonymousFilter.getName()))
            debateRDTO.setAnonymousFilter(anonymousFilter)
        } else {
            debateRDTO.setFilterId(command.filterId)
        }

        // Multimedia URL
        if (command.headerPictureId) {
            KuorumFile picture = KuorumFile.get(command.headerPictureId)
            picture = fileService.convertTemporalToFinalFile(picture)
            fileService.deleteTemporalFiles(user)
            debateRDTO.setPhotoUrl(picture.getUrl())
        } else if (command.videoPost) {
            KuorumFile urlYoutubeFile = fileService.createYoutubeKuorumFile(command.videoPost, user)
            debateRDTO.setVideoUrl(urlYoutubeFile?.url)
        }

        debateRDTO
    }

}
