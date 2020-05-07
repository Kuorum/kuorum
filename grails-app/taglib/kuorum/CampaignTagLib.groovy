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
                campaign.newsletter?.status== org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SENT && !campaign.closed){
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
            }else if(campaign.closed){
                String callClosedTimeAgo;
                if (campaign.endDate?.before(new Date())) {
                    callClosedTimeAgo = kuorumDate.humanDate(date: campaign.endDate)
                    closedMsg = g.message(code:'event.callToAction.subTitle.campaign.close.after', args: [callClosedTimeAgo])
                }else {
                    callClosedTimeAgo = kuorumDate.humanDate(date: campaign.startDate)
                    closedMsg = g.message(code:'event.callToAction.subTitle.campaign.close.before', args: [callClosedTimeAgo])
                }
            }else if(isNotOpen){
                closedMsg = g.message(code:'event.callToAction.subTitle.noOpen', args: [campaign.user.name])
            }

            if (isClosed || isNotOpen || campaign.closed){
                out << body(closedMsg:closedMsg)
            }

        }
    }

    def showIcon  = {attrs ->
        CampaignRSDTO campaign = attrs.campaign
        String css= attrs.css
        SolrType solrType = SolrType.getByCampaign(campaign)
        out << "<span class='fal ${solrType.getFaIcon()} $css'></span>"
    }
}
