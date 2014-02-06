package kuorum

import grails.test.mixin.TestFor
import kuorum.helper.Helper
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Institution)
class InstitutionSpec extends Specification {

    @Shared
    Region europe = new Region(name:"Europe", iso3166_2: "EU")

    @Shared
    Region spain = new Region(name:"Spain", iso3166_2: "SP")

    def setup() {
        mockForConstraintsTests(Institution, [new Institution(name:"name", region:europe)])
    }


    def cleanup() {
    }

    @Unroll("test INSTITUCION constraints: Checking #field = #value expected #error")
    def "test INSTITUCION all constraints"() {
        when:
        def params = [ name:'name',
                region:europe
        ]
        def obj1 = new Institution(params)
        def obj2 = new Institution(params)
        obj2."$field" = value

        then:
        assert result == (obj1 == obj2)

        where:
        result        | field      | value
        true          | 'name'     | 'name'
        false         | 'name'     | 'nameXX'
        true          | 'region'   | europe
        false         | 'region'   | spain
    }

    @Unroll("test INSTITUCION equals Checking #field = #value expected #error")
    def "test INSTITUCION equals"() {
        when:
        def params = [ name:'name',
                region:europe
        ]
        params[field] = value
        def obj = new Institution()
        obj."$field" = value

        then:
        Helper.validateConstraints(obj, field, error)

        where:
        error           | field      | value
        'OK'            | 'name'     | 'nombre'
        'blank'         | 'name'     | ''
        'nullable'      | 'name'     | null
        'nullable'      | 'region'   | null
    }

}
