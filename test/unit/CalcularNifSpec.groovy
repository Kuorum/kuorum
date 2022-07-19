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
        isOrganization == cif.isAsociacion()
        controlDigit == cif.calcControlDigit()
        where:
        orgNif      | controlDigit | isValid | isOrganization | personaFisica
        "B12345678" | "B123456784" | false   | false          | false
        "B86761459" | "B867614599" | true    | false          | false
        "G28197564" | "G281975644" | true    | true           | false
        "G08967713" | "G089677133" | true    | true           | false
        "G11111111" | "G111111119" | false   | true           | false
        "53392474K" | "53392474K"  | true    | false          | true
        "53392474"  | "53392474K"  | false   | false          | true
        "1"         | null         | false   | false          | false
        "1234"      | null         | false   | false          | false
        "123D"      | null         | false   | false          | false

    }

}
