package kuorum.post

import com.mongodb.DBRef
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
    def fixtureLoader

    def setup(){
        KuorumUser.collection.getDB().dropDatabase()
        fixtureLoader.load("testData")
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
        Law law = Law.findByHashtag("#leyAborto")

        Post post = new Post(
                owner : user,
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

    @Unroll
    void "test sponsoring a post by #email amount #amount => #total"(){
        given: "A post"
            KuorumUser user = KuorumUser.findByEmail("peter@example.com")
            KuorumUser sponsorUser = KuorumUser.findByEmail(email)
            Sponsor sponsor = new Sponsor(kuorumUser:sponsorUser , amount: amount)
            Post post = Post.findByOwner(user)
        when: "sponsoring the post"
            postService.sponsorAPost(post, sponsor)
        then: "The post has a sponsor"
            Post recoveredPost = Post.get(post.id)
            recoveredPost.sponsors.find{it.kuorumUser == sponsorUser} != null
            recoveredPost.sponsors.find{it.kuorumUser == sponsorUser}.amount == total
            Post.withNewSession {
                //Check if is in DB
                Post recoveredPostNewSession = Post.get(post.id)
                recoveredPostNewSession.sponsors.find{it.kuorumUser == sponsorUser} != null
                recoveredPostNewSession.sponsors.find{it.kuorumUser == sponsorUser}.amount == total
            }
        where:
            email                       | amount | total
            "equo@example.com"          | 5      | 10
            "ecologistas@example.com"   | 5      | 5



    }
}
