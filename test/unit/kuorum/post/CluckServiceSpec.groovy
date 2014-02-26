package kuorum.post

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import kuorum.notifications.NotificationService
import kuorum.post.CluckController
import kuorum.users.KuorumUser
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(CluckService)
@Mock([KuorumUser, Post, Cluck])
class CluckServiceSpec extends Specification {

    NotificationService notificationServiceMock = Mock(NotificationService)

    def setup() {
        service.notificationService = notificationServiceMock
    }

    def cleanup() {
    }

    void "test notification is called"() {
        given: "A post"
        //fixtureLoader.load("testData")
        Post post = new Post()
        post.save()
        KuorumUser user = new KuorumUser()
        user.save()



        when: "Creating a cluck"
        //"service" represents the grails service you are testing for
        Cluck cluck = service.createCluck(post,user)
        then: "All OK"
            cluck != null
            cluck.post == post
            cluck.owner == user
            1 * notificationServiceMock.sendCluckNotification(_)
    }
}
