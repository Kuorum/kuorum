package kuorum.web.commands.payment.participatoryBudget

import grails.validation.Validateable

@Validateable
class DistrictProposalChooseDistrictCommand {
    Long districtId
    String cause

    static constraints = {
        districtId nullable: false
        cause nullable:true
    }

}
