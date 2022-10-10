package kuorum.core.navigation.cache

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class PathKeyExtractorServiceSpec extends Specification {

    private static String OWNER = "testOwner"

    private static String CAMPAIGN = "testCampaignWithALotOfNumbers7777andThinks"

    @Subject
    PathKeyExtractorService pathKeyExtractorService = new PathKeyExtractorService()

    @Unroll
    void 'Should extract owner an campaign from patter #path'(){
        when:
        String key = pathKeyExtractorService.extractKey(path)
        then:
            key == OWNER + CAMPAIGN
        where:
        path << [
                '/'+OWNER+'/'+CAMPAIGN,
                '/account/'+OWNER+'/cta/'+CAMPAIGN+'/proyecto10101731-3539/edit-content',
                '/account/'+OWNER+'/pb/'+CAMPAIGN+'/new-proposal'
        ]
    }
}
