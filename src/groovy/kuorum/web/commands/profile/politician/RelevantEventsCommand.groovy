package kuorum.web.commands.profile.politician

import grails.validation.Validateable
import kuorum.users.KuorumUser
import kuorum.users.extendedPoliticianData.PoliticianRelevantEvent

/**
 * Created by iduetxe on 17/12/15.
 */
@Validateable
class RelevantEventsCommand {
    KuorumUser politician
    List<PoliticianRelevantEvent> politicianRelevantEvents
    static constraints = {
        politician nullable: false;
        politicianRelevantEvents maxSize: 5
    }
}
