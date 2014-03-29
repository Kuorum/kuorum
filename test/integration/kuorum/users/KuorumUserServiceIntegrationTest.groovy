package kuorum.users

import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by iduetxe on 17/03/14.
 */
class KuorumUserServiceIntegrationTest extends Specification {

    def kuorumUserService
    def fixtureLoader

    def setup(){
        KuorumUser.collection.getDB().dropDatabase()
        fixtureLoader.load("testBasicData")
    }

    void "test creating a follower"() {
        given: "2 users"
        KuorumUser follower = KuorumUser.findByEmail(followerEmail)
        KuorumUser following = KuorumUser.findByEmail(followingEmail)
        Integer futureNumFollowers = following.numFollowers
        if (!follower.following.contains(following)){
            futureNumFollowers ++
        }

        when: "Adding a follower"
        //"service" represents the grails service you are testing for
        kuorumUserService.createFollower(follower,following)
        then: "All OK"
        follower.following.contains(following)
        following.followers.contains(follower)
        futureNumFollowers == following.numFollowers
        KuorumUser.withNewSession {
            KuorumUser followerNS = KuorumUser.findByEmail(followerEmail)
            KuorumUser followingNS = KuorumUser.findByEmail(followingEmail)
            followerNS.following.contains(followingNS)
            followingNS.followers.contains(followerNS)
            futureNumFollowers == followingNS.numFollowers
        }
        where:
        followerEmail           | followingEmail
        "peter@example.com"     | "equo@example.com"
        "peter@example.com"     | "carmen@example.com"
    }

    @Unroll
    void "test converting as premium on #email"() {
        given: "1 users"
        KuorumUser user = KuorumUser.findByEmail(email)
        when:
        kuorumUserService.convertAsPremium(user)
        then:
        user.authorities.collect{it.authority}.contains("ROLE_PREMIUM")
        user.authorities.size() == 2
        KuorumUser.withNewSession {
            KuorumUser userNS = KuorumUser.findByEmail(email)
            userNS.authorities.collect{it.authority}.contains("ROLE_PREMIUM")
            userNS.authorities.size() == 2
        }
        where:
        email                       | result
        "peter@example.com"         | true  //He has not the role yet
        "juanjoalvite@example.com"  | true  // He has already the role
    }

    @Unroll
    void "test deleting premium roles on user #email"() {
        given: "1 premium users"
        KuorumUser user = KuorumUser.findByEmail(email)
        when:
        kuorumUserService.convertAsNormalUser(user)
        then:
        !user.authorities.collect{it.authority}.contains("ROLE_PREMIUM")
        user.authorities.size() == 1
        KuorumUser.withNewSession {
            KuorumUser userNS = KuorumUser.findByEmail(email)
            !userNS.authorities.collect{it.authority}.contains("ROLE_PREMIUM")
            userNS.authorities.size() == 1
        }
        where:
        email                       | result
        "peter@example.com"         | true  //He has not the role yet
        "juanjoalvite@example.com"  | true  // He has already the role

    }
}
