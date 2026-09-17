import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.web.commands.profile.funnel.FunnelFillBasicDataCommand
import org.kuorum.rest.model.domain.DomainRSDTO
import spock.lang.Specification
import spock.lang.Unroll

// Covers the nid validator's pharmacy (format-only: 9 chars, 8 digits + 1 letter) vs association (CIF, not A/B) branches
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

    private static boolean fieldHasErrorCode(FunnelFillBasicDataCommand command, String field, String expectedCode) {
        def fieldError = command.errors.getFieldError(field)
        return fieldError != null && fieldError.codes.contains(expectedCode)
    }

    private FunnelFillBasicDataCommand buildCommandWithAddressAndCinfaCode(String address, String cinfaCode) {
        new FunnelFillBasicDataCommand(
                name: "Test",
                phonePrefix: "34",
                phone: "600000000",
                contactName: "Test",
                nid: "K12345670",
                address: address,
                cinfaCode: cinfaCode
        )
    }

    // ---------------------------------------------------------------------
    // PHARMACY branch (contestApplicationWithCollaborator = true)
    // KPV-8589: format-only check (9 characters: 8 digits + 1 letter, in any
    // position) — no checksum, no entity-type restriction, matching what
    // Cinfa's own systems check internally.
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
        nid           | expectedValid | expectedErrorCode                                                                        | description
        "53392474K"   | true          | null                                                                                      | "DNI shape: 8 digits + letter at the end"
        "53392474k"   | true          | null                                                                                      | "lowercase letter is still fine"
        "K53392474"   | true          | null                                                                                      | "CIF shape: letter at the start + 8 digits"
        "5339K2474"   | true          | null                                                                                      | "letter in the middle is also accepted (no fixed position)"
        "A56349707"   | true          | null                                                                                      | "9 chars, 1 letter, 8 digits regardless of which entity-type letter it is"
        "G28197564"   | true          | null                                                                                      | "letter G (association-only under the old rule) is now accepted too — no entity-type restriction anymore"
        "B12345678"   | true          | null                                                                                      | "no checksum is validated anymore, so a 'wrong control digit' NIF/CIF is now accepted as long as the shape matches"
        "X1234567L"   | false         | "kuorum.web.commands.profile.funnel.FunnelFillBasicDataCommand.nid.pharmacy.notPharmacy" | "a real NIE has 2 letters (start + control) and 7 digits, so it fails the '8 digits + 1 letter' rule"
        "123456789"   | false         | "kuorum.web.commands.profile.funnel.FunnelFillBasicDataCommand.nid.pharmacy.notPharmacy" | "9 digits, 0 letters"
        "AB1234567"   | false         | "kuorum.web.commands.profile.funnel.FunnelFillBasicDataCommand.nid.pharmacy.notPharmacy" | "2 letters, 7 digits"
        "A1234567"    | false         | "kuorum.web.commands.profile.funnel.FunnelFillBasicDataCommand.nid.pharmacy.notPharmacy" | "only 8 characters total"
        "A123456789"  | false         | "kuorum.web.commands.profile.funnel.FunnelFillBasicDataCommand.nid.pharmacy.notPharmacy" | "10 characters total"
        "1234"        | false         | "kuorum.web.commands.profile.funnel.FunnelFillBasicDataCommand.nid.pharmacy.notPharmacy" | "garbage / wrong length"
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

    // ---------------------------------------------------------------------
    // KPV-8589: address / cinfaCode — pharmacy-exclusive, required only when
    // contestApplicationWithCollaborator (isPharma) is true
    // ---------------------------------------------------------------------

    @Unroll
    void "pharmacy address '#address' should be valid=#expectedValid"() {
        given:
        mockDomain(true)
        FunnelFillBasicDataCommand command = buildCommandWithAddressAndCinfaCode(address, "12345")

        when:
        command.validate()

        then:
        !command.errors.hasFieldErrors('address') == expectedValid

        and:
        expectedValid || fieldHasErrorCode(command, 'address', "kuorum.web.commands.profile.funnel.FunnelFillBasicDataCommand.address.blank")

        where:
        address                | expectedValid
        "Calle Mayor 1, Madrid" | true
        null                    | false
        ""                      | false
        "   "                   | false
    }

    void "address is not required when isPharma is false"() {
        given:
        mockDomain(false)
        FunnelFillBasicDataCommand command = buildCommandWithAddressAndCinfaCode(null, null)

        when:
        command.validate()

        then:
        !command.errors.hasFieldErrors('address')
    }

    @Unroll
    void "pharmacy cinfaCode '#cinfaCode' (#description) should be valid=#expectedValid"() {
        given:
        mockDomain(true)
        FunnelFillBasicDataCommand command = buildCommandWithAddressAndCinfaCode("Calle Mayor 1, Madrid", cinfaCode)

        when:
        command.validate()

        then:
        !command.errors.hasFieldErrors('cinfaCode') == expectedValid

        and:
        expectedValid || fieldHasErrorCode(command, 'cinfaCode', expectedErrorCode)

        where:
        cinfaCode | expectedValid | expectedErrorCode                                                                    | description
        "12345"   | true          | null                                                                                  | "5 digits, minimum allowed"
        "123456"  | true          | null                                                                                  | "6 digits, maximum allowed"
        null      | false         | "kuorum.web.commands.profile.funnel.FunnelFillBasicDataCommand.cinfaCode.blank"      | "missing"
        ""        | false         | "kuorum.web.commands.profile.funnel.FunnelFillBasicDataCommand.cinfaCode.blank"      | "blank"
        "1234"    | false         | "kuorum.web.commands.profile.funnel.FunnelFillBasicDataCommand.cinfaCode.matches.error" | "only 4 digits"
        "1234567" | false         | "kuorum.web.commands.profile.funnel.FunnelFillBasicDataCommand.cinfaCode.matches.error" | "7 digits, too many"
        "12a45"   | false         | "kuorum.web.commands.profile.funnel.FunnelFillBasicDataCommand.cinfaCode.matches.error" | "contains a letter"
    }

    void "cinfaCode is not required when isPharma is false"() {
        given:
        mockDomain(false)
        FunnelFillBasicDataCommand command = buildCommandWithAddressAndCinfaCode(null, null)

        when:
        command.validate()

        then:
        !command.errors.hasFieldErrors('cinfaCode')
    }
}
