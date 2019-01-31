package kuorum.web.commands.profile

import grails.validation.Validateable

/**
 * Created by iduetxe on 4/01/16.
 */
@Validateable
class TimeZoneCommand {

    TimeZoneCommand(){}
//
//    TimeZoneCommand(KuorumUser user){
//        this.timeZoneId = user.timeZone?.getID()
//    }

    String timeZoneRedirect
    String timeZoneId


    static constraints = {
        timeZoneId nullable: false
        timeZoneRedirect nullable:true
    }
}
