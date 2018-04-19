package kuorum.web.commands.payment.massMailing

import grails.validation.Validateable

@Validateable
class PinProposalCommand {

    // Filter
    Long debateId
    Long proposalId
    String debateUserId
    Boolean pin

    static constraints = {
        debateId nullable: false
        proposalId nullable: false
        pin nullable: false
        debateUserId nullable: true
    }

}
