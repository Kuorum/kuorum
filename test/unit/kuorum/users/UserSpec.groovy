package kuorum.users

import grails.test.mixin.TestFor
import kuorum.core.model.AvailableLanguage
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

    @Unroll
    def "test USER constraints with params #params -> result: #isValidate"(){
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
        true  || [name:'nombre', email:'email@email.com', password:"XX"]
        true  || [
                name:'nombre',
                password:'XX',
                email:'email@email.com',
                username:'email@email.com',
                languaje:AvailableLanguage.es_ES
                ]
    }

    @Unroll
    def "test USER equals with params #params -> result: #equals"(){
        given: "PersonUser params..."
        def user1 = new User()
        def user2 = new User()
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
        def user = new User()
        user.properties = params
        expect: "Check to String"
        toString == "${user}"
        where: "with params...."
        toString || params
        "email@email.com" || [name:'name', email:"email@email.com"]
        "nickname" || [username:'nickname',email:"email@email.com"]
    }
}
