package kuorum.law

import kuorum.Institution
import kuorum.Region
import kuorum.core.model.CommissionType
import spock.lang.Specification

/**
 * Created by iduetxe on 4/03/14.
 */
class LawServiceIntegrationSpec extends Specification{

    def lawService
    def fixtureLoader

    def setup(){
        Law.collection.getDB().dropDatabase()
        fixtureLoader.load("testData")
    }

    void "test update a law"() {
        given: "A law"
        Law law = Law.findByHashtag("#leyAborto")
        when: "Updating the law"
        Region region = Region.findByIso3166_2("EU")
        Institution institution = Institution.findByName("Parlamento europeo")
        String realName = "realName"
        String shortName = "shortName"
        def commissions = [CommissionType.AGRICULTURE, CommissionType.BUDGETS, CommissionType.CONSTITUTIONAL]

        law.shortName = shortName
        law.region = region
        law.institution = institution
        law.realName = realName
        law.commissions = commissions
        Law lawSaved = lawService.updateLaw(law)
        then: "Law is update properly"

        lawSaved.shortName ==shortName
        lawSaved.region == region
        lawSaved.region.iso3166_2 == region.iso3166_2
        lawSaved.region.id == region.id
        lawSaved.realName == realName
        lawSaved.commissions == commissions
        lawSaved.institution == institution
        lawSaved.institution.name == institution.name
        Law.withNewSession {
            Law lawRecovered = Law.findByHashtag("#leyAborto")
            lawRecovered.shortName ==shortName
            lawRecovered.region == region
            lawRecovered.region.iso3166_2 == region.iso3166_2
            lawRecovered.region.id == region.id
            lawRecovered.realName == realName
            lawRecovered.commissions == commissions
            lawRecovered.institution == institution
            lawRecovered.institution.name == institution.name
        }

    }

}
