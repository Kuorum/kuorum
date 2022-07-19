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
        where:
        orgNif      | isValid | isOrganization
        "B12345678" | false   | false
        "B86761459" | true    | false
        "G28197564" | true    | true
        "G08967713" | true    | true
        "G11111111" | false   | true

    }

}
