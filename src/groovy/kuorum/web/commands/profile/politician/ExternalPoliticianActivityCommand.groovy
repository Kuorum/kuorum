package kuorum.web.commands.profile.politician

import grails.validation.Validateable
import kuorum.users.KuorumUser
import kuorum.users.extendedPoliticianData.ExternalPoliticianActivity

/**
 * Created by iduetxe on 15/12/15.
 */
@Validateable
class ExternalPoliticianActivityCommand {
    KuorumUser politician
    List<ExternalPoliticianActivity> externalPoliticianActivities
    static constraints = {
        politician nullable: false;

    }
}
