package payment.campaign

import com.fasterxml.jackson.core.type.TypeReference
import grails.transaction.Transactional
import kuorum.register.KuorumUserSession
import kuorum.util.TimeZoneUtil
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.communication.bulletin.BulletinRDTO
import org.kuorum.rest.model.communication.bulletin.BulletinRSDTO
import org.kuorum.rest.model.notification.campaign.NewsletterRQDTO
import org.kuorum.rest.model.notification.campaign.NewsletterRSDTO
import org.kuorum.rest.model.notification.campaign.config.NewsletterConfigRQDTO
import org.kuorum.rest.model.notification.campaign.config.NewsletterConfigRSDTO
import org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatsByCampaignPageRSDTO
import org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO

@Transactional
class NewsletterService {

    RestKuorumApiService restKuorumApiService

    BulletinService bulletinService;


    BulletinRSDTO campaignSend(KuorumUserSession user, NewsletterRQDTO campaignRQDTO, Long campaignId = null){
        campaignRQDTO.sentOn = Calendar.getInstance(user.getTimeZone()).time
        sendScheduledCampaignWithoutDateModifications(user, campaignRQDTO, campaignId)
    }

    BulletinRSDTO campaignSchedule(KuorumUserSession user, NewsletterRQDTO campaignRQDTO, Date date, Long campaignId = null){
        campaignRQDTO.sentOn = TimeZoneUtil.convertToUserTimeZone(date, user.timeZone)
        sendScheduledCampaignWithoutDateModifications(user, campaignRQDTO, campaignId)
    }

    private BulletinRSDTO sendScheduledCampaignWithoutDateModifications(KuorumUserSession user, NewsletterRQDTO newsletterRQDTO, Long campaignId = null){
        if (campaignId && campaignId > 0){
            //UPDATE
            return updateNewsletter(user, newsletterRQDTO, campaignId)
        }else{
            //CREATE
            return createNewsletter(user, newsletterRQDTO)
        }

    }

    private BulletinRSDTO updateNewsletter(KuorumUserSession user, NewsletterRQDTO newsletterRQDTO, Long campaignId){
        Map<String, String> params = [userId:user.id.toString(), "campaignId":campaignId.toString()]
        Map<String, String> query = [:]
        RestKuorumApiService.ApiMethod apiMethod = RestKuorumApiService.ApiMethod.ACCOUNT_MASS_MAILING
        def response= restKuorumApiService.post(
                apiMethod,
                params,
                query,
                newsletterRQDTO,
                new TypeReference<BulletinRSDTO>(){}
        )
        BulletinRSDTO campaignSaved=null
        if (response.data){
            campaignSaved = response.data
        }
        campaignSaved
    }
    private BulletinRSDTO createNewsletter(KuorumUserSession user, NewsletterRQDTO newsletterRQDTO){
        Map<String, String> params = [userId:user.id.toString()]
        Map<String, String> query = [:]
        RestKuorumApiService.ApiMethod apiMethod = RestKuorumApiService.ApiMethod.ACCOUNT_MASS_MAILINGS
        def response= restKuorumApiService.post(
                apiMethod,
                params,
                query,
                newsletterRQDTO,
                new TypeReference<BulletinRSDTO>(){}
        )
        BulletinRSDTO campaignSaved=null
        if (response.data){
            campaignSaved = response.data
        }
        campaignSaved
    }

    BulletinRSDTO campaignDraft(KuorumUserSession user, NewsletterRQDTO campaignRQDTO, Long campaignId = null){
        sendScheduledCampaignWithoutDateModifications(user, campaignRQDTO, campaignId)
    }

    NewsletterRSDTO campaignTest(KuorumUserSession user, Long newsletterId){
        Map<String, String> params = [userId:user.id.toString(), newsletterId:newsletterId.toString()]
        Map<String, String> query = [:]
        def response= restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.ACCOUNT_MASS_MAILING_SEND_TEST,
                params,
                query,
                new TypeReference<NewsletterRSDTO>() {}
        )
        NewsletterRSDTO campaignSaved=null
        if (response.data){
            campaignSaved = (NewsletterRSDTO)response.data
        }
        campaignSaved
    }

    /**
     * User bulletinService.copy
     * @param user
     * @param campaignId
     * @return
     */
    @Deprecated
    NewsletterRSDTO copyNewsletter(KuorumUserSession user, Long campaignId){
        BulletinRSDTO bulletinRSDTO = bulletinService.copy(user, campaignId);
        return bulletinRSDTO.newsletter
    }

