package kuorum.domain

import com.fasterxml.jackson.core.type.TypeReference
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.files.LessCompilerService
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.admin.AdminConfigMailingRDTO
import org.kuorum.rest.model.domain.*
import org.kuorum.rest.model.domain.creation.NewDomainPaymentDataRDTO
import org.kuorum.rest.model.payment.BillingAmountUsersRangeDTO
import org.kuorum.rest.model.payment.KuorumPaymentPlanDTO
import org.springframework.beans.factory.annotation.Value

class DomainService {

    RestKuorumApiService restKuorumApiService

    LessCompilerService lessCompilerService

    @Value('${kuorum.rest.apiKey}')
    String kuorumAdminRestApiKey

    DomainRSDTO getConfig(String domain){
        Map<String, String> params = [:]
        Map<String, String> query = [domainName:domain]

        try{
            def apiResponse= restKuorumApiService.get(
                    RestKuorumApiService.ApiMethod.DOMAIN_CONFIG,
                    params,
                    query,
                    new TypeReference<DomainRSDTO>(){},
                    kuorumAdminRestApiKey)
            DomainRSDTO config
            if (apiResponse.data){
                config = apiResponse.data
            }
            return config
        }catch (Exception e){
            log.warn("Domain not found: ${domain}")
            return null
        }
    }

    DomainRSDTO updateConfig(DomainRSDTO domainRSDTO ){
        def valid = DomainRDTO.getDeclaredFields().grep {  !it.synthetic }.collect{it.name}
        DomainRDTO domainRDTO = new DomainRDTO(domainRSDTO.properties.findAll{valid.contains(it.key)})
        return updateConfigSettingDomain(domainRDTO, domainRSDTO.domain)
    }
    DomainRSDTO updateConfig(DomainRDTO domainRDTO ){
        return updateConfigSettingDomain(domainRDTO,CustomDomainResolver.domain)
    }
    private DomainRSDTO updateConfigSettingDomain(DomainRDTO domainRDTO, String domain){
        domainRDTO.domain = domain
        Map<String, String> params = [:]
        Map<String, String> query = [:]

        try{
            def apiResponse= restKuorumApiService.put(
                    RestKuorumApiService.ApiMethod.DOMAIN,
                    params,
                    query,
                    domainRDTO,
                    new TypeReference<DomainRSDTO>(){},
                    kuorumAdminRestApiKey)
            DomainRSDTO domainRSDTO
            if (apiResponse.data){
                domainRSDTO = apiResponse.data
            }
            lessCompilerService.compileCssForDomain(domainRSDTO)
            return domainRSDTO
        }catch (Exception e){
            log.warn("Error updating config. [Excp: ${e.getMessage()}]")
            return null
        }
    }

    List<DomainRSDTO> findAllDomains(){
        Map<String, String> params = [:]
        Map<String, String> query = [:]

        try{
            def apiResponse= restKuorumApiService.get(
                    RestKuorumApiService.ApiMethod.DOMAIN,
                    params,
                    query,
                    new TypeReference<List<DomainRSDTO>>(){},
                    kuorumAdminRestApiKey)
            return apiResponse.data
        }catch (Exception e){
            log.warn("Error recovering domains", e)
            return null
        }
    }

    DomainLegalInfoRSDTO getLegalInfo(String domain){
        Map<String, String> params = [:]
        Map<String, String> query = [domainName:domain]

        try{
            def apiResponse= restKuorumApiService.get(
                    RestKuorumApiService.ApiMethod.DOMAIN_LEGAL,
                    params,
                    query,
                    new TypeReference<DomainLegalInfoRSDTO>(){},
                    kuorumAdminRestApiKey)
            return apiResponse.data
        }catch (Exception e){
            log.warn("Domain not found: ${domain}")
            return null
        }
    }

    DomainLegalInfoRSDTO updateLegalInfo (DomainLegalInfoRDTO domainLegalInfoRDTO ) {

        String domain = CustomDomainResolver.domain
        Map<String, String> params = [:]
        Map<String, String> query = [domainName:domain]

        try {
            def apiResponse = restKuorumApiService.put(
                    RestKuorumApiService.ApiMethod.DOMAIN_LEGAL,
                    params,
                    query,
                    domainLegalInfoRDTO,
                    new TypeReference<DomainLegalInfoRSDTO>() {},
                    kuorumAdminRestApiKey)
            DomainLegalInfoRSDTO domainLegalInfoRSDTO
            if (apiResponse.data){
                domainLegalInfoRSDTO = apiResponse.data
            }
            return domainLegalInfoRSDTO
        } catch (Exception e) {
            log.warn("Error updating config. [Excp: ${e.getMessage()}")
        }
    }


    void updateNewsletterConfig(AdminConfigMailingRDTO adminRDTO){
        Map<String, String> params = [:]
        Map<String, String> query = [:]
        def response= restKuorumApiService.put(
                RestKuorumApiService.ApiMethod.DOMAIN_MAIL_CONFIG,
                params,
                query,
                adminRDTO,
                null,
                kuorumAdminRestApiKey
        )
    }



    DomainPaymentInfoRSDTO getPaymentInfo(){
        String domain = CustomDomainResolver.domain
        Map<String, String> params = [:]
        Map<String, String> query = [domainName:domain]

        try{
            def apiResponse= restKuorumApiService.get(
                    RestKuorumApiService.ApiMethod.DOMAIN_PAYMENT,
                    params,
                    query,
                    new TypeReference<DomainPaymentInfoRSDTO>(){},
                    kuorumAdminRestApiKey)
            return apiResponse.data
        }catch (Exception e){
            log.warn("Domain not found: ${domain}")
            return null
        }
    }

    DomainPaymentInfoRSDTO updatePaymentInfo (NewDomainPaymentDataRDTO domainPaymentInfoRDTO) {

        String domain = CustomDomainResolver.domain
        Map<String, String> params = [:]
        Map<String, String> query = [domainName:domain]

        def apiResponse = restKuorumApiService.put(
                RestKuorumApiService.ApiMethod.DOMAIN_PAYMENT,
                params,
                query,
                domainPaymentInfoRDTO,
                new TypeReference<DomainPaymentInfoRSDTO>() {},
                kuorumAdminRestApiKey)
        DomainPaymentInfoRSDTO domainPaymentInfo
        if (apiResponse.data){
            domainPaymentInfo = apiResponse.data
        }
        return domainPaymentInfo
    }

    List<KuorumPaymentPlanDTO> getPlans(BillingAmountUsersRangeDTO usersRangeDTO){
        Map<String, String> params = [:]
        Map<String, String> query = [billingAmountUsersRange:usersRangeDTO, billingType: usersRangeDTO.getBillingType()]
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.CUSTOMER_PLANS,
                params,
                query,
                new TypeReference<List<KuorumPaymentPlanDTO>>(){}
        )
        response.data
    }

}
