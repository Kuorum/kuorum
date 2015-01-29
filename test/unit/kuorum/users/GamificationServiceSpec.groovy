package kuorum.users

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import kuorum.core.exception.KuorumException
import kuorum.core.model.gamification.GamificationAward
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
        grailsApplication.config.kuorum.gamification.voteProject=[(GamificationElement.EGG):1]
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

    @Unroll
    void "test buying award #award = [E:#initEggs, P:#initPlumes, C:#initCorns ]"() {
        given:"A user"
        KuorumUser user = Helper.createDefaultUser("email@email.com")
        user.gamification.numEggs   = award.numEggs + initEggs
        user.gamification.numPlumes = award.numPlumes + initPlumes
        user.gamification.numCorns  = award.numCorns + initCorns
        user.save()
        when:
        String errorCode = ""
        try{
            service.buyAward(user,award )
        }catch(KuorumException e){
            errorCode = e.errors[0].code
        }
        then:
        if (errorCode){
            errorCode == 'error.gamification.notAllowBuyAward'
            error
        }else{
            user.gamification.numEggs == initEggs
            user.gamification.numPlumes == initPlumes
            user.gamification.numCorns == initCorns
            user.gamification.activeRole == award
        }
        where:
        award                                   |error  | initEggs | initPlumes | initCorns
        GamificationAward.ROLE_MILITANTE        | false | 1        |   1        | 1
        GamificationAward.ROLE_MILITANTE        | false | 2        |   1        | 0
        GamificationAward.ROLE_MILITANTE        | false | 3        |   0        | 1
        GamificationAward.ROLE_MILITANTE        | true | -1        |   0        | 1
        GamificationAward.ROLE_MILITANTE        | true | 3         |   -1       | 1
        GamificationAward.ROLE_MILITANTE        | true | 3         |   0        | -1
    }
}
