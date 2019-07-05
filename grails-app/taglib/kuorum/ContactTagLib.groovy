package kuorum

import grails.plugin.springsecurity.SpringSecurityService
import org.kuorum.rest.model.contact.ContactRSDTO
import payment.contact.ContactService

class ContactTagLib {
    static defaultEncodeAs = [taglib: 'raw']
    //static encodeAsForTags = [tagName: [taglib:'html'], otherTagName: [taglib:'none']]

    static namespace = "contactUtil"

    ContactService contactService
    SpringSecurityService springSecurityService

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

    def printContactMail = {attrs ->
        ContactRSDTO contact = attrs.contact

        String envelope = "<span class=\"emailAddress fal fa-envelope\"\n" +
                    "      aria-hidden=\"true\"\n" +
                    "      rel=\"tooltip\"\n" +
                    "      data-toggle=\"tooltip\"\n" +
                    "      data-placement=\"bottom\"\n" +
                    "      title=\"\"\n" +
                    "      data-original-title=\"${g.message(code: 'org.kuorum.rest.model.contact.emailTooltip.text')}\"></span>"

        String follower = "<span class=\"follower fal fa-user\"\n" +
                    "      aria-hidden=\"true\"\n" +
                    "      rel=\"tooltip\"\n" +
                    "      data-toggle=\"tooltip\"\n" +
                    "      data-placement=\"bottom\"\n" +
                    "      title=\"\"\n" +
                    "      data-original-title=\"${g.message(code: 'org.kuorum.rest.model.contact.followerTooltip.text')}\"></span>"

        if (contact.blackList){
            out << """
                    <abbr title="${g.message(code:'tools.contact.edit.email.blackList')}" class='text-danger'>
                        <span class="fal fa-exclamation-circle"></span>
                    </abbr>
                    """
        }else if(contact.email && contact.isFollower){
            out << follower + envelope
        }else if(contact.email && !contact.isFollower){
            out << envelope
        }else{
            out << follower
        }
        out << "<span class='raw-email'>${contact.email}</span>"

    }

    def importSocialContact= { attrs, body ->
        String provider = attrs.provider
        String userId = springSecurityService.principal.id.toString()
        String url = contactService.getSocialImportContactUrl(userId, provider);
        out << "<a href='${url}' id='${provider}' provider='${provider}' role='button' class='actionIcon'>"
        out << body()
        out << "</a>"
    }
}
