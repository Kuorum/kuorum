package kuorum.post

import grails.converters.JSON
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import kuorum.core.exception.KuorumException
import kuorum.helper.Helper
import kuorum.project.Project
import kuorum.notifications.NotificationService
import kuorum.users.GamificationService
import kuorum.users.KuorumUser
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(PostVoteService)
@Mock([Post, KuorumUser, Project,PostVote])
class PostVoteServiceSpec extends Specification {

    NotificationService notificationServiceMock = Mock(NotificationService)
    GamificationService gamificationService = Mock(GamificationService)

    def setup() {
        service.notificationService = notificationServiceMock
        service.gamificationService = gamificationService
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
    void "test voting a post anonymous: #anonymous"() {
        given: "A post"
        //fixtureLoader.load("testData")
        Post post = Helper.createDefaultPost()
        post.numVotes = 0
        post.save()
        KuorumUser user = Helper.createDefaultUser("user@example.com").save()

        long numVotes = post.numVotes

        when: "Voting a post"
        //"service" represents the grails service you are testing for
        PostVote postVote = service.votePost(post,user, anonymous)

        then: "Checking num votes and notifications calls"
        PostVote.findAllByPost(post).size() == numVotes +1
        post.numVotes == numVotes + 1
        postVote.anonymous == anonymous
        postVote.post == post
        postVote.user == user
        0 * notificationServiceMock.sendPublicMilestoneNotification(post)
        0 * gamificationService.postVotedAward(post.owner, post)
        1 * gamificationService.postVotedAward(user, post)
        where:
        anonymous | kk
        true      | false
        false      | false
    }


    void "test voting twice"() {
        given: "A post and an user"
        //fixtureLoader.load("testData")
        Post post = Helper.createDefaultPost().save()
        KuorumUser user = Helper.createDefaultUser("user@example.com").save()
        PostVote postVote = service.votePost(post,user)
        when: "Voting a post twice"
        //"service" represents the grails service you are testing for

        long numVotes = post.numVotes
        PostVote postVote2 = service.votePost(post,user)

        then: "Exception thrown"
        final KuorumException exception = thrown()
        exception.errors[0].code == "error.postVoteService.userAleradyVote"
        numVotes == post.numVotes
    }

    @Unroll
    void "test voting post #votes times"() {
        given: "A post"
        //fixtureLoader.load("testData")
        Post post = Helper.createDefaultPost().save()

        when: "Voting a post"
        //"service" represents the grails service you are testing for
        long numVotes = post.numVotes

        (1..votes).each{
            KuorumUser user = Helper.createDefaultUser("user${it}@example.com").save()
            PostVote postVote = service.votePost(post,user)
        }

        then: "Checking num votes and notifications calls"
        PostVote.findAllByPost(post).size() == votes
        post.numVotes == numVotes +votes
        publicMilestoneNotification * notificationServiceMock.sendPublicMilestoneNotification(post)
        publicMilestoneNotification * gamificationService.postCreatedAward(post.owner, post)
        milestoneNotification * notificationServiceMock.sendMilestoneNotification(post)
        votes * gamificationService.postVotedAward(_,post)
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
