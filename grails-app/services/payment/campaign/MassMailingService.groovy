package payment.campaign

import com.fasterxml.jackson.core.type.TypeReference
import grails.transaction.Transactional
import kuorum.users.KuorumUser
import kuorum.util.TimeZoneUtil
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.notification.campaign.CampaignRQDTO
import org.kuorum.rest.model.notification.campaign.CampaignRSDTO
import org.kuorum.rest.model.notification.campaign.config.NewsletterConfigRQDTO
import org.kuorum.rest.model.notification.campaign.config.NewsletterConfigRSDTO
import org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatsByCampaignPageRSDTO

@Transactional
class MassMailingService {

    RestKuorumApiService restKuorumApiService;


    CampaignRSDTO campaignSend(KuorumUser user, CampaignRQDTO campaignRQDTO, Long campaignId = null){
        campaignRQDTO.sentOn = new Date();
        sendScheduledCampaignWithoutDateModifications(user, campaignRQDTO, campaignId)
    }

    CampaignRSDTO campaignSchedule(KuorumUser user, CampaignRQDTO campaignRQDTO, Date date, Long campaignId = null){
        campaignRQDTO.sentOn = TimeZoneUtil.convertToUserTimeZone(date, user.timeZone)
        sendScheduledCampaignWithoutDateModifications(user, campaignRQDTO, campaignId)
    }

    private CampaignRSDTO sendScheduledCampaignWithoutDateModifications(KuorumUser user, CampaignRQDTO campaignRQDTO, Long campaignId = null){
        Map<String, String> params = [userAlias:user.id.toString()]
        Map<String, String> query = [:]
        RestKuorumApiService.ApiMethod apiMethod = RestKuorumApiService.ApiMethod.ACCOUNT_MASS_MAILINGS
        if (campaignId){
            apiMethod = RestKuorumApiService.ApiMethod.ACCOUNT_MASS_MAILING
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
        sendScheduledCampaignWithoutDateModifications(user, campaignRQDTO, campaignId)
    }

    CampaignRSDTO campaignTest(KuorumUser user, Long campaignId){
        Map<String, String> params = [userAlias:user.id.toString(), campaignId:campaignId.toString()]
        Map<String, String> query = [test:true]
        def response= restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.ACCOUNT_MASS_MAILING_SEND,
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
                RestKuorumApiService.ApiMethod.ACCOUNT_MASS_MAILINGS,
                params,
                query,
                new TypeReference<List<CampaignRSDTO>>(){}
        )
        List<CampaignRSDTO> campaigns=[];
        if (response.data){
            campaigns = response.data
        }
        campaigns
    }

    void removeCampaign(KuorumUser user, Long campaignId) {
        Map<String, String> params = [userAlias: user.id.toString(), campaignId: campaignId.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.delete(
                RestKuorumApiService.ApiMethod.ACCOUNT_MASS_MAILING,
                params,
                query
        )
    }

    CampaignRSDTO findCampaign(KuorumUser user, Long campaignId){
        Map<String, String> params = [userAlias:user.id.toString(), campaignId:campaignId.toString()]
        Map<String, String> query = [test:true]
        def response= restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.ACCOUNT_MASS_MAILING,
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

    TrackingMailStatsByCampaignPageRSDTO findTrackingMails(KuorumUser user, Long campaignId, Integer page = 0, Integer size=10){
        Map<String, String> params = [userAlias:user.id.toString(), campaignId:campaignId.toString()]
        Map<String, String> query = [page:page.toString(), size:size.toString()]
        def response= restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.ACCOUNT_MASS_MAILING_TRACKING,
                params,
                query,
                new TypeReference<TrackingMailStatsByCampaignPageRSDTO>(){}
        )
        TrackingMailStatsByCampaignPageRSDTO trackingMailStatsByCampaignPageRSDTO=null;
        if (response.data){
            trackingMailStatsByCampaignPageRSDTO = (TrackingMailStatsByCampaignPageRSDTO)response.data
        }
        trackingMailStatsByCampaignPageRSDTO
    }
    void findTrackingMailsReport(KuorumUser user, Long campaignId){
        Map<String, String> params = [userAlias:user.id.toString(), campaignId:campaignId.toString()]
        Map<String, String> query = [:]
        def response= restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.ACCOUNT_MASS_MAILING_REPORT,
                params,
                query,
                null
        )
    }

    NewsletterConfigRSDTO findNewsletterConfig(KuorumUser user){
        Map<String, String> params = [userAlias:user.id.toString()]
        Map<String, String> query = [:]
        def response= restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.ACCOUNT_MASS_MAILING_CONFIG,
                params,
                query,
                new TypeReference<NewsletterConfigRSDTO>(){}
        )
        NewsletterConfigRSDTO newsletterConfigRDTO=null;
        if (response.data){
            newsletterConfigRDTO = (NewsletterConfigRSDTO)response.data
        }
        newsletterConfigRDTO
    }

    NewsletterConfigRSDTO updateNewsletterConfig(KuorumUser user, NewsletterConfigRQDTO config){
        Map<String, String> params = [userAlias:user.id.toString()]
        Map<String, String> query = [:]
        def response= restKuorumApiService.put(
                RestKuorumApiService.ApiMethod.ACCOUNT_MASS_MAILING_CONFIG,
                params,
                query,
                config,
                new TypeReference<NewsletterConfigRSDTO>(){}
        )
        NewsletterConfigRSDTO newsletterConfigRDTO=null;
        if (response.data){
            newsletterConfigRDTO = (NewsletterConfigRSDTO)response.data
        }
        newsletterConfigRDTO
    }
}
