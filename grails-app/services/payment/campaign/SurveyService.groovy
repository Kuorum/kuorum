package payment.campaign

import com.fasterxml.jackson.core.type.TypeReference
import grails.transaction.Transactional
import kuorum.core.exception.KuorumException
import kuorum.mail.KuorumMailService
import kuorum.users.KuorumUser
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.communication.event.EventRDTO
import org.kuorum.rest.model.communication.survey.SurveyRDTO
import org.kuorum.rest.model.communication.survey.SurveyRSDTO

@Transactional
class SurveyService implements CampaignCreatorService<SurveyRSDTO, SurveyRDTO>{

    def grailsApplication
    def indexSolrService
    def notificationService
    def fileService
    KuorumMailService kuorumMailService
    RestKuorumApiService restKuorumApiService

    SurveyRSDTO save(KuorumUser user, SurveyRDTO surveyRDTO, Long surveyId){

        SurveyRSDTO survey = null;
        if (surveyId) {
            survey= update(user, surveyRDTO, surveyId)
        } else {
            survey= createSurvey(user, surveyRDTO)
        }
        indexSolrService.deltaIndex();
        survey
    }

    SurveyRSDTO createSurvey(KuorumUser user, SurveyRDTO surveyRDTO){
        Map<String, String> params = [userAlias: user.id.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.post(
                RestKuorumApiService.ApiMethod.ACCOUNT_SURVEYS,
                params,
                query,
                surveyRDTO,
                new TypeReference<SurveyRSDTO>(){}
        )

        SurveyRSDTO surveySaved = null
        if (response.data) {
            surveySaved = response.data
        }

        surveySaved
    }

    SurveyRSDTO find(KuorumUser user, Long surveyId, String viewerUid = null){
        if (!surveyId){
            return null;
        }
        Map<String, String> params = [userAlias: user.id.toString(), surveyId: surveyId.toString()]
        Map<String, String> query = [:]
        if (viewerUid){
            query.put("viewerUid",viewerUid)
        }
        try {
            def response = restKuorumApiService.get(
                    RestKuorumApiService.ApiMethod.ACCOUNT_SURVEY,
                    params,
                    query,
                    new TypeReference<SurveyRSDTO>() {}
            )

            return response.data ?: null;
        }catch (KuorumException e){
            log.info("Survey not found [Excpt: ${e.message}")
            return null;
        }
    }

    SurveyRSDTO update(KuorumUser user, SurveyRDTO surveyRDTO, Long surveyId) {
        Map<String, String> params = [userAlias: user.id.toString(), surveyId: surveyId.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.put(
                RestKuorumApiService.ApiMethod.ACCOUNT_SURVEY,
                params,
                query,
                surveyRDTO,
                new TypeReference<SurveyRSDTO>(){}
        )

        SurveyRSDTO surveySaved = null
        if (response.data) {
            surveySaved = response.data
        }
        surveySaved
    }

    void remove(KuorumUser user, Long surveyId) {
        Map<String, String> params = [userAlias: user.id.toString(), surveyId: surveyId.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.delete(
                RestKuorumApiService.ApiMethod.ACCOUNT_SURVEY,
                params,
                query
        )
        surveyId
    }

    @Override
    SurveyRDTO map(SurveyRSDTO surveyRSDTO) {
        SurveyRDTO surveyRDTO = new SurveyRDTO()
        if(surveyRSDTO){
            surveyRDTO.title = surveyRSDTO.title
            surveyRDTO.body = surveyRSDTO.body
            surveyRDTO.photoUrl = surveyRSDTO.photoUrl
            surveyRDTO.videoUrl = surveyRSDTO.videoUrl
            surveyRDTO.publishOn = surveyRSDTO.datePublished
            surveyRDTO.name = surveyRSDTO.name
            surveyRDTO.triggeredTags = surveyRSDTO.triggeredTags
            surveyRDTO.anonymousFilter = surveyRSDTO.anonymousFilter
            surveyRDTO.filterId = surveyRSDTO.newsletter?.filter?.id
            surveyRDTO.causes = surveyRSDTO.causes
            if (surveyRSDTO.event){
                surveyRDTO.event = new EventRDTO();
                surveyRDTO.event.eventDate = surveyRSDTO.event.eventDate
                surveyRDTO.event.latitude = surveyRSDTO.event.latitude
                surveyRDTO.event.longitude = surveyRSDTO.event.longitude
                surveyRDTO.event.zoom = surveyRSDTO.event.zoom
                surveyRDTO.event.localName = surveyRSDTO.event.localName
                surveyRDTO.event.address = surveyRSDTO.event.address
                surveyRDTO.event.capacity = surveyRSDTO.event.capacity
            }
            //MAP QUESTIONS
        }
        return surveyRDTO;
    }

    @Override
    def buildView(SurveyRSDTO campaignRSDTO, KuorumUser campaignOwner, String viewerUid, def params) {
        def model = [survey: campaignRSDTO, surveyUser: campaignOwner]
        [view: "/survey/show", model:model]
    }
}
