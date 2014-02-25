package kuorum.users

import grails.test.mixin.TestFor
import kuorum.Region
import kuorum.core.model.AvailableLanguage
import kuorum.helper.Helper
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(KuorumUser)
class KuorumUserSpec extends Specification {

    @Shared
    Region europe = new Region(name:"Europe", iso3166_2: "EU")

    def setup() {
        mockForConstraintsTests(KuorumUser, [new KuorumUser()])
    }

    def cleanup() {
    }

    @Unroll("test USER constraints: Checking #field = #value expected #error")
    def "test USER all constraints"() {
        when:

        def params = [
                name:'nombre',
                password:'XX',
                email:'email@email.com',
                languaje:AvailableLanguage.es_ES
        ]
        params[field] = value
        def obj = new KuorumUser( params)
        then:
        Helper.validateConstraints(obj, field, error)

        where:
        error           | field         | value
        'OK'            | 'name'        | 'name'
        'email'         | 'email'       | 'email'
    }

    @Unroll
    def "test USER equals with params #params -> result: #equals"(){
        given: "PersonUser params..."
        def user1 = new KuorumUser()
        def user2 = new KuorumUser()
        user1.properties = params.user1
        user2.properties = params.user2
        expect: "Equals..."
        equals == (user1 == user2)
        where: "with params...."
        equals || params
        false || [:]
        false || [user1:[name:'nombre'],user2:[name:'nombre']]
        false || [user1:[email:'email1@email.com'],user2:[email:'email2@email.com']]
        true || [user1:[email:'email@email.com'],user2:[email:'email@email.com']]
    }


    @Unroll
    def "test USER toString"(){
        given: "PersonUser params..."
        def user = new KuorumUser()
        user.properties = params
        expect: "Check to String"
        toString == "${user}"
        where: "with params...."
        toString || params
        "name" || [name:'name', email:"email@email.com"]
//        "nickname" || [username:'nickname',email:"email@email.com"]
    }
}
