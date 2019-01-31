package kuorum.web.commands.profile.politician

import grails.validation.Validateable
import kuorum.users.extendedPoliticianData.PoliticianRelevantEvent

/**
 * Created by iduetxe on 17/12/15.
 */
@Validateable
class RelevantEventsCommand {
    List<PoliticianRelevantEvent> politicianRelevantEvents
    static constraints = {
        politicianRelevantEvents nullable: true, maxSize: 5
    }
}
