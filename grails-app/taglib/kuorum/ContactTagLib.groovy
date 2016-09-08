package kuorum

import org.kuorum.rest.model.contact.ContactRSDTO

class ContactTagLib {
    static defaultEncodeAs = [taglib: 'raw']
    //static encodeAsForTags = [tagName: [taglib:'html'], otherTagName: [taglib:'none']]

    static namespace = "contactUtil"

    def engagement = {attrs ->
        ContactRSDTO contact = attrs.concat
        out << g.render(template: "/contacts/contactEngagement", model:[contact:contact])
    }

    def openRate = {attrs ->

        ContactRSDTO contact = attrs.contact
        String openRateValue = "-"

        if(contact.stats.numMails>0) {
            Number openRateNum = contact.stats.opens/contact.stats.numMails
            openRateValue = g.formatNumber(number: openRateNum, maxFractionDigits: 1, type: 'percent')
        }
        out << openRateValue
    }

    def clickRate = {attrs ->

        ContactRSDTO contact = attrs.contact
        String clickRateValue = "-"

        if(contact.stats.numMails>0) {
            Number clickRateNum = contact.stats.clicks/contact.stats.numMails
            clickRateValue = g.formatNumber(number: clickRateNum, maxFractionDigits: 1, type: 'percent')
        }
        out << clickRateValue

    }
}
