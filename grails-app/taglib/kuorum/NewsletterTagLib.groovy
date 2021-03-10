package kuorum

import org.kuorum.rest.model.notification.campaign.NewsletterLightRSDTO
import org.kuorum.rest.model.notification.campaign.NewsletterRSDTO

class NewsletterTagLib {
    static defaultEncodeAs = [taglib:'raw']
    //static encodeAsForTags = [tagName: [taglib:'html'], otherTagName: [taglib:'none']]
    static namespace = "newsletterUtil"

    def campaignsSent = {attrs ->
        Long numberRecipients=0
        if (attrs.campaign instanceof NewsletterRSDTO){
            NewsletterRSDTO newsletter = attrs.campaign
            numberRecipients= newsletter.numberRecipients
        }else if (attrs.campaign instanceof NewsletterLightRSDTO){
            NewsletterLightRSDTO newsletter = attrs.campaign
            numberRecipients= newsletter.numberRecipients
        }
        out<< numberRecipients?: ''
    }

    def openRate = {attrs ->

        Long numberOpens=0
        Long numberRecipients=0
        if (attrs.campaign instanceof NewsletterRSDTO){
            NewsletterRSDTO newsletter = attrs.campaign
            numberRecipients= newsletter.numberRecipients
            numberOpens= newsletter.numberOpens
        }else if (attrs.campaign instanceof NewsletterLightRSDTO){
            NewsletterLightRSDTO newsletter = attrs.campaign
            numberRecipients= newsletter.numberRecipients
            numberOpens= newsletter.numberOpens
        }
        NewsletterRSDTO campaignRSDTO = attrs.campaign
        out << printPrettyStat(numberOpens,numberRecipients)
    }

    def clickRate = {attrs ->
        Long numberClicks=0
        Long numberRecipients=0
        if (attrs.campaign instanceof NewsletterRSDTO){
            NewsletterRSDTO newsletter = attrs.campaign
            numberRecipients= newsletter.numberRecipients
            numberClicks= newsletter.numberOpens
        }else if (attrs.campaign instanceof NewsletterLightRSDTO){
            NewsletterLightRSDTO newsletter = attrs.campaign
            numberRecipients= newsletter.numberRecipients
            numberClicks= newsletter.numberOpens
        }
        out << printPrettyStat(numberClicks,numberRecipients)
    }

    def unsubscribeRate = {attrs ->
        Long numberUnsubscribe=0
        Long numberRecipients=0
        if (attrs.campaign instanceof NewsletterRSDTO){
            NewsletterRSDTO newsletter = attrs.campaign
            numberRecipients= newsletter.numberRecipients
            numberUnsubscribe= newsletter.numberUnsubscribe
        }else if (attrs.campaign instanceof NewsletterLightRSDTO){
            NewsletterLightRSDTO newsletter = attrs.campaign
            numberRecipients= newsletter.numberRecipients
            numberUnsubscribe= newsletter.numberUnsubscribe
        }

        out << printPrettyStat(numberUnsubscribe,numberRecipients)
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
