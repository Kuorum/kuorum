import kuorum.util.cif.CalculaNif
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by iduetxe on 24/03/14.
 */
class CalcularNifSpec extends Specification {


    @Unroll
    void "test Checking Spanish NIF #orgNif => is valid #isValid"() {
        given: "The orginal nif"
        when:
        CalculaNif cif = new CalculaNif(orgNif)
        then:
        isValid == cif.isValid()
        isAsociacion == cif.isAsociacion()
        isLegalEntity == cif.isLegalEntity()
        controlDigit == cif.calcControlDigit()
        where:
        descripcion                                          | orgNif        | controlDigit   | isValid | isAsociacion | isLegalEntity | personaFisica
        
        // Sociedad Anonima/Limitada (A/B): legal entities, but NOT associations - the case that was missing.
        "Sociedad Anónima (legal entity, not association)"   | "A56349707"   | "A563497077"  | true    | false        | true          | false
        "Sociedad Limitada, checksum inválido"               | "B12345678"   | "B123456784"  | false   | false        | true          | false
        "Sociedad Limitada (entidad, no asociación)"         | "B86761459"   | "B867614599"  | true    | false        | true          | false

        // Any other authorized CIF letter (G here) counts as an association, and is also a legal entity.
        "Asociación válida"                                  | "G28197564"   | "G281975644"  | true    | true         | true          | false
        "Asociación válida"                                  | "G08967713"   | "G089677133"  | true    | true         | true          | false
        "Asociación, checksum inválido"                      | "G11111111"   | "G111111119"  | false   | true         | true          | false

        // Non legal entity  - Valid DNI is not associacion or legal entity
        "DNI physical person valid"                          | "53392474K"   | "53392474K"   | true    | false        | false         | true
        "DNI no control char, invalid"                       | "53392474"    | null          | false   | false        | false         | true
        "Pharmacy Format (char at the end) as DNI"           | "12345678Z"   | "12345678Z"   | true    | false        | false         | true
        "Invalid data "                                      | "1"           | null          | false   | false        | false         | false
        "Invalid data "                                      | "1234"        | null          | false   | false        | false         | false
        "Invalid data "                                      | "123D"        | null          | false   | false        | false         | false

    }

}
