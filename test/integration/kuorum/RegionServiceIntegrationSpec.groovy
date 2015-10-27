package kuorum

import spock.lang.Specification
import spock.lang.Unroll
/**
 * Created by iduetxe on 17/03/14.
 */
class RegionServiceIntegrationSpec extends Specification{


    def regionService
    def fixtureLoader

    def setup(){
        Region.collection.getDB().dropDatabase()
        fixtureLoader.load("basicData")
    }

    @Unroll
    void "test get region #regionExpectedName by postalCode #postalCode on country #countryIso"() {
        given: "Username ..."
        Region country = Region.findByIso3166_2(countryIso)
        when:"Search by postalCode"
        Region region = regionService.findProvinceByPostalCode(country, postalCode)
        then: "Correct region should be recovered"
        if (provinceExpectedName)
            region.name == provinceExpectedName
        else
            !region
        where: "Username with params...."
        countryIso | postalCode | provinceExpectedName
        "EU-ES"    | "28021"    | "Madrid"
        "EU-ES-CL" | "47021"    | "Valladolid"
        "EU-ES-CL" | "28021"    | ""
        "EU-ES"    | "20001"    | "Guipuzkoa"

    }
}
