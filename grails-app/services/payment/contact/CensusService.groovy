package payment.contact

import com.fasterxml.jackson.core.type.TypeReference
import grails.transaction.Transactional
import kuorum.register.KuorumUserSession
import kuorum.util.rest.RestKuorumApiService
import kuorum.web.commands.payment.contact.ContactFilterCommand
import org.kuorum.rest.model.contact.*
import org.kuorum.rest.model.contact.filter.ExtendedFilterRSDTO
import org.kuorum.rest.model.contact.filter.FilterRDTO
import org.kuorum.rest.model.kuorumUser.BasicDataKuorumUserRSDTO
import org.kuorum.rest.model.kuorumUser.KuorumUserRSDTO

@Transactional
class CensusService {

    RestKuorumApiService restKuorumApiService

    ContactRSDTO getContactByCensusCode(String censusCode){
        Map<String, String> params = [censusCode:censusCode]
        Map<String, String> query = [:]

        try{
            def response= restKuorumApiService.get(
                    RestKuorumApiService.ApiMethod.CENSUS_LOGIN,
                    params,
                    query,
                    new TypeReference<ContactRSDTO>(){})
            ContactRSDTO contactRSDTO =null
            if (response.data){
                contactRSDTO = response.data
            }
            contactRSDTO
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
        Map<String, String> params = [censusCode:censusCode]
        Map<String, String> query = [:]

        restKuorumApiService.delete(
                RestKuorumApiService.ApiMethod.CENSUS_LOGIN,
                params,
                query,
                new TypeReference<String>(){})
    }
}
