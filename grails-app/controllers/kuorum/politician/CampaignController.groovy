package kuorum.politician

import grails.plugin.springsecurity.SpringSecurityService
import kuorum.campaign.Campaign
import org.kuorum.rest.model.communication.CampaignRDTO
import org.kuorum.rest.model.communication.debate.DebateRDTO
import org.kuorum.rest.model.communication.event.EventRDTO
import payment.campaign.PostService
import kuorum.users.KuorumUser
import kuorum.web.commands.payment.CampaignSettingsCommand
import kuorum.web.commands.payment.contact.ContactFilterCommand
import org.bson.types.ObjectId
import org.kuorum.rest.model.communication.CampaignRSDTO
import org.kuorum.rest.model.communication.debate.DebateRSDTO
import org.kuorum.rest.model.communication.post.PostRSDTO
import org.kuorum.rest.model.contact.ContactPageRSDTO
import org.kuorum.rest.model.contact.filter.ExtendedFilterRSDTO
import org.kuorum.rest.model.contact.filter.FilterRDTO
import org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO
import payment.campaign.DebateService
import payment.campaign.CampaignService
import payment.contact.ContactService

class CampaignController {

    PostService postService
    DebateService debateService
    ContactService contactService
    SpringSecurityService springSecurityService

    def findLiUserCampaigns(String userId){
        KuorumUser user = KuorumUser.get(new ObjectId(userId));
        List<PostRSDTO> posts = postService.findAllPosts(user).findAll{it.newsletter.status==CampaignStatusRSDTO.SENT};
        List<DebateRSDTO> debates = debateService.findAllDebates(user).findAll{it.newsletter.status==CampaignStatusRSDTO.SENT};
        List<CampaignRSDTO> campaigns = posts + debates
        render template: '/campaigns/cards/campaignsList', model: [campaigns:campaigns, showAuthor:true]

    }

    protected def modelSettings(CampaignSettingsCommand command, CampaignRSDTO campaignRSDTO = null) {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        List<ExtendedFilterRSDTO> filters = contactService.getUserFilters(user)
        ContactPageRSDTO contactPageRSDTO = contactService.getUsers(user)

        if(campaignRSDTO){
            command.campaignName = campaignRSDTO.name
            command.tags = campaignRSDTO.triggeredTags
            command.filterId = campaignRSDTO.newsletter?.filter?.id
            if (campaignRSDTO.hasProperty('causes')){
                command.causes = campaignRSDTO.causes
            }
            if(campaignRSDTO.newsletter.filter && !filters.find{it.id==campaignRSDTO.newsletter.filter.id}){
                ExtendedFilterRSDTO anonymousFilter = contactService.getFilter(user, campaignRSDTO.newsletter.filter.id)
                filters.add(anonymousFilter)
            }
        }
        [
                filters: filters,
                command: command,
                totalContacts: contactPageRSDTO.total,
                campaign: campaignRSDTO
        ]
    }

    protected FilterRDTO recoverAnonymousFilterSettings(params, CampaignSettingsCommand command) {
        ContactFilterCommand filterCommand = (ContactFilterCommand) bindData(new ContactFilterCommand(), params)
        contactService.transformCommand(filterCommand, "Custom filter for ${command.campaignName}")
    }

    protected mapCommandSettingsToRDTO(CampaignRDTO rdto, CampaignSettingsCommand command, FilterRDTO anonymousFilter){
        rdto.name = command.campaignName
        rdto.setTriggeredTags(command.tags)
        rdto.causes = command.causes
        if (command.filterEdited) {
            //anonymousFilter.setName(g.message(code:'tools.contact.filter.anonymousName', args: anonymousFilter.getName()))
            rdto.setAnonymousFilter(anonymousFilter)
            rdto.setFilterId(null)
        } else {
            rdto.setFilterId(command.filterId)
        }
        if (command.eventAttached && !rdto.event){
            rdto.event = new EventRDTO();
        }
        rdto
    }

    protected CampaignRDTO createRDTO(KuorumUser user, Long campaignId, CampaignService campaignService){
        CampaignRSDTO campaignRSDTO = campaignService.find(user, campaignId)
        return campaignService.map(campaignRSDTO)
    }

    protected CampaignRDTO convertCommandSettingsToRDTO(
            CampaignSettingsCommand command,
            KuorumUser user,
            FilterRDTO anonymousFilter,
            Long debateId,
            CampaignService campaignService) {
        CampaignRDTO campaignRDTO = createRDTO(user, debateId, campaignService)
        mapCommandSettingsToRDTO(campaignRDTO, command, anonymousFilter)
    }

    protected Map<String, Object> saveCampaignSettings(
            KuorumUser user,
            CampaignSettingsCommand command,
            Long campaignId = null,
            FilterRDTO anonymousFilter = null,
            CampaignService campaignService){
        CampaignRDTO campaignRDTO = convertCommandSettingsToRDTO(command, user, anonymousFilter, campaignId, campaignService)
        CampaignRSDTO campaignSaved = campaignService.save(user, campaignRDTO, campaignId)
        String msg = g.message(code:'tools.massMailing.saveDraft.advise', args: [campaignSaved.title])

        [msg: msg, campaign: campaignSaved]
    }
}
