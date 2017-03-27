package payment.campaign

import com.fasterxml.jackson.core.type.TypeReference
import grails.plugin.springsecurity.SpringSecurityService
import grails.transaction.Transactional
import kuorum.solr.IndexSolrService
import kuorum.users.KuorumUser
import kuorum.util.TimeZoneUtil
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.communication.debate.DebateRDTO
import org.kuorum.rest.model.communication.debate.DebateRSDTO

import java.text.SimpleDateFormat

@Transactional
class DebateService {

    RestKuorumApiService restKuorumApiService
    IndexSolrService indexSolrService

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

    DebateRSDTO findDebate(KuorumUser user, Long debateId, String viewerUid = null) {
        findDebate(user.getId().toString(), debateId, viewerUid);
    }

    DebateRSDTO findDebate(String userId, Long debateId, String viewerUid = null) {
        Map<String, String> params = [userAlias: userId, debateId: debateId.toString()]
        Map<String, String> query = [:]
        if (viewerUid){
            query.put("viewerUid",viewerUid)
        }
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

            debateFound
    }

    DebateRSDTO saveDebate(KuorumUser user, DebateRDTO debateRDTO, Long debateId) {
        if (debateRDTO.publishOn != null) {
            debateRDTO.publishOn = TimeZoneUtil.convertToUserTimeZone(debateRDTO.publishOn, user.timeZone)
        }
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

}
