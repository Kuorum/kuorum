package kuorum

import kuorum.users.KuorumUser
import org.springframework.security.core.userdetails.UserDetails
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created with IntelliJ IDEA.
 * KuorumUser: iduetxe
 * Date: 8/12/13
 * Time: 11:20
 * To change this template use File | Settings | File Templates.
 */
class MongoUserDetailServiceIntegrationSpec extends Specification{

    def mongoUserDetailsService
    def fixtureLoader

    def setup(){
        KuorumUser.collection.getDB().dropDatabase()
        fixtureLoader.load("testBasicData")
    }


    @Unroll
    void "test login user with mongo: #params -> #usernameDDBB"() {
        given: "Username ..."
        def username = params.username
        UserDetails userDetails = mongoUserDetailsService.loadUserByUsername(username)
        expect: "KuorumUser details"
        usernameDDBB == userDetails?.username
        where: "Username with params...."
        usernameDDBB || params
        "peter@example.com" || [username:'Peter@example.com']
        "peter@example.com" || [username:'peter@example.com']
    }

    @Unroll
    void "test login user no exists"() {
        given: "Username ..."
        def username = "XXX"
        when:
        UserDetails userDetails = mongoUserDetailsService.loadUserByUsername(username)

        then:
        final org.springframework.security.core.userdetails.UsernameNotFoundException exception = thrown()
        exception.message == "KuorumUser not found"
    }

    @Unroll
    void "test login username empty"() {
        given: "Username ..."
        def username = ""
        when:
        UserDetails userDetails = mongoUserDetailsService.loadUserByUsername(username)

        then:
        final org.springframework.security.core.userdetails.UsernameNotFoundException exception = thrown()
        exception.message == "Empty username"
    }
}
