package payment.campaign

import com.fasterxml.jackson.core.type.TypeReference
import grails.transaction.Transactional
import kuorum.core.exception.KuorumException
import kuorum.mail.KuorumMailService
import kuorum.users.KuorumUser
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.communication.petition.PagePetitionRSDTO
import org.kuorum.rest.model.communication.petition.PetitionRDTO
import org.kuorum.rest.model.communication.petition.PetitionRSDTO

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
    List<PetitionRSDTO> findAllPosts(KuorumUser user, String viewerUid = null){
        Map<String, String> params = [userId: user.id.toString()]
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

    PetitionRSDTO save(KuorumUser user, PetitionRDTO petitionRDTO, Long petitionId){

        PetitionRSDTO petition = null;
        if (petitionId) {
            petition= update(user, petitionRDTO, petitionId)
        } else {
            petition= create(user, petitionRDTO)
        }
        indexSolrService.deltaIndex();
        petition
    }

    PetitionRSDTO create(KuorumUser user, PetitionRDTO petitionRDTO){
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

    PetitionRSDTO find(KuorumUser user, Long petitionId, String viewerUid = null){
        if (!petitionId){
            return null;
        }
        Map<String, String> params = [userId: user.id.toString(), petitionId: petitionId.toString()]
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

            return response.data ?: null;
        }catch (KuorumException e){
            log.info("Petition not found [Excpt: ${e.message}")
            return null;
        }
    }

    PetitionRSDTO update(KuorumUser user, PetitionRDTO petitionRDTO, Long petitionId) {
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
//
//    @PreAuthorize("hasPermission(#postId, 'like')")
    PetitionRSDTO signPetition (Long petitionId, KuorumUser currentUser, Boolean sign, String petitionUserId){
        Map<String, String> params = [userId: petitionUserId, petitionId: petitionId.toString()]
        Map<String, String> query = [viewerUid: currentUser.id.toString()]
        PetitionRSDTO petitionRSDTO;
        if(sign){
            def response = restKuorumApiService.put(
                    RestKuorumApiService.ApiMethod.ACCOUNT_PETITION_SIGN,
                    params,
                    query,
                    null,
                    new TypeReference<PetitionRSDTO>(){}
            );
            petitionRSDTO = response.data
        }
        else {
            def response = restKuorumApiService.delete(
                    RestKuorumApiService.ApiMethod.ACCOUNT_PETITION_SIGN,
                    params,
                    query,
                    new TypeReference<PetitionRSDTO>(){}
            );
            petitionRSDTO = response.data
        }
        return  petitionRSDTO;
    }

    void remove(KuorumUser user, Long petitionId) {
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
        return campaignService.basicMapping(postRSDTO, new PetitionRDTO());
    }

    @Override
    def buildView(PetitionRSDTO campaignRSDTO, KuorumUser campaignOwner, String viewerUid, def params) {
        def model = [petition: campaignRSDTO, petitionUser: campaignOwner]
        [view: "/petition/show", model:model]
    }
}