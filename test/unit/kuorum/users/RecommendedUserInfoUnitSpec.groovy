package kuorum.users

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import kuorum.helper.Helper
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@TestFor(RecommendedUserInfo)
@Mock([RecommendedUserInfo, KuorumUser])
class RecommendedUserInfoUnitSpec extends  Specification{

    @Shared
    KuorumUser user

    def setupSpec() {
        user = Helper.createDefaultUser("userEmail@example.es")
    }

    @Unroll
    void "unit test to check the recommendedUserInfo object"(){
        given:
        RecommendedUserInfo recommendedUserInfo = new RecommendedUserInfo(inputData)

        expect:"create a class and check"
        resultCreate == recommendedUserInfo.validate()

        where: "input for check"
        resultCreate    | inputData
        false			| 		[:]
        false			| 		[recommendedUsers:[]]
        false			| 		[deletedRecommendedUsers:[]]
        true			|		[user: user]
        true            |       [user: user, recommendedUsers:[]]
        true            |       [user: user, deletedRecommendedUsers:[]]
        true            |       [user: user,  recommendedUsers:[], deletedRecommendedUsers:[]]
    }
}
