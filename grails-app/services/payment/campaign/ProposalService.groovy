package payment.campaign

import com.fasterxml.jackson.core.type.TypeReference
import grails.transaction.Transactional
import kuorum.users.KuorumUser
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.communication.debate.DebateRDTO
import org.kuorum.rest.model.communication.debate.DebateRSDTO
import org.kuorum.rest.model.communication.debate.ProposalRDTO
import org.kuorum.rest.model.communication.debate.ProposalRSDTO
import org.kuorum.rest.model.communication.debate.search.ProposalPageRSDTO
import org.kuorum.rest.model.communication.debate.search.SearchProposalRSDTO

import java.text.SimpleDateFormat

@Transactional
class ProposalService {

    RestKuorumApiService restKuorumApiService

    ProposalPageRSDTO findProposal(DebateRSDTO debate, SearchProposalRSDTO searchProposalRSDTO){
        Map<String, String> params = [userAlias: debate.userAlias,debateId:debate.id.toString()]
        Map<String, String> query = searchProposalRSDTO.encodeAsQueryParams()
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.ACCOUNT_DEBATE_PROPOSAL,
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

    ProposalRSDTO addProposal(KuorumUser user, String debateAlias, Long debateId, String body) {
        Map<String, String> params = [userAlias: debateAlias,debateId:debateId.toString()]
        Map<String, String> query = [:]
        ProposalRDTO proposalRDTO = new ProposalRDTO();
        proposalRDTO.body=body
        proposalRDTO.userAlias=user.alias
        def response = restKuorumApiService.post(
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

}
