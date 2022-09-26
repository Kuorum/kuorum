package payment.campaign

import com.fasterxml.jackson.core.type.TypeReference
import grails.plugin.springsecurity.SpringSecurityService
import kuorum.core.exception.KuorumException
import kuorum.register.KuorumUserSession
import kuorum.solr.IndexSolrService
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.communication.contest.ContestApplicationRSDTO
import org.kuorum.rest.model.communication.contest.ContestApplicationRankingRSDTO
import org.kuorum.rest.model.communication.contest.ContestRDTO
import org.kuorum.rest.model.communication.contest.ContestRSDTO
import org.kuorum.rest.model.communication.contest.FilterContestApplicationRDTO
import org.kuorum.rest.model.communication.contest.PageContestApplicationRSDTO
import org.kuorum.rest.model.kuorumUser.BasicDataKuorumUserRSDTO

import static org.kuorum.rest.model.communication.contest.ContestStatusDTO.ADDING_APPLICATIONS

class ContestService extends AbstractCampaignCreatorService<ContestRSDTO, ContestRDTO> implements CampaignCreatorService<ContestRSDTO, ContestRDTO> {

    CampaignService campaignService
    IndexSolrService indexSolrService
    SpringSecurityService springSecurityService


//    @Override
    List<ContestRSDTO> findAll(KuorumUserSession user, String viewerUid = null) {
        Map<String, String> params = [userId: user.getId().toString()]
        Map<String, String> query = [:]
        if (viewerUid) {
            query.put("viewerUid", viewerUid)
        }
        try {
            def response = restKuorumApiService.get(
                    RestKuorumApiService.ApiMethod.ACCOUNT_CONTESTS,
                    params,
                    query,
                    new TypeReference<List<ContestRSDTO>>() {}
            )

            List<ContestRSDTO> contestRSDTOS = null
            if (response.data) {
                contestRSDTOS = (List<ContestRSDTO>) response.data
            }
            return contestRSDTOS
        } catch (KuorumException e) {
            log.info("Error recovering debate $campaignId : ${e.message}")
            return null
        }
    }

    ContestRSDTO find(KuorumUserSession user, Long campaignId, String viewerUid = null) {
        find(user.getId().toString(), campaignId, viewerUid)
    }

    ContestRSDTO find(String userId, Long campaignId, String viewerUid = null) {
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
                    RestKuorumApiService.ApiMethod.ACCOUNT_CONTEST,
                    params,
                    query,
                    new TypeReference<ContestRSDTO>() {}
            )

