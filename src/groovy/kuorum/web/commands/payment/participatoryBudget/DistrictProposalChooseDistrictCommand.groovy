package kuorum.web.commands.payment.participatoryBudget

import grails.validation.Validateable

@Validateable
class DistrictProposalChooseDistrictCommand {
    String name
    Long districtId
    String cause
    String type

    static constraints = {
        name nullable: false, maxSize: 100
        districtId nullable: false
        cause nullable:true
        type nullable: false
    }

}
