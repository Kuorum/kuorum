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

    private void mockDomain(boolean hasCollaborator) {
        DomainRSDTO domainRSDTO = new DomainRSDTO()
        domainRSDTO.contestApplicationWithCollaborator = hasCollaborator
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

    private static final String NID_ERROR_PREFIX = "kuorum.web.commands.profile.funnel.FunnelFillBasicDataCommand.nid."

    // Códigos de error esperados. Van a nivel de clase porque las filas de where: se evalúan antes de
    // ejecutar el test y no ven las variables locales declaradas en given:
    private static final String notAPharmacyError = NID_ERROR_PREFIX + "pharmacy.notPharmacy"
    private static final String notAsocError = NID_ERROR_PREFIX + "notAsoc"
    private static final String invalidError = NID_ERROR_PREFIX + "invalid"
    private static final String cinfaCodeBlankError = "kuorum.web.commands.profile.funnel.FunnelFillBasicDataCommand.cinfaCode.blank"
    private static final String cinfaCodeError = "kuorum.web.commands.profile.funnel.FunnelFillBasicDataCommand.cinfaCode.matches.error"

    // "OK" si el nid es válido; si no, el sufijo del código de error (invalid, notAsoc, pharmacy.notPharmacy, ...)
    // o el código de la restricción (nullable)
    private static String nidState(FunnelFillBasicDataCommand command) {
        def fieldError = command.errors.getFieldError('nid')
        if (fieldError == null) {
            return "OK"
        }
        String code = fieldError.code
        return code.startsWith(NID_ERROR_PREFIX) ? code.substring(NID_ERROR_PREFIX.length()) : code
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
        nid           | expectedValid | expectedErrorCode   | description
        "53392474K"   | true          | null                | "DNI shape: 8 digits + letter at the end"
        "53392474k"   | true          | null                | "lowercase letter is still fine"
        "K53392474"   | true          | null                | "CIF shape: letter at the start + 8 digits"
        "5339K2474"   | true          | null                | "letter in the middle is also accepted (no fixed position)"
        "A56349707"   | true          | null                | "9 chars, 1 letter, 8 digits regardless of which entity-type letter it is"
        "G28197564"   | false         | notAPharmacyError   | "a CIF starting with G is an association: it can't register as a pharmacy"
        "12345668G"   | true          | null                | "a DNI whose CONTROL letter is G is a natural person: only a CIF STARTING with G is rejected"
        "B12345678"   | true          | null                | "no checksum is validated anymore, so a 'wrong control digit' NIF/CIF is now accepted as long as the shape matches"
        "X1234567L"   | false         | notAPharmacyError               | "a real NIE has 2 letters (start + control) and 7 digits, so it fails the '8 digits + 1 letter' rule"
        "123456789"   | false         | notAPharmacyError               | "9 digits, 0 letters"
        "AB1234567"   | false         | notAPharmacyError               | "2 letters, 7 digits"
        "A1234567"    | false         | notAPharmacyError               | "only 8 characters total"
        "A123456789"  | false         | notAPharmacyError               | "10 characters total"
        "1234"        | false         | notAPharmacyError               | "garbage / wrong length"
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
        nid           | expectedValid | expectedErrorCode    | description
        "G28197564"   | true          | null                 | "valid CIF, letter G (association)"
        "C12345674"   | true          | null                 | "valid CIF, letter C (Sociedad Colectiva) is a valid association type"
        "K1234567D"   | true          | null                 | "valid CIF, letter-based control digit (K) is a valid association type"
        "A56349707"   | false         | notAsocError         | "valid CIF but letter A is explicitly disallowed for associations"
        "B86761459"   | false         | notAsocError         | "valid CIF but letter B is explicitly disallowed for associations"
        "53392474K"   | false         | notAsocError         | "a plain DNI is rejected, associations must be a CIF (its first char isn't a CIF letter at all)"
        "G28197560"   | false         | invalidError         | "letter G (allowed type) but wrong control digit"
        "1234"        | false         | invalidError         | "garbage / wrong format"
    }

    // ---------------------------------------------------------------------
    // KPV-8589: address / cinfaCode — pharmacy-exclusive, required only when
    // contestApplicationWithCollaborator (hasCollaborator) is true
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

    void "address is not required when hasCollaborator is false"() {
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
        cinfaCode | expectedValid | expectedErrorCode    | description
        "12345"   | true          | null                 | "5 digits, minimum allowed"
        "123456"  | true          | null                 | "6 digits, maximum allowed"
        null      | false         | cinfaCodeBlankError  | "missing"
        ""        | false         | cinfaCodeBlankError  | "blank"
        "1234"    | false         | cinfaCodeError       | "only 4 digits"
        "1234567" | false         | cinfaCodeError       | "7 digits, too many"
        "12a45"   | false         | cinfaCodeError       | "contains a letter"
    }

    void "cinfaCode is not required when hasCollaborator is false"() {
        given:
        mockDomain(false)
        FunnelFillBasicDataCommand command = buildCommandWithAddressAndCinfaCode(null, null)

        when:
        command.validate()

        then:
        !command.errors.hasFieldErrors('cinfaCode')
    }

    // ---------------------------------------------------------------------
    // nid: estado de validación REAL de cada NIF/CIF en los dos dominios, lado a lado.
    //
    //  - dominio colaborador (contestApplicationWithCollaborator = true): solo formato
    //    (9 caracteres, 1 letra en cualquier posición y 8 dígitos) y no puede ser una asociación (CIF que
    //    EMPIEZA por G; un DNI cuya letra de control sea G es una persona física y vale).
    //    Estados: OK | pharmacy.notPharmacy
    //  - dominio asociación (= false), en este orden:
    //      1. invalid        -> no es un NIF/NIE/CIF válido (formato o dígito/letra de control)
    //      2. notAsoc        -> es válido pero no es una asociación (CIF que empieza por A o B, DNI o NIE)
    //      (la regla final "matches.error" no se alcanza nunca: lo que pasa isAsociacion() ya cumple ese formato)
    //  - el @BindUsing del campo pasa el nid a mayúsculas y le quita los espacios, también al construir
    //    el command con un mapa, así que " g28197564 " se valida como "G28197564".
    //  - null: lo rechaza la restricción nullable antes de llegar al validador, en los dos dominios.
    // ---------------------------------------------------------------------

    @Unroll
    void "nid '#nid' (#descripcion) => colaborador=#colaborador, asociacion=#asociacion"() {
        when: "se valida el mismo nid en el dominio colaborador..."
        mockDomain(true)
        FunnelFillBasicDataCommand comandoColaborador = buildCommand(nid)
        comandoColaborador.validate()

        and: "...y en el dominio asociación"
        mockDomain(false)
        FunnelFillBasicDataCommand comandoAsociacion = buildCommand(nid)
        comandoAsociacion.validate()

        then:
        nidState(comandoColaborador) == colaborador
        nidState(comandoAsociacion) == asociacion

        where:
        nid           | colaborador            | asociacion      | descripcion
        // Asociaciones: CIF válido que no empieza por A ni B
        "G28197564"   | "pharmacy.notPharmacy" | "OK"            | "asociación G, control numérico (en colaborador se rechaza por ser G)"
        "C12345674"   | "OK"                   | "OK"            | "asociación C, no solo G"
        "E12345674"   | "OK"                   | "OK"            | "asociación E, control numérico obligatorio"
        "H12345674"   | "OK"                   | "OK"            | "asociación H, control numérico obligatorio"
        "G10000040"   | "pharmacy.notPharmacy" | "OK"            | "asociación G, dígito de control 0 (en colaborador se rechaza por ser G)"
        "G2819756D"   | "pharmacy.notPharmacy" | "OK"            | "asociación G con letra de control (en colaborador se rechaza por ser G (y además tiene 2 letras))"
        "G1000004J"   | "pharmacy.notPharmacy" | "OK"            | "asociación G, control 0 como letra J (en colaborador se rechaza por ser G (y además tiene 2 letras))"
        "K1234567D"   | "pharmacy.notPharmacy" | "OK"            | "asociación K, control por letra obligatorio"
        "P1234567D"   | "pharmacy.notPharmacy" | "OK"            | "asociación P, control por letra obligatorio"
        "N1234567D"   | "pharmacy.notPharmacy" | "OK"            | "asociación N, control por letra obligatorio"
        "W1234567D"   | "pharmacy.notPharmacy" | "OK"            | "asociación W, control por letra obligatorio"

        // Válidos pero NO asociación: sociedades A/B y personas físicas
        "A56349707"   | "OK"                   | "notAsoc"       | "Sociedad Anónima válida"
        "B86761459"   | "OK"                   | "notAsoc"       | "Sociedad Limitada válida"
        "A10000040"   | "OK"                   | "notAsoc"       | "Sociedad Anónima, dígito de control 0"
        "53392474K"   | "OK"                   | "notAsoc"       | "DNI válido"
        "50769719W"   | "OK"                   | "notAsoc"       | "DNI válido"
        "12345668G"   | "OK"                   | "notAsoc"       | "DNI válido cuya letra de control es G: persona física, no se rechaza"
        "X1234567L"   | "pharmacy.notPharmacy" | "notAsoc"       | "NIE válido (2 letras: no vale como farmacia)"

        // Inválidos como NIF/NIE/CIF en el dominio asociación (el colaborador solo mira el formato)
        "G28197560"   | "pharmacy.notPharmacy" | "invalid"       | "CIF G con dígito de control erróneo (en colaborador se rechaza por ser G)"
        "B12345678"   | "OK"                   | "invalid"       | "CIF B con dígito de control erróneo"
        "50769719A"   | "OK"                   | "invalid"       | "DNI con letra de control incorrecta"
        "K12345674"   | "OK"                   | "invalid"       | "K exige letra de control y lleva número"
        "A5634970G"   | "pharmacy.notPharmacy" | "invalid"       | "A exige número de control y lleva letra"
        "123456789"   | "pharmacy.notPharmacy" | "invalid"       | "9 dígitos, ninguna letra"
        "AB1234567"   | "pharmacy.notPharmacy" | "invalid"       | "2 letras y 7 dígitos"
        "1234"        | "pharmacy.notPharmacy" | "invalid"       | "basura"
        ""            | "pharmacy.notPharmacy" | "invalid"       | "cadena vacía"

        // Normalización (@BindUsing) y nulos
        "g28197564"   | "pharmacy.notPharmacy" | "OK"            | "minúscula: se pasa a mayúsculas antes de validar (y entonces es una G: en colaborador se rechaza)"
        " g28197564 " | "pharmacy.notPharmacy" | "OK"            | "espacios alrededor: se quitan antes de validar (y entonces es una G: en colaborador se rechaza)"
        null          | "nullable"             | "nullable"      | "nid obligatorio"
    }
}
