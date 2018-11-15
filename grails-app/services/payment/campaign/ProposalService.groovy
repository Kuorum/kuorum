package payment.campaign

import com.fasterxml.jackson.core.type.TypeReference
import grails.transaction.Transactional
import kuorum.register.KuorumUserSession
import kuorum.users.KuorumUser
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.communication.debate.*
import org.kuorum.rest.model.communication.debate.search.ProposalPageRSDTO
import org.kuorum.rest.model.communication.debate.search.SearchProposalRSDTO

@Transactional
class ProposalService {

    RestKuorumApiService restKuorumApiService

//    @Cacheable(value = 'proposal', key="#debate.id")
    ProposalPageRSDTO findProposal(DebateRSDTO debate, SearchProposalRSDTO searchProposalRSDTO, String viewerUid = null){
        Map<String, String> params = [userId: debate.user.id,debateId:debate.id.toString()]
        Map<String, String> query = searchProposalRSDTO.encodeAsQueryParams()
        if (viewerUid){
            query.put("viewerUid",viewerUid)
        }
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.ACCOUNT_DEBATE_PROPOSALS,
                params,
                query,
                new TypeReference<ProposalPageRSDTO>(){}
        )

        ProposalPageRSDTO proposalPageRSDTO= null
        if (response.data) {
            proposalPageRSDTO = (ProposalPageRSDTO) response.data
        }

        proposalPageRSDTO
    }

//    @CacheEvict(value = 'proposal', key="#debate.id")
    ProposalRSDTO addProposal(KuorumUserSession user, DebateRSDTO debate, String body) {
        Map<String, String> params = [userId: debate.user.id,debateId:debate.id.toString()]
        Map<String, String> query = [:]
        ProposalRDTO proposalRDTO = new ProposalRDTO();
        proposalRDTO.body=body
        proposalRDTO.userId=user.id.toString()
        def response = restKuorumApiService.post(
                RestKuorumApiService.ApiMethod.ACCOUNT_DEBATE_PROPOSALS,
                params,
                query,
                proposalRDTO,
                new TypeReference<ProposalRSDTO>(){}
        )

        ProposalRSDTO proposalRSDTO= null
        if (response.data) {
            proposalRSDTO = (ProposalRSDTO) response.data
        }

        proposalRSDTO
    }

//    @CacheEvict(value = 'proposal', key="#debate.id")
    ProposalRSDTO pinProposal(KuorumUserSession user, String debateUserId, Long debateId, Long proposalId, Boolean pin) {
        Map<String, String> params = [userId: debateUserId,debateId:debateId.toString(), proposalId:proposalId.toString()]
        Map<String, String> query = [:]
        ProposalRDTO proposalRDTO = new ProposalRDTO();
        proposalRDTO.userId=user.id.toString()
        proposalRDTO.body=""
        proposalRDTO.pinned = pin
        def response = restKuorumApiService.put(
                RestKuorumApiService.ApiMethod.ACCOUNT_DEBATE_PROPOSAL,
                params,
                query,
                proposalRDTO,
                new TypeReference<ProposalRSDTO>(){}
        )

        ProposalRSDTO proposalRSDTO= null
        if (response.data) {
            proposalRSDTO = (ProposalRSDTO) response.data
        }

        proposalRSDTO
    }

//    @CacheEvict(value = 'proposal', key="#debate.id")
    void likeProposal(KuorumUserSession user, String debateUserId, Long debateId, Long proposalId, Boolean like) {
        Map<String, String> params = [userId: debateUserId,debateId:debateId.toString(), proposalId:proposalId.toString()]
        Map<String, String> query = [likeUserId:user.id]
        def response
        if (like) {
            response = restKuorumApiService.put(
                    RestKuorumApiService.ApiMethod.ACCOUNT_DEBATE_PROPOSAL_LIKE,
                    params,
                    query,
                    null,
                    new TypeReference<ProposalRSDTO>() {}
            )
        }else{
            response = restKuorumApiService.delete(
                    RestKuorumApiService.ApiMethod.ACCOUNT_DEBATE_PROPOSAL_LIKE,
                    params,
                    query
            )
        }
    }

//    @CacheEvict(value = 'proposal', key="#debate.id")
    void deleteProposal(KuorumUserSession user, String debateUserId, Long debateId, Long proposalId){
        Map<String, String> params = [userId: debateUserId,debateId:debateId.toString(), proposalId:proposalId.toString()]
        Map<String, String> query = [userActionId:user.id.toString()]
        restKuorumApiService.delete(
                RestKuorumApiService.ApiMethod.ACCOUNT_DEBATE_PROPOSAL,
                params,
                query
        )
    }

//    @CacheEvict(value = 'proposal', key="#debate.id")
    ProposalRSDTO addComment(KuorumUserSession user, DebateRSDTO debate, Long proposalId, String body) {
        Map<String, String> params = [userId: debate.user.id,debateId:debate.id.toString(), proposalId:proposalId.toString() ]
        Map<String, String> query = [:]
        ProposalCommentRDTO commentRDTO = new ProposalCommentRDTO();
        commentRDTO.body=body.encodeAsRemovingScriptTags().encodeAsTargetBlank()
        commentRDTO.userId=user.id.toString()
        def response = restKuorumApiService.post(
                RestKuorumApiService.ApiMethod.ACCOUNT_DEBATE_PROPOSAL_COMMENTS,
                params,
                query,
                commentRDTO,
                new TypeReference<ProposalRSDTO>(){}
        )

        ProposalRSDTO proposalRSDTO= null
        if (response.data) {
            proposalRSDTO = (ProposalRSDTO) response.data
        }

        proposalRSDTO
    }

//    @CacheEvict(value = 'proposal', key="#debate.id")
    void deleteComment(KuorumUserSession user,Long debateId, String debateUserId, Long proposalId, Long commentId) {
        Map<String, String> params = [userId: debateUserId,debateId:debateId.toString(), proposalId:proposalId.toString(), commentId:commentId.toString() ]
        Map<String, String> query = [userAction:user.id.toString()]
        restKuorumApiService.delete(
                RestKuorumApiService.ApiMethod.ACCOUNT_DEBATE_PROPOSAL_COMMENT,
                params,
                query
        )

    }

//    @CacheEvict(value = 'proposal', key="#debate.id")
    ProposalCommentRSDTO voteComment(KuorumUserSession user,Long debateId, String debateUserId, Long proposalId, Long commentId, Integer vote) {
        Map<String, String> params = [userId: debateUserId,debateId:debateId.toString(), proposalId:proposalId.toString(), commentId:commentId.toString() ]
        Map<String, String> query = [userAction:user.id.toString(), vote:vote.toString()]
        def response = restKuorumApiService.put(
                RestKuorumApiService.ApiMethod.ACCOUNT_DEBATE_PROPOSAL_COMMENT_VOTE,
                params,
                query,
                null,
                new TypeReference<ProposalCommentRSDTO>(){}
        )
        ProposalCommentRSDTO proposalCommentRSDTO= null
        if (response.data) {
            proposalCommentRSDTO = (ProposalCommentRSDTO) response.data
        }

        proposalCommentRSDTO
    }


}
