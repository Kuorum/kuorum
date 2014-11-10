package kuorum.post

import grails.converters.JSON
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import kuorum.ShortUrlService
import kuorum.core.exception.KuorumException
import kuorum.core.model.search.Pagination
import kuorum.helper.Helper
import kuorum.law.Law
import kuorum.notifications.NotificationService
import kuorum.solr.IndexSolrService
import kuorum.users.KuorumUser
import kuorum.users.RoleUser
import spock.lang.Specification
import spock.lang.Unroll

@TestFor(PostService)
@Mock([KuorumUser, Post, Law, RoleUser])
class PostServiceSpec extends Specification{

    IndexSolrService indexSolrService = Mock(IndexSolrService)
    PostVoteService postVoteService = Mock(PostVoteService)
    NotificationService notificationService = Mock(NotificationService)
    CluckService cluckService = Mock(CluckService)
    ShortUrlService shortUrlService = Mock(ShortUrlService)

    def setup() {

        grailsApplication.config.kuorum.promotion.mailPrice = 0.15
        service.indexSolrService = indexSolrService
        service.postVoteService = postVoteService
        service.notificationService = notificationService
        service.cluckService = cluckService
        service.shortUrlService = shortUrlService

        Post.metaClass.static.getCollection = {->
            [
                    findOne: {
                        delegate.findWhere(it) as JSON
                    },
                    update:{filter, updateData ->
                        Post post = Post.get(filter._id)
                        post.victory = updateData.'$set'.'victory'
                        post.save(flush:true)
                        //post as JSON
                    }
            ]
        }
        Post.metaClass.refresh={->
            //REFRESH FAILS with null pointer
        }
    }


    void "test create post with wrong params"() {
        given: "A post"
            //fixtureLoader.load("testData")
            Post post = new Post()
            Law law = Helper.createDefaultLaw("#hashtag")
            KuorumUser user = Helper.createDefaultUser("otherUser@example.com")

            def cluckServiceMock = mockFor(CluckService)
            service.cluckService = cluckServiceMock

        when: "Saving a post"
            //"service" represents the grails service you are testing for
        service.savePost(post, law, user)

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
        Law law = post.law
        post.law = null
        KuorumUser user = Helper.createDefaultUser("otherUser@example.com")

        when: "Saving a post"
        //"service" represents the grails service you are testing for
        Post postSaved = service.savePost(post,law, user)

        then: "Expected an exception"
        0 * indexSolrService.index(_)
        0 * cluckService.createCluck(_,_)
        0 * postVoteService.votePost(_,_)
        postSaved.owner == user
        postSaved.law == law
    }

    void "test publishing a post"() {
        given: "A post"
        //fixtureLoader.load("testData")
        Post post = Helper.createDefaultPost()

        when: "Saving a post"
        //"service" represents the grails service you are testing for
        service.publishPost(post)

        then: "called services done"
//        post.owner.activity.numPurposes == 1
//        post.owner.activity.purposes.contains(post.id)
        1 * indexSolrService.index(post)
        1 * cluckService.createCluck(post,post.owner)
        1 * postVoteService.votePost(post,post.owner)
        1 * shortUrlService.shortUrl(post)
    }

    @Unroll
    void "test #numEmails sending emails when promotion amount is #amount €"() {
        given: "A post"
        when: "Calculating num emails"
        //"service" represents the grails service you are testing for
       Integer numEmailsCalculated= service.calculateNumEmails(amount)

        then: "Expected num emails"
        numEmailsCalculated == numEmails
        where:
        amount  | numEmails
        0       |  0
        1       |  5
        4.99    |  5
        5       |  30
        10      |  30
        14.99   |  30
        15      |  100
        15.10   | 100
        15.15   | 101
        15.90   | 106
        16.5    | 110
    }

    void "test recomended post"(){
        given:"Some posts"
        Law law = Helper.createDefaultLaw("#law").save()
        KuorumUser user = Helper.createDefaultUser("email@email.com").save()
        (1..10).each{
            Post post = Helper.createDefaultPost(user,law)
            post.numVotes = (Math.random() *100) as Integer //Truncate
            post.title ="Title$it"
            post.save()
        }
        Pagination pagination = new Pagination()
        when:"Recovering recommended posts"
        List<Post> recommendedPost = service.recommendedPosts(user, law, pagination)
        then:
        recommendedPost.first().numVotes>=recommendedPost.last().numVotes
        recommendedPost.size() <= pagination.max
    }

