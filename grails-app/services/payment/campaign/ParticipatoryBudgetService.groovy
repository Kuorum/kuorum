package payment.campaign

import com.fasterxml.jackson.core.type.TypeReference
import grails.transaction.Transactional
import kuorum.core.exception.KuorumException
import kuorum.solr.IndexSolrService
import kuorum.users.KuorumUser
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetRDTO
import org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetRSDTO

@Transactional
class ParticipatoryBudgetService implements CampaignCreatorService<ParticipatoryBudgetRSDTO, ParticipatoryBudgetRDTO> {

    RestKuorumApiService restKuorumApiService
    IndexSolrService indexSolrService

    ParticipatoryBudgetRSDTO find(KuorumUser user, Long campaignId, String viewerUid = null) {
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

    ParticipatoryBudgetRSDTO save(KuorumUser user, ParticipatoryBudgetRDTO participatoryBudgetRDTO, Long debateId) {
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

    ParticipatoryBudgetRSDTO createParticipatoryBudget(KuorumUser user, ParticipatoryBudgetRDTO participatoryBudgetRDTO) {
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

    ParticipatoryBudgetRSDTO updateParticipatoryBudget(KuorumUser user, ParticipatoryBudgetRDTO participatoryBudgetRDTO, Long campaignId) {
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

    void remove(KuorumUser user, Long campaignId) {
        Map<String, String> params = [userId: user.id.toString(), campaignId: campaignId.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.delete(
                RestKuorumApiService.ApiMethod.ACCOUNT_PARTICIPATORY_BUDGET,
                params,
                query
        )

        campaignId
    }

    @Override
    ParticipatoryBudgetRDTO map(ParticipatoryBudgetRSDTO participatoryBudgetRSDTO) {
        ParticipatoryBudgetRDTO participatoryBudgetRDTO = new ParticipatoryBudgetRDTO()
        if(participatoryBudgetRSDTO) {
            participatoryBudgetRDTO.name = participatoryBudgetRSDTO.name
            participatoryBudgetRDTO.checkValidation = participatoryBudgetRSDTO.checkValidation
            participatoryBudgetRDTO.triggeredTags = participatoryBudgetRSDTO.triggeredTags
            participatoryBudgetRDTO.anonymousFilter = participatoryBudgetRDTO.anonymousFilter
            participatoryBudgetRDTO.filterId = participatoryBudgetRSDTO.newsletter?.filter?.id
            participatoryBudgetRDTO.photoUrl = participatoryBudgetRSDTO.photoUrl
            participatoryBudgetRDTO.videoUrl = participatoryBudgetRSDTO.videoUrl
            participatoryBudgetRDTO.title = participatoryBudgetRSDTO.title
            participatoryBudgetRDTO.body = participatoryBudgetRSDTO.body
            participatoryBudgetRDTO.publishOn = participatoryBudgetRSDTO.datePublished
            participatoryBudgetRDTO.endDate = participatoryBudgetRSDTO.endDate
            participatoryBudgetRDTO.causes = participatoryBudgetRSDTO.causes
            participatoryBudgetRDTO.deadLineVotes = participatoryBudgetRSDTO.deadLineVotes
            participatoryBudgetRDTO.deadLineTechnicalReview= participatoryBudgetRSDTO.deadLineTechnicalReview
            participatoryBudgetRDTO.deadLineProposals= participatoryBudgetRSDTO.deadLineProposals
            participatoryBudgetRDTO.status= participatoryBudgetRSDTO.status
        }
        return participatoryBudgetRDTO
    }

    @Override
    def buildView(ParticipatoryBudgetRSDTO participatoryBudget, KuorumUser campaignUser, String viewerUid, def params) {

        def model = [participatoryBudget: participatoryBudget, campaignUser: campaignUser];
        return [view: '/participatoryBudget/show', model: model]
    }
}
