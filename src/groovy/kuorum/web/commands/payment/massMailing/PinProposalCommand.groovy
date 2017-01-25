package kuorum.web.commands.payment.massMailing

import grails.validation.Validateable

@Validateable
class PinProposalCommand {

    // Filter
    Long debateId
    Long proposalId
    String debateAlias
    Boolean pin

    static constraints = {
        debateId nullable: false
        proposalId nullable: false
        pin nullable: false
        debateAlias nullable: true
    }

}
