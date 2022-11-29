package payment.campaign

import com.fasterxml.jackson.core.type.TypeReference
import grails.transaction.Transactional
import kuorum.core.exception.KuorumException
import kuorum.register.KuorumUserSession
import kuorum.util.rest.RestKuorumApiService
import kuorum.web.constants.WebConstants
import org.kuorum.rest.model.communication.CampaignLightPageRSDTO
import org.kuorum.rest.model.communication.CampaignRDTO
import org.kuorum.rest.model.communication.CampaignRSDTO
import org.kuorum.rest.model.communication.event.EventRDTO
import org.kuorum.rest.model.communication.search.SearchCampaignRDTO
import org.kuorum.rest.model.contact.ContactRSDTO
import org.kuorum.rest.model.kuorumUser.BasicDataKuorumUserRSDTO

@Transactional
class CampaignService {

    RestKuorumApiService restKuorumApiService

    CampaignRSDTO findStarredCampaign(List<CampaignRSDTO> relevantCampaigns, Long starredCampaign) {

        CampaignRSDTO relevantCampaign = null;
        if (relevantCampaigns) {
            relevantCampaign = relevantCampaigns.find { it.id == starredCampaign }
        }
        if (relevantCampaign == null) {
            relevantCampaign = this.find(WebConstants.FAKE_LANDING_ALIAS_USER, starredCampaign)
        }
        return relevantCampaign
    }

    List<CampaignRSDTO> findRelevantDomainCampaigns(String viewerUid = null) {
        Map<String, String> params = [:]
        Map<String, String> query = [:]
        if (viewerUid) {
            query.put("viewerUid", viewerUid)
        }
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.CAMPAIGNS_DOMAIN,
                params,
                query,
                new TypeReference<List<CampaignRSDTO>>() {}
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

    CampaignLightPageRSDTO findAllCampaigns(KuorumUserSession user, SearchCampaignRDTO searchCampaignRSDTO) {
        return findAllCampaigns(user.id.toString(), searchCampaignRSDTO)
    }

    CampaignLightPageRSDTO findAllCampaigns(String userId, SearchCampaignRDTO searchCampaignRSDTO) {
        Map<String, String> params = [userId: userId]
        Map<String, String> query = searchCampaignRSDTO.encodeAsQueryParams()
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.ACCOUNT_CAMPAIGNS,
                params,
                query,
                new TypeReference<CampaignLightPageRSDTO>() {}
        )

        response.data
    }

//    @Cacheable(value="debate", key='#campaignId')
    CampaignRSDTO find(KuorumUserSession user, Long campaignId, String viewerUid = null) {
        find(user.getId().toString(), campaignId, viewerUid)
    }

    CampaignRSDTO find(BasicDataKuorumUserRSDTO user, Long campaignId, String viewerUid = null) {
        find(user.getAlias(), campaignId, viewerUid)
    }

    CampaignRSDTO findByQrCode(String qrCode) {
        Map<String, String> params = [qrCode: qrCode]
        try {
            def response = restKuorumApiService.get(RestKuorumApiService.ApiMethod.CAMPAIGN_QR_CODE,
                    params,
                    [:],
                    new TypeReference<CampaignRSDTO>() {}
            )
            return response.data?:null
        } catch (Exception e) {
            log.info("Error recovering campaign by qr code $qrCode : ${e.message}")
            return null
        }
    }

//    @Cacheable(value="debate", key='#campaignId')
    CampaignRSDTO find(String userId, Long campaignId, String viewerUid = null) {
        if (!campaignId) {
            return null
        }
        Map<String, String> params = [userId: userId, campaignId: campaignId.toString()]
        Map<String, String> query = [:]
        if (viewerUid) {
            query.put("viewerUid", viewerUid)
        }
        try {
            def response = restKuorumApiService.get(
                    RestKuorumApiService.ApiMethod.ACCOUNT_CAMPAIGN,
                    params,
                    query,
                    new TypeReference<CampaignRSDTO>() {}
            )

            CampaignRSDTO campaignRSDTO = null
            if (response.data) {
                campaignRSDTO = (CampaignRSDTO) response.data
            }
            return campaignRSDTO
        } catch (KuorumException e) {
            log.info("Error recovering campaign $campaignId : ${e.message}")
            return null
        }
    }

