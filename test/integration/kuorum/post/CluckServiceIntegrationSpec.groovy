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

    def setup(){

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
            "juanjoalvite@example.com"      | 1
            "peter@example.com"             | 1
    }

}
