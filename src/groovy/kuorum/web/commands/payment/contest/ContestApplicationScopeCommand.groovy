package kuorum.web.commands.payment.contest

import grails.validation.Validateable
import org.kuorum.rest.model.communication.contest.ContestApplicationActivityTypeDTO
import org.kuorum.rest.model.communication.contest.ContestApplicationFocusTypeDTO
import org.kuorum.rest.model.communication.participatoryBudget.BackerTypeRSDTO

@Validateable
class ContestApplicationScopeCommand {
    String name
    String cause
    ContestApplicationActivityTypeDTO activityType
    ContestApplicationFocusTypeDTO focusType
    Integer numBenefitedCaregivers
    Integer numBenefitedPacients
    Integer numBeneficiaries
    String associationName
    String associationImage

    // Not bound from the request: set server-side by ContestApplicationController, never trusted from the client
    Boolean hasCollaborator

    static constraints = {
        name nullable: false
        cause nullable: false
        activityType nullable: false
        focusType nullable: false
        numBenefitedCaregivers nullable: false
        numBenefitedPacients nullable: false
        numBeneficiaries nullable: true, validator: { val, obj ->
            if (obj.hasCollaborator && (val == null || val <= 0)) {
                return 'nullable'
            }
        }
        associationName nullable: true, validator: { val, obj ->
            if (obj.hasCollaborator && !val) {
                return 'nullable'
            }
        }
        associationImage nullable: true, validator: { val, obj ->
            if (obj.hasCollaborator && !val) {
                return 'nullable'
            }
        }
        hasCollaborator nullable: true
    }

}
