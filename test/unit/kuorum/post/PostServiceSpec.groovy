package kuorum.post

import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.userdetails.GrailsUser
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.test.spock.IntegrationSpec
import kuorum.core.exception.KuorumException
import kuorum.core.model.PostType
import kuorum.law.Law
import kuorum.users.KuorumUser
import org.bson.types.ObjectId
import spock.lang.Specification

@TestFor(PostService)
@Mock([KuorumUser, Post, Law])
class PostServiceSpec extends Specification{

    def setup(){
        def springSecurityServiceMock = mockFor(SpringSecurityService)
        service.springSecurityService = [principal: [id: new ObjectId("52fd88f044ae06c6be306f39")], currentUser:Mock(KuorumUser){id:new ObjectId("52fd88f044ae06c6be306f39")}]
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
}
