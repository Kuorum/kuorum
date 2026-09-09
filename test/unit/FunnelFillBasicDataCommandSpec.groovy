import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.web.commands.profile.funnel.FunnelFillBasicDataCommand
import org.kuorum.rest.model.domain.DomainRSDTO
import spock.lang.Specification
import spock.lang.Unroll

// Covers the nid validator's pharmacy (DNI or CIF A/B/E/J) vs association (CIF, not A/B) branches
@TestMixin(GrailsUnitTestMixin)
class FunnelFillBasicDataCommandSpec extends Specification {

    void setup() {
        mockForConstraintsTests(FunnelFillBasicDataCommand)
    }

    void cleanup() {
        GroovySystem.metaClassRegistry.removeMetaClass(CustomDomainResolver)
    }

    private void mockDomain(boolean isPharma) {
        DomainRSDTO domainRSDTO = new DomainRSDTO()
        domainRSDTO.contestApplicationWithCollaborator = isPharma
        CustomDomainResolver.metaClass.static.getDomainRSDTO = { -> domainRSDTO }
    }

    private FunnelFillBasicDataCommand buildCommand(String nid) {
        new FunnelFillBasicDataCommand(
                name: "Test",
                phonePrefix: "34",
                phone: "600000000",
                contactName: "Test",
                nid: nid
        )
    }

    private static boolean nidHasErrorCode(FunnelFillBasicDataCommand command, String expectedCode) {
        def fieldError = command.errors.getFieldError('nid')
        return fieldError != null && fieldError.codes.contains(expectedCode)
    }

    // ---------------------------------------------------------------------
    // PHARMACY branch (contestApplicationWithCollaborator = true)
    // ---------------------------------------------------------------------

    @Unroll
    void "pharmacy nid '#nid' (#description) should be valid=#expectedValid"() {
        given:
        mockDomain(true)
        FunnelFillBasicDataCommand command = buildCommand(nid)

        when:
        command.validate()

        then:
        !command.errors.hasFieldErrors('nid') == expectedValid

        and:
        expectedValid || nidHasErrorCode(command, expectedErrorCode)

        where:
        nid           | expectedValid | expectedErrorCode                                                                 | description
        "53392474K"   | true          | null                                                                               | "valid DNI, autonomous pharmacist"
        "53392474k"   | true          | null                                                                               | "DNI with a lowercase control letter is still fine"
        "A56349707"   | true          | null                                                                               | "valid CIF, letter A (S.A.)"
        "B86761459"   | true          | null                                                                               | "valid CIF, letter B (S.L.)"
        "b86761459"   | true          | null                                                                               | "valid CIF, lowercase letter b (S.L.)"
        "E12345674"   | true          | null                                                                               | "valid CIF, letter E (Comunidad de Bienes)"
        "J12345674"   | true          | null                                                                               | "valid CIF, letter J (Sociedad Civil)"
        "G28197564"   | false         | "kuorum.web.commands.profile.funnel.FunnelFillBasicDataCommand.nid.pharmacy.notPharmacy" | "valid CIF but letter G (association) is not an allowed pharmacy type"
        "C12345674"   | false         | "kuorum.web.commands.profile.funnel.FunnelFillBasicDataCommand.nid.pharmacy.notPharmacy" | "valid CIF but letter C (Sociedad Colectiva) is not an allowed pharmacy type"
        "K1234567D"   | false         | "kuorum.web.commands.profile.funnel.FunnelFillBasicDataCommand.nid.pharmacy.notPharmacy" | "valid CIF, letter-based control digit (K), still not an allowed pharmacy type"
        "B12345678"   | false         | "kuorum.web.commands.profile.funnel.FunnelFillBasicDataCommand.nid.invalid"       | "letter B (allowed type) but wrong control digit"
        "53392474Z"   | false         | "kuorum.web.commands.profile.funnel.FunnelFillBasicDataCommand.nid.invalid"       | "DNI-shaped but wrong control letter"
        "X1234567L"   | false         | "kuorum.web.commands.profile.funnel.FunnelFillBasicDataCommand.nid.invalid"       | "NIE is not a recognised format at all (letrasInicioAutorizadas excludes X/Y/Z)"
        "I1234567D"   | false         | "kuorum.web.commands.profile.funnel.FunnelFillBasicDataCommand.nid.invalid"       | "letter I is not a recognised CIF start letter"
        "1234"        | false         | "kuorum.web.commands.profile.funnel.FunnelFillBasicDataCommand.nid.invalid"       | "garbage / wrong format"
    }

    // ---------------------------------------------------------------------
    // ASSOCIATION branch (contestApplicationWithCollaborator = false) — pre-existing behaviour
    // ---------------------------------------------------------------------

    @Unroll
    void "association nid '#nid' (#description) should be valid=#expectedValid"() {
        given:
        mockDomain(false)
        FunnelFillBasicDataCommand command = buildCommand(nid)

        when:
        command.validate()

        then:
        !command.errors.hasFieldErrors('nid') == expectedValid

        and:
        expectedValid || nidHasErrorCode(command, expectedErrorCode)

        where:
        nid           | expectedValid | expectedErrorCode                                                           | description
        "G28197564"   | true          | null                                                                         | "valid CIF, letter G (association)"
        "C12345674"   | true          | null                                                                         | "valid CIF, letter C (Sociedad Colectiva) is a valid association type"
        "K1234567D"   | true          | null                                                                         | "valid CIF, letter-based control digit (K) is a valid association type"
        "A56349707"   | false         | "kuorum.web.commands.profile.funnel.FunnelFillBasicDataCommand.nid.notAsoc" | "valid CIF but letter A is explicitly disallowed for associations"
        "B86761459"   | false         | "kuorum.web.commands.profile.funnel.FunnelFillBasicDataCommand.nid.notAsoc" | "valid CIF but letter B is explicitly disallowed for associations"
        "53392474K"   | false         | "kuorum.web.commands.profile.funnel.FunnelFillBasicDataCommand.nid.notAsoc" | "a plain DNI is rejected, associations must be a CIF (its first char isn't a CIF letter at all)"
        "G28197560"   | false         | "kuorum.web.commands.profile.funnel.FunnelFillBasicDataCommand.nid.invalid" | "letter G (allowed type) but wrong control digit"
        "1234"        | false         | "kuorum.web.commands.profile.funnel.FunnelFillBasicDataCommand.nid.invalid" | "garbage / wrong format"
    }
}
