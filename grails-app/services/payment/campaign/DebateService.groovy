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
import org.kuorum.rest.model.communication.event.EventRDTO

@Transactional
class DebateService implements CampaignCreatorService<DebateRSDTO, DebateRDTO> {

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
    DebateRSDTO find(KuorumUser user, Long debateId, String viewerUid = null) {
        find(user.getId().toString(), debateId, viewerUid);
    }

//    @Cacheable(value="debate", key='#debateId')
    DebateRSDTO find(String userId, Long debateId, String viewerUid = null) {
        if (!debateId){
            return null;
        }
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

//    @CacheEvict(value="debate", key='#debateId')
    DebateRSDTO save(KuorumUser user, DebateRDTO debateRDTO, Long debateId) {
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

    @Override
    DebateRDTO map(DebateRSDTO debateRSDTO) {
        DebateRDTO debateRDTO = new DebateRDTO()
        if(debateRSDTO) {
            debateRDTO.name = debateRSDTO.name
            debateRDTO.triggeredTags = debateRSDTO.triggeredTags
            debateRDTO.anonymousFilter = debateRDTO.anonymousFilter
            debateRDTO.filterId = debateRSDTO.newsletter?.filter?.id
            debateRDTO.photoUrl = debateRSDTO.photoUrl
            debateRDTO.videoUrl = debateRSDTO.videoUrl
            debateRDTO.title = debateRSDTO.title
            debateRDTO.body = debateRSDTO.body
            debateRDTO.publishOn = debateRSDTO.datePublished
            debateRDTO.causes = debateRSDTO.causes
            if (debateRSDTO.event){
                debateRDTO.event = new EventRDTO();
                debateRDTO.event.eventDate = debateRSDTO.event.eventDate
                debateRDTO.event.latitude = debateRSDTO.event.latitude
                debateRDTO.event.longitude = debateRSDTO.event.longitude
                debateRDTO.event.zoom = debateRSDTO.event.zoom
                debateRDTO.event.localName = debateRSDTO.event.localName
                debateRDTO.event.address = debateRSDTO.event.address
                debateRDTO.event.capacity = debateRSDTO.event.capacity
            }
        }
        return debateRDTO
    }
}
