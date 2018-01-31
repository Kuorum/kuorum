package kuorum

import org.kuorum.rest.model.notification.campaign.NewsletterRSDTO

class NewsletterTagLib {
    static defaultEncodeAs = [taglib:'raw']
    //static encodeAsForTags = [tagName: [taglib:'html'], otherTagName: [taglib:'none']]
    static namespace = "newsletterUtil"

    def campaignsSent = {attrs ->
        NewsletterRSDTO campaignRSDTO = attrs.campaign
        out<< campaignRSDTO.numberRecipients?: ''
    }

    def openRate = {attrs ->

        NewsletterRSDTO campaignRSDTO = attrs.campaign
        out << printPrettyStat(campaignRSDTO.numberOpens,campaignRSDTO.numberRecipients)
    }

    def clickRate = {attrs ->

        NewsletterRSDTO campaignRSDTO = attrs.campaign
        out << printPrettyStat(campaignRSDTO.numberClicks,campaignRSDTO.numberRecipients)
    }

    def unsubscribeRate = {attrs ->

        NewsletterRSDTO campaignRSDTO = attrs.campaign
        out << printPrettyStat(campaignRSDTO.numberUnsubscribe,campaignRSDTO.numberRecipients)
    }

    private String printPrettyStat(numberOfEvents, numberRecipients){
        String rateValue = ""

        if(numberRecipients>0) {
            Number clickRateNum = numberOfEvents/numberRecipients*100
            rateValue = g.formatNumber(number: clickRateNum, maxFractionDigits: 1, type: 'number', format: '\\$###.#0')
        }
        else{
            rateValue = '- '
        }
        return """
                <span class="click-number stat" data-openRateNum="${numberOfEvents}" data-openRatePtg="${rateValue}%">${rateValue}%</span>

        """
    }
}
