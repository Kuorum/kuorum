package kuorum.web.commands.payment.massMailing

import grails.validation.Validateable
import kuorum.register.KuorumUserSession
import kuorum.util.TimeZoneUtil
import kuorum.web.commands.payment.CampaignContentCommand
import kuorum.web.constants.WebConstants
import org.grails.databinding.BindingFormat
import org.kuorum.rest.model.notification.campaign.NewsletterTemplateDTO

/**
 * Created by toni on 25/4/17.
 */
@Validateable
class MassMailingContentCommand {
    Long campaignId
    String subject
    String text
    String headerPictureId
    NewsletterTemplateDTO contentType

    @BindingFormat(WebConstants.WEB_FORMAT_DATE)
    Date publishOn
    String sendType

//    public static KuorumUser currentUser(){
//        Object appContext = ServletContextHolder.servletContext.getAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT)
//        SpringSecurityService springSecurityService = (SpringSecurityService)appContext.springSecurityService
//        KuorumUser user = springSecurityService.currentUser
//
//        return user
//    }

    static constraints = {
        campaignId nullable: false
        contentType nullable: false
        subject nullable: true, maxSize: 155, validator: { val, obj ->
            if (obj.sendType!= "DRAFT" && !val){
                return "kuorum.web.commands.payment.massMailing.MassMailingCommand.subject.nullable"
            }
        }
        text nullable: true, validator: { val, obj ->
            if (obj.sendType!= "DRAFT" && !val){
                return "kuorum.web.commands.payment.massMailing.MassMailingCommand.text.nullable"
            }
        }
        headerPictureId nullable: true, validator: {val, obj ->
            if (obj.sendType!= "DRAFT" && NewsletterTemplateDTO.NEWSLETTER.equals(obj.contentType) && !val){
                return "kuorum.web.commands.payment.massMailing.MassMailingCommand.headerPictureId.nullable"
            }
        }
        publishOn nullable: true, validator: { val, obj ->
            KuorumUserSession kuorumUser = CampaignContentCommand.currentUser()
            Date scheduledTimeZone = TimeZoneUtil.convertToUserTimeZone(val, kuorumUser.timeZone)
            Date userTimeZone = Calendar.getInstance(kuorumUser.getTimeZone()).getTime()
            if (val && obj.sendType == "SCHEDULED" && scheduledTimeZone < userTimeZone) {
                return "kuorum.web.commands.payment.massMailing.MassMailingCommand.scheduled.min.error"
            }
        }

        sendType nullable: false, inList:["DRAFT", "SCHEDULED", "SEND", "SEND_TEST", "ACTIVATE"]
    }
}
