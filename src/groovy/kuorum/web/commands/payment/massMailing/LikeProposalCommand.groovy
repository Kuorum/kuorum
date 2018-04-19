package kuorum.web.commands.payment.massMailing

import grails.validation.Validateable

@Validateable
class LikeProposalCommand {

    Long debateId
    String debateUserId
    Long proposalId
    Boolean like
    static constraints = {
        debateId nullable: false
        proposalId nullable: false
        like nullable: false
    }

}
