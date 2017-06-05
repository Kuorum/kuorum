package kuorum

import org.kuorum.rest.model.notification.campaign.CampaignRSDTO

class CampaignTagLib {
    static defaultEncodeAs = [taglib:'raw']
    //static encodeAsForTags = [tagName: [taglib:'html'], otherTagName: [taglib:'none']]
    static namespace = "campaignUtil"

    def campaignsSent = {attrs ->
        CampaignRSDTO campaignRSDTO = attrs.campaign
        out<< campaignRSDTO.numberRecipients?: ''
    }

    def openRate = {attrs ->

        CampaignRSDTO campaignRSDTO = attrs.campaign
        String openRateValue = ""

        if(campaignRSDTO.numberRecipients>0) {
            Number openRateNum = campaignRSDTO.numberOpens/campaignRSDTO.numberRecipients*100
            openRateValue = g.formatNumber(number: openRateNum, maxFractionDigits: 1, type: 'number', format: '\\$###.#0')
        }
        else{
            openRateValue = '- '
        }
        out << """
                <span class="open-number stat" data-openRateNum="${campaignRSDTO.numberOpens}" data-openRatePtg="${openRateValue}%">${openRateValue}%</span>

        """

    }

    def clickRate = {attrs ->

        CampaignRSDTO campaignRSDTO = attrs.campaign
        String clickRateValue = ""

        if(campaignRSDTO.numberRecipients>0) {
            Number clickRateNum = campaignRSDTO.numberClicks/campaignRSDTO.numberRecipients*100
            clickRateValue = g.formatNumber(number: clickRateNum, maxFractionDigits: 1, type: 'number', format: '\\$###.#0')
        }
        else{
            clickRateValue = '- '
        }
        out << """
                <span class="click-number stat" data-openRateNum="${campaignRSDTO.numberClicks}" data-openRatePtg="${clickRateValue}%">${clickRateValue}%</span>

        """

    }
}
