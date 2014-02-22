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
class CluckServiceIntegrationSpec extends Specification{

    def cluckService
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
        when: "creating a firstCluck"
            cluckService.createCluck(post,clucker)
            Cluck cluck = Cluck.findByLawAndOwner(post.law,clucker)
        then:
            cluck.owner == clucker
            cluck.postOwner == user
            cluck.post == post
            cluck.law == post.law

    }

    @Unroll
    void "test dashboard clucks for user #email founding #numClucks"() {
        given: "A user"
        KuorumUser kuorumUser = KuorumUser.findByEmail(email)

        when: "Saving a post"
        List<Cluck> clucks = cluckService.dashboardClucks(kuorumUser)

        then: "Check the results"
            clucks.size() == numClucks
        where:
            email                           | numClucks
            "juanjoalvite@example.com"      | 3
            "peter@example.com"             | 1
            "equo@example.com"              | 1
            "politician@example.com"        | 0
    }

}
