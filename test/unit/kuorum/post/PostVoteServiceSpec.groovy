package kuorum.post

import grails.converters.JSON
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import kuorum.core.exception.KuorumException
import kuorum.helper.Helper
import kuorum.law.Law
import kuorum.notifications.NotificationService
import kuorum.users.KuorumUser
import org.bson.types.ObjectId
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(PostVoteService)
@Mock([Post, KuorumUser, Law,PostVote])
class PostVoteServiceSpec extends Specification {

    NotificationService notificationServiceMock = Mock(NotificationService)

    def setup() {
        service.notificationService = notificationServiceMock
        Post.metaClass.static.getCollection = {->
            [findOne: {
                delegate.findWhere(it) as JSON

            },
            update:{filter, updateData ->
                Post post = Post.get(filter._id)
                post.numVotes++
                post.save(flush:true)
                //post as JSON
            }
            ]
        }
        Post.metaClass.refresh={->
            //REFRESH FAILS with null pointer
        }
    }
    def cleanup() {
    }

    @Unroll
    void "test voting post #votes times"() {
        given: "A post"
        //fixtureLoader.load("testData")
        Post post = Helper.createDefaultPost().save()
        KuorumUser user = Helper.createDefaultUser("user@example.com").save()

        when: "Voting a post"
        //"service" represents the grails service you are testing for
        long numVotes = post.numVotes

        (1..votes).each{
            PostVote postVote = service.votePost(post,user)
        }

        then: "Checking num votes and notifications calls"
        PostVote.findAllByPost(post).size() == votes
        post.numVotes == numVotes +votes
        publicMilestoneNotification * notificationServiceMock.sendPublicMilestoneNotification(post)
        milestoneNotification * notificationServiceMock.sendMilestoneNotification(post)
        where:
            votes | publicMilestoneNotification | milestoneNotification
            1     | 0                           | 0
            4     | 0                           | 1
            10    | 1                           | 1
            25    | 1                           | 2
        //LAZY ARRAY
            50    | 1                           | 3
            100   | 1                           | 4
            250   | 1                           | 5
            500   | 1                           | 6
            1000  | 1                           | 7
            2500  | 1                           | 8
    }
}
