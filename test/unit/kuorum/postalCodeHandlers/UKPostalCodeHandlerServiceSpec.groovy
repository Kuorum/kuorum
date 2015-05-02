package kuorum.postalCodeHandlers

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(UKPostalCodeHandlerService)
class UKPostalCodeHandlerServiceSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "testing UK prefix for postal code #postalCode"() {
        given: "A postalcode"
        when: "province Prefix"
        //"service" represents the grails service you are testing for
        String prefixProvincePostalCode= service.getPrefixProvincePostalCode(postalCode)

        then: "Expected num emails"
        prefixProvincePostalCode == provincePrefix
        where:
        postalCode  | provincePrefix
        "AB123CD45" | "AB"
        "A3CD45"    | "A"
        "ABC123CD4" | "ABC"
    }
}
