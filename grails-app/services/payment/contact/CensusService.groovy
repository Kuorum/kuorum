package payment.contact

import com.fasterxml.jackson.core.type.TypeReference
import grails.transaction.Transactional
import kuorum.security.evidences.Evidences
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.CensusLoginRDTO
import org.kuorum.rest.model.kuorumUser.KuorumUserRSDTO
import org.kuorum.rest.model.kuorumUser.domainValidation.CensusTokenValidationDTO

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


    KuorumUserRSDTO createUserByCensusCode(String censusCode, Evidences evidences){
        Map<String, String> params = [censusCode:censusCode]
        Map<String, String> query = [:]
        CensusTokenValidationDTO censusTokenValidationDTO = new CensusTokenValidationDTO(censusCode: censusCode, ip:evidences.getIp(), browserType: evidences.getBrowser())
        def response= restKuorumApiService.post(
                RestKuorumApiService.ApiMethod.CENSUS_LOGIN_POST,
                params,
                query,
                censusTokenValidationDTO,
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
