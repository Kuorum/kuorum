package kuorum.users

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
    }

    def cleanup() {
    }

    void "test creating a follower"() {
        given: "2 users"
        KuorumUser follower = Helper.createDefaultUser("user1@ex.com")
        follower.save()
        KuorumUser following = Helper.createDefaultUser("user2@ex.com")
        following.save()

        when: "Adding a follower"
        //"service" represents the grails service you are testing for
        service.createFollower(follower,following)
        then: "All OK"
        follower.following.contains(following)
        following.followers.contains(follower)
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
}