            ContestRSDTO contestRSDTO = null
            if (response.data) {
                contestRSDTO = (ContestRSDTO) response.data
            }
            return contestRSDTO
        } catch (KuorumException e) {
            log.info("Error recovering debate $campaignId : ${e.message}")
            return null
        }
    }

    @Override
    ContestRSDTO save(KuorumUserSession user, ContestRDTO contestRDTO, Long debateId) {
        contestRDTO.body = contestRDTO.body.encodeAsRemovingScriptTags().encodeAsTargetBlank()

        ContestRSDTO contestRSDTO
        if (debateId) {
            contestRSDTO = updateContest(user, contestRDTO, debateId)
        } else {
            contestRSDTO = createContest(user, contestRDTO)
        }
        indexSolrService.deltaIndex()
        return contestRSDTO
    }

    private ContestRSDTO createContest(KuorumUserSession user, ContestRDTO contestRDTO) {
        Map<String, String> params = [userId: user.id.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.post(
                RestKuorumApiService.ApiMethod.ACCOUNT_CONTESTS,
                params,
                query,
                contestRDTO,
                new TypeReference<ContestRSDTO>() {}
        )

        ContestRSDTO contestRSDTO = null
        if (response.data) {
            contestRSDTO = response.data
        }

        contestRSDTO
    }

    private ContestRSDTO updateContest(KuorumUserSession user, ContestRDTO contestRDTO, Long campaignId) {
        Map<String, String> params = [userId: user.id.toString(), campaignId: campaignId.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.put(
                RestKuorumApiService.ApiMethod.ACCOUNT_CONTEST,
                params,
                query,
                contestRDTO,
                new TypeReference<ContestRSDTO>() {}
        )

        ContestRSDTO contestRSDTO = null
        if (response.data) {
            contestRSDTO = response.data
        }

        contestRSDTO
    }

    void remove(KuorumUserSession user, Long campaignId) {
        Map<String, String> params = [userId: user.id.toString(), campaignId: campaignId.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.delete(
                RestKuorumApiService.ApiMethod.ACCOUNT_CONTEST,
                params,
                query
        )

        campaignId
    }

    @Override
    ContestRDTO map(ContestRSDTO contestRSDTO) {
        ContestRDTO contestRDTO = new ContestRDTO()
        if (contestRSDTO) {
            contestRDTO = campaignService.basicMapping(contestRSDTO, contestRDTO)
            contestRDTO.deadLineApplications = contestRSDTO.deadLineApplications
            contestRDTO.deadLineReview = contestRSDTO.deadLineReview
            contestRDTO.deadLineVotes = contestRSDTO.deadLineVotes
            contestRDTO.deadLineResults = contestRSDTO.deadLineResults
            contestRDTO.numWinnerApplications = contestRSDTO.numWinnerApplications
            contestRDTO.maxApplicationsPerUser = contestRSDTO.maxApplicationsPerUser
            contestRDTO.status = contestRSDTO.status
        }
        return contestRDTO
    }

    @Override
    def buildView(ContestRSDTO contestRSDTO, BasicDataKuorumUserRSDTO campaignUser, String viewerUid, def params) {
        Random seed = new Random()
        Double randomSeed = seed.nextDouble()
        int userContestApplications = getUserContestApplicationCountIfNeeded(campaignUser.id, contestRSDTO, viewerUid)
        def model = [contest: contestRSDTO, campaignUser: campaignUser, randomSeed: randomSeed, userContestApplications: userContestApplications]
        return [view: '/contest/show', model: model]
    }

    @Override
    protected TypeReference<ContestRSDTO> getRsdtoType() {
        return new TypeReference<ContestRSDTO>() {}
    }

    @Override
    protected RestKuorumApiService.ApiMethod getCopyApiMethod() {
        return RestKuorumApiService.ApiMethod.ACCOUNT_CONTEST_COPY;
    }

    private PageContestApplicationRSDTO countContestApplications(String ownerCampaignId, Long contestId, KuorumUserSession contestApplicationOwner) {
        countContestApplications(ownerCampaignId, contestId, contestApplicationOwner.id.toString())
    }

    private PageContestApplicationRSDTO countContestApplications(String ownerCampaignId, Long contestId, String countestApplicationOwner) {
        return findContestApplications(ownerCampaignId, contestId, buildApplicationsCountFilter(countestApplicationOwner))
    }

    PageContestApplicationRSDTO findContestApplications(KuorumUserSession user, Long contestId, FilterContestApplicationRDTO filter, String viewerUid = null) {
        return findContestApplications(user.id.toString(), contestId, filter, viewerUid)
    }

    PageContestApplicationRSDTO findContestApplications(BasicDataKuorumUserRSDTO user, Long contestId, FilterContestApplicationRDTO filter, String viewerUid = null) {
        return findContestApplications(user.id, contestId, filter, viewerUid) //User.id don't affect to filter
    }

    PageContestApplicationRSDTO findContestApplications(String ownerCampaignId, Long contestId, FilterContestApplicationRDTO filter, String viewerUid = null) {
        if (!contestId) {
            return null
        }
        Map<String, String> params = [userId: ownerCampaignId, campaignId: contestId.toString()]
        Map<String, String> query = filter.encodeAsQueryParams()
        if (viewerUid) {
            query.put("viewerUid", viewerUid)
        }
        try {
            def response = restKuorumApiService.get(
                    RestKuorumApiService.ApiMethod.ACCOUNT_CONTEST_APPLICATIONS_LIST,
                    params,
                    query,
                    new TypeReference<PageContestApplicationRSDTO>() {}
            )

            return response.data
        } catch (KuorumException e) {
            log.info("Error recovering applications [Contest ID: ${filter.id} ]: ${e.message}")
            return null
        }
    }

    private FilterContestApplicationRDTO buildApplicationsCountFilter(String ownerId) {
        FilterContestApplicationRDTO filter = new FilterContestApplicationRDTO(page: 0, size: 1);
        filter.setAttachNotPublished(true)
        filter.setOwnerId(ownerId)
        return filter
    }

    private int getUserContestApplicationCountIfNeeded(String ownerCampaignId, ContestRSDTO contestRSDTO, String viewerUid) {
        return maxApplicationsLimitAppliable(contestRSDTO) ? secureCountUserApplications(ownerCampaignId, contestRSDTO.getId(), viewerUid) : 0
    }

    private boolean maxApplicationsLimitAppliable(ContestRSDTO contestRSDTO) {
        ADDING_APPLICATIONS == contestRSDTO.status && contestRSDTO.maxApplicationsPerUser > 0
    }

    int secureCountUserApplications(String ownerCampaignId, Long contestId, String viewerUid) {
        return springSecurityService.isLoggedIn() ? countUserApplications(ownerCampaignId, contestId, viewerUid) : 0
    }

    private long countUserApplications(String ownerCampaignId, Long contestId, String viewerId) {
        return countContestApplications(ownerCampaignId, contestId, viewerId).total
    }

    void sendVotesReport(KuorumUserSession user, Long campaignId) {
        Map<String, String> params = [userId: user.getId().toString(), campaignId: campaignId.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.ACCOUNT_CONTEST_VOTES_REPORT,
                params,
                query,
                null
        )
    }


    List<ContestApplicationRankingRSDTO> getRanking(String ownerCampaignId, Long contestId) {
        if (!contestId) {
            return null
        }
        Map<String, String> params = [userId: ownerCampaignId, campaignId: contestId.toString()]
        try {
            def response = restKuorumApiService.get(
                    RestKuorumApiService.ApiMethod.ACCOUNT_CONTEST_RANKING,
                    params,
                    null,
                    new TypeReference<List<ContestApplicationRankingRSDTO>>() {}
            )

            return response.data
        } catch (KuorumException e) {
            log.info("Error recovering applications [Contest ID: ${filter.id} ]: ${e.message}")
            return null
        }
    }
}