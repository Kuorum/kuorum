package kuorum.web.commands.payment.participatoryBudget

import grails.validation.Validateable

@Validateable
class DistrictProposalTechnicalReviewCommand {
    Long districtProposalId
    Long participatoryBudgetId
    String districtProposalUserId
    Boolean approved
    Double price
    String rejectComment

    static constraints = {
        districtProposalId nullable: false
        districtProposalUserId nullable: false
        participatoryBudgetId nullable: false
        approved nullable:true
        price nullable:true
        rejectComment nullable:true
    }
}
