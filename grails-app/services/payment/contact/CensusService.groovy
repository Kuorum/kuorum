package payment.contact

import com.fasterxml.jackson.core.type.TypeReference
import grails.transaction.Transactional
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.CensusLoginRDTO
import org.kuorum.rest.model.kuorumUser.KuorumUserRSDTO

@Transactional
class CensusService {

    RestKuorumApiService restKuorumApiService

    CensusLoginRDTO getContactByCensusCode(String censusCode){
        Map<String, String> params = [censusCode:censusCode]
        Map<String, String> query = [:]

        try{
            def response= restKuorumApiService.get(
                    RestKuorumApiService.ApiMethod.CENSUS_LOGIN,
                    params,
                    query,
                    new TypeReference<CensusLoginRDTO>(){})
            CensusLoginRDTO censusLoginRDTO =null
            if (response.data){
                censusLoginRDTO = response.data
            }
            censusLoginRDTO
        }catch(Exception e){
            log.info("Searching contact with census code ${censusCode} not found")
            return null;
        }
    }


    KuorumUserRSDTO createUserByCensusCode(String censusCode){
        Map<String, String> params = [censusCode:censusCode]
        Map<String, String> query = [:]

        def response= restKuorumApiService.post(
                RestKuorumApiService.ApiMethod.CENSUS_LOGIN,
                params,
                query,
                params,
                new TypeReference<KuorumUserRSDTO>(){})
        KuorumUserRSDTO kuorumUserRSDTO =null
        if (response.data){
            kuorumUserRSDTO = response.data
        }
        kuorumUserRSDTO
    }

    void deleteCensusCode(String censusCode){
        if (censusCode){
            Map<String, String> params = [censusCode:censusCode]
            Map<String, String> query = [:]
            restKuorumApiService.delete(
                    RestKuorumApiService.ApiMethod.CENSUS_LOGIN,
                    params,
                    query,
                    new TypeReference<String>(){})
        }
    }
}
