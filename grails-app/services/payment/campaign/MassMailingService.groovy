package payment.campaign

import grails.transaction.Transactional
import kuorum.users.KuorumUser
import kuorum.util.rest.RestKuorumApiService
import org.codehaus.jackson.type.TypeReference
import org.kuorum.rest.model.notification.campaign.CampaignRQDTO
import org.kuorum.rest.model.notification.campaign.CampaignRSDTO
import org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatsByCampaignPageRSDTO

import java.text.SimpleDateFormat

@Transactional
class MassMailingService {

    RestKuorumApiService restKuorumApiService;


    private Date getNowUTC(){
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date now = cal.getTime();
        return now;
    }
    CampaignRSDTO campaignSend(KuorumUser user, CampaignRQDTO campaignRQDTO, Long campaignId = null){
        campaignSchedule(user, campaignRQDTO, getNowUTC(), campaignId)
    }

    private Date convertToUserTimeZone(Date date, TimeZone userTimeZone){
        def dateFormat = 'yyyy/MM/dd HH:mm'
        String rawDate = date.format(dateFormat)
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setTimeZone(userTimeZone);
        sdf.parse(rawDate)
    }

    CampaignRSDTO campaignSchedule(KuorumUser user, CampaignRQDTO campaignRQDTO, Date date, Long campaignId = null){
        campaignRQDTO.sentOn = convertToUserTimeZone(date, user.timeZone)
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
                RestKuorumApiService.ApiMethod.ACCOUNT_CAMPAIGN,
                params,
                query
        )
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

    TrackingMailStatsByCampaignPageRSDTO findTrackingMails(KuorumUser user, Long campaignId, Integer page = 0, Integer size=10){
        Map<String, String> params = [userAlias:user.id.toString(), campaignId:campaignId.toString()]
        Map<String, String> query = [page:page.toString(), size:size.toString()]
        def response= restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.ACCOUNT_CAMPAIGN_TRACKING,
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
}
