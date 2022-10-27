package kuorum

import grails.plugin.springsecurity.SpringSecurityService
import org.kuorum.rest.model.communication.CampaignLightRSDTO
import org.kuorum.rest.model.communication.CampaignTypeRSDTO
import org.kuorum.rest.model.contact.ContactActivityRSDTO
import org.kuorum.rest.model.contact.ContactRSDTO
import org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO
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

        if (contact.blackList) {
            out << """
                    <abbr title="${g.message(code: 'tools.contact.edit.email.blackList')}" class='text-danger'>
                        <span class="fal fa-exclamation-circle"></span>
                    </abbr>
                    """
        }else if (!contact.getSubscribed()){
            out << """
                    <abbr title="${g.message(code: 'tools.massMailing.list.unsubscribe')}" class='text-danger'>
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
        if (contact.email){
            out << "<span class='raw-email'>${contact.email}</span>"
        }

    }

    def importSocialContact = { attrs, body ->
        String provider = attrs.provider
        String userId = springSecurityService.principal.id.toString()
        String url = contactService.getSocialImportContactUrl(userId, provider);
        out << "<a href='${url}' id='${provider}' provider='${provider}' role='button' class='actionIcon'>"
        out << body()
        out << "</a>"
    }

    def showResendBulletin = { attrs, body ->
        ContactRSDTO contact = attrs.contact
        ContactActivityRSDTO activity = attrs.activity
        ContactActivityRSDTO.ContactActivityEventRSDTO event = attrs.event
        String linkButton = g.createLink(mapping: "politicianMassMailingTrackEventsResend", params: [campaignId: activity.campaignId, tackingMailId: activity.trackingId])
        if (showResendButton(contact, activity, event)) {
            checkContactBlacklisted(contact, linkButton, "resend-email")
        }
    }

    def showCopyAndSendBulletin = {attrs, body ->
        ContactRSDTO contact = attrs.contact
        CampaignLightRSDTO bulletin = attrs.bulletin
        String linkButton = g.createLink(mapping:"politicianMassMailingBulletinCopyAndSend", params: [campaignId: bulletin.id, contactId: contact.id])
        checkContactBlacklisted(contact, linkButton, "resend-bulletin")
    }

    private void checkContactBlacklisted(ContactRSDTO contact, String linkButton, String extraCssClass) {
        if (contact.blackList) {
            out << "<span>${g.message(code: "contact.tag.lib.blackList.message")}</span>"
        } else {
            out << """
            <a href="${linkButton}" class="btn btn-blue inverted ${extraCssClass}">
                ${g.message(code: "tools.massMailing.actions.resend")}
                <span class="fal fa-angle-double-right"></span>
            </a>
            """
        }
    }

    private def STATUS_WITH_RESENT_BUTTON = [TrackingMailStatusRSDTO.SENT, TrackingMailStatusRSDTO.RESENT, TrackingMailStatusRSDTO.BOUNCED, TrackingMailStatusRSDTO.HARD_BOUNCED, TrackingMailStatusRSDTO.NOT_SENT, TrackingMailStatusRSDTO.REJECT, TrackingMailStatusRSDTO.SPAM]

    private Boolean showResendButton(ContactRSDTO contact, ContactActivityRSDTO activity, ContactActivityRSDTO.ContactActivityEventRSDTO event) {
        return STATUS_WITH_RESENT_BUTTON.contains(event.status) && activity.trackingId && activity.campaignType == CampaignTypeRSDTO.BULLETIN
    }
}
