package kuorum.users

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import kuorum.core.model.gamification.GamificationElement
import kuorum.helper.Helper
import kuorum.post.Post
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(GamificationService)
@Mock([Post, KuorumUser])
class GamificationServiceSpec extends Specification {

    def setup() {
        grailsApplication.config.kuorum.gamification.voteLaw=[(GamificationElement.EGG):1]
        grailsApplication.config.kuorum.gamification.newPost=[(GamificationElement.PLUME):1]
        grailsApplication.config.kuorum.gamification.votePost=[(GamificationElement.CORN):1]
    }

    def cleanup() {
    }

    @Unroll
    void "test gamification of sponsoring a post with #numMails mails and init gamifications [E:#initEggs, P:#initPlumes, C:#initCorns ]"() {
        given:"A user"
        KuorumUser user = Helper.createDefaultUser("email@email.com")
        user.gamification.numEggs = initEggs
        user.gamification.numPlumes = initPlumes
        user.gamification.numCorns = initCorns
        user.save()
        when:
        service.sponsorAPostAward(user, numMails)
        then:
        user.gamification.numEggs == initEggs + 0
        user.gamification.numPlumes == initPlumes + 0
        user.gamification.numCorns == initCorns + numMails
        where:
        numMails | initEggs | initPlumes | initCorns
        1        |  1       |   1        | 1
        2        |  2       |   1        | 0
        5        |  3       |   0        | 1
    }
}
