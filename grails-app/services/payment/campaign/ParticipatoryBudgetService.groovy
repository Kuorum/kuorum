package payment.campaign

import com.fasterxml.jackson.core.type.TypeReference
import grails.transaction.Transactional
import kuorum.core.exception.KuorumException
import kuorum.register.KuorumUserSession
import kuorum.solr.IndexSolrService
import kuorum.users.KuorumUser
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.communication.participatoryBudget.*

@Transactional
class ParticipatoryBudgetService implements CampaignCreatorService<ParticipatoryBudgetRSDTO, ParticipatoryBudgetRDTO> {

    CampaignService campaignService
    RestKuorumApiService restKuorumApiService
    IndexSolrService indexSolrService

    ParticipatoryBudgetRSDTO find(KuorumUserSession user, Long campaignId, String viewerUid = null) {
        find(user.getId().toString(), campaignId, viewerUid);
    }

    ParticipatoryBudgetRSDTO find(String userId, Long campaignId, String viewerUid = null) {
        if (!campaignId){
            return null;
        }
        Map<String, String> params = [userId: userId, campaignId: campaignId.toString()]
        Map<String, String> query = [:]
        if (viewerUid){
            query.put("viewerUid",viewerUid)
        }
        try {
            def response = restKuorumApiService.get(
                    RestKuorumApiService.ApiMethod.ACCOUNT_PARTICIPATORY_BUDGET,
                    params,
                    query,
                    new TypeReference<ParticipatoryBudgetRSDTO>(){}
            )

            ParticipatoryBudgetRSDTO debateFound = null
            if (response.data) {
                debateFound = (ParticipatoryBudgetRSDTO) response.data
            }
            return debateFound;
        }catch (KuorumException e){
            log.info("Error recovering debate $campaignId : ${e.message}")
            return null;
        }
    }

    ParticipatoryBudgetRSDTO save(KuorumUserSession user, ParticipatoryBudgetRDTO participatoryBudgetRDTO, Long debateId) {
        participatoryBudgetRDTO.body = participatoryBudgetRDTO.body.encodeAsRemovingScriptTags().encodeAsTargetBlank()

        ParticipatoryBudgetRSDTO debate
        if (debateId) {
            debate = updateParticipatoryBudget(user, participatoryBudgetRDTO, debateId)
        } else {
            debate = createParticipatoryBudget(user, participatoryBudgetRDTO)
        }
        indexSolrService.deltaIndex();
        return debate;
    }

    private ParticipatoryBudgetRSDTO createParticipatoryBudget(KuorumUserSession user, ParticipatoryBudgetRDTO participatoryBudgetRDTO) {
        Map<String, String> params = [userId: user.id.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.post(
                RestKuorumApiService.ApiMethod.ACCOUNT_PARTICIPATORY_BUDGETS,
                params,
                query,
                participatoryBudgetRDTO,
                new TypeReference<ParticipatoryBudgetRSDTO>(){}
        )

        ParticipatoryBudgetRSDTO debateSaved = null
        if (response.data) {
            debateSaved = response.data
        }

        debateSaved
    }

    private ParticipatoryBudgetRSDTO updateParticipatoryBudget(KuorumUserSession user, ParticipatoryBudgetRDTO participatoryBudgetRDTO, Long campaignId) {
        Map<String, String> params = [userId: user.id.toString(), campaignId: campaignId.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.put(
                RestKuorumApiService.ApiMethod.ACCOUNT_PARTICIPATORY_BUDGET,
                params,
                query,
                participatoryBudgetRDTO,
                new TypeReference<ParticipatoryBudgetRSDTO>(){}
        )

        ParticipatoryBudgetRSDTO debateSaved = null
        if (response.data) {
            debateSaved = response.data
        }

        debateSaved
    }

    void remove(KuorumUserSession user, Long campaignId) {
        Map<String, String> params = [userId: user.id.toString(), campaignId: campaignId.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.delete(
                RestKuorumApiService.ApiMethod.ACCOUNT_PARTICIPATORY_BUDGET,
                params,
                query
        )

        campaignId
    }

    List<ParticipatoryBudgetRSDTO> findActiveParticipatoryBudgets(ParticipatoryBudgetStatusDTO budgetStatusDTO) {
        Map<String, String> params = [:]
        Map<String, String> query = [status: budgetStatusDTO.toString()]
        try {
            def response = restKuorumApiService.get(
                    RestKuorumApiService.ApiMethod.ACCOUNT_ACTIVE_PARTICIPATORY_BUDGETS,
                    params,
                    query,
                    new TypeReference<PageParticipatoryBudgetRSDTO>() {}
            )

            PageParticipatoryBudgetRSDTO pageFound = null
            if (response.data) {
                pageFound = (PageParticipatoryBudgetRSDTO) response.data
        }
        return pageFound.getData();

        }catch (KuorumException e){
            log.info("Error recovering participatory budgets : ${e.message}")
            return null;
        }
    }

    @Override
    ParticipatoryBudgetRDTO map(ParticipatoryBudgetRSDTO participatoryBudgetRSDTO) {
        ParticipatoryBudgetRDTO participatoryBudgetRDTO = new ParticipatoryBudgetRDTO()
        if(participatoryBudgetRSDTO) {
            participatoryBudgetRDTO = campaignService.basicMapping(participatoryBudgetRSDTO, participatoryBudgetRDTO)
            participatoryBudgetRDTO.deadLineVotes = participatoryBudgetRSDTO.deadLineVotes
            participatoryBudgetRDTO.deadLineTechnicalReview= participatoryBudgetRSDTO.deadLineTechnicalReview
            participatoryBudgetRDTO.deadLineProposals= participatoryBudgetRSDTO.deadLineProposals
            participatoryBudgetRDTO.deadLineFinalReview= participatoryBudgetRSDTO.deadLineFinalReview
            participatoryBudgetRDTO.status= participatoryBudgetRSDTO.status
            participatoryBudgetRDTO.districts = participatoryBudgetRSDTO.districts
        }
        return participatoryBudgetRDTO
    }


    void sendReport(KuorumUserSession user, Long campaignId){
        Map<String, String> params = [userId: user.getId().toString(), campaignId: campaignId.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.ACCOUNT_PARTICIPATORY_BUDGET_REPORT,
                params,
                query,
                null
        )
    }


    PageDistrictProposalRSDTO findDistrictProposalsByDistrict(KuorumUser user, Long participatoryBudgetId, FilterDistrictProposalRDTO filter, String viewerUid = null){
        if (!participatoryBudgetId){
            return null;
        }
        Map<String, String> params = [userId: user.getId().toString(), campaignId: participatoryBudgetId.toString()]
        Map<String, String> query = filter.encodeAsQueryParams()
        if (viewerUid){
            query.put("viewerUid",viewerUid)
        }
        try {
            def response = restKuorumApiService.get(
                    RestKuorumApiService.ApiMethod.ACCOUNT_PARTICIPATORY_BUDGET_DISTRICT_PROPOSALS,
                    params,
                    query,
                    new TypeReference<PageDistrictProposalRSDTO>(){}
            )

            return response.data;
        }catch (KuorumException e){
            log.info("Error recovering district proposals [districtId: $districtId ]: ${e.message}")
            return null;
        }
    }

    @Override
    def buildView(ParticipatoryBudgetRSDTO participatoryBudget, KuorumUser campaignUser, String viewerUid, def params) {
        Random seed = new Random();
        Double randomSeed = seed.nextDouble();

        def model = [participatoryBudget: participatoryBudget, campaignUser: campaignUser, randomSeed:randomSeed];
        return [view: '/participatoryBudget/show', model: model]
    }
}
