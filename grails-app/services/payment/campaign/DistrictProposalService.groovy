package payment.campaign

import com.fasterxml.jackson.core.type.TypeReference
import grails.transaction.Transactional
import kuorum.core.exception.KuorumException
import kuorum.solr.IndexSolrService
import kuorum.users.KuorumUser
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.communication.participatoryBudget.DistrictProposalRDTO
import org.kuorum.rest.model.communication.participatoryBudget.DistrictProposalRSDTO

@Transactional
class DistrictProposalService implements CampaignCreatorService<DistrictProposalRSDTO, DistrictProposalRDTO> {

    CampaignService campaignService
    RestKuorumApiService restKuorumApiService
    IndexSolrService indexSolrService

    DistrictProposalRSDTO find(KuorumUser user, Long campaignId, String viewerUid = null) {
        find(user.getId().toString(), campaignId, viewerUid);
    }

    DistrictProposalRSDTO find(String userId, Long campaignId, String viewerUid = null) {
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
                    RestKuorumApiService.ApiMethod.ACCOUNT_DISTRICT_PROPOSAL,
                    params,
                    query,
                    new TypeReference<DistrictProposalRSDTO>(){}
            )

            DistrictProposalRSDTO campaign = response.data
            return campaign;
        }catch (KuorumException e){
            log.info("Error recovering district proposal $campaignId : ${e.message}")
            return null;
        }
    }

    DistrictProposalRSDTO save(KuorumUser user, DistrictProposalRDTO districtProposalRDTO, Long campaignId) {
        districtProposalRDTO.body = districtProposalRDTO.body.encodeAsRemovingScriptTags().encodeAsTargetBlank()

        DistrictProposalRSDTO districtProposalRSDTO
        if (campaignId) {
            districtProposalRSDTO = updateDistrictProposal(user, districtProposalRDTO, campaignId)
        } else {
            districtProposalRSDTO = createDistrictProposal(user, districtProposalRDTO)
        }
        indexSolrService.deltaIndex();
        return districtProposalRSDTO;
    }

    DistrictProposalRSDTO createDistrictProposal(KuorumUser user, DistrictProposalRDTO districtProposalRDTO) {
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

    DistrictProposalRSDTO updateDistrictProposal(KuorumUser user, DistrictProposalRDTO districtProposalRDTO, Long campaignId) {
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

    void remove(KuorumUser user, Long campaignId) {
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
    def buildView(DistrictProposalRSDTO districtProposal, KuorumUser campaignUser, String viewerUid, def params) {
        def model = [districtProposal: districtProposal, campaignUser: campaignUser];
        return [view: '/districtProposal/show', model: model]
    }


    /// END CRUD

    DistrictProposalRSDTO vote(
            KuorumUser user,
            KuorumUser participatoryBudgetUser,
            Long participatoryBudgetId,
            Long districtProposalId){
        Map<String, String> params = [
                userId: participatoryBudgetUser.id.toString(),
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

        return response.data;
    }

    DistrictProposalRSDTO unvote(
            KuorumUser user,
            KuorumUser participatoryBudgetUser,
            Long participatoryBudgetId,
            Long districtId,
            Long districtProposalId){
        Map<String, String> params = [
                userId: participatoryBudgetUser.id.toString(),
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

        return response.data;
    }

    DistrictProposalRSDTO support(
            KuorumUser user,
            KuorumUser participatoryBudgetUser,
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

        return response.data;
    }

    DistrictProposalRSDTO unsupport(
            KuorumUser user,
            KuorumUser participatoryBudgetUser,
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

        return response.data;
    }
}
