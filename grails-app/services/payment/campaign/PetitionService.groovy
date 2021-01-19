package payment.campaign

import com.fasterxml.jackson.core.type.TypeReference
import grails.transaction.Transactional
import kuorum.core.exception.KuorumException
import kuorum.mail.KuorumMailService
import kuorum.register.KuorumUserSession
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.communication.petition.PagePetitionRSDTO
import org.kuorum.rest.model.communication.petition.PetitionRDTO
import org.kuorum.rest.model.communication.petition.PetitionRSDTO
import org.kuorum.rest.model.kuorumUser.BasicDataKuorumUserRSDTO
import org.kuorum.rest.model.kuorumUser.BasicUserPageRSDTO

@Transactional
class PetitionService implements CampaignCreatorService<PetitionRSDTO, PetitionRDTO>{

    def grailsApplication
    def indexSolrService
    def notificationService
    def fileService
    KuorumMailService kuorumMailService
    RestKuorumApiService restKuorumApiService
    CampaignService campaignService

    PagePetitionRSDTO findAllPetition(Integer page = 0, Integer size = 10){
        Map<String, String> params = [:]
        Map<String, String> query = [page:page.toString(), size:size.toString()]
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.ACCOUNT_PETITIONS_ALL,
                params,
                query,
                new TypeReference<PagePetitionRSDTO>(){}
        )

        response.data

    }
    PetitionRSDTO save(KuorumUserSession user, PetitionRDTO petitionRDTO, Long petitionId){

        PetitionRSDTO petition = null
        if (petitionId) {
            petition= update(user, petitionRDTO, petitionId)
        } else {
            petition= create(user, petitionRDTO)
        }
        indexSolrService.deltaIndex()
        petition
    }

    List<PetitionRSDTO> findAllPetitionsByUser(String userId, String viewerUid = null){
        Map<String, String> params = [userId: userId]
        Map<String, String> query = [:]
        if (viewerUid){
            query.put("viewerUid",viewerUid)
        }
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.ACCOUNT_PETITIONS,
                params,
                query,
                new TypeReference<List<PetitionRSDTO>>(){}
        )

        response.data
    }

    private PetitionRSDTO create(KuorumUserSession user, PetitionRDTO petitionRDTO){
        Map<String, String> params = [userId: user.id.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.post(
                RestKuorumApiService.ApiMethod.ACCOUNT_PETITIONS,
                params,
                query,
                petitionRDTO,
                new TypeReference<PetitionRSDTO>(){}
        )

        PetitionRSDTO petitionSaved = null
        if (response.data) {
            petitionSaved = response.data
        }

        petitionSaved
    }

    List<PetitionRSDTO> findAll(KuorumUserSession user,String viewerUid = null) {
        Map<String, String> params = [userId: user.getId().toString()]
        Map<String, String> query = [:]
        if (viewerUid){
            query.put("viewerUid",viewerUid)
        }
        try {
            def response = restKuorumApiService.get(
                    RestKuorumApiService.ApiMethod.ACCOUNT_PETITIONS,
                    params,
                    query,
                    new TypeReference<List<PetitionRSDTO>>() {}
            )

            return response.data ?: null
        }catch (KuorumException e){
            log.info("Petition not found [Excpt: ${e.message}")
            return null
        }
    }

    PetitionRSDTO find(KuorumUserSession user, Long petitionId, String viewerUid = null){
        find(user.id.toString(), petitionId, viewerUid)
    }
    PetitionRSDTO find(String userId, Long petitionId, String viewerUid = null){
        if (!petitionId){
            return null
        }
        Map<String, String> params = [userId: userId, petitionId: petitionId.toString()]
        Map<String, String> query = [:]
        if (viewerUid){
            query.put("viewerUid",viewerUid)
        }
        try {
            def response = restKuorumApiService.get(
                    RestKuorumApiService.ApiMethod.ACCOUNT_PETITION,
                    params,
                    query,
                    new TypeReference<PetitionRSDTO>() {}
            )

            return response.data ?: null
        }catch (KuorumException e){
            log.info("Petition not found [Excpt: ${e.message}")
            return null
        }
    }

    private PetitionRSDTO update(KuorumUserSession user, PetitionRDTO petitionRDTO, Long petitionId) {
        Map<String, String> params = [userId: user.id.toString(), petitionId: petitionId.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.put(
                RestKuorumApiService.ApiMethod.ACCOUNT_PETITION,
                params,
                query,
                petitionRDTO,
                new TypeReference<PetitionRSDTO>(){}
        )

        PetitionRSDTO petitionSaved = null
        if (response.data) {
            petitionSaved = response.data
        }

        petitionSaved
    }

    BasicUserPageRSDTO listSigns (Long petitionId, Integer page, Integer size){
        Map<String, String> params = [petitionId: petitionId.toString()]
        Map<String, String> query = [page: page.toString(), size:size.toString()]
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.ACCOUNT_PETITION_SIGN,
                params,
                query,
                new TypeReference<BasicUserPageRSDTO>(){}
        )
        return response.data
    }

    PetitionRSDTO signPetition (Long petitionId, KuorumUserSession currentUser, Boolean sign, String petitionUserId){
        Map<String, String> params = [userId: petitionUserId, petitionId: petitionId.toString()]
        Map<String, String> query = [viewerUid: currentUser.id.toString()]
        PetitionRSDTO petitionRSDTO
        if(sign){
            def response = restKuorumApiService.put(
                    RestKuorumApiService.ApiMethod.ACCOUNT_PETITION_SIGN,
                    params,
                    query,
                    null,
                    new TypeReference<PetitionRSDTO>(){}
            )
            petitionRSDTO = response.data
        }
        else {
            def response = restKuorumApiService.delete(
                    RestKuorumApiService.ApiMethod.ACCOUNT_PETITION_SIGN,
                    params,
                    query,
                    new TypeReference<PetitionRSDTO>(){}
            )
            petitionRSDTO = response.data
        }
        return  petitionRSDTO
    }

    void remove(KuorumUserSession user, Long petitionId) {
        Map<String, String> params = [userId: user.id.toString(), petitionId: petitionId.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.delete(
                RestKuorumApiService.ApiMethod.ACCOUNT_PETITION,
                params,
                query
        )

        petitionId
    }

    @Override
    PetitionRDTO map(PetitionRSDTO postRSDTO) {
        return campaignService.basicMapping(postRSDTO, new PetitionRDTO())
    }

    @Override
    def buildView(PetitionRSDTO campaignRSDTO, BasicDataKuorumUserRSDTO campaignOwner, String viewerUid, def params) {
        Integer page = params.page?Integer.parseInt(params.page):0
        Integer size = params.size?Integer.parseInt(params.size):12
        BasicUserPageRSDTO signs = listSigns(campaignRSDTO.id, page, size)
        def model = [petition: campaignRSDTO, petitionUser: campaignOwner, signs: signs.data]
        [view: "/petition/show", model:model]
    }

    @Override
    PetitionRSDTO copy(KuorumUserSession user, Long campaignId) {
        //TODO: KPV-1606
        return null
    }

    @Override
    PetitionRSDTO copy(String userId, Long campaignId) {
        //TODO: KPV-1606
        return null
    }
}
