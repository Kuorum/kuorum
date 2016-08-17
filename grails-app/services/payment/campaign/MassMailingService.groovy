package payment.campaign

import grails.transaction.Transactional
import kuorum.users.KuorumUser
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.contact.ContactRSDTO
import org.kuorum.rest.model.notification.campaign.CampaignRSDTO

@Transactional
class MassMailingService {

    RestKuorumApiService restKuorumApiService;


    CampaignRSDTO campaignSend(KuorumUser user, CampaignRSDTO campaignRSDTO){
        campaignSchedule(user, campaignRSDTO, new Date()-10)
    }
    CampaignRSDTO campaignSchedule(KuorumUser user, CampaignRSDTO campaignRSDTO, Date date){
        campaignRSDTO.sentOn = date
        Map<String, String> params = [userAlias:user.id.toString()]
        Map<String, String> query = [:]
        def response= restKuorumApiService.post(
                RestKuorumApiService.ApiMethod.ACCOUNT_CAMPAIGNS,
                params,
                query,
                campaignRSDTO
        )
        CampaignRSDTO campaignSaved=null;
        if (response.data){
            campaignSaved = (CampaignRSDTO)response.data
        }
        campaignSaved
    }

    CampaignRSDTO campaignDraft(KuorumUser user, CampaignRSDTO campaignRSDTO){
        return campaignSchedule(user, campaignRSDTO, null)
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
}
