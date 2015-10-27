package kuorum.post

import kuorum.project.Project
import kuorum.notifications.CluckNotification
import kuorum.users.KuorumUser
import spock.lang.Ignore
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

    @Ignore
    void "test create clucks"(){
        given: "A user and a Post"
            KuorumUser user = KuorumUser.findByEmail("patxi@example.com")
            KuorumUser clucker = KuorumUser.findByEmail("equo@example.com")
            Post post = Post.findByOwner(user)
            long numClucks = post.numClucks
        when: "creating a firstCluck and check if notification is created"
            cluckService.createCluck(post,clucker)
            Cluck cluck = Cluck.findByProjectAndOwner(post.project,clucker)
            CluckNotification cluckNotification = CluckNotification.findByKuorumUserAndClucker(user,clucker)
        then:
            post.numClucks == numClucks
            cluckNotification
            cluckNotification.clucker == clucker
            cluckNotification.kuorumUser== user
            cluck.owner == clucker
            cluck.postOwner == user
            cluck.post == post
            cluck.project == post.project

    }

    @Unroll
    @Ignore
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
            "juanjoalvite@example.com"      | 8
            "patxi@example.com"             | 6
            "equo@example.com"              | 2
            "politician@example.com"        | 2
            "admin@example.com"             | 0
    }

    @Unroll
    @Ignore
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
            "juanjoalvite@example.com"      | 3
            "patxi@example.com"             | 1
            "equo@example.com"              | 2
            "politician@example.com"        | 2
            "admin@example.com"             | 0
    }

    @Unroll
    @Ignore
    void "test clucks for project #hashtag founding #numClucks clucks"() {
        given: "A user"
        Project project = Project.findByHashtag(hashtag)

        when: "Saving a post"
        List<Cluck> clucks = cluckService.projectClucks(project)

        then: "Check the results"
        clucks.size() == numClucks
        if (numClucks>0){
            clucks.sort { a, b -> a.lastUpdated <=> b.lastUpdated }.last() == clucks.last()
            clucks.sort { a, b -> a.lastUpdated <=> b.lastUpdated }.first() == clucks.first()
        }
        where:
        hashtag                 | numClucks
        "#leyAborto"            | 2
        "#codigoPenal"          | 1
        "#parquesNacionales"    | 2
        "#prohibicionFraking"   | 0
    }

}
