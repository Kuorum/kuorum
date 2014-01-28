package kuorum.users

import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(User)
class UserSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "test something"() {
    }

    @Unroll
    def "test validating basic user constraints with params #params -> result: #isValidate"(){
        given: "PersonUser params..."
        def user = new User()
        user.properties = params
        expect: "Validation constraints..."
        isValidate == user.validate()
        where: "with params...."
        isValidate || params
        false || [:]
        false || [name:'nombre']
        false || [name:'nombre', username:'nicknmae']
        true  || [name:'nombre', mail:'email@email.com']
        true  || [username:'nombre', mail:'email@email.com']
        true  || [name: 'nombre', username:'nombre', mail:'email@email.com']
    }
}
