package kuorum.web.commands.profile.politician

import grails.validation.Validateable
import kuorum.users.extendedPoliticianData.PoliticianRelevantEvent

/**
 * Created by iduetxe on 17/12/15.
 */
@Validateable
class RelevantEventsCommand {
    String politicianId
    List<PoliticianRelevantEvent> politicianRelevantEvents
    static constraints = {
        politicianId nullable: false;

    }
}
