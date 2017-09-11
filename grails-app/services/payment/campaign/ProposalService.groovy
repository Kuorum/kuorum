package payment.campaign

import com.fasterxml.jackson.core.type.TypeReference
import grails.transaction.Transactional
import kuorum.users.KuorumUser
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.communication.debate.DebateRSDTO
import org.kuorum.rest.model.communication.debate.ProposalCommentRDTO
import org.kuorum.rest.model.communication.debate.ProposalCommentRSDTO
import org.kuorum.rest.model.communication.debate.ProposalRDTO
import org.kuorum.rest.model.communication.debate.ProposalRSDTO
import org.kuorum.rest.model.communication.debate.search.ProposalPageRSDTO
import org.kuorum.rest.model.communication.debate.search.SearchProposalRSDTO

@Transactional
class ProposalService {

    RestKuorumApiService restKuorumApiService

    ProposalPageRSDTO findProposal(DebateRSDTO debate, SearchProposalRSDTO searchProposalRSDTO, String viewerUid = null){
        Map<String, String> params = [userAlias: debate.user.alias,debateId:debate.id.toString()]
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

    ProposalRSDTO addProposal(KuorumUser user, DebateRSDTO debate, String body) {
        Map<String, String> params = [userAlias: debate.user.alias,debateId:debate.id.toString()]
        Map<String, String> query = [:]
        ProposalRDTO proposalRDTO = new ProposalRDTO();
        proposalRDTO.body=body
        proposalRDTO.userAlias=user.alias
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

    ProposalRSDTO pinProposal(KuorumUser user, String debateAlias, Long debateId, Long proposalId, Boolean pin) {
        Map<String, String> params = [userAlias: debateAlias,debateId:debateId.toString(), proposalId:proposalId.toString()]
        Map<String, String> query = [:]
        ProposalRDTO proposalRDTO = new ProposalRDTO();
        proposalRDTO.userAlias=user.alias
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

    void likeProposal(KuorumUser user, String debateAlias, Long debateId, Long proposalId, Boolean like) {
        Map<String, String> params = [userAlias: debateAlias,debateId:debateId.toString(), proposalId:proposalId.toString()]
        Map<String, String> query = [likeUserAlias:user.alias]
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

    void deleteProposal(KuorumUser user, String debateAlias, Long debateId, Long proposalId){
        Map<String, String> params = [userAlias: debateAlias,debateId:debateId.toString(), proposalId:proposalId.toString()]
        Map<String, String> query = [userAction:user.alias]
        restKuorumApiService.delete(
                RestKuorumApiService.ApiMethod.ACCOUNT_DEBATE_PROPOSAL,
                params,
                query
        )
    }

    ProposalRSDTO addComment(KuorumUser user, DebateRSDTO debate, Long proposalId, String body) {
        Map<String, String> params = [userAlias: debate.user.alias,debateId:debate.id.toString(), proposalId:proposalId.toString() ]
        Map<String, String> query = [:]
        ProposalCommentRDTO commentRDTO = new ProposalCommentRDTO();
        commentRDTO.body=body.encodeAsRemovingScriptTags().encodeAsTargetBlank()
        commentRDTO.userAlias=user.alias
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

    void deleteComment(KuorumUser user,Long debateId, String debateAlias, Long proposalId, Long commentId) {
        Map<String, String> params = [userAlias: debateAlias,debateId:debateId.toString(), proposalId:proposalId.toString(), commentId:commentId.toString() ]
        Map<String, String> query = [userAction:user.alias]
        restKuorumApiService.delete(
                RestKuorumApiService.ApiMethod.ACCOUNT_DEBATE_PROPOSAL_COMMENT,
                params,
                query
        )

    }

    ProposalCommentRSDTO voteComment(KuorumUser user,Long debateId, String debateAlias, Long proposalId, Long commentId, Integer vote) {
        Map<String, String> params = [userAlias: debateAlias,debateId:debateId.toString(), proposalId:proposalId.toString(), commentId:commentId.toString() ]
        Map<String, String> query = [userAction:user.alias, vote:vote.toString()]
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
