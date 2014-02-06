package kuorum

import grails.test.mixin.TestFor
import kuorum.helper.Helper
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Region)
class RegionSpec extends Specification {

    @Shared
    Region europe = new Region(name:"Europe", iso3166_2: "EU")
    @Shared
    Region spain = new Region(name:"España", iso3166_2: "EU-SP", superRegion: europe)

    def setup() {
        mockForConstraintsTests(Region, [new Region(name:'España',  iso3166_2:"SP")])
    }

    @Unroll("test REGION all constraints #field is #error")
    def "test REGION all constraints"() {
        when:
        def obj = new Region(params)

        then:
        Helper.validateConstraints(obj, field, error)

        where:
        error                       | field        | params
        'OK'                        | 'name'       | [name:'Europa',  iso3166_2:"EU"]
        'nullable'                  | 'name'       | [name:null, iso3166_2:"EU"]
        'nullable'                  | 'name'       | [name:'', iso3166_2:"EU"]
        'nullable'                  | 'iso3166_2'  | [name:'Europa',  iso3166_2:null]
        'nullable'                  | 'iso3166_2'  | [name:'Europa',  iso3166_2:'']
        'matches'                   | 'iso3166_2'  | [name:'España', superRegion:europe, iso3166_2:"EU-SP-"]
        'OK'                        | 'iso3166_2'  | [name:'España', superRegion:europe, iso3166_2:"EU-SP"]
        'incorrectMaterializedPath' | 'iso3166_2'  | [name:'Cataluña', superRegion:spain, iso3166_2:"CT"]
        'incorrectMaterializedPath' | 'iso3166_2'  | [name:'Cataluña', superRegion:spain, iso3166_2:"ES-CT"]
        'OK'                        | 'iso3166_2'  | [name:'Cataluña', superRegion:spain, iso3166_2:"EU-SP-CT"]
    }

}
