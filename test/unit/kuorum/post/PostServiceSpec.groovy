package kuorum.post

import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.userdetails.GrailsUser
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.test.spock.IntegrationSpec
import kuorum.core.exception.KuorumException
import kuorum.core.model.PostType
import kuorum.helper.Helper
import kuorum.law.Law
import kuorum.notifications.NotificationService
import kuorum.solr.IndexSolrService
import kuorum.users.KuorumUser
import org.bson.types.ObjectId
import spock.lang.Specification

@TestFor(PostService)
@Mock([KuorumUser, Post, Law])
class PostServiceSpec extends Specification{

    IndexSolrService indexSolrService = Mock(IndexSolrService)
    PostVoteService postVoteService = Mock(PostVoteService)
    NotificationService notificationService = Mock(NotificationService)
    CluckService cluckService = Mock(CluckService)
    def setup() {
        service.indexSolrService = indexSolrService
        service.postVoteService = postVoteService
        service.notificationService = notificationService
        service.cluckService = cluckService
    }


    void "test create post with wrong params"() {
        given: "A post"
            //fixtureLoader.load("testData")
            Post post = new Post()


            def cluckServiceMock = mockFor(CluckService)
            service.cluckService = cluckServiceMock

        when: "Saving a post"
            //"service" represents the grails service you are testing for
        service.savePost(post)

        then: "Expected an exception"
            final KuorumException exception = thrown()
            0 * cluckServiceMock.createCluck(post, post.owner)
            //exception.message == "KuorumUser not found"
            //Assertion goes here
    }

    void "test create post with correct params "() {
        given: "A post"
        //fixtureLoader.load("testData")
        Post post = Helper.createDefaultPost()

        when: "Saving a post"
        //"service" represents the grails service you are testing for
        service.savePost(post)

        then: "Expected an exception"
        0 * indexSolrService.index(_)
        0 * cluckService.createCluck(_,_)
        0 * postVoteService.votePost(_,_)
    }

    void "test publishing a post"() {
        given: "A post"
        //fixtureLoader.load("testData")
        Post post = Helper.createDefaultPost()

        when: "Saving a post"
        //"service" represents the grails service you are testing for
        service.publishPost(post)

        then: "Expected an exception"
        1 * indexSolrService.index(post)
        1 * cluckService.createCluck(post,post.owner)
        1 * postVoteService.votePost(post,post.owner)
    }
}
