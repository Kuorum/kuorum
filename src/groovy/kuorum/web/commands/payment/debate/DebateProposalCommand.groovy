package kuorum.web.commands.payment.debate

import grails.validation.Validateable

@Validateable
class DebateProposalCommand {

    Long debateId
    String debateAlias
    String body

    static constraints = {
        debateId nullable: false
        body nullable: false
        debateAlias nullable: true
    }

}
