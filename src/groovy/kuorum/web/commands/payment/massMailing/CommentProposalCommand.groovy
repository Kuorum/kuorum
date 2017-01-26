package kuorum.web.commands.payment.massMailing

import grails.validation.Validateable

@Validateable
class CommentProposalCommand {

    // Filter
    Long debateId
    Long proposalId
    String debateAlias
    String body

    static constraints = {
        debateId nullable: false
        proposalId nullable: false
        body nullable: false
        debateAlias nullable: true
    }

}
