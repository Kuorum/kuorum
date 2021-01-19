package payment.campaign

import com.fasterxml.jackson.core.type.TypeReference
import grails.transaction.Transactional
import kuorum.core.exception.KuorumException
import kuorum.register.KuorumUserSession
import kuorum.solr.IndexSolrService
import kuorum.users.KuorumUserService
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.communication.participatoryBudget.DistrictProposalRDTO
import org.kuorum.rest.model.communication.participatoryBudget.DistrictProposalRSDTO
import org.kuorum.rest.model.communication.participatoryBudget.DistrictProposalTechnicalReviewRDTO
import org.kuorum.rest.model.kuorumUser.BasicDataKuorumUserRSDTO

@Transactional
class DistrictProposalService implements CampaignCreatorService<DistrictProposalRSDTO, DistrictProposalRDTO> {

    CampaignService campaignService
    RestKuorumApiService restKuorumApiService
    IndexSolrService indexSolrService
    KuorumUserService kuorumUserService

    List<DistrictProposalRSDTO> findAll(KuorumUserSession user,String viewerUid = null) {
        Map<String, String> params = [userId: user.getId().toString()]
        Map<String, String> query = [:]
        if (viewerUid){
            query.put("viewerUid",viewerUid)
        }
        try {
            def response = restKuorumApiService.get(
                    RestKuorumApiService.ApiMethod.ACCOUNT_DISTRICT_PROPOSALS,
                    params,
                    query,
                    new TypeReference<List<DistrictProposalRSDTO>>(){}
            )

            List<DistrictProposalRSDTO> campaigns = response.data
            return campaigns
        }catch (KuorumException e){
            log.info("Error recovering district proposal $campaignId : ${e.message}")
            return null
        }
    }

    DistrictProposalRSDTO find(KuorumUserSession user, Long campaignId, String viewerUid = null) {
        find(user.getId().toString(), campaignId, viewerUid)
    }

    DistrictProposalRSDTO find(String userId, Long campaignId, String viewerUid = null) {
        if (!campaignId){
            return null
        }
        Map<String, String> params = [userId: userId, campaignId: campaignId.toString()]
        Map<String, String> query = [:]
        if (viewerUid){
            query.put("viewerUid",viewerUid)
        }
        try {
            def response = restKuorumApiService.get(
                    RestKuorumApiService.ApiMethod.ACCOUNT_DISTRICT_PROPOSAL,
                    params,
                    query,
                    new TypeReference<DistrictProposalRSDTO>(){}
            )

            DistrictProposalRSDTO campaign = response.data
            return campaign
        }catch (KuorumException e){
            log.info("Error recovering district proposal $campaignId : ${e.message}")
            return null
        }
    }

    DistrictProposalRSDTO save(KuorumUserSession user, DistrictProposalRDTO districtProposalRDTO, Long campaignId) {
        districtProposalRDTO.body = districtProposalRDTO.body.encodeAsRemovingScriptTags().encodeAsTargetBlank()

        DistrictProposalRSDTO districtProposalRSDTO
        if (campaignId) {
            districtProposalRSDTO = updateDistrictProposal(user, districtProposalRDTO, campaignId)
        } else {
            districtProposalRSDTO = createDistrictProposal(user, districtProposalRDTO)
        }
        indexSolrService.deltaIndex()
        return districtProposalRSDTO
    }

