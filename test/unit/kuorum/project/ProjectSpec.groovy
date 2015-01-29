package kuorum.project

import grails.test.mixin.TestFor
import kuorum.Institution
import kuorum.Region
import kuorum.helper.Helper
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Project)
class ProjectSpec extends Specification {


    @Shared Region europe = new Region(name:"Europe", iso3166_2: "EU")
    @Shared Region spain = new Region(name:"Spain", iso3166_2: "EU-ES", superRegion: europe)

    @Shared parliamentEurope = new Institution(name:"Parlamenteo Europeo", region: europe)
    @Shared parliamentSpain = new Institution(name:"Parlamenteo Europeo", region: spain)

    def setup() {
        mockForConstraintsTests(Project, [new Project()])
    }

    def cleanup() {
    }

    @Unroll("test LAW constraints: Checking #field = #value expected #error")
    def "test LAW all constraints"() {
        when:

        def params = [ hashtag:'#nombre',
                shortName:'shortaname',
                realName:"realname",
                description:"desc",
                region:europe,
                institution: parliamentEurope,
                commissions:[]
        ]
        params[field] = value
        def obj = new Project( params)
        then:
        Helper.validateConstraints(obj, field, error)

        where:
        error           | field         | value
        'OK'            | 'hashtag'     | '#nombre'
        'matches'       | 'hashtag'     | '#'
        'nullable'      | 'hashtag'     | ''
        'nullable'      | 'hashtag'     | null
        'nullable'      | 'shortName'   | ''
        'nullable'      | 'shortName'   | null
        'nullable'      | 'realName'    | ''
        'nullable'      | 'realName'    | null
        'nullable'      | 'description' | ''
        'nullable'      | 'description' | null
        'nullable'      | 'commissions' | null
        'nullable'      | 'region'      | null
        'notSameRegionAsInstitution'      | 'institution'      | parliamentSpain
    }
}
