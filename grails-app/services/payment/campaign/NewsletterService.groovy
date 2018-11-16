package payment.campaign

import com.fasterxml.jackson.core.type.TypeReference
import grails.transaction.Transactional
import kuorum.register.KuorumUserSession
import kuorum.users.KuorumUser
import kuorum.util.TimeZoneUtil
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.admin.AdminConfigMailingRDTO
import org.kuorum.rest.model.notification.campaign.NewsletterRQDTO
import org.kuorum.rest.model.notification.campaign.NewsletterRSDTO
import org.kuorum.rest.model.notification.campaign.config.NewsletterConfigRQDTO
import org.kuorum.rest.model.notification.campaign.config.NewsletterConfigRSDTO
import org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatsByCampaignPageRSDTO
import org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO

@Transactional
class NewsletterService {

    RestKuorumApiService restKuorumApiService


    NewsletterRSDTO campaignSend(KuorumUserSession user, NewsletterRQDTO campaignRQDTO, Long campaignId = null){
        campaignRQDTO.sentOn = Calendar.getInstance(user.getTimeZone()).time
        sendScheduledCampaignWithoutDateModifications(user, campaignRQDTO, campaignId)
    }

    NewsletterRSDTO campaignSchedule(KuorumUserSession user, NewsletterRQDTO campaignRQDTO, Date date, Long campaignId = null){
        campaignRQDTO.sentOn = TimeZoneUtil.convertToUserTimeZone(date, user.timeZone)
        sendScheduledCampaignWithoutDateModifications(user, campaignRQDTO, campaignId)
    }

    private NewsletterRSDTO sendScheduledCampaignWithoutDateModifications(KuorumUserSession user, NewsletterRQDTO campaignRQDTO, Long campaignId = null){
        Map<String, String> params = [userId:user.id.toString()]
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
                new TypeReference<NewsletterRSDTO>(){}
        )
        NewsletterRSDTO campaignSaved=null
        if (response.data){
            campaignSaved = response.data
        }
        campaignSaved
    }

    NewsletterRSDTO campaignDraft(KuorumUserSession user, NewsletterRQDTO campaignRQDTO, Long campaignId = null){
        sendScheduledCampaignWithoutDateModifications(user, campaignRQDTO, campaignId)
    }

    NewsletterRSDTO campaignTest(KuorumUserSession user, Long campaignId){
        Map<String, String> params = [userId:user.id.toString(), campaignId:campaignId.toString()]
        Map<String, String> query = [test:true]
        def response= restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.ACCOUNT_MASS_MAILING_SEND,
                params,
                query,
                new TypeReference<NewsletterRSDTO>(){}
        )
        NewsletterRSDTO campaignSaved=null
        if (response.data){
            campaignSaved = (NewsletterRSDTO)response.data
        }
        campaignSaved
    }

    List<NewsletterRSDTO> findCampaigns(KuorumUserSession user){
        Map<String, String> params = [userId:user.id.toString()]
        Map<String, String> query = [:]
        def response= restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.ACCOUNT_MASS_MAILINGS,
                params,
                query,
                new TypeReference<List<NewsletterRSDTO>>(){}
        )
        List<NewsletterRSDTO> campaigns=[]
        if (response.data){
            campaigns = response.data
        }
        campaigns
    }

    void removeCampaign(KuorumUserSession user, Long campaignId) {
        Map<String, String> params = [userId: user.id.toString(), campaignId: campaignId.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.delete(
                RestKuorumApiService.ApiMethod.ACCOUNT_MASS_MAILING,
                params,
                query
        )
    }

    NewsletterRSDTO findCampaign(KuorumUserSession user, Long campaignId){
        Map<String, String> params = [userId:user.id.toString(), campaignId:campaignId.toString()]
        Map<String, String> query = [test:true]
        def response= restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.ACCOUNT_MASS_MAILING,
                params,
                query,
                new TypeReference<NewsletterRSDTO>(){}
        )
        NewsletterRSDTO campaignSaved=null
        if (response.data){
            campaignSaved = (NewsletterRSDTO)response.data
        }
        campaignSaved
    }

    TrackingMailStatsByCampaignPageRSDTO findTrackingMails(KuorumUserSession user, Long campaignId, TrackingMailStatusRSDTO status = null, Integer page = 0, Integer size=10){
        Map<String, String> params = [userId:user.id.toString(), campaignId:campaignId.toString()]
        Map<String, String> query = [page:page.toString(), size:size.toString()]
        if (status){
            query.put("status", status)
        }
        def response= restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.ACCOUNT_MASS_MAILING_TRACKING,
                params,
                query,
                new TypeReference<TrackingMailStatsByCampaignPageRSDTO>(){}
        )
        TrackingMailStatsByCampaignPageRSDTO trackingMailStatsByCampaignPageRSDTO=null
        if (response.data){
            trackingMailStatsByCampaignPageRSDTO = (TrackingMailStatsByCampaignPageRSDTO)response.data
        }
        trackingMailStatsByCampaignPageRSDTO
    }
    void findCampaignsCollectionReport(KuorumUserSession user){
        Map<String, String> params = [userId:user.id.toString()]
        Map<String, String> query = [:]
        def response= restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.ACCOUNT_MASS_MAILING_CAMPAIGNS_REPORT,
                params,
                query,
                null
        )
    }
    void findTrackingMailsReport(KuorumUserSession user, Long campaignId){
        Map<String, String> params = [userId:user.id.toString(), campaignId:campaignId.toString()]
        Map<String, String> query = [:]
        def response= restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.ACCOUNT_MASS_MAILING_REPORT,
                params,
                query,
                null
        )
    }

    NewsletterConfigRSDTO findNewsletterConfig(KuorumUser user){
        Map<String, String> params = [userId:user.id.toString()]
        Map<String, String> query = [:]
        def response= restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.ACCOUNT_MASS_MAILING_CONFIG,
                params,
                query,
                new TypeReference<NewsletterConfigRSDTO>(){}
        )
        NewsletterConfigRSDTO newsletterConfigRDTO=null
        if (response.data){
            newsletterConfigRDTO = (NewsletterConfigRSDTO)response.data
        }
        newsletterConfigRDTO
    }

    NewsletterConfigRSDTO updateNewsletterConfig(KuorumUser user, NewsletterConfigRQDTO config){
        Map<String, String> params = [userId:user.id.toString()]
        Map<String, String> query = [:]
        def response= restKuorumApiService.put(
                RestKuorumApiService.ApiMethod.ACCOUNT_MASS_MAILING_CONFIG,
                params,
                query,
                config,
                new TypeReference<NewsletterConfigRSDTO>(){}
        )
        NewsletterConfigRSDTO newsletterConfigRDTO=null
        if (response.data){
            newsletterConfigRDTO = (NewsletterConfigRSDTO)response.data
        }
        newsletterConfigRDTO
    }

    void updateNewsletterConfig(KuorumUser user, AdminConfigMailingRDTO adminRDTO){
        Map<String, String> params = [userId:user.id.toString()]
        Map<String, String> query = [:]
        def response= restKuorumApiService.put(
                RestKuorumApiService.ApiMethod.ADMIN_USER_CONFIG_SENDER,
                params,
                query,
                adminRDTO,
                null
        )
    }
}
