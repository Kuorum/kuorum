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

class DomainService {

    RestKuorumApiService restKuorumApiService

    LessCompilerService lessCompilerService

    DomainRSDTO getConfig(String domain){
        Map<String, String> params = [domainName:domain]
        Map<String, String> query = [:]

        try{
            def apiResponse= restKuorumApiService.get(
                    RestKuorumApiService.ApiMethod.DOMAIN_CONFIG,
                    params,
                    query,
                    new TypeReference<DomainRSDTO>(){})
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
        Map<String, String> params = [domainName: domain]
        Map<String, String> query = [:]

        try{
            def apiResponse= restKuorumApiService.put(
                    RestKuorumApiService.ApiMethod.DOMAIN,
                    params,
                    query,
                    domainRDTO,
                    new TypeReference<DomainRSDTO>(){})
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
                    RestKuorumApiService.ApiMethod.DOMAINS,
                    params,
                    query,
                    new TypeReference<List<DomainRSDTO>>(){},
                    CustomDomainResolver.getAdminApiToken())
            return apiResponse.data
        }catch (Exception e){
            log.warn("Error recovering domains", e)
            return null
        }
    }

    DomainLegalInfoRSDTO getLegalInfo(String domain){
        Map<String, String> params = [domainName:domain]
        Map<String, String> query = [:]

        try{
            def apiResponse= restKuorumApiService.get(
                    RestKuorumApiService.ApiMethod.DOMAIN_LEGAL,
                    params,
                    query,
                    new TypeReference<DomainLegalInfoRSDTO>(){})
            return apiResponse.data
        }catch (Exception e){
            log.warn("Domain not found: ${domain}")
            return null
        }
    }

    DomainLegalInfoRSDTO updateLegalInfo (DomainLegalInfoRDTO domainLegalInfoRDTO ) {

        String domain = CustomDomainResolver.domain
        Map<String, String> params = [domainName:domain]
        Map<String, String> query = [:]

        try {
            def apiResponse = restKuorumApiService.put(
                    RestKuorumApiService.ApiMethod.DOMAIN_LEGAL,
                    params,
                    query,
                    domainLegalInfoRDTO,
                    new TypeReference<DomainLegalInfoRSDTO>() {})
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
        Map<String, String> params = [domainName:adminRDTO.domainName]
        Map<String, String> query = [:]
        def response= restKuorumApiService.put(
                RestKuorumApiService.ApiMethod.DOMAIN_MAIL_CONFIG,
                params,
                query,
                adminRDTO,
                null
        )
    }



    DomainPaymentInfoRSDTO getPaymentInfo(){
        String domain = CustomDomainResolver.domain
        Map<String, String> params = [domainName:domain]
        Map<String, String> query = [:]

        try{
            def apiResponse= restKuorumApiService.get(
                    RestKuorumApiService.ApiMethod.DOMAIN_PAYMENT,
                    params,
                    query,
                    new TypeReference<DomainPaymentInfoRSDTO>(){})
            return apiResponse.data
        }catch (Exception e){
            log.warn("Domain not found: ${domain}")
            return null
        }
    }

    DomainPaymentInfoRSDTO updatePaymentInfo (NewDomainPaymentDataRDTO domainPaymentInfoRDTO) {

        String domain = CustomDomainResolver.domain
        Map<String, String> params = [domainName:domain]
        Map<String, String> query = [:]

        def apiResponse = restKuorumApiService.put(
                RestKuorumApiService.ApiMethod.DOMAIN_PAYMENT,
                params,
                query,
                domainPaymentInfoRDTO,
                new TypeReference<DomainPaymentInfoRSDTO>() {})
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

    void removeDomain(String domain){
        Map<String, String> params = [domainName:domain]
        Map<String, String> query = [:]

        try{
            def apiResponse= restKuorumApiService.delete(
                    RestKuorumApiService.ApiMethod.DOMAIN,
                    params,
                    query)
        }catch (Exception e){
            log.warn("It was not possible to delete domain : ${domain}")
            throw e;
        }
    }

}
