package kuorum.users

import spock.lang.Specification

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
}
