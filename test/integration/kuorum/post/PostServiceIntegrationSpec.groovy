package kuorum.post

import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.SpringSecurityUtils
import kuorum.core.exception.KuorumException
import kuorum.core.model.PostType
import kuorum.law.Law
import kuorum.users.KuorumUser
import org.bson.types.ObjectId
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by iduetxe on 2/02/14.
 */
class PostServiceIntegrationSpec extends Specification{

    def postService
    def springSecurityService
    def fixtureLoader

    def setup(){

    }

    void "test create post with wrong params"() {
        given: "A post"
        Post post = new Post()

        when: "Saving a post"
        Post savedPost = postService.savePost(post)

        then: "Expected an exception"
        final KuorumException exception = thrown()
    }

    void "test create post with correct params"() {
        given: "A post"
        KuorumUser user = KuorumUser.findByEmail("peter@example.com")
        springSecurityService.reauthenticate(user.email, user.password)
        Law law = Law.findByHashtag("#leyAborto")

        Post post = new Post(
                law:law,
                numClucks:1,
                numVotes:1,
                postType:PostType.PURPOSE,
                title:"Enmienda a la totalidad",
                text:"Lorem ipsum"
        )

        when: "Saving a post"
        //"service" represents the grails service you are testing for
        Post savedPost = postService.savePost(post)

        then: "Expected an exception"
        savedPost.id != null
        Cluck cluck = Cluck.findByPostAndOwnerAndPostOwner(savedPost, user,user)
        cluck != null
        cluck.post == savedPost
        cluck.postOwner == savedPost.owner
        //exception.message == "KuorumUser not found"
        //Assertion goes here
    }
}
