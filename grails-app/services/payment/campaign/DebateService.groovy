package payment.campaign

import com.fasterxml.jackson.core.type.TypeReference
import grails.transaction.Transactional
import kuorum.core.exception.KuorumException
import kuorum.solr.IndexSolrService
import kuorum.users.KuorumUser
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.communication.debate.DebateRDTO
import org.kuorum.rest.model.communication.debate.DebateRSDTO
import org.kuorum.rest.model.communication.debate.PageDebateRSDTO
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable

@Transactional
class DebateService {

    RestKuorumApiService restKuorumApiService
    IndexSolrService indexSolrService

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
        Map<String, String> params = [userAlias: user.id.toString()]
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

//    @Cacheable(value="debate", key='#debateId')
    DebateRSDTO findDebate(KuorumUser user, Long debateId, String viewerUid = null) {
        findDebate(user.getId().toString(), debateId, viewerUid);
    }

//    @Cacheable(value="debate", key='#debateId')
    DebateRSDTO findDebate(String userId, Long debateId, String viewerUid = null) {
        Map<String, String> params = [userAlias: userId, debateId: debateId.toString()]
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
            return debateFound;
        }catch (KuorumException e){
            log.info("Error recovering debate $debateId : ${e.message}")
            return null;
        }
    }

    @CacheEvict(value="debate", key='#debateId')
    DebateRSDTO saveDebate(KuorumUser user, DebateRDTO debateRDTO, Long debateId) {
        debateRDTO.body = debateRDTO.body.encodeAsRemovingScriptTags().encodeAsTargetBlank()

        DebateRSDTO debate
        if (debateId) {
            debate = updateDebate(user, debateRDTO, debateId)
        } else {
            debate = createDebate(user, debateRDTO)
        }
        indexSolrService.deltaIndex();
        return debate;
    }

    DebateRSDTO createDebate(KuorumUser user, DebateRDTO debateRDTO) {
        Map<String, String> params = [userAlias: user.id.toString()]
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

    DebateRSDTO updateDebate(KuorumUser user, DebateRDTO debateRDTO, Long debateId) {
        Map<String, String> params = [userAlias: user.id.toString(), debateId: debateId.toString()]
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

    Long removeDebate(KuorumUser user, Long debateId) {
        Map<String, String> params = [userAlias: user.id.toString(), debateId: debateId.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.delete(
                RestKuorumApiService.ApiMethod.ACCOUNT_DEBATE,
                params,
                query
        )

        debateId
    }

    void sendReport(KuorumUser user, Long debateId) {
        Map<String, String> params = [userAlias: user.id.toString(), debateId: debateId.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.ACCOUNT_DEBATE_REPORT,
                params,
                query,
                null
        )
        response
    }
}
