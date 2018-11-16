package payment.campaign

import com.fasterxml.jackson.core.type.TypeReference
import grails.transaction.Transactional
import kuorum.core.exception.KuorumException
import kuorum.register.KuorumUserSession
import kuorum.solr.IndexSolrService
import kuorum.users.KuorumUser
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.communication.CampaignRDTO
import org.kuorum.rest.model.communication.CampaignRSDTO
import org.kuorum.rest.model.communication.event.EventRDTO

@Transactional
class CampaignService {

    RestKuorumApiService restKuorumApiService
    IndexSolrService indexSolrService

    List<CampaignRSDTO> findRelevantDomainCampaigns(String viewerUid = null) {
        Map<String, String> params = [:]
        Map<String, String> query = [:]
        if (viewerUid){
            query.put("viewerUid",viewerUid)
        }
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.CAMPAIGNS_DOMAIN,
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

    void updateRelevantDomainCampaigns(List<Long> campaignIds) {
        Map<String, String> params = [:]
        Map<String, String> query = [:]
        def response = restKuorumApiService.put(
                RestKuorumApiService.ApiMethod.CAMPAIGNS_DOMAIN,
                params,
                query,
                campaignIds,
                null
        )

        List<CampaignRSDTO> debatesFound = null
        if (response.data) {
            debatesFound = (List<CampaignRSDTO>) response.data
        }

        debatesFound
    }


    List<CampaignRSDTO> findAllCampaigns(KuorumUserSession user, String viewerUid = null) {
        findAllCampaigns(user.id.toString(), viewerUid)
    }
    List<CampaignRSDTO> findAllCampaigns(String userId, String viewerUid = null) {
        Map<String, String> params = [userId: userId]
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
    CampaignRSDTO find(KuorumUserSession user, Long campaignId, String viewerUid = null) {
        find(user.getId().toString(), campaignId, viewerUid)
    }

    @Deprecated
    CampaignRSDTO find(KuorumUser user, Long campaignId, String viewerUid = null) {
        find(user.getId().toString(), campaignId, viewerUid)
    }

//    @Cacheable(value="debate", key='#campaignId')
    CampaignRSDTO find(String userId, Long campaignId, String viewerUid = null) {
        if (!campaignId){
            return null
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
            return campaignRSDTO
        }catch (KuorumException e){
            log.info("Error recovering debate $campaignId : ${e.message}")
            return null
        }
    }

    CampaignRDTO basicMapping(CampaignRSDTO campaignRSDTO, CampaignRDTO rdto){
        if(campaignRSDTO) {
            rdto.name = campaignRSDTO.name
            rdto.checkValidation = campaignRSDTO.checkValidation
            rdto.triggeredTags = campaignRSDTO.triggeredTags
            rdto.anonymousFilter = campaignRSDTO.anonymousFilter
            rdto.filterId = campaignRSDTO.newsletter?.filter?.id
            rdto.photoUrl = campaignRSDTO.photoUrl
            rdto.videoUrl = campaignRSDTO.videoUrl
            rdto.title = campaignRSDTO.title
            rdto.body = campaignRSDTO.body
            rdto.publishOn = campaignRSDTO.datePublished
            rdto.endDate = campaignRSDTO.endDate
            rdto.causes = campaignRSDTO.causes
            if (campaignRSDTO.event){
                rdto.event = new EventRDTO()
                rdto.event.eventDate = campaignRSDTO.event.eventDate
                rdto.event.latitude = campaignRSDTO.event.latitude
                rdto.event.longitude = campaignRSDTO.event.longitude
                rdto.event.zoom = campaignRSDTO.event.zoom
                rdto.event.localName = campaignRSDTO.event.localName
                rdto.event.address = campaignRSDTO.event.address
                rdto.event.capacity = campaignRSDTO.event.capacity
            }
            
        }
        return rdto
    }
}
