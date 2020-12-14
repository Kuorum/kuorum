package kuorum

import kuorum.core.model.solr.SolrType
import org.kuorum.rest.model.communication.CampaignRSDTO
import org.kuorum.rest.model.communication.CampaignTypeRSDTO
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
        if (campaign.event){
            // This option has sense if the campaign has an event
            Boolean isClosed = event.eventDate < new Date() && campaign.newsletter?.status== CampaignStatusRSDTO.SENT
            Boolean isNotOpen = campaign.newsletter?.status!= CampaignStatusRSDTO.SENT
            String closedMsg="";

            if (isClosed){
                closedMsg = g.message(code:'event.callToAction.subTitle.close', args: [campaign.user.name])
            }else if(isNotOpen){
                closedMsg = g.message(code:'event.callToAction.subTitle.noOpen', args: [campaign.user.name])
            }

            if (isClosed || isNotOpen ){
                out << body(closedMsg:closedMsg)
            }

        }
    }

    def showIcon  = {attrs ->
        CampaignRSDTO campaign = attrs.campaign
        SolrType solrType
        SolrType defaultValue = attrs.defaultType?:SolrType.KUORUM_USER
        if (campaign != null){
            solrType = SolrType.getByCampaign(campaign)
        }else{
            CampaignTypeRSDTO campaignType = attrs.campaignType
            solrType = SolrType.getByCampaignType(campaignType)
        }
        if (solrType == null){
            solrType = defaultValue
        }
        String css= attrs.css
        out << "<span class='fal ${solrType.getFaIcon()} $css'></span>"
    }
}
