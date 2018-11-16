package payment.campaign

import com.fasterxml.jackson.core.type.TypeReference
import grails.transaction.Transactional
import kuorum.core.exception.KuorumException
import kuorum.register.KuorumUserSession
import kuorum.solr.IndexSolrService
import kuorum.users.KuorumUser
import kuorum.util.rest.RestKuorumApiService
import org.bson.types.ObjectId
import org.kuorum.rest.model.communication.debate.DebateRDTO
import org.kuorum.rest.model.communication.debate.DebateRSDTO
import org.kuorum.rest.model.communication.debate.PageDebateRSDTO
import org.kuorum.rest.model.communication.debate.search.ProposalPageRSDTO
import org.kuorum.rest.model.communication.debate.search.SearchProposalRSDTO
import org.kuorum.rest.model.communication.debate.search.SortProposalRDTO

@Transactional
class DebateService implements CampaignCreatorService<DebateRSDTO, DebateRDTO> {

    RestKuorumApiService restKuorumApiService
    IndexSolrService indexSolrService
    ProposalService proposalService
    CampaignService campaignService

    PageDebateRSDTO findAllDebates(Integer page = 0, Integer size = 10) {
        Map<String, String> params = [:]
        Map<String, String> query = [page:page, size:size]
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.ACCOUNT_DEBATES_ALL,
                params,
                query,
                new TypeReference<PageDebateRSDTO>(){}
        )
        response.data
    }
    List<DebateRSDTO> findAllDebates(KuorumUser user) {
        Map<String, String> params = [userId: user.id.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.ACCOUNT_DEBATES,
                params,
                query,
                new TypeReference<List<DebateRSDTO>>(){}
        )

        List<DebateRSDTO> debatesFound = null
        if (response.data) {
            debatesFound = (List<DebateRSDTO>) response.data
        }

        debatesFound
    }

    DebateRSDTO find(KuorumUserSession user, Long debateId, String viewerUid = null) {
        find(user.getId().toString(), debateId, viewerUid)
    }
//    @Cacheable(value="debate", key='#campaignId')
    DebateRSDTO find(String userId, Long debateId, String viewerUid = null) {
        if (!debateId){
            return null
        }
        Map<String, String> params = [userId: userId, debateId: debateId.toString()]
        Map<String, String> query = [:]
        if (viewerUid){
            query.put("viewerUid",viewerUid)
        }
        try {
            def response = restKuorumApiService.get(
                    RestKuorumApiService.ApiMethod.ACCOUNT_DEBATE,
                    params,
                    query,
                    new TypeReference<DebateRSDTO>(){}
            )

            DebateRSDTO debateFound = null
            if (response.data) {
                debateFound = (DebateRSDTO) response.data
            }
            return debateFound
        }catch (KuorumException e){
            log.info("Error recovering debate $debateId : ${e.message}")
            return null
        }
    }

//    @CacheEvict(value="debate", key='#campaignId')
    DebateRSDTO save(KuorumUserSession user, DebateRDTO debateRDTO, Long debateId) {
        debateRDTO.body = debateRDTO.body.encodeAsRemovingScriptTags().encodeAsTargetBlank()

        DebateRSDTO debate
        if (debateId) {
            debate = updateDebate(user, debateRDTO, debateId)
        } else {
            debate = createDebate(user, debateRDTO)
        }
        indexSolrService.deltaIndex()
        return debate
    }

    private DebateRSDTO createDebate(KuorumUserSession user, DebateRDTO debateRDTO) {
        Map<String, String> params = [userId: user.id.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.post(
                RestKuorumApiService.ApiMethod.ACCOUNT_DEBATES,
                params,
                query,
                debateRDTO,
                new TypeReference<DebateRSDTO>(){}
        )

        DebateRSDTO debateSaved = null
        if (response.data) {
            debateSaved = response.data
        }

        debateSaved
    }

    private DebateRSDTO updateDebate(KuorumUserSession user, DebateRDTO debateRDTO, Long debateId) {
        Map<String, String> params = [userId: user.id.toString(), debateId: debateId.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.put(
                RestKuorumApiService.ApiMethod.ACCOUNT_DEBATE,
                params,
                query,
                debateRDTO,
                new TypeReference<DebateRSDTO>(){}
        )

        DebateRSDTO debateSaved = null
        if (response.data) {
            debateSaved = response.data
        }

        debateSaved
    }

    void remove(KuorumUserSession user, Long debateId) {
        Map<String, String> params = [userId: user.id.toString(), debateId: debateId.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.delete(
                RestKuorumApiService.ApiMethod.ACCOUNT_DEBATE,
                params,
                query
        )

        debateId
    }

    void sendReport(KuorumUserSession user, Long debateId) {
        Map<String, String> params = [userId: user.id.toString(), debateId: debateId.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.ACCOUNT_DEBATE_REPORT,
                params,
                query,
                null
        )
        response
    }

    @Override
    DebateRDTO map(DebateRSDTO debateRSDTO) {
        return campaignService.basicMapping(debateRSDTO, new DebateRDTO())
    }

    @Override
    def buildView(DebateRSDTO debate, KuorumUser debateUser, String viewerUid, def params) {
        SearchProposalRSDTO searchProposalRSDTO = new SearchProposalRSDTO()
        searchProposalRSDTO.sort = new SortProposalRDTO()
        searchProposalRSDTO.sort.direction = SortProposalRDTO.Direction.DESC
        searchProposalRSDTO.sort.field = SortProposalRDTO.Field.LIKES
        searchProposalRSDTO.size = Integer.MAX_VALUE // Sorting and filtering will be done using JS. We expect maximum 100 proposals

        ProposalPageRSDTO proposalPage = proposalService.findProposal(debate, searchProposalRSDTO,viewerUid)
//            if (lastActivity){
//                lastModified(debate.lastActivity)
//            }
        List<KuorumUser> pinnedUsers = proposalPage.data
                .findAll{it.pinned}
                .collect{KuorumUser.get(new ObjectId(it.user.id))}
                .findAll{it}
                .unique()
                .sort{ ku1, ku2 -> ku1.avatar != null?-1:ku2.avatar!=null?1:0 }

        def model = [debate: debate, debateUser: debateUser, proposalPage:proposalPage, pinnedUsers:pinnedUsers]

        if (params.printAsWidget){
            return [view: '/debate/widgetDebate', model: model]
        }else{
            return [view: '/debate/show', model: model]
        }
    }
}
