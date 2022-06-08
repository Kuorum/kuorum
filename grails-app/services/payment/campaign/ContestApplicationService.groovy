package payment.campaign

import com.fasterxml.jackson.core.type.TypeReference
import kuorum.core.exception.KuorumException
import kuorum.register.KuorumUserSession
import kuorum.solr.IndexSolrService
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.communication.contest.ContestApplicationRDTO
import org.kuorum.rest.model.communication.contest.ContestApplicationRSDTO
import org.kuorum.rest.model.kuorumUser.BasicDataKuorumUserRSDTO

class ContestApplicationService extends AbstractCampaignCreatorService<ContestApplicationRSDTO, ContestApplicationRDTO> implements CampaignCreatorService<ContestApplicationRSDTO, ContestApplicationRDTO> {

    CampaignService campaignService
    IndexSolrService indexSolrService

//    @Override
    List<ContestApplicationRSDTO> findAll(KuorumUserSession user, String viewerUid = null) {
        Map<String, String> params = [userId: user.getId().toString()]
        Map<String, String> query = [:]
        if (viewerUid) {
            query.put("viewerUid", viewerUid)
        }
        try {
            def response = restKuorumApiService.get(
                    RestKuorumApiService.ApiMethod.ACCOUNT_CONTEST_APPLICATIONS,
                    params,
                    query,
                    new TypeReference<List<ContestApplicationRSDTO>>() {}
            )

            List<ContestApplicationRSDTO> contestApplicationRSDTOS = null
            if (response.data) {
                contestApplicationRSDTOS = (List<ContestApplicationRSDTO>) response.data
            }
            return contestApplicationRSDTOS
        } catch (KuorumException e) {
            log.info("Error recovering debate $campaignId : ${e.message}")
            return null
        }
    }

    ContestApplicationRSDTO find(KuorumUserSession user, Long campaignId, String viewerUid = null) {
        find(user.getId().toString(), campaignId, viewerUid)
    }

    ContestApplicationRSDTO find(String userId, Long campaignId, String viewerUid = null) {
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
                    RestKuorumApiService.ApiMethod.ACCOUNT_CONTEST_APPLICATION,
                    params,
                    query,
                    new TypeReference<ContestApplicationRSDTO>() {}
            )

            ContestApplicationRSDTO contestApplicationRSDTO = null
            if (response.data) {
                contestApplicationRSDTO = (ContestApplicationRSDTO) response.data
            }
            return contestApplicationRSDTO
        } catch (KuorumException e) {
            log.info("Error recovering debate $campaignId : ${e.message}")
            return null
        }
    }

    @Override
    ContestApplicationRSDTO save(KuorumUserSession user, ContestApplicationRDTO contestApplicationRDTO, Long debateId) {
        contestApplicationRDTO.body = contestApplicationRDTO.body.encodeAsRemovingScriptTags().encodeAsTargetBlank()

        ContestApplicationRSDTO contestApplicationRSDTO
        if (debateId) {
            contestApplicationRSDTO = updateContestApplication(user, contestApplicationRDTO, debateId)
        } else {
            contestApplicationRSDTO = createContestApplication(user, contestApplicationRDTO)
        }
        indexSolrService.deltaIndex()
        return contestApplicationRSDTO
    }

    private ContestApplicationRSDTO createContestApplication(KuorumUserSession user, ContestApplicationRDTO contestApplicationRDTO) {
        Map<String, String> params = [userId: user.id.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.post(
                RestKuorumApiService.ApiMethod.ACCOUNT_CONTEST_APPLICATIONS,
                params,
                query,
                contestApplicationRDTO,
                new TypeReference<ContestApplicationRSDTO>() {}
        )

        ContestApplicationRSDTO contestApplicationRSDTO = null
        if (response.data) {
            contestApplicationRSDTO = response.data
        }

        contestApplicationRSDTO
    }

    private ContestApplicationRSDTO updateContestApplication(KuorumUserSession user, ContestApplicationRDTO contestApplicationRDTO, Long campaignId) {
        Map<String, String> params = [userId: user.id.toString(), campaignId: campaignId.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.put(
                RestKuorumApiService.ApiMethod.ACCOUNT_CONTEST_APPLICATION,
                params,
                query,
                contestApplicationRDTO,
                new TypeReference<ContestApplicationRSDTO>() {}
        )

        ContestApplicationRSDTO contestApplicationRSDTO = null
        if (response.data) {
            contestApplicationRSDTO = response.data
        }

        contestApplicationRSDTO
    }

    void remove(KuorumUserSession user, Long campaignId) {
        Map<String, String> params = [userId: user.id.toString(), campaignId: campaignId.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.delete(
                RestKuorumApiService.ApiMethod.ACCOUNT_CONTEST_APPLICATION,
                params,
                query
        )

        campaignId
    }

    @Override
    ContestApplicationRDTO map(ContestApplicationRSDTO contestApplicationRSDTO) {
        ContestApplicationRDTO contestApplicationRDTO = new ContestApplicationRDTO()
        if (contestApplicationRSDTO) {
            contestApplicationRDTO = campaignService.basicMapping(contestApplicationRSDTO, contestApplicationRDTO)
            contestApplicationRDTO.activityType = contestApplicationRSDTO.activityType
            contestApplicationRDTO.focusType = contestApplicationRSDTO.focusType
            contestApplicationRDTO.contestId = contestApplicationRSDTO.contest.id
        }
        return contestApplicationRDTO
    }

    @Override
    def buildView(ContestApplicationRSDTO contestApplicationRSDTO, BasicDataKuorumUserRSDTO campaignUser, String viewerUid, def params) {
        Random seed = new Random()
        Double randomSeed = seed.nextDouble()

        def model = [contestApplication: contestApplicationRSDTO, campaignUser: campaignUser]
        return [view: '/contestApplication/show', model: model]
    }

    @Override
    protected TypeReference<ContestApplicationRSDTO> getRsdtoType() {
        return new TypeReference<ContestApplicationRSDTO>() {}
    }

    @Override
    protected RestKuorumApiService.ApiMethod getCopyApiMethod() {
        return RestKuorumApiService.ApiMethod.ACCOUNT_CONTEST_APPLICATION_COPY;
    }
}
