package kuorum

import grails.test.mixin.TestFor
import kuorum.helper.Helper
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(PoliticalParty)
class PoliticalPartySpec extends Specification {


    @Shared
    Region europe = new Region(name:"Europe", iso3166_2: "EU")

    @Shared
    Region spain = new Region(name:"Spain", iso3166_2: "SP")

    @Shared
    Institution parliament = new Institution(name:"parliament", region: europe)

    def setup() {
        mockForConstraintsTests(PoliticalParty, [new PoliticalParty(name:"name", region:europe, institution: parliament)])
    }

    def cleanup() {
    }

    @Unroll("test PARLAMENTARY_GROUP constraints: Checking #field = #value expected #error")
    def "test PARLAMENTARY_GROUP all constraints"() {
        when:
        def params = [
                name:'name',
                region:europe,
                institution: parliament
        ]
        params[field] = value
        def obj = new PoliticalParty()
        obj.institution = parliament
        obj."$field" = value

        then:
        Helper.validateConstraints(obj, field, error)

        where:
        error           | field         | value
        'OK'            | 'name'        | 'nombre'
        'blank'         | 'name'        | ''
        'nullable'      | 'name'        | null
        'nullable'      | 'region'      | null
        'nullable'      | 'institution' | null
        'notSameRegionAsInstitution'      | 'region' | spain
    }

}
