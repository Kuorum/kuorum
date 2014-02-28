package kuorum.notifications

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import kuorum.helper.Helper
import kuorum.law.Law
import kuorum.mail.KuorumMailService
import kuorum.post.Cluck
import kuorum.post.Post
import kuorum.post.PostComment
import kuorum.post.PostVote
import kuorum.users.KuorumUser
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(NotificationService)
@Mock([KuorumUser, Cluck, Law, Post,CluckNotification, FollowerNotification, CommentNotification, PostVote,PublicMilestoneNotification,DebateAlertNotification,DebateNotification])
class NotificationServiceSpec extends Specification {

    KuorumMailService kuorumMailService = Mock(KuorumMailService)
    def setup() {
        service.kuorumMailService = kuorumMailService
    }

    def cleanup() {
    }

    void "test cluck notification"() {
        given: "A cluck"
        KuorumUser user1 = Helper.createDefaultUser("user1@ex.com").save()
        KuorumUser user2 = Helper.createDefaultUser("user2@ex.com").save()
        Law law  = Helper.createDefaultLaw("#law"); law.save()
        Post post = Helper.createDefaultPost(user1,law); post.save()
        Cluck cluck = new Cluck(
                law:law,
                owner: user2,
                postOwner: user1,
                post:post

        )

        cluck.save()

        when: "Sending notification"
        //"service" represents the grails service you are testing for
        service.sendCluckNotification(cluck)
        CluckNotification cluckNotification = CluckNotification.findByCluckerAndKuorumUser(user2, user1)
        then: "All OK"
        cluckNotification != null
        cluckNotification.post == post
        1 * kuorumMailService.sendCluckNotificationMail(cluck)
    }

    void "test new follower"() {
        given: "2 users"
        KuorumUser user1 = Helper.createDefaultUser("user1@ex.com").save()
        KuorumUser user2 = Helper.createDefaultUser("user2@ex.com").save()

        when: "Sending notification"
        //"service" represents the grails service you are testing for
        service.sendFollowerNotification(user1, user2)
        FollowerNotification followerNotification = FollowerNotification.findByFollowerAndKuorumUser(user1,user2)
        then: "All OK and mail service has been called"
        followerNotification
        1 * kuorumMailService.sendFollowerNotificationMail(user1, user2)
    }

    void "test new comment"() {
        given: "2 users"
        KuorumUser user1 = Helper.createDefaultUser("user1@ex.com").save()
        Law law = Helper.createDefaultLaw("#test")
        Post post = Helper.createDefaultPost(user1, law)
        KuorumUser user2 = Helper.createDefaultUser("user2@ex.com").save()

        when: "Sending notification"
        //"service" represents the grails service you are testing for
        service.sendCommentNotification(user2, post)
        CommentNotification commentNotification = CommentNotification.findByTertullianAndKuorumUser(user2,user1)
        then: "All OK and mail service has not been called"
        commentNotification
        0 * kuorumMailService._(1..99) //NO se si esto hace algo
    }

    void "test public milestone voting posts"() {
            given: "A post"
            Post post = Helper.createDefaultPost().save()

            when: "Sending notification"
            //"service" represents the grails service you are testing for
            service.sendPublicMilestoneNotification(post)
            PublicMilestoneNotification publicMilestoneNotification = PublicMilestoneNotification.findByPost(post)
            then: "All OK and mail service has been called"
            publicMilestoneNotification
            1 * kuorumMailService.sendPublicMilestoneNotificationMail(post)
        }


    void "test sending debate notification"() {
        given: "Creating a post, its votes and a comment"
        Post post = Helper.createDefaultPost().save()
        //Owner vote
        PostVote ownerVote = new PostVote(post:post, user:post.owner, personalData: post.owner.personalData)
        ownerVote.save()
        //Politician
        KuorumUser politician = Helper.createDefaultUser("politician@example.com")
        post.debates = [new PostComment(kuorumUser: politician, text: "TEXTO MOLON")]
        politician.save()
        Integer numVotes = 5
        KuorumUser user
        (1..numVotes).each {
            user = Helper.createDefaultUser("user${it}@example.com")
            user.save()
            PostVote vote = new PostVote(post:post, user:user, personalData: user.personalData)
            vote.save()
        }
        KuorumUser followerPolitician = Helper.createDefaultUser("follower@example.com")
        politician.followers = [followerPolitician,user]
        politician.save()
        followerPolitician.following << politician
        user.following << politician
        followerPolitician.save()
        user.save()

        when: "Sending notification"
        //"service" represents the grails service you are testing for
        def promise = service.sendDebateNotification(post)
        then: "All OK and mail service has been called"
        DebateNotification.findAllByPost(post).size() - DebateAlertNotification.findAllByPost(post).size() ==6
        DebateAlertNotification.findAllByPost(post).size()==1
        1 * kuorumMailService.sendDebateNotificationMail(post, { it.size == numVotes })
    }


}
