package payment.campaign

import com.fasterxml.jackson.core.type.TypeReference
import grails.transaction.Transactional
import kuorum.core.exception.KuorumException
import kuorum.solr.IndexSolrService
import kuorum.users.KuorumUser
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.communication.CampaignRSDTO

@Transactional
class CampaignService {

    RestKuorumApiService restKuorumApiService
    IndexSolrService indexSolrService

    List<CampaignRSDTO> findAllCampaigns(KuorumUser user,String viewerUid = null) {
        Map<String, String> params = [userId: user.id.toString()]
        Map<String, String> query = [:]
        if (viewerUid){
            query.put("viewerUid",viewerUid)
        }
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.ACCOUNT_CAMPAIGNS,
                params,
                query,
                new TypeReference<List<CampaignRSDTO>>(){}
        )

        List<CampaignRSDTO> debatesFound = null
        if (response.data) {
            debatesFound = (List<CampaignRSDTO>) response.data
        }

        debatesFound
    }

//    @Cacheable(value="debate", key='#campaignId')
    CampaignRSDTO find(KuorumUser user, Long campaignId, String viewerUid = null) {
        find(user.getId().toString(), campaignId, viewerUid);
    }

//    @Cacheable(value="debate", key='#campaignId')
    CampaignRSDTO find(String userId, Long campaignId, String viewerUid = null) {
        if (!campaignId){
            return null;
        }
        Map<String, String> params = [userId: userId, campaignId: campaignId.toString()]
        Map<String, String> query = [:]
        if (viewerUid){
            query.put("viewerUid",viewerUid)
        }
        try {
            def response = restKuorumApiService.get(
                    RestKuorumApiService.ApiMethod.ACCOUNT_CAMPAIGN,
                    params,
                    query,
                    new TypeReference<CampaignRSDTO>(){}
            )

            CampaignRSDTO campaignRSDTO = null
            if (response.data) {
                campaignRSDTO = (CampaignRSDTO) response.data
            }
            return campaignRSDTO;
        }catch (KuorumException e){
            log.info("Error recovering debate $campaignId : ${e.message}")
            return null;
        }
    }
}
