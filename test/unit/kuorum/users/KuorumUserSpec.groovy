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
        KuorumUser user = Helper.createDefaultUser("email@email.com")
        user."$field"=value
        then:
        Helper.validateConstraints(user, field, error)

        where:
        error           | field         | value
        'OK'            | 'name'        | 'name'
        'email'         | 'email'       | 'email'
    }

    @Unroll
    def "test #email1 equals #email2 are equals #equals"(){
        given: "PersonUser params..."
        def user1 = Helper.createDefaultUser(email1)
        def user2 = email2?Helper.createDefaultUser(email2):null
        expect: "Equals..."
        equals == (user1 == user2)
        where: "with params...."
        equals  |    email1             | email2
        false   | 'email1@email.com'    | 'email2@email.com'
        false   | 'email1@email.com'    | null
        false   | 'email1@email.com'    | ''
        true    | 'email1@email.com'    | 'email1@email.com'
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
