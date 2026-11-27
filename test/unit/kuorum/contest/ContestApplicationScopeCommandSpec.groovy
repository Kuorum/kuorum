package kuorum.contest

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import kuorum.web.commands.payment.contest.ContestApplicationScopeCommand
import org.kuorum.rest.model.communication.contest.ContestApplicationActivityTypeDTO
import org.kuorum.rest.model.communication.contest.ContestApplicationFocusTypeDTO
import spock.lang.Specification
import spock.lang.Unroll

@TestMixin(GrailsUnitTestMixin)
class ContestApplicationScopeCommandSpec extends Specification {

    void setup() {
        mockForConstraintsTests(ContestApplicationScopeCommand)
    }

    private ContestApplicationScopeCommand command(Boolean hasCollaborator, Integer numBeneficiaries) {
        new ContestApplicationScopeCommand(cause: "c",
                activityType: ContestApplicationActivityTypeDTO.values().first(), focusType: ContestApplicationFocusTypeDTO.values().first(),
                numBenefitedCaregivers: 1, numBenefitedPacients: 1,
                hasCollaborator: hasCollaborator, numBeneficiaries: numBeneficiaries)
    }

    @Unroll
    void "collaborator=#hasCollaborator numBeneficiaries=#numBeneficiaries -> valid=#valid"() {
        expect:
        command(hasCollaborator, numBeneficiaries).validate() == valid

        where:
        hasCollaborator | numBeneficiaries || valid
        true            | 10               || true
        true            | 1                || true
        true            | null             || false
        true            | 0                || false
        true            | -2               || false
        false           | null             || true
        false           | 0                || true
        null            | null             || true
    }

    void "a missing number of beneficiaries uses the 'nullable' error code, so its i18n message applies"() {
        when:
        def command = command(true, null)
        command.validate()

        then:
        command.errors.getFieldError("numBeneficiaries").code == "nullable"
    }
}
