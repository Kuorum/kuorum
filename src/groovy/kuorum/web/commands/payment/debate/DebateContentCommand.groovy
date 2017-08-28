package kuorum.web.commands.payment.debate

import grails.plugin.springsecurity.SpringSecurityService
import grails.validation.Validateable
import kuorum.users.KuorumUser
import kuorum.util.TimeZoneUtil
import kuorum.web.constants.WebConstants
import org.codehaus.groovy.grails.web.context.ServletContextHolder
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.grails.databinding.BindingFormat

/**
 * Created by toni on 27/4/17.
 */
@Validateable
class DebateContentCommand {

    String title
    String body

    String fileType
    String headerPictureId
    String videoPost

    @BindingFormat(WebConstants.WEB_FORMAT_DATE)
    Date publishOn
    String sendType

    public static KuorumUser currentUser(){
        Object appContext = ServletContextHolder.servletContext.getAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT)
        SpringSecurityService springSecurityService = (SpringSecurityService)appContext.springSecurityService
        KuorumUser user = springSecurityService.currentUser

        return user
    }

    static constraints = {
        title nullable: true, validator: { val, obj ->
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
            KuorumUser kuorumUser = DebateContentCommand.currentUser()
            Date scheduledTimeZone = TimeZoneUtil.convertToUserTimeZone(val, kuorumUser.timeZone)
            Date userTimeZone = Calendar.getInstance(kuorumUser.getTimeZone()).getTime()
            if (val && obj.sendType == "SCHEDULED" && scheduledTimeZone < userTimeZone) {
                return "kuorum.web.commands.payment.massMailing.MassMailingCommand.scheduled.min.error"
            }
        }

        sendType nullable: false, inList:["DRAFT", "SCHEDULED", "SEND", "SEND_TEST"]
    }

}
