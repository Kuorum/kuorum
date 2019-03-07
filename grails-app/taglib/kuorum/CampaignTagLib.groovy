package kuorum

import kuorum.core.model.solr.SolrType
import org.kuorum.rest.model.communication.CampaignRSDTO
import org.kuorum.rest.model.communication.event.EventRSDTO
import org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO

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
        // Evaluates if the event is already close
        Boolean evaluatesIfIsClose=attrs.evaluatesIfIsClose?Boolean.parseBoolean(attrs.evaluatesIfIsClose):false
        // Evaluates if the event is not open yet
        Boolean evaluatesIfIsNotOpen=attrs.evaluatesIfIsNotOpen?Boolean.parseBoolean(attrs.evaluatesIfIsNotOpen):false

        if (campaign.event){
            // This option has sense if the campaign has an event
            Boolean isClosed = evaluatesIfIsClose? event.eventDate < new Date() && campaign.newsletter?.status== CampaignStatusRSDTO.SENT:false
            Boolean isNotOpen = evaluatesIfIsNotOpen? campaign.newsletter?.status!= CampaignStatusRSDTO.SENT:false
            if (isClosed || isNotOpen){
                out << body()
            }

        }
    }

    def showIcon  = {attrs ->
        CampaignRSDTO campaign = attrs.campaign
        SolrType solrType = SolrType.getByCampaign(campaign)
        out << "<span class='fa ${solrType.getFaIcon()}'></span>"
    }
}
