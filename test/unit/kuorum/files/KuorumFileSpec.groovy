package kuorum.files

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import kuorum.KuorumFile
import kuorum.Region
import kuorum.core.FileGroup
import kuorum.helper.Helper
import kuorum.users.KuorumUser
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(KuorumFile)
@Mock(KuorumUser)
class KuorumFileSpec extends Specification {

    def setup() {
        mockForConstraintsTests(KuorumFile, [new KuorumFile()])
    }

    def cleanup() {
    }

    @Unroll("test KuorumFile constraints (local=#local): Checking #field = #value expected #error")
    def "test USER all constraints"() {
        when:
        KuorumUser user = Helper.createDefaultUser("email@email.com")
        KuorumFile kuorumFile = new KuorumFile(
                fileGroup: FileGroup.USER_AVATAR,
                temporal:true,
                user: user,
                url:"http://kuorum.org",
                local: local,
                storagePath: "/tmp",
                fileName: "file.jpg"
        )
        kuorumFile."$field"=value
        then:
        Helper.validateConstraints(kuorumFile, checkField, error)

        where:
        error           | field         | value  | checkField  |  local
        'OK'            | 'fileName'    | 'name' | 'fileName'  |  true
        'validator'     | 'fileName'    | ''     | 'local'     |  true
        'validator'     | 'fileName'    | null   | 'local'     |  true
        'validator'     | 'storagePath' | ''     | 'local'     |  true
        'validator'     | 'storagePath' | null   | 'local'     |  true
        'OK'            | 'fileName'    | ''     | 'local'     |  false
        'OK'            | 'fileName'    | null   | 'local'     |  false
        'OK'            | 'storagePath' | ''     | 'local'     |  false
        'OK'            | 'storagePath' | null   | 'local'     |  false
        'url'           | 'url'         | 'kk'   | 'url'       |  true

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
