package kuorum.web.commands.payment.participatoryBudget

import grails.validation.Validateable

@Validateable
class DistrictProposalVoteCommand {
    Long districtId
    Long participatoryBudgetId
    Long proposalId
    String userAlias;
    Boolean vote;

    static constraints = {
        districtId nullable: false
        participatoryBudgetId nullable: false
        proposalId nullable: false
        userAlias nullable: false
        vote nullable: false
    }

}
