package kuorum

import org.kuorum.rest.model.notification.campaign.CampaignRSDTO

class CampaignTagLib {
    static defaultEncodeAs = [taglib:'raw']
    //static encodeAsForTags = [tagName: [taglib:'html'], otherTagName: [taglib:'none']]
    static namespace = "campaignUtil"

    def camapignsSent = {attrs ->

        CampaignRSDTO campaignRSDTO = attrs.campaign
        def campaignsSentValue = campaignRSDTO.numberRecipients>0 ? campaignRSDTO.numberRecipients : ''
        out<< "<span class='recip-number'>${campaignsSentValue}</span>"

    }

    def openRate = {attrs ->

        CampaignRSDTO campaignRSDTO = attrs.campaign
        def openRateValue  = campaignRSDTO.numberRecipients>0 ? campaignRSDTO.numberOpens/campaignRSDTO.numberRecipients*100 : ''
        out<< "<span class='open-number'>${openRateValue}</span>"

    }

    def clickRate = {attrs ->

        CampaignRSDTO campaignRSDTO = attrs.campaign
        def clickRateValue = campaignRSDTO.numberRecipients>0 ? campaignRSDTO.numberClicks/campaignRSDTO.numberRecipients*100 : ''
        out<< "<span class='click-number'>${clickRateValue}</span>"

    }
}
