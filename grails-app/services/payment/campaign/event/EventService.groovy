package payment.campaign.event

import com.fasterxml.jackson.core.type.TypeReference
import grails.transaction.Transactional
import kuorum.users.KuorumUser
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.communication.event.EventRSDTO
import org.kuorum.rest.model.communication.event.EventRegistrationRSDTO

@Transactional
class EventService {

    RestKuorumApiService restKuorumApiService

    EventRegistrationRSDTO addAssistant(String eventUserId, Long campaignId, KuorumUser assistant){
        Map<String, String> params = [
                userId: eventUserId,
                campaignId:campaignId.toString(),
                assistantId:assistant.id.toString()
        ]
        Map<String, String> query = [:]
        def response = restKuorumApiService.put(
                RestKuorumApiService.ApiMethod.ACCOUNT_EVENT_ADD_ASSISTANT,
                params,
                query,
                null,
                new TypeReference<EventRegistrationRSDTO>(){}
        )

        EventRegistrationRSDTO eventRSDTO = null
        if (response.data) {
            eventRSDTO = response.data
        }

        eventRSDTO
    }

    EventRegistrationRSDTO checkIn(Long contactId, Long campaignId, KuorumUser user, String hash){
        Map<String, String> params = [
                userId: user.getId().toString(),
                campaignId:campaignId.toString()
        ]
        Map<String, String> query = [contactId:contactId, hash:hash]
        try {
            def response = restKuorumApiService.put(
                    RestKuorumApiService.ApiMethod.ACCOUNT_EVENT_CHECK_IN,
                    params,
                    query,
                    null,
                    new TypeReference<EventRegistrationRSDTO>() {}
            )

            EventRegistrationRSDTO eventRSDTO = null
            if (response.data) {
                eventRSDTO = response.data
            }
            return eventRSDTO;
        }catch (Exception e){
            log.error("Error checking in the contact ${contactId} on event ${campaignId}")
            return null;
        }
    }

    EventRegistrationRSDTO findAssistant(String eventUserId, Long eventId, KuorumUser assistant){
        Map<String, String> params = [
                userId: eventUserId,
                eventId:eventId.toString(),
                assistantId:assistant.id.toString()
        ]
        Map<String, String> query = [:]
        try {
            def response = restKuorumApiService.get(
                    RestKuorumApiService.ApiMethod.ACCOUNT_EVENT_ADD_ASSISTANT,
                    params,
                    query,
                    new TypeReference<EventRegistrationRSDTO>(){}
            )

            EventRegistrationRSDTO eventRSDTO = null
            if (response.data) {
                eventRSDTO = response.data
            }

            return eventRSDTO
        }catch (Exception e){
            return null;
        }
    }

    EventRSDTO findEvent(KuorumUser user, Long campaignId){
        Map<String, String> params = [
                userId: user.getId().toString(),
                campaignId:campaignId.toString()
        ]
        Map<String, String> query = [:]
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.ACCOUNT_EVENT,
                params,
                query,
                null,
                new TypeReference<EventRSDTO>(){}
        )

        EventRSDTO eventRSDTO = null
        if (response.data) {
            eventRSDTO = response.data
        }

        eventRSDTO
    }

    void sendReport(KuorumUser user, Long campaignId, Boolean checkList = false) {
        Map<String, String> params = [userId: user.id.toString(), campaignId: campaignId.toString()]
        Map<String, String> query = [checkList:checkList]
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.ACCOUNT_EVENT_REPORT,
                params,
                query,
                null
        )
        response
    }

    List<EventRSDTO> findEvents(KuorumUser user){
        Map<String, String> params = [
                userId: user.id.toString()
        ]
        Map<String, String> query = [:]
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.ACCOUNT_EVENTS,
                params,
                query,
                new TypeReference<List<EventRSDTO>>(){}
        )

        List<EventRSDTO> eventRSDTOs = null
        if (response.data) {
            eventRSDTOs = response.data
        }

        eventRSDTOs
    }

}
