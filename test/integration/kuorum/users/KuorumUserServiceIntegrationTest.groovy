package kuorum.users

import spock.lang.Specification

/**
 * Created by iduetxe on 17/03/14.
 */
class KuorumUserServiceIntegrationTest extends Specification {

    def kuorumUserService

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
            KuorumUser followerNS = KuorumUser.findByEmail("peter@example.com")
            KuorumUser followingNS = KuorumUser.findByEmail("equo@example.com")
            followerNS.following.contains(followingNS)
            followingNS.followers.contains(followerNS)
            futureNumFollowers == followingNS.numFollowers -1
        }
        where:
        followerEmail           | followingEmail
        "peter@example.com"     | "equo@example.com"
        "peter@example.com"     | "carmen@example.com"
    }
}
