package kuorum.web.commands.profile.politician

import grails.validation.Validateable
import kuorum.users.extendedPoliticianData.ExternalPoliticianActivity

/**
 * Created by iduetxe on 15/12/15.
 */
@Validateable
class ExternalPoliticianActivityCommand {
    String politicianId
    List<ExternalPoliticianActivity> externalPoliticianActivities
    static constraints = {
        politicianId nullable: false;

    }
}
