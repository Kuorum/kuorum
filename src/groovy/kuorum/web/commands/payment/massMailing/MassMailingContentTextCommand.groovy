package kuorum.web.commands.payment.massMailing

import grails.plugin.springsecurity.SpringSecurityService
import grails.validation.Validateable
import kuorum.register.KuorumUserSession
import kuorum.util.TimeZoneUtil
import org.codehaus.groovy.grails.web.context.ServletContextHolder
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.grails.databinding.BindingFormat

/**
 * Created by toni on 21/4/17.
 */
@Validateable
class MassMailingContentTextCommand {
    String subject
    String text

    @BindingFormat('dd/MM/yyyy HH:mm')
    Date scheduled
    String sendType

    static KuorumUserSession currentUser(){
        Object appContext = ServletContextHolder.servletContext.getAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT)
        SpringSecurityService springSecurityService = (SpringSecurityService)appContext.springSecurityService
        KuorumUserSession user = springSecurityService.principal

        return user
    }

    static constraints = {

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

        scheduled nullable: true, validator: { val, obj ->
            KuorumUserSession userSession = MassMailingContentTextCommand.currentUser()
            Date scheduledTimeZone = TimeZoneUtil.convertToUserTimeZone(val, userSession.timeZone)
            Date userTimeZone = Calendar.getInstance(userSession.getTimeZone()).getTime()
            if (val && obj.sendType== "SCHEDULED" && scheduledTimeZone < userTimeZone){
                return "kuorum.web.commands.payment.massMailing.MassMailingCommand.scheduled.min.error"
            }
        }
        sendType nullable: false, inList:["DRAFT", "SCHEDULED", "SEND", "SEND_TEST"]
    }
}
