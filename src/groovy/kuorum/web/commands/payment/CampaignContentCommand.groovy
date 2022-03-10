package kuorum.web.commands.payment

import grails.plugin.springsecurity.SpringSecurityService
import grails.validation.Validateable
import kuorum.register.KuorumUserSession
import kuorum.util.TimeZoneUtil
import kuorum.web.constants.WebConstants
import org.codehaus.groovy.grails.web.context.ServletContextHolder
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.grails.databinding.BindingFormat
import org.kuorum.rest.model.communication.survey.CampaignVisibilityRSDTO

/**
 * Created by toni on 26/4/17.
 */

@Validateable
class CampaignContentCommand {
    public static String CAMPAIGN_SEND_TYPE_DRAFT = "DRAFT";
    public static String CAMPAIGN_SEND_TYPE_SCHEDULED = "SCHEDULED";
    public static String CAMPAIGN_SEND_TYPE_SEND = "SEND";
    public static String CAMPAIGN_SEND_TYPE_SEND_TEST = "SEND_TEST";
    public static String CAMPAIGN_SEND_TYPE_ACTIVATE = "ACTIVATE";

    String title
    String body

    String fileType
    String headerPictureId
    String videoPost

    CampaignVisibilityRSDTO campaignVisibility = CampaignVisibilityRSDTO.VISIBLE

    @BindingFormat(WebConstants.WEB_FORMAT_DATE)
    Date publishOn
    String sendType

    private static KuorumUserSession currentUser(){
        Object appContext = ServletContextHolder.servletContext.getAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT)
        SpringSecurityService springSecurityService = (SpringSecurityService)appContext.springSecurityService
        KuorumUserSession user = springSecurityService.principal

        return user
    }

    static constraints = {
        title nullable: true, maxSize: 100, validator: { val, obj ->
            if (obj.publishOn && !val){
                return "kuorum.web.commands.payment.massMailing.DebateCommand.title.nullable"
            }
        }
        body nullable: true, validator: { val, obj ->
            if (obj.publishOn && !val){
                return "kuorum.web.commands.payment.massMailing.DebateCommand.body.nullable"
            }
        }
        fileType nullable: true
        headerPictureId nullable: true
        videoPost nullable: true
        publishOn nullable: true, validator: { val, obj ->
            KuorumUserSession kuorumUser = CampaignContentCommand.currentUser()
            Date scheduledTimeZone = TimeZoneUtil.convertToUserTimeZone(val, kuorumUser.timeZone)
            Date userTimeZone = Calendar.getInstance(kuorumUser.getTimeZone()).getTime()
            if (val && obj.sendType == CAMPAIGN_SEND_TYPE_SCHEDULED && scheduledTimeZone < userTimeZone) {
                return "kuorum.web.commands.payment.massMailing.MassMailingCommand.scheduled.min.error"
            }
        }

        sendType nullable: false, inList: [
                CampaignContentCommand.CAMPAIGN_SEND_TYPE_DRAFT,
                CampaignContentCommand.CAMPAIGN_SEND_TYPE_SCHEDULED,
                CampaignContentCommand.CAMPAIGN_SEND_TYPE_SEND,
                CampaignContentCommand.CAMPAIGN_SEND_TYPE_SEND_TEST,
                CampaignContentCommand.CAMPAIGN_SEND_TYPE_ACTIVATE]
    }
}
