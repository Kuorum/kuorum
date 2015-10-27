package kuorum.users
import kuorum.helper.IntegrationHelper
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class RecommendedUserInfoIntegrationSpec extends Specification{

    @Shared
    KuorumUser user

    def setupSpec() {
        user = (IntegrationHelper.createDefaultUser("userEmail@example.es")).save()
    }

    @Unroll
    void "Integration test to check the recommendedUserInfo object"(){
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

    def cleanupSpec(){
        user?.delete()
    }
}
