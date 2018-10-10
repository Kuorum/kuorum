package kuorum.web.commands.payment.participatoryBudget

import grails.validation.Validateable

@Validateable
class ParticipatoryBudgetDeadlinesCommand {
    Long campaignId
    Date deadLineProposals
    Date deadLineTechnicalReview
    Date deadLineVotes
    Date deadLineFinalReview
    Date deadLineResults

    static constraints = {
        campaignId nullable: false
        deadLineProposals nullable: false
        deadLineTechnicalReview nullable: false
        deadLineVotes nullable: false
        deadLineFinalReview nullable: false
        deadLineResults nullable: false
    }

}
