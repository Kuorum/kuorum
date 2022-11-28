package payment.contact

import com.fasterxml.jackson.core.type.TypeReference
import grails.transaction.Transactional
import kuorum.security.evidences.Evidences
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.CensusLoginRDTO
import org.kuorum.rest.model.contact.ContactRSDTO
import org.kuorum.rest.model.kuorumUser.KuorumUserRSDTO
import org.kuorum.rest.model.kuorumUser.domainValidation.CensusTokenValidationDTO
import org.kuorum.rest.model.kuorumUser.domainValidation.ExternalIdValidationDTO

@Transactional
class CensusService {

    RestKuorumApiService restKuorumApiService

    CensusLoginRDTO getContactByCensusCode(String censusCode) {
        Map<String, String> params = [censusCode: censusCode]
        Map<String, String> query = [:]

        try {
            def response = restKuorumApiService.get(
                    RestKuorumApiService.ApiMethod.CENSUS_LOGIN,
                    params,
                    query,
                    new TypeReference<CensusLoginRDTO>() {})
            CensusLoginRDTO censusLoginRDTO = null
            if (response.data) {
                censusLoginRDTO = response.data
            }
            censusLoginRDTO
        } catch (Exception e) {
            log.info("Searching contact with census code ${censusCode} not found")
            return null;
        }
    }

    KuorumUserRSDTO createUserByExternalId(ContactRSDTO contact, Evidences evidences, String ownerId, Long campaignId) {
        ExternalIdValidationDTO externalIdValidationDTO = new ExternalIdValidationDTO(externalId: contact.externalId, ownerId: ownerId, campaignId: campaignId, ip: evidences.getIp(), browserType: evidences.getBrowser())
        def response = restKuorumApiService.post(
                RestKuorumApiService.ApiMethod.EXTERNAL_ID_LOGIN_POST,
                [:],
                [:],
                externalIdValidationDTO,
                new TypeReference<KuorumUserRSDTO>() {})
        response.data ?: null
    }

    KuorumUserRSDTO createUserByCensusCode(String censusCode, Evidences evidences) {
        Map<String, String> params = [censusCode: censusCode]
        Map<String, String> query = [:]
        CensusTokenValidationDTO censusTokenValidationDTO = new CensusTokenValidationDTO(censusCode: censusCode, ip: evidences.getIp(), browserType: evidences.getBrowser())
        def response = restKuorumApiService.post(
                RestKuorumApiService.ApiMethod.CENSUS_LOGIN_POST,
                params,
                query,
                censusTokenValidationDTO,
                new TypeReference<KuorumUserRSDTO>() {})
        response.data ?: null
    }

    void deleteCensusCode(String censusCode) {
        if (censusCode) {
            Map<String, String> params = [censusCode: censusCode]
            Map<String, String> query = [:]
            restKuorumApiService.delete(
                    RestKuorumApiService.ApiMethod.CENSUS_LOGIN,
                    params,
                    query,
                    new TypeReference<String>() {})
        }
    }
}
