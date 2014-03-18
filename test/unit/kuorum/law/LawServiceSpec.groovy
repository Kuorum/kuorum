package kuorum.law

import grails.converters.JSON
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import kuorum.core.model.VoteType
import kuorum.helper.Helper
import kuorum.law.LawService
import kuorum.notifications.NotificationService
import kuorum.users.GamificationService
import kuorum.users.KuorumUser
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(LawService)
@Mock([Law, LawVote,KuorumUser])
class LawServiceSpec extends Specification {


    GamificationService gamificationServiceMock= Mock(GamificationService)

    def setup() {
        service.gamificationService = gamificationServiceMock

        Law.metaClass.static.getCollection = {->
            [findOne: {
                delegate.findWhere(it) as JSON

            },
                    update:{filter, updateData ->
                        Law law = Law.get(filter._id)
                        def yesInc = updateData.'$inc'.'peopleVotes.yes'
                        def noInc = updateData.'$inc'.'peopleVotes.no'
                        def absInc = updateData.'$inc'.'peopleVotes.abs'
                        if (yesInc)law.peopleVotes.yes ++
                        if (noInc)law.peopleVotes.no ++
                        if (absInc)law.peopleVotes.abs ++
                        law.save()
                        //post as JSON
                    }
            ]
        }
        Law.metaClass.refresh={->
            //REFRESH FAILS with null pointer
        }
    }


    @Unroll
    void "test voteLaw voting #votes"() {
        given: "A law and a user voting #votes"
        Law law = Helper.createDefaultLaw("#law").save()
        KuorumUser user = Helper.createDefaultUser("email@email.com").save()
        when: "User votes the law"
        LawVote lawVote
        (0..numVotes-1).each {
            lawVote = service.voteLaw(law, user, votes[it])
        }
        then: "All ok"
        lawVote.voteType == votes.last()
        LawVote.count() == 1
        1 * gamificationServiceMock.lawVotedAward(user, law)
        switch (votes.last()){
            case VoteType.ABSTENTION:   law.peopleVotes.abs = 1; break;
            case VoteType.POSITIVE:     law.peopleVotes.abs = 1; break;
            case VoteType.NEGATIVE:     law.peopleVotes.abs = 1; break;
        }
        where:
        numVotes | votes
        1        | [VoteType.POSITIVE]
        2        | [VoteType.POSITIVE, VoteType.POSITIVE]
        2        | [VoteType.POSITIVE, VoteType.NEGATIVE]


    }
}