//    List<NewsletterRSDTO> findCampaigns(KuorumUserSession user){
//        Map<String, String> params = [userId:user.id.toString()]
//        Map<String, String> query = [:]
//        def response= restKuorumApiService.get(
//                RestKuorumApiService.ApiMethod.ACCOUNT_MASS_MAILINGS,
//                params,
//                query,
//                new TypeReference<List<NewsletterRSDTO>>(){}
//        )
//        List<NewsletterRSDTO> campaigns=[]
//        if (response.data){
//            campaigns = response.data
//        }
//        campaigns
//    }

    /**
     * Call bulletin service
     * @param user
     * @param campaignId
     */
    @Deprecated
    void removeCampaign(KuorumUserSession user, Long campaignId) {
        bulletinService.remove(user, campaignId);
    }

    /**
     * User bulletinService.find()
     * @param user
     * @param campaignId
     * @return
     */
    @Deprecated
    NewsletterRSDTO findCampaign(KuorumUserSession user, Long campaignId){
        BulletinRSDTO bulletinRSDTO = bulletinService.find(user, campaignId);
        NewsletterRSDTO newsletterRSDTO=null

        if (bulletinRSDTO != null){
            newsletterRSDTO = bulletinRSDTO.newsletter
        }
        newsletterRSDTO
    }

    TrackingMailStatsByCampaignPageRSDTO findTrackingMails(KuorumUserSession user, Long newsletterId, TrackingMailStatusRSDTO status = null, Integer page = 0, Integer size=10){
        Map<String, String> params = [userId:user.id.toString(), campaignId:newsletterId.toString()]
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

    void resendEmail(KuorumUserSession user, Long campaignId, Long trackingEmailId){
        Map<String, String> params = [userId:user.id.toString(), campaignId:campaignId.toString(), trackingEmailId:trackingEmailId.toString()]
        Map<String, String> query = [:]
        def response= restKuorumApiService.put(
                RestKuorumApiService.ApiMethod.ACCOUNT_MASS_MAILING_TRACKING_RESEND,
                params,
                query,
                null,
                null
        )
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
    void generateTrackingMailsReport(KuorumUserSession user, Long campaignId){
        Map<String, String> params = [userId:user.id.toString(), campaignId:campaignId.toString()]
        Map<String, String> query = [:]
        def response= restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.ACCOUNT_MASS_MAILING_REPORT,
                params,
                query,
                null
        )
    }

    NewsletterConfigRSDTO findNewsletterConfig(KuorumUserSession user){
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

    NewsletterConfigRSDTO updateNewsletterConfig(KuorumUserSession user, NewsletterConfigRQDTO config){
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

    String uploadFile(KuorumUserSession user, Long campaignId, File file, String fileName){
        fileName = java.net.URLEncoder.encode(fileName, "UTF-8")
        Map<String, String> params = [campaignId: campaignId.toString(), userId: user.id.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.putFile(
                RestKuorumApiService.ApiMethod.ACCOUNT_MASS_MAILING_ATTACHMENT,
                params,
                query,
                file,
                fileName
        )
    }

    List<String> getFiles(KuorumUserSession user, BulletinRSDTO bulletinRSDTO){
        Map<String, String> params = [campaignId: bulletinRSDTO.getId().toString(), userId: user.alias]
        Map<String, String> query = [:]
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.ACCOUNT_MASS_MAILING_ATTACHMENT,
                params,
                query,
                new TypeReference<List<String>>(){}
        )
        response.data
    }
    List<String> getReports(KuorumUserSession user, Long campaignId){
        Map<String, String> params = [campaignId: campaignId.toString(), userId:  user.alias]
        Map<String, String> query = [:]
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.ACCOUNT_MASS_MAILING_REPORTS,
                params,
                query,
                new TypeReference<List<String>>(){}
        )
        response.data
    }


    void getReport(KuorumUserSession user, Long campaignId, String fileName, OutputStream outputStream){
        Map<String, String> params = [campaignId: campaignId.toString(), userId: user.getId().toString(), fileName: fileName]
        Map<String, String> query = [:]
        def response = restKuorumApiService.getFile(
                RestKuorumApiService.ApiMethod.ACCOUNT_MASS_MAILING_REPORT_FILE,
                params,
                query,
                outputStream
        )
    }
    void deleteReport(KuorumUserSession user, Long campaignId, String fileName){
        Map<String, String> params = [campaignId: campaignId.toString(), userId: user.getId().toString(), fileName: fileName]
        Map<String, String> query = [:]
        def response = restKuorumApiService.delete(
                RestKuorumApiService.ApiMethod.ACCOUNT_MASS_MAILING_REPORT_FILE,
                params,
                query,
                new TypeReference<String>(){}
        )
    }

    void deleteFile(KuorumUserSession user, Long newsletterId, String fileName){
        Map<String, String> params = [campaignId: newsletterId.toString(), userId: user.id.toString()]
        Map<String, String> query = [fileName:fileName]
        def response = restKuorumApiService.delete(
                RestKuorumApiService.ApiMethod.ACCOUNT_MASS_MAILING_ATTACHMENT,
                params,
                query,
                new TypeReference<String>(){}
        )
    }
}
