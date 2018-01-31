package payment.campaign

import com.fasterxml.jackson.core.type.TypeReference
import grails.transaction.Transactional
import kuorum.core.exception.KuorumException
import kuorum.solr.IndexSolrService
import kuorum.users.KuorumUser
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.communication.CampaignRSDTO
import org.kuorum.rest.model.communication.debate.DebateRDTO
import org.kuorum.rest.model.communication.debate.DebateRSDTO
import org.kuorum.rest.model.communication.debate.PageDebateRSDTO
import org.kuorum.rest.model.communication.event.EventRDTO

@Transactional
class CampaignService {

    RestKuorumApiService restKuorumApiService
    IndexSolrService indexSolrService

    List<CampaignRSDTO> findAllCampaigns(KuorumUser user,String viewerUid = null) {
        Map<String, String> params = [userAlias: user.id.toString()]
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

//    @Cacheable(value="debate", key='#debateId')
    CampaignRSDTO find(KuorumUser user, Long debateId, String viewerUid = null) {
        find(user.getId().toString(), debateId, viewerUid);
    }

//    @Cacheable(value="debate", key='#debateId')
    CampaignRSDTO find(String userId, Long debateId, String viewerUid = null) {
        if (!debateId){
            return null;
        }
        Map<String, String> params = [userAlias: userId, campaignId: debateId.toString()]
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
            log.info("Error recovering debate $debateId : ${e.message}")
            return null;
        }
    }
}
