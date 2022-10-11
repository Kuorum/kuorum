package kuorum.core.navigation.cache

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class PathKeyExtractorSpec extends Specification {

    private static String OWNER = "testOwner"

    private static String CAMPAIGN = "testCampaignWithNumbersAndThinks-7777"

    private static String SUBCAMPAIGN = "testSubCampaign-888"

    @Subject
    PathKeyExtractor pathKeyExtractorService = new PathKeyExtractor()

    @Unroll
    void 'Should extract owner an campaign from patter #path'() {
        when:
        String key = pathKeyExtractorService.extractKey(path)
        then:
        key == OWNER + CAMPAIGN
        where:
        path << [
                '/' + OWNER + '/' + CAMPAIGN,
                '/account/' + OWNER + '/cta/' + CAMPAIGN + '/proyecto10101731-3539/edit-content',
                '/account/' + OWNER + '/pb/' + CAMPAIGN + '/new-proposal',
                '/ajax/' + OWNER + '/ct/' + CAMPAIGN + '/cta/' + SUBCAMPAIGN + 'campaignId/vote',
                '/ajax/account/' + OWNER + '/pb/' + CAMPAIGN + '/remove'
        ]
    }

    void 'Should retrun empty on default path'() {
        given:
        String defaultPath = "/index.html"
        when:
        String key = pathKeyExtractorService.extractKey(defaultPath)
        then:
        key == ""
    }
}
