package kuorum.web.commands.payment.massMailing

import grails.plugin.springsecurity.SpringSecurityService
import grails.validation.Validateable
import kuorum.users.KuorumUser
import kuorum.util.TimeZoneUtil
import org.codehaus.groovy.grails.web.context.ServletContextHolder
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.grails.databinding.BindingFormat
import org.kuorum.rest.model.notification.campaign.CampaignTemplateDTO

/**
 * Created by toni on 25/4/17.
 */
@Validateable
class MassMailingContentCommand {
    Long campaignId
    String subject
    String text
    String headerPictureId
    CampaignTemplateDTO contentType

    @BindingFormat('dd/MM/yyyy HH:mm')
    Date scheduled
    String sendType

    public static KuorumUser currentUser(){
        Object appContext = ServletContextHolder.servletContext.getAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT)
        SpringSecurityService springSecurityService = (SpringSecurityService)appContext.springSecurityService
        KuorumUser user = springSecurityService.currentUser

        return user
    }

    static constraints = {
        campaignId nullable: false
        contentType nullable: false
        subject nullable: true, validator: { val, obj ->
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
            if (obj.sendType!= "DRAFT" && CampaignTemplateDTO.NEWSLETTER.equals(obj.contentType) && !val){
                return "kuorum.web.commands.payment.massMailing.MassMailingCommand.headerPictureId.nullable"
            }
        }
        scheduled nullable: true, validator: { val, obj ->
            KuorumUser kuorumUser = MassMailingContentTextCommand.currentUser()
            Date scheduledTimeZone = TimeZoneUtil.convertToUserTimeZone(val, kuorumUser.timeZone)
            Date userTimeZone = Calendar.getInstance(kuorumUser.getTimeZone()).getTime()
            if (val && obj.sendType== "SCHEDULED" && scheduledTimeZone < userTimeZone){
                return "kuorum.web.commands.payment.massMailing.MassMailingCommand.scheduled.min.error"
            }
        }
        sendType nullable: false, inList:["DRAFT", "SCHEDULED", "SEND", "SEND_TEST"]
    }
}
