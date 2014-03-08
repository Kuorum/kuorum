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
    def postVoteService
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
        Cluck cluck = Cluck.findByPostAndOwnerAndPostOwner(savedPost, user,user)
        then: "Post created but not published"
        cluck == null
        savedPost.id != null
        savedPost.published == Boolean.FALSE

    }

    void "test updating post"() {
        given: "A post"
        KuorumUser user = KuorumUser.findByEmail("peter@example.com")
        Law law = Law.findByHashtag("#leyAborto")
        Post  post = Post.findByOwnerAndLaw(user,law)
        def expectedData = [
                published:post.published,
                debates:post.debates,
                firstCluck:post.firstCluck,
                title: "TITULO",
                text:"TEXT",
                postType: PostType.QUESTION,
                comments: post.comments,
                numClucks: post.numClucks,
                numVotes: post.numVotes

        ]
        post.firstCluck = null
        post.title = expectedData.title
        post.text = expectedData.text
        post.postType = expectedData.postType
        post.numVotes = 1111
        post.numClucks = 1111
        post.published = !expectedData.published
        when: "Updating a post"
        //"service" represents the grails service you are testing for
        Post savedPost = postService.updatePost(post)
        then: "Post created but not published"
        savedPost.id != null
        savedPost.published == expectedData.published
        savedPost.firstCluck == expectedData.firstCluck
        savedPost.title == expectedData.title
        savedPost.text == expectedData.text
        savedPost.postType == expectedData.postType
        savedPost.numVotes == expectedData.numVotes
        savedPost.numClucks == expectedData.numClucks
        Post.withNewSession {
            Post  recoveredPost = Post.findByOwnerAndLaw(user,law)
            recoveredPost.published == expectedData.published
            recoveredPost.firstCluck == expectedData.firstCluck
            recoveredPost.title == expectedData.title
            recoveredPost.text == expectedData.text
            recoveredPost.postType == expectedData.postType
            recoveredPost.numVotes == expectedData.numVotes
            recoveredPost.numClucks == expectedData.numClucks
        }

    }

    void "test publish a post with correct params"() {
        given: "A post"
        KuorumUser user = KuorumUser.findByEmail("peter@example.com")
        Law law = Law.findByHashtag("#leyAborto")

        Post post = new Post(
                owner : user,
                law:law,
                numClucks:0,
                numVotes:0,
                postType:PostType.PURPOSE,
                title:"Enmienda a la totalidad",
                text:"Lorem ipsum"
        ).save()

        when: "Saving a post"
        Post savedPost = postService.publishPost(post)

        then: "Post created"
        savedPost.id != null
        savedPost.published == Boolean.TRUE
        Cluck cluck = Cluck.findByPostAndOwnerAndPostOwner(savedPost, user,user)
        cluck != null
        cluck.post == savedPost
        cluck.postOwner == savedPost.owner
        cluck.isFirstCluck == Boolean.TRUE

        PostVote.findAllByPostAndUser(post, post.owner).size() == 1
        PostVote postVote = PostVote.findByPostAndUser(post, post.owner)
        postVote != null
        postVote.user == post.owner
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

    void "test deleting comments"() {
        given: "A post"
        KuorumUser user = KuorumUser.findByEmail("peter@example.com")
        KuorumUser noe = KuorumUser.findByEmail("noe@example.com")
        KuorumUser carmen = KuorumUser.findByEmail("carmen@example.com")
        Post post = Post.findByOwner(user)
        PostComment postComment1 = new PostComment(kuorumUser:noe, text:"1 -- Loren ipsum")
        PostComment postComment2 = new PostComment(kuorumUser:user, text:"2 -- Loren ipsum")
        PostComment postComment3 = new PostComment(kuorumUser:user, text:"3 -- Loren ipsum")

        postService.addComment(post, postComment1)
        postService.addComment(post, postComment2)
        postService.addComment(post, postComment3)

        when: "deleting comments a post"
        postService.deleteComment(user,post,0)
        then: "Correct comments in correct order"
        post.comments.size() == 3
        post.comments[0].kuorumUser == noe
        post.comments[0].text.startsWith("1")
        post.comments[0].deleted == Boolean.TRUE
        post.comments[1].kuorumUser == user
        post.comments[1].text.startsWith("2")
        post.comments[1].deleted == Boolean.FALSE
        post.comments[2].kuorumUser == user
        post.comments[2].text.startsWith("3")
        post.comments[2].deleted == Boolean.FALSE
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
            post.comments[2].deleted == Boolean.FALSE
        }
    }

    void "test adding debate"() {
        given: "A post"
        KuorumUser user = KuorumUser.findByEmail("peter@example.com")
        KuorumUser noe = KuorumUser.findByEmail("politician@example.com")
        KuorumUser voter1 = KuorumUser.findByEmail("carmen@example.com")
        KuorumUser voter2 = KuorumUser.findByEmail("juanjoalvite@example.com")
        Post post = Post.findByOwner(user)
        postVoteService.votePost(post,voter1)
        postVoteService.votePost(post,voter2)

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

    @Unroll
    void "test user #user is allowed to write debates = #isAllowedToWriteDebate"(){
        given: "A post"
        KuorumUser owner = KuorumUser.findByEmail("peter@example.com")
        KuorumUser userDebate = KuorumUser.findByEmail(user)
        Post post = Post.findByOwner(owner)

        expect: "Adding debates a post"
        //"service" represents the grails service you are testing for
        isAllowedToWriteDebate == postService.isAllowedToAddDebate(post, userDebate)


        where:
        user                    || isAllowedToWriteDebate
        "peter@example.com"     || true
        "politician@example.com"|| true
        "NONE"                  || false
        "noe@example.com"       || false

    }
}
