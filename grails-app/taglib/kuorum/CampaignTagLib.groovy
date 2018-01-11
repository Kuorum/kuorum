package kuorum

import org.kuorum.rest.model.communication.CampaignRSDTO
import org.kuorum.rest.model.communication.event.EventRSDTO

class CampaignTagLib {
    static defaultEncodeAs = [taglib:'raw']
    //static encodeAsForTags = [tagName: [taglib:'html'], otherTagName: [taglib:'none']]
    static namespace = "campaignUtil"

    def ifActiveEvent = {attrs, body ->
        CampaignRSDTO campaign = attrs.campaign
        EventRSDTO event = campaign.event
        // PRINTS ONLY IF THE CAMPAIGN HAS AN EVENT
        if (event &&
            event.eventDate >= new Date() &&
                campaign.newsletter?.status== org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SENT){
            out << body()
        }
    }

    def ifInactiveEvent = {attrs, body ->
        CampaignRSDTO campaign = attrs.campaign
        EventRSDTO event = campaign.event
        // PRINTS ONLY IF THE CAMPAIGN HAS AN EVENT
        if (event &&
                event.eventDate < new Date() &&
                campaign.newsletter?.status!= org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SENT){
            out << body()
        }

    }
}
