package kuorum.web.commands.payment.participatoryBudget

import grails.validation.Validateable
import org.kuorum.rest.model.communication.participatoryBudget.BackerTypeRSDTO

@Validateable
class DistrictProposalChooseDistrictCommand {
    String name
    Long districtId
    String cause
    BackerTypeRSDTO backerType

    static constraints = {
        name nullable: false, maxSize: 100
        districtId nullable: false
        cause nullable:true
        backerType nullable: false
    }

}
