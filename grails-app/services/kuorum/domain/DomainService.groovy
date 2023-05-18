package kuorum.domain

import com.fasterxml.jackson.core.type.TypeReference
import grails.async.Promise
import grails.plugin.springsecurity.SpringSecurityService
import grails.util.Environment
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.files.LessCompilerService
import kuorum.register.KuorumUserSession
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.admin.AdminConfigMailingRDTO
import org.kuorum.rest.model.domain.*
import org.kuorum.rest.model.domain.creation.NewDomainAdminUserDataRDTO
import org.kuorum.rest.model.domain.creation.NewDomainDataRDTO
import org.kuorum.rest.model.domain.creation.NewDomainDataRSDTO
import org.kuorum.rest.model.domain.creation.NewDomainPaymentDataRDTO
import org.kuorum.rest.model.payment.BillingAmountUsersRangeDTO
import org.kuorum.rest.model.payment.KuorumPaymentPlanDTO
import org.slf4j.MDC

class DomainService {

    RestKuorumApiService restKuorumApiService

    LessCompilerService lessCompilerService

    SpringSecurityService springSecurityService;

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
            log.warn("Domain not found: ${domain}: Excp: ${e.getMessage()}")
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
            log.warn("Error updating config. [Excp: ${e.getMessage()}]", e)
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

    void updateAllDomainCss(Boolean waitUntilFinish = false){
        URL url = new URL("https://kuorum.org/kuorum")
        CustomDomainResolver.setUrl(url, "")
        List<DomainRSDTO> domains = this.findAllDomains()
        List<Promise> asyncUpdateConfig = []
        domains.each { domainRSDTO ->
            asyncUpdateConfig << grails.async.Promises.task {
                URL urlThread = new URL("https://${domainRSDTO.domain}/kuorum")
                CustomDomainResolver.setUrl(urlThread, "")
                if (domainRSDTO.domain == "kuorum.org"){
                    log.debug("Ingoring domain configuration of kuorum.org")
                }else if (domainRSDTO.version != 0){
                    MDC.put("domain", domainRSDTO.domain)
                    updateConfig(domainRSDTO)
                    MDC.clear()
                }else{
                    log.info("Ingoring domain cofiguration because its version is 0 and it was not configured")
                }
                // Used to update the version number and force browsers to download again the css and uploads the new css
                // lessCompilerService.compileCssForDomain(it)
            }
        }
        if (waitUntilFinish) {
            grails.async.Promises.waitAll(asyncUpdateConfig)
        }
        CustomDomainResolver.clear()
    }


    Boolean showPrivateContent(Boolean checkLogged = true) {
        CustomDomainResolver.domainRSDTO.domainPrivacy == DomainPrivacyRDTO.PUBLIC || (checkLogged && springSecurityService.isLoggedIn())
    }

    Boolean isSurveyPlatform() {
        return CustomDomainResolver.domainRSDTO.getDomainTypeRSDTO() == DomainTypeRSDTO.SURVEY
    }

    NewDomainDataRSDTO createNewDomain(String prefixDomain) {
        String salesAdminApiToken = CustomDomainResolver.getSalesAdminApiToken()

        Map<String, String> params = [:]
        Map<String, String> query = [:]
        NewDomainDataRDTO newDomainDataRDTO = buildDomainDataRDTO(prefixDomain)
        try {
            def apiResponse = restKuorumApiService.post(
                    RestKuorumApiService.ApiMethod.DOMAINS,
                    params,
                    query,
                    newDomainDataRDTO,
                    new TypeReference<NewDomainDataRSDTO>() {},
                    salesAdminApiToken)
            return apiResponse.data
        } catch (Exception e) {
            log.warn("Error recovering domains", e)
            return null
        }
    }

    private NewDomainDataRDTO buildDomainDataRDTO(String prefixDomain) {
        String prefixDomainNormalized = prefixDomain.replaceAll("[^\\p{Alpha}\\p{Digit}]+", "")
        String baseDomain = "kuorum.org"
        NewDomainDataRDTO newDomainDataRDTO = new NewDomainDataRDTO();
        DomainRDTO domainRDTO = new DomainRDTO()
        domainRDTO.domain = "${prefixDomainNormalized}.${baseDomain}"
        domainRDTO.name = prefixDomainNormalized
        domainRDTO.domainPrivacy = DomainPrivacyRDTO.PRIVATE
        domainRDTO.mainColor = "#171a33"
        domainRDTO.platformType = PlatformTypeDTO.ELECTION
        domainRDTO.slogan = "Slogan ${prefixDomainNormalized}"
//        domainRDTO.subtitle

        NewDomainAdminUserDataRDTO adminUserData = new NewDomainAdminUserDataRDTO()
        adminUserData.email = 'soporte+admin@kuorum.org'
        adminUserData.name = prefixDomainNormalized
        adminUserData.password = 'SUPER_ADMIN_KUORUM' + Math.random()

        NewDomainPaymentDataRDTO domainPaymentInfoRSDTO = new NewDomainPaymentDataRDTO()
        domainPaymentInfoRSDTO.billingAmountUsersRange = BillingAmountUsersRangeDTO.SURVEY_BLOCK_50
        domainPaymentInfoRSDTO.billingCycle = BillingCycleRDTO.YEARLY
        domainPaymentInfoRSDTO.domainPlan = DomainPlanRSDTO.SURVEYS_FREE
        domainPaymentInfoRSDTO.customerId = null // THINK THIS ID

        newDomainDataRDTO.setAdminUserData(adminUserData)
        newDomainDataRDTO.setDomainData(domainRDTO)
        newDomainDataRDTO.setPaymentData(domainPaymentInfoRSDTO)
        return newDomainDataRDTO;
    }
}
