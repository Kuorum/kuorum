package payment.campaign

import grails.transaction.Transactional
import kuorum.users.KuorumUser
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.contact.ContactRSDTO
import org.kuorum.rest.model.notification.campaign.CampaignRQDTO
import org.kuorum.rest.model.notification.campaign.CampaignRSDTO

@Transactional
class MassMailingService {

    RestKuorumApiService restKuorumApiService;


    CampaignRSDTO campaignSend(KuorumUser user, CampaignRQDTO campaignRQDTO){
        campaignSchedule(user, campaignRQDTO, new Date()-10)
    }
    CampaignRSDTO campaignSchedule(KuorumUser user, CampaignRQDTO campaignRQDTO, Date date){
        campaignRQDTO.sentOn = date
        Map<String, String> params = [userAlias:user.id.toString()]
        Map<String, String> query = [:]
        def response= restKuorumApiService.post(
                RestKuorumApiService.ApiMethod.ACCOUNT_CAMPAIGNS,
                params,
                query,
                campaignRQDTO
        )
        CampaignRSDTO campaignSaved=null;
        if (response.data){
            campaignSaved = (CampaignRSDTO)response.data
        }
        campaignSaved
    }

    CampaignRSDTO campaignDraft(KuorumUser user, CampaignRQDTO campaignRQDTO){
        return campaignSchedule(user, campaignRQDTO, null)
    }

    CampaignRSDTO campaignTest(KuorumUser user, Long campaignId){
        Map<String, String> params = [userAlias:user.id.toString(), campaignId:campaignId.toString()]
        Map<String, String> query = [test:true]
        def response= restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.ACCOUNT_CAMPAIGN_SEND,
                params,
                query
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
                query
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
                query
        )
        CampaignRSDTO campaignSaved=null;
        if (response.data){
            campaignSaved = (CampaignRSDTO)response.data
        }
        campaignSaved
    }
}
