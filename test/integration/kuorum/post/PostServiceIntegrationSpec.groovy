package kuorum.post

import kuorum.core.exception.KuorumException
import kuorum.core.model.PostType
import kuorum.law.Law
import kuorum.users.KuorumUser
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
            Cluck cluck = recoveredPost.firstCluck
            recoveredPost.sponsors.find{it.kuorumUser == sponsorUser} != null
            recoveredPost.sponsors.find{it.kuorumUser == sponsorUser}.amount == total
            cluck.sponsors.find{it.kuorumUser == sponsorUser} != null
            cluck.sponsors.find{it.kuorumUser == sponsorUser}.amount == total
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

    void "test adding comments"() {
        given: "A post"
        KuorumUser user = KuorumUser.findByEmail("peter@example.com")
        KuorumUser noe = KuorumUser.findByEmail("noe@example.com")
        Post post = Post.findByOwner(user)
        PostComment postComment1 = new PostComment(kuorumUser:noe, text:"1 -- Loren ipsum")
        PostComment postComment2 = new PostComment(kuorumUser:user, text:"2 -- Loren ipsum")
        PostComment postComment3 = new PostComment(kuorumUser:user, text:"3 -- Loren ipsum")

        when: "Adding comments a post"
        //"service" represents the grails service you are testing for
        postService.addComment(post, postComment1)
        postService.addComment(post, postComment2)
        postService.addComment(post, postComment3)

        then: "Correct comments in correct order"
        post.comments.size() == 3
        post.comments[0].kuorumUser == noe
        post.comments[0].text.startsWith("1")
        post.comments[1].kuorumUser == user
        post.comments[1].text.startsWith("2")
        post.comments[2].kuorumUser == user
        post.comments[2].text.startsWith("3")
        Post.withNewSession {
            //Check if is in DB
            Post recoveredPostNewSession = Post.get(post.id)
            recoveredPostNewSession.comments.size() == 3
            recoveredPostNewSession.comments[0].kuorumUser == noe
            recoveredPostNewSession.comments[0].text.startsWith("1")
            recoveredPostNewSession.comments[1].kuorumUser == user
            recoveredPostNewSession.comments[1].text.startsWith("2")
            recoveredPostNewSession.comments[2].kuorumUser == user
            recoveredPostNewSession.comments[2].text.startsWith("3")
        }
    }

    void "test adding debate"() {
        given: "A post"
        KuorumUser user = KuorumUser.findByEmail("peter@example.com")
        KuorumUser noe = KuorumUser.findByEmail("politician@example.com")
        Post post = Post.findByOwner(user)
        PostComment postComment1 = new PostComment(kuorumUser:noe, text:"1 -- Loren ipsum")
        PostComment postComment2 = new PostComment(kuorumUser:user, text:"2 -- Loren ipsum")
        PostComment postComment3 = new PostComment(kuorumUser:user, text:"3 -- Loren ipsum")

        when: "Adding debates a post"
        //"service" represents the grails service you are testing for
        postService.addDebate(post, postComment1)
        postService.addDebate(post, postComment2)
        postService.addDebate(post, postComment3)

        then: "Correct debates in correct order"
        post.debates.size() == 3
        post.debates[0].kuorumUser == noe
        post.debates[0].text.startsWith("1")
        post.debates[1].kuorumUser == user
        post.debates[1].text.startsWith("2")
        post.debates[2].kuorumUser == user
        post.debates[2].text.startsWith("3")
        Post.withNewSession {
            //Check if is in DB
            Post recoveredPostNewSession = Post.get(post.id)
            recoveredPostNewSession.debates.size() == 3
            recoveredPostNewSession.debates[0].kuorumUser == noe
            recoveredPostNewSession.debates[0].text.startsWith("1")
            recoveredPostNewSession.debates[1].kuorumUser == user
            recoveredPostNewSession.debates[1].text.startsWith("2")
            recoveredPostNewSession.debates[2].kuorumUser == user
            recoveredPostNewSession.debates[2].text.startsWith("3")
        }
        post.debates[0].dateCreated != null
        post.debates[0].dateCreated < new Date()
    }

    void "test adding debate by normal user"() {
        given: "A post"
        KuorumUser user = KuorumUser.findByEmail("peter@example.com")
        KuorumUser noe = KuorumUser.findByEmail("noe@example.com")
        Post post = Post.findByOwner(user)
        PostComment postComment1 = new PostComment(kuorumUser:noe, text:"1 -- Loren ipsum")

        when: "Adding debates a post"
        //"service" represents the grails service you are testing for
        postService.addDebate(post, postComment1)

        then: "Exception expected"
        final KuorumException exception = thrown()
        exception.errors[0].code == "error.security.post.notDebateAllowed"
    }
}
