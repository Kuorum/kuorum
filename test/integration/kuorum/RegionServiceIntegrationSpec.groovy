package kuorum

import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by iduetxe on 17/03/14.
 */
class RegionServiceIntegrationSpec extends Specification{


    def regionService

    @Unroll
    void "test get region #regionExpectedName by postalCode #postalCode on country #countryIso"() {
        given: "Username ..."
        Region country = Region.findByIso3166_2(countryIso)
        when:"Search by postalCode"
        Region region = regionService.getRegionByPostalCode(country, postalCode)
        then: "Correct region should be recovered"
        if (regionExpectedName)
            region.name == regionExpectedName
        else
            !region
        where: "Username with params...."
        countryIso | postalCode | regionExpectedName
        "EU-ES"    | "28021"    | "Madrid"
        "EU-ES-CL" | "47021"    | "Valladolid"
        "EU-ES-CL" | "28021"    | ""
        "EU-ES"    | "20001"    | "Guipuzkoa"

    }
}
