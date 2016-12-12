package kuorum.web.commands.profile

import grails.validation.Validateable
import kuorum.Region
import kuorum.core.model.AvailableLanguage
import kuorum.users.KuorumUser
import kuorum.web.binder.RegionBinder
import org.codehaus.groovy.grails.web.context.ServletContextHolder
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.grails.databinding.BindUsing

/**
 * Created by iduetxe on 4/01/16.
 */
@Validateable
class TimeZoneCommand {

    public TimeZoneCommand(){}
    public TimeZoneCommand(KuorumUser user){
        this.timeZoneId = user.timeZone?.getID()
    }

    String timeZoneId


    static constraints = {
        timeZoneId nullable: false
    }
}
