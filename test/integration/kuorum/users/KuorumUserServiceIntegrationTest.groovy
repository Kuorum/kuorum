package kuorum.users

import spock.lang.Ignore
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by iduetxe on 17/03/14.
 */
class KuorumUserServiceIntegrationTest extends Specification {

    def kuorumUserService
    def fixtureLoader

    def setup() {
        KuorumUser.collection.getDB().dropDatabase()
        fixtureLoader.load("testBasicData")
    }

    void "test creating a follower"() {
        given: "2 users"
        KuorumUser follower = KuorumUser.findByEmail(followerEmail)
        KuorumUser following = KuorumUser.findByEmail(followingEmail)
        Integer futureNumFollowers = following.numFollowers
        if (!follower.following.contains(following.id)) {
            futureNumFollowers++
        }

        when: "Adding a follower"
        //"service" represents the grails service you are testing for
        kuorumUserService.createFollower(follower, following)
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
        followerEmail       | followingEmail
        "peter@example.com" | "equo@example.com"
        "peter@example.com" | "carmen@example.com"
    }

    @Unroll
    void "test deleting a follower"() {
        given: "2 users"
        KuorumUser follower = KuorumUser.findByEmail(followerEmail)
        KuorumUser following = KuorumUser.findByEmail(followingEmail)

        if (createFollower)
            kuorumUserService.createFollower(follower, following)

        Integer futureNumFollowers = following.numFollowers
        if (follower.following.contains(following.id)) {
            futureNumFollowers--
        }

        when: "Deleting follower"
        //"service" represents the grails service you are testing for
        kuorumUserService.deleteFollower(follower, following)
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
        followerEmail       | followingEmail       | createFollower
        "peter@example.com" | "equo@example.com"   | false
        "peter@example.com" | "carmen@example.com" | true
    }

    @Unroll
    void "test converting as premium on #email"() {
        given: "1 users"
        KuorumUser user = KuorumUser.findByEmail(email)
        when:
        kuorumUserService.convertAsPremium(user)
        then:
        user.authorities.collect { it.authority }.contains("ROLE_PREMIUM")
        user.authorities.size() == 2
        KuorumUser.withNewSession {
            KuorumUser userNS = KuorumUser.findByEmail(email)
            userNS.authorities.collect { it.authority }.contains("ROLE_PREMIUM")
            userNS.authorities.size() == 2
        }
        where:
        email                      | result
        "peter@example.com"        | true  //He has not the role yet
        "juanjoalvite@example.com" | true  // He has already the role
    }

    @Unroll
    void "test deleting premium roles on user #email"() {
        given: "1 premium users"
        KuorumUser user = KuorumUser.findByEmail(email)
        when:
        kuorumUserService.convertAsNormalUser(user)
        then:
        !user.authorities.collect { it.authority }.contains("ROLE_PREMIUM")
        user.authorities.size() == 1
        KuorumUser.withNewSession {
            KuorumUser userNS = KuorumUser.findByEmail(email)
            !userNS.authorities.collect { it.authority }.contains("ROLE_PREMIUM")
            userNS.authorities.size() == 1
        }
        where:
        email                      | result
        "peter@example.com"        | true  //He has not the role yet
        "juanjoalvite@example.com" | true  // He has already the role

    }

    @Ignore('Check this test when the fixtures are solved')
    void "Calculate the activity between two users"() {
        given: "A user"
        KuorumUser user = KuorumUser.findByEmail('politician@example.com')

        and: "All users, except the created user"
        KuorumUser compareUser = KuorumUser.findByEmail('carmen@example.com')

        when: "Calculate the activity between the users"
        Integer activity = kuorumUserService.calculateActivityClosure.call(user, compareUser)

        then: "The expected list and the obtained recommended user are equals"
        activity == 150
    }

    @Ignore('Check this test when the fixtures are solved')
    @Unroll
    void "Calculate the recommended users by Facebook friends"() {
        given: "A user"
        KuorumUser user = KuorumUser.findByEmail('politician@example.com')

        and: "A recommendeUserInfo for the user"
        new RecommendedUserInfo(
                user: user,
                recommendedUsers: [KuorumUser.findByEmail('carmen@example.com').id]
        ).save()

        and: "A mock list of facebook friends"
        Map facebookFriends = [data: []]

        emails.each { String email ->
            facebookFriends.data << [id: KuorumUser.findByEmail(email).id]
        }

        and: "A mock list of FacebookUsers"
        List<FacebookUser> facebookUsers = []
        emails.eachWithIndex { String email, int i ->
            facebookUsers << new FacebookUser(
                    user: KuorumUser.findByEmail(email),
                    accessToken: 'accesToken',
                    id: KuorumUser.findByEmail(email).id
            ).save(flush: true)
        }
        //Change the maxSize constraint only for test mode
        RecommendedUserInfo.constraints.recommendedUsers.maxSize = 2

        when: "Calculate the recommended users by Facebook friends"
        List<KuorumUser> recommendedUsersByFacebookFriends = kuorumUserService.recommendedUsersByFacebookFriends(user, facebookUsers, facebookFriends)

        then: "The expected list and the obtained recommended user are equals"
        recommendedUsersByFacebookFriends.size() == resultSize

        where:
        emails                                                                      | resultSize
        ['ecologistas@example.com']                                                 | 2
        ['ecologistas@example.com', 'equo@example.com']                             | 2
        ['ecologistas@example.com', 'equo@example.com', 'juanjoalvite@example.com'] | 2
    }
}
