package payment.campaign

import grails.transaction.Transactional
import kuorum.users.KuorumUser
import kuorum.util.rest.RestKuorumApiService
import org.codehaus.jackson.type.TypeReference
import org.kuorum.rest.model.contact.ContactRSDTO
import org.kuorum.rest.model.notification.campaign.CampaignRQDTO
import org.kuorum.rest.model.notification.campaign.CampaignRSDTO

@Transactional
class MassMailingService {

    RestKuorumApiService restKuorumApiService;


    CampaignRSDTO campaignSend(KuorumUser user, CampaignRQDTO campaignRQDTO, Long campaignId = null){
        campaignSchedule(user, campaignRQDTO, new Date(), campaignId)
    }
    CampaignRSDTO campaignSchedule(KuorumUser user, CampaignRQDTO campaignRQDTO, Date date, Long campaignId = null){
        campaignRQDTO.sentOn = date
        Map<String, String> params = [userAlias:user.id.toString()]
        Map<String, String> query = [:]
        RestKuorumApiService.ApiMethod apiMethod = RestKuorumApiService.ApiMethod.ACCOUNT_CAMPAIGNS
        if (campaignId){
            apiMethod = RestKuorumApiService.ApiMethod.ACCOUNT_CAMPAIGN
            params.put("campaignId",campaignId.toString())
        }
        def response= restKuorumApiService.post(
                apiMethod,
                params,
                query,
                campaignRQDTO,
                new TypeReference<CampaignRSDTO>(){}
        )
        CampaignRSDTO campaignSaved=null;
        if (response.data){
            campaignSaved = response.data
        }
        campaignSaved
    }

    CampaignRSDTO campaignDraft(KuorumUser user, CampaignRQDTO campaignRQDTO, Long campaignId = null){
        return campaignSchedule(user, campaignRQDTO, null, campaignId)
    }

    CampaignRSDTO campaignTest(KuorumUser user, Long campaignId){
        Map<String, String> params = [userAlias:user.id.toString(), campaignId:campaignId.toString()]
        Map<String, String> query = [test:true]
        def response= restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.ACCOUNT_CAMPAIGN_SEND,
                params,
                query,
                new TypeReference<CampaignRSDTO>(){}
        )
        CampaignRSDTO campaignSaved=null;
        if (response.data){
            campaignSaved = (CampaignRSDTO)response.data
        }
        campaignSaved
    }

    List<CampaignRSDTO> findCampaigns(KuorumUser user){
        Map<String, String> params = [userAlias:user.id.toString()]
        Map<String, String> query = [:]
        def response= restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.ACCOUNT_CAMPAIGNS,
                params,
                query,
                new TypeReference<List<CampaignRSDTO>>(){}
        )
        List<CampaignRSDTO> campaigns=null;
        if (response.data){
            campaigns = response.data
        }
        campaigns
    }

    CampaignRSDTO findCampaign(KuorumUser user, Long campaignId){
        Map<String, String> params = [userAlias:user.id.toString(), campaignId:campaignId.toString()]
        Map<String, String> query = [test:true]
        def response= restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.ACCOUNT_CAMPAIGN,
                params,
                query,
                new TypeReference<CampaignRSDTO>(){}
        )
        CampaignRSDTO campaignSaved=null;
        if (response.data){
            campaignSaved = (CampaignRSDTO)response.data
        }
        campaignSaved
    }
}
