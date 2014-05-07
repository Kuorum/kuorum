package kuorum.post

import kuorum.law.Law
import kuorum.notifications.CluckNotification
import kuorum.users.KuorumUser
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by iduetxe on 2/02/14.
 */
class CluckServiceIntegrationSpec extends Specification{

    def cluckService
    def notificationService
    def fixtureLoader

    def setup(){
        KuorumUser.collection.getDB().dropDatabase()
        fixtureLoader.load("testData")
    }

    void "test create clucks"(){
        given: "A user and a Post"
            KuorumUser user = KuorumUser.findByEmail("peter@example.com")
            KuorumUser clucker = KuorumUser.findByEmail("equo@example.com")
            Post post = Post.findByOwner(user)
            long numClucks = post.numClucks
        when: "creating a firstCluck and check if notification is created"
            cluckService.createCluck(post,clucker)
            Cluck cluck = Cluck.findByLawAndOwner(post.law,clucker)
            CluckNotification cluckNotification = CluckNotification.findByKuorumUserAndClucker(user,clucker)
        then:
            post.numClucks == numClucks +1
            cluckNotification
            cluckNotification.clucker == clucker
            cluckNotification.kuorumUser== user
            cluck.owner == clucker
            cluck.postOwner == user
            cluck.post == post
            cluck.law == post.law
            cluck.isFirstCluck == Boolean.FALSE

    }

    @Unroll
    void "test dashboard clucks for user #email founding #numClucks clucks"() {
        given: "A user"
        KuorumUser kuorumUser = KuorumUser.findByEmail(email)

        when: "Saving a post"
        List<Cluck> clucks = cluckService.dashboardClucks(kuorumUser)

        then: "Check the results"
            clucks.size() == numClucks
            if (numClucks>0){
                clucks.sort { a, b -> a.lastUpdated <=> b.lastUpdated }.last() == clucks.last()
                clucks.sort { a, b -> a.lastUpdated <=> b.lastUpdated }.first() == clucks.first()
            }
        where:
            email                           | numClucks
            "juanjoalvite@example.com"      | 5
            "peter@example.com"             | 5
            "equo@example.com"              | 2
            "politician@example.com"        | 1
            "admin@example.com"             | 0
    }

    @Unroll
    void "test user clucks for user #email founding #numClucks clucks"() {
        given: "A user"
        KuorumUser kuorumUser = KuorumUser.findByEmail(email)

        when: "Saving a post"
        List<Cluck> clucks = cluckService.userClucks(kuorumUser)

        then: "Check the results are in order"
            clucks.size() == numClucks
            if (numClucks>0){
                clucks.sort { a, b -> a.lastUpdated <=> b.lastUpdated }.last() == clucks.last()
                clucks.sort { a, b -> a.lastUpdated <=> b.lastUpdated }.first() == clucks.first()
            }
        where:
            email                           | numClucks
            "juanjoalvite@example.com"      | 2
            "peter@example.com"             | 1
            "equo@example.com"              | 2
            "politician@example.com"        | 1
            "admin@example.com"             | 0
    }

    @Unroll
    void "test clucks for law #hashtag founding #numClucks clucks"() {
        given: "A user"
        Law law = Law.findByHashtag(hashtag)

        when: "Saving a post"
        List<Cluck> clucks = cluckService.lawClucks(law)

        then: "Check the results"
        clucks.size() == numClucks
        if (numClucks>0){
            clucks.sort { a, b -> a.lastUpdated <=> b.lastUpdated }.last() == clucks.last()
            clucks.sort { a, b -> a.lastUpdated <=> b.lastUpdated }.first() == clucks.first()
        }
        where:
        hashtag                 | numClucks
        "#leyAborto"            | 1
        "#codigoPenal"          | 0
        "#parquesNacionales"    | 1
        "#prohibicionFraking"   | 0
    }

}