    CampaignRDTO basicMapping(CampaignRSDTO campaignRSDTO, CampaignRDTO rdto) {
        if (campaignRSDTO) {
            rdto.name = campaignRSDTO.name
            rdto.validationType = campaignRSDTO.validationType
            rdto.triggeredTags = campaignRSDTO.triggeredTags
            rdto.anonymousFilter = campaignRSDTO.anonymousFilter
            rdto.filterId = campaignRSDTO.newsletter?.filter?.id
            rdto.photoUrl = campaignRSDTO.photoUrl
            rdto.videoUrl = campaignRSDTO.videoUrl
            rdto.title = campaignRSDTO.title
            rdto.body = campaignRSDTO.body
            rdto.publishOn = campaignRSDTO.datePublished
            rdto.campaignVisibility = campaignRSDTO.campaignVisibility
            rdto.groupValidation = campaignRSDTO.groupValidation
            rdto.profileComplete = campaignRSDTO.profileComplete
            rdto.newsletterCommunication = campaignRSDTO.newsletterCommunication
            rdto.causes = campaignRSDTO.causes
            rdto.startDate = campaignRSDTO.startDate
            rdto.endDate = campaignRSDTO.endDate
            if (campaignRSDTO.event) {
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

    String uploadFile(KuorumUserSession user, Long campaignId, File file, String fileName) {
        fileName = java.net.URLEncoder.encode(fileName, "UTF-8")
        Map<String, String> params = [campaignId: campaignId.toString(), userId: user.id.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.putFile(
                RestKuorumApiService.ApiMethod.ACCOUNT_CAMPAIGN_FILES,
                params,
                query,
                file,
                fileName
        )
    }

    List<String> getFiles(CampaignRSDTO campaignRSDTO) {
        Map<String, String> params = [campaignId: campaignRSDTO.getId().toString(), userId: campaignRSDTO.getUser().getId()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.ACCOUNT_CAMPAIGN_FILES,
                params,
                query,
                new TypeReference<List<String>>() {}
        )
        response.data
    }

    List<String> getContactFiles(CampaignRSDTO campaignRSDTO, String viewerUid) {
        Map<String, String> params = [campaignId: campaignRSDTO.getId().toString(), userId: campaignRSDTO.getUser().getId()]
        Map<String, String> query = [viewerUid: viewerUid]
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.ACCOUNT_CAMPAIGN_CONTACT_FILES,
                params,
                query,
                new TypeReference<List<String>>() {}
        )
        response.data
    }

    ContactRSDTO getContactData(CampaignRSDTO campaignRSDTO, String viewerUid) {
        Map<String, String> params = [campaignId: campaignRSDTO.getId().toString(), userId: campaignRSDTO.getUser().getId()]
        Map<String, String> query = [viewerUid: viewerUid]
        try {
            def response = restKuorumApiService.get(
                    RestKuorumApiService.ApiMethod.ACCOUNT_CAMPAIGN_CONTACT_DATA,
                    params,
                    query,
                    new TypeReference<ContactRSDTO>() {}
            )
            response.data
        } catch (Exception e) {
            log.info("Contact of the campaign not found")
            return null;
        }
    }

    List<String> getReports(KuorumUserSession loggedUser, Long campaignId) {
        Map<String, String> params = [campaignId: campaignId.toString(), userId: loggedUser.getId().toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.ACCOUNT_CAMPAIGN_REPORTS,
                params,
                query,
                new TypeReference<List<String>>() {}
        )
        response.data
    }

    void getReport(KuorumUserSession user, Long campaignId, String fileName, OutputStream outputStream) {
        Map<String, String> params = [campaignId: campaignId.toString(), userId: user.getId().toString(), fileName: fileName]
        Map<String, String> query = [:]
        def response = restKuorumApiService.getFile(
                RestKuorumApiService.ApiMethod.ACCOUNT_CAMPAIGN_REPORT_FILE,
                params,
                query,
                outputStream
        )
    }

    void deleteReport(KuorumUserSession user, Long campaignId, String fileName) {
        Map<String, String> params = [campaignId: campaignId.toString(), userId: user.getId().toString(), fileName: fileName]
        Map<String, String> query = [:]
        def response = restKuorumApiService.delete(
                RestKuorumApiService.ApiMethod.ACCOUNT_CAMPAIGN_REPORT_FILE,
                params,
                query,
                new TypeReference<String>() {}
        )
    }

    void deleteFile(KuorumUserSession user, Long campaignId, String fileName) {
        Map<String, String> params = [campaignId: campaignId.toString(), userId: user.id.toString()]
        Map<String, String> query = [fileName: fileName]
        def response = restKuorumApiService.delete(
                RestKuorumApiService.ApiMethod.ACCOUNT_CAMPAIGN_FILES,
                params,
                query,
                new TypeReference<String>() {}
        )
    }

    void pauseCampaign(KuorumUserSession user, Long campaignId, boolean activePause) {
        Map<String, String> params = [campaignId: campaignId.toString(), userId: user.id.toString()]
        Map<String, String> query = [:]
        if (activePause) {
            def response = restKuorumApiService.put(
                    RestKuorumApiService.ApiMethod.ACCOUNT_CAMPAIGN_PAUSE,
                    params,
                    query,
                    null,
                    new TypeReference<CampaignRSDTO>() {}
            )
        } else {
            def response = restKuorumApiService.delete(
                    RestKuorumApiService.ApiMethod.ACCOUNT_CAMPAIGN_PAUSE,
                    params,
                    query,
                    new TypeReference<CampaignRSDTO>() {}
            )
        }
    }

    boolean userBelongToCampaignGroup(String userId, Long campaignId, String viewerUid = nul) {
        if (!campaignId) {
            return null
        }
        Map<String, String> params = [userId: userId, campaignId: campaignId.toString()]
        Map<String, String> query = [:]
        if (viewerUid) {
            query.put("viewerUid", viewerUid)
        }
        try {
            def response = restKuorumApiService.get(
                    RestKuorumApiService.ApiMethod.ACCOUNT_CAMPAIGN_GROUPS,
                    params,
                    query,
                    new TypeReference<Boolean>() {}
            )

            Boolean userBelongsToGroup = response.data
            return userBelongsToGroup
        } catch (Exception e) {
            log.error("Error checking if user belongs to group", e)
            return false
        }
    }

    def copy(KuorumUserSession user, Long campaignId) {
        return null
    }

    void sendReport(KuorumUserSession user, Long campaignId, RestKuorumApiService.ApiMethod reportApiMethod) {
        Map<String, String> params = [userId: user.getId().toString(), campaignId: campaignId.toString()]
        Map<String, String> query = [:]
        restKuorumApiService.get(
                reportApiMethod,
                params,
                query,
                null
        )
    }
}