    @Unroll
    void "test if comment in pos #commentPos is deletable by user #email (admin: #isAdmin )"(){
        given:"A user and a post"
        Law law = Helper.createDefaultLaw("#law").save()
        KuorumUser owner = Helper.createDefaultUser("owner@email.com").save()
        KuorumUser user = Helper.createDefaultUser("user@email.com").save()
        Post post = Helper.createDefaultPost(owner,law).save()
        post.comments = [new PostComment([text:"0", kuorumUser:user]),new PostComment([text:"0", kuorumUser:owner])]
        post.save()
        KuorumUser deletedBy = email==owner.email?owner:user
        if (isAdmin){
            deletedBy.authorities =[new RoleUser(authority: "ROLE_ADMIN").save()]
            deletedBy.save()
        }
        when:
        boolean isDeletable = service.isCommentDeletableByUser(deletedBy,post, commentPos)
        then:
        isDeletable == deletable
        where:
        email               | deletable | commentPos | isAdmin
        "user@email.com"    | false     | 1          | false
        "user@email.com"    | true      | 0          | false
        "owner@email.com"   | true      | 1          | false
        "owner@email.com"   | true      | 0          | false
        "user@email.com"    | true      | 1          | true

    }

    @Unroll
    void "test corosScripting processor #raw => #expectedText"(){
        given:"A text written by user"

        when:
        String text  = service.removeCustomCrossScripting(raw)
        then:
        expectedText == text
        where:
        raw                             | expectedText
        "<i> hola </i>"                 | "<i> hola </i>"
        "<b> hola </b>"                 | "<b> hola </b>"
        "<u> hola </u>"                 | "<u> hola </u>"
        "<a href='dd'> hola </a>"                           | "<a href='dd' rel='nofollow' target='_blank'> hola </a>"
        "<a href='dd' style=''> hola </a>"                  | "<a href='dd' rel='nofollow' target='_blank'> hola </a>"
        "<a class='' href='dd' style=''> hola </a>"         | "<a href='dd' rel='nofollow' target='_blank'> hola </a>"
        "<script> hola </script>"                   | " hola "
        "<strong class='dd'> hola </strong>"        | " hola "
        "<script>alert(\"hola\")</script>"          | "alert(\"hola\")"

        "<br>hola<br>feo<br>"                                   | "<br>hola<br>feo<br>"
        "<br>hola<br>feo<br><br>"                               | "<br>hola<br>feo<br>"
        "<br>hola<br><br><br>feo<br>"                           | "<br>hola<br>feo<br>"
        "<br><br>hola<br><br><br>feo<br><br><br><br><br><br>"   | "<br>hola<br>feo<br>"
    }

    @Unroll
    void "test given victory to post(vicotry=#stausVictoryPost) by #emailUser"(){
        given:"A post"
            KuorumUser user = Helper.createDefaultUser("user@email.com").save()
            KuorumUser politician = Helper.createDefaultUser("politician@email.com").save()
            Law law = Helper.createDefaultLaw("#law").save()
            Post post = Helper.createDefaultPost(user, law).save()
            KuorumUser userGivenVictory = KuorumUser.findByEmail(emailUser)
            post.victory = stausVictoryPost
        when:
            service.victory(post, userGivenVictory, true)
        then:
//            final KuorumException exception = thrown()
//            if (exceptionCode){
//                thrown(KuorumException)
////                exception.errors[0].code == exceptionCode
//                0 * notificationService.sendVictoryNotification(_)
//            }else{
                1 * notificationService.sendVictoryNotification(post)
                post.victory
//            }
        where:
            stausVictoryPost  | emailUser               | exceptionCode
//            true              | 'user@email.com'        | 'error.security.post.victory.alreadyVictoryGiven'
//            false             | 'politician@email.com'  | 'error.security.post.victory.notOwnerGivenVictory'
            false             | 'user@email.com'        | ''
    }
}
