package kuorum.users

import grails.converters.JSON
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import kuorum.core.exception.KuorumException
import kuorum.helper.Helper
import kuorum.notifications.NotificationService
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(KuorumUserService)
@Mock([KuorumUser])
class KuorumUserServiceSpec extends Specification {

    NotificationService notificationServiceMock = Mock(NotificationService)

    def setup() {
        service.notificationService = notificationServiceMock

        KuorumUser.metaClass.static.getCollection = {->
            [findOne: {
                delegate.findWhere(it) as JSON

            },
                update:{filter, updateData ->
                    KuorumUser user = KuorumUser.get(filter._id)
                    KuorumUser following = KuorumUser.get(updateData.'$addToSet'.following)
                    KuorumUser follower = KuorumUser.get(updateData.'$addToSet'.followers)
                    if (following) user.following << following.id
                    if (follower) user.followers << follower.id
                    user.save(flush:true)
                    //post as JSON
                }
            ]
        }
        KuorumUser.metaClass.refresh={->
            //REFRESH FAILS with null pointer
        }
    }

    def cleanup() {
    }

    void "test creating a follower"() {
        given: "2 users"
        KuorumUser follower = Helper.createDefaultUser("user1@ex.com")
        follower.save()
        KuorumUser following = Helper.createDefaultUser("user2@ex.com")
        following.save()
        Integer numFollowers = following.numFollowers

        when: "Adding a follower"
        //"service" represents the grails service you are testing for
        service.createFollower(follower,following)
        then: "All OK"
        follower.following.contains(following.id)
        following.followers.contains(follower.id)
        numFollowers == following.numFollowers -1
        1 * notificationServiceMock.sendFollowerNotification(follower,following)
        0 * notificationServiceMock.sendFollowerNotification(following,follower)
    }

    void "test creating a follower for himself"() {
        given: "A user"
        KuorumUser user = new KuorumUser()
        user.save()
        when: "Following himself"
        service.createFollower(user,user)
        then: "thorws kuorum exception"
        final KuorumException exception = thrown()
        exception.errors[0].code == "error.following.sameUser"
        0 * notificationServiceMock.sendFollowerNotification(_,_)
    }

    void "test creating a follower for a user that is already following"() {
        given: "2 users"
        KuorumUser following = Helper.createDefaultUser("user2@ex.com")
        following.save()

        KuorumUser follower = Helper.createDefaultUser("user1@ex.com")
        follower.following.add(following.id)
        follower.save()
        following.followers.add(follower.id)
        following.numFollowers++
        following.save()
        Integer numFollowers = following.numFollowers

        when: "Adding a follower"
        //"service" represents the grails service you are testing for
        service.createFollower(follower,following)
        then: "All OK"
        following.followers.contains(follower.id)
        follower.following.contains(following.id)
        follower.following.findAll{it==following.id}.size() == 1
        following.followers.findAll{it==follower.id}.size() == 1
        numFollowers == following.numFollowers
        0 * notificationServiceMock.sendFollowerNotification(follower,following)
        0 * notificationServiceMock.sendFollowerNotification(following,follower)
    }

    void "test user recommendations"(){
        given: "many users"
        List.metaClass.isSorted = { -> delegate == delegate.sort( false ) }
        (1..numUsers).each{
            KuorumUser user= Helper.createDefaultUser("user${it}@ex.com")
            user.numFollowers = it %3
            user.save()
        }

        when: "recovery recommended users"
        List<KuorumUser> recommended = service.recommendedUsers()
        then: "Best 10 users are showed"
        recommended.size() == numResults
        recommended.collect{it.numFollowers}.isSorted()

        where:
        numUsers | numResults
        5        | 5
        20       | 10
    }
}