    private DistrictProposalRSDTO createDistrictProposal(KuorumUserSession user, DistrictProposalRDTO districtProposalRDTO) {
        Map<String, String> params = [userId: user.id.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.post(
                RestKuorumApiService.ApiMethod.ACCOUNT_DISTRICT_PROPOSALS,
                params,
                query,
                districtProposalRDTO,
                new TypeReference<DistrictProposalRSDTO>(){}
        )

        DistrictProposalRSDTO campaign = null
        if (response.data) {
            campaign = response.data
        }

        campaign
    }

    private DistrictProposalRSDTO updateDistrictProposal(KuorumUserSession user, DistrictProposalRDTO districtProposalRDTO, Long campaignId) {
        Map<String, String> params = [userId: user.id.toString(), campaignId: campaignId.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.put(
                RestKuorumApiService.ApiMethod.ACCOUNT_DISTRICT_PROPOSAL,
                params,
                query,
                districtProposalRDTO,
                new TypeReference<DistrictProposalRSDTO>(){}
        )

        return response.data
    }

    void remove(KuorumUserSession user, Long campaignId) {
        Map<String, String> params = [userId: user.id.toString(), campaignId: campaignId.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.delete(
                RestKuorumApiService.ApiMethod.ACCOUNT_DISTRICT_PROPOSAL,
                params,
                query
        )

        campaignId
    }

    @Override
    DistrictProposalRDTO map(DistrictProposalRSDTO districtProposalRSDTO) {
        DistrictProposalRDTO districtProposalRDTO = new DistrictProposalRDTO()
        if(districtProposalRSDTO) {
            districtProposalRDTO = campaignService.basicMapping(districtProposalRSDTO, districtProposalRDTO)
            districtProposalRDTO.districtId = districtProposalRSDTO.district.id
            districtProposalRDTO.participatoryBudgetId = districtProposalRSDTO.participatoryBudget.id
        }
        return districtProposalRDTO
    }

    @Override
    def buildView(DistrictProposalRSDTO districtProposal, BasicDataKuorumUserRSDTO participatoryBudgetUser, String viewerUid, def params) {
        BasicDataKuorumUserRSDTO campaignUser = kuorumUserService.findBasicUserRSDTO(districtProposal.user.id)
        def model = [districtProposal: districtProposal, campaignUser: campaignUser]
        return [view: '/districtProposal/show', model: model]
    }


    /// END CRUD

    DistrictProposalRSDTO vote(
            KuorumUserSession user,
            BasicDataKuorumUserRSDTO participatoryBudgetUser,
            Long participatoryBudgetId,
            Long districtProposalId) throws KuorumException{
        Map<String, String> params = [
                userId: participatoryBudgetUser.id,
                campaignId: participatoryBudgetId.toString(),
                proposalId: districtProposalId.toString(),
        ]
        Map<String, String> query = [viewerUid:user.id.toString()]
        def response = restKuorumApiService.put(
                RestKuorumApiService.ApiMethod.ACCOUNT_PARTICIPATORY_BUDGET_DISTRICT_PROPOSAL_VOTE,
                params,
                query,
                null,
                new TypeReference<DistrictProposalRSDTO>(){}
        )

        return response.data
    }

    DistrictProposalRSDTO unvote(
            KuorumUserSession user,
            BasicDataKuorumUserRSDTO participatoryBudgetUser,
            Long participatoryBudgetId,
            Long districtProposalId){
        Map<String, String> params = [
                userId: participatoryBudgetUser.id,
                campaignId: participatoryBudgetId.toString(),
                proposalId: districtProposalId.toString(),
        ]
        Map<String, String> query = [viewerUid:user.id.toString()]
        def response = restKuorumApiService.delete(
                RestKuorumApiService.ApiMethod.ACCOUNT_PARTICIPATORY_BUDGET_DISTRICT_PROPOSAL_VOTE,
                params,
                query,
                new TypeReference<DistrictProposalRSDTO>(){}
        )

        return response.data
    }

    DistrictProposalRSDTO support(
            KuorumUserSession user,
            BasicDataKuorumUserRSDTO participatoryBudgetUser,
            Long participatoryBudgetId,
            Long districtProposalId){
        Map<String, String> params = [
                userId: participatoryBudgetUser.id.toString(),
                campaignId: participatoryBudgetId.toString(),
                proposalId: districtProposalId.toString(),
        ]
        Map<String, String> query = [viewerUid:user.id.toString()]
        def response = restKuorumApiService.put(
                RestKuorumApiService.ApiMethod.ACCOUNT_PARTICIPATORY_BUDGET_DISTRICT_PROPOSAL_SUPPORT,
                params,
                query,
                null,
                new TypeReference<DistrictProposalRSDTO>(){}
        )

        return response.data
    }

    DistrictProposalRSDTO unsupport(
            KuorumUserSession user,
            BasicDataKuorumUserRSDTO participatoryBudgetUser,
            Long participatoryBudgetId,
            Long districtProposalId){
        Map<String, String> params = [
                userId: participatoryBudgetUser.id.toString(),
                campaignId: participatoryBudgetId.toString(),
                proposalId: districtProposalId.toString(),
        ]
        Map<String, String> query = [viewerUid:user.id.toString()]
        def response = restKuorumApiService.delete(
                RestKuorumApiService.ApiMethod.ACCOUNT_PARTICIPATORY_BUDGET_DISTRICT_PROPOSAL_SUPPORT,
                params,
                query,
                new TypeReference<DistrictProposalRSDTO>(){}
        )

        return response.data
    }
    DistrictProposalRSDTO technicalReview(
            KuorumUserSession participatoryBudgetUser,
            Long participatoryBudgetId,
            Long districtProposalId,
            DistrictProposalTechnicalReviewRDTO technicalReviewRDTO
            ){
        Map<String, String> params = [
                userId: participatoryBudgetUser.id.toString(),
                campaignId: participatoryBudgetId.toString(),
                proposalId: districtProposalId.toString(),
        ]
        Map<String, String> query = [:]
        def response = restKuorumApiService.post(
                RestKuorumApiService.ApiMethod.ACCOUNT_PARTICIPATORY_BUDGET_DISTRICT_PROPOSAL_TECHNICAL_REVIEW,
                params,
                query,
                technicalReviewRDTO,
                new TypeReference<DistrictProposalRSDTO>(){}
        )

        return response.data
    }

    @Override
    DistrictProposalRSDTO copy(KuorumUserSession user, Long campaignId) {
        //TODO: KPV-1606
        return null
    }

    @Override
    DistrictProposalRSDTO copy(String userId, Long campaignId) {
        //TODO: KPV-1606
        return null
    }
}
