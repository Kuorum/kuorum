package kuorum.users

import kuorum.Region
import kuorum.core.model.RegionType
import kuorum.core.model.UserType
import spock.lang.IgnoreRest
import spock.lang.Specification
import spock.lang.Unroll
import kuorum.helper.IntegrationHelper

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
        if (!follower.following.contains(following.id)){
            futureNumFollowers ++
        }

        when: "Adding a follower"
        //"service" represents the grails service you are testing for
        kuorumUserService.createFollower(follower,following)
        then: "All OK"
        follower.following.contains(following.id)
        following.followers.contains(follower.id)
        futureNumFollowers == following.numFollowers
        KuorumUser.withNewSession {
            KuorumUser followerNS = KuorumUser.findByEmail(followerEmail)
            KuorumUser followingNS = KuorumUser.findByEmail(followingEmail)
            followerNS.following.contains(followingNS.id)
            followingNS.followers.contains(followerNS.id)
            futureNumFollowers == followingNS.numFollowers
        }
        where:
        followerEmail           | followingEmail
        "peter@example.com"     | "equo@example.com"
        "peter@example.com"     | "carmen@example.com"
    }

    @Unroll
    void "test deleting a follower"() {
        given: "2 users"
        KuorumUser follower = KuorumUser.findByEmail(followerEmail)
        KuorumUser following = KuorumUser.findByEmail(followingEmail)

        if (createFollower)
            kuorumUserService.createFollower(follower,following)

        Integer futureNumFollowers = following.numFollowers
        if (follower.following.contains(following.id)){
            futureNumFollowers --
        }

        when: "Deleting follower"
        //"service" represents the grails service you are testing for
        kuorumUserService.deleteFollower(follower,following)
        then: "All OK"
        !follower.following.contains(following.id)
        !following.followers.contains(follower.id)
        futureNumFollowers == following.numFollowers
        KuorumUser.withNewSession {
            KuorumUser followerNS = KuorumUser.findByEmail(followerEmail)
            KuorumUser followingNS = KuorumUser.findByEmail(followingEmail)
            !followerNS.following.contains(followingNS.id)
            !followingNS.followers.contains(followerNS.id)
            futureNumFollowers == followingNS.numFollowers
        }
        where:
        followerEmail           | followingEmail        | createFollower
        "peter@example.com"     | "equo@example.com"    | false
        "peter@example.com"     | "carmen@example.com"  | true
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

    void "Order users by the activity criteria"(){
        given: "A user"
        KuorumUser user= IntegrationHelper.createDefaultUser("user${new Date().getTime()}@ex.com").save(flush: true)

        and: "All users, except the created user"
        List<KuorumUser> userList = KuorumUser.findAllByIdNotEqual(user.id)

        and: "The expected list of users sorted by the activity criteria"
        List<KuorumUser> expectedUserList = [
                KuorumUser.findByEmail('ecologistas@example.com'), KuorumUser.findByEmail('equo@example.com'),
                KuorumUser.findByEmail('politicianinactive@example.com'), KuorumUser.findByEmail('politician@example.com'),
                KuorumUser.findByEmail('carmen@example.com'), KuorumUser.findByEmail('juanjoalvite@example.com'),
                KuorumUser.findByEmail('peter@example.com'), KuorumUser.findByEmail('admin@example.com'),
                KuorumUser.findByEmail('noe@example.com'), KuorumUser.findByEmail('newuser@example.com')]

        when: "Obtain the recommended users"
        List<KuorumUser> recommended = kuorumUserService.orderUsersByActivity(user, userList)

        then: "The expected list and the obtained recommended user are equals"
        recommended == expectedUserList

        cleanup:
        user.delete(flush: true)

    }
}
