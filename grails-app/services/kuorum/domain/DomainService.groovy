package kuorum.domain

import com.fasterxml.jackson.core.type.TypeReference
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.files.LessCompilerService
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.domain.DomainRDTO
import org.kuorum.rest.model.domain.DomainRSDTO

class DomainService {

    RestKuorumApiService restKuorumApiService;

    LessCompilerService lessCompilerService

    DomainRSDTO getConfig(String domain){
        Map<String, String> params = [:]
        Map<String, String> query = [domainName:domain]

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
            return config;
        }catch (Exception e){
            log.warn("Domain not found: ${domain}")
            return null;
        }
    }

    DomainRSDTO updateConfig(DomainRDTO domainRDTO ){
        domainRDTO.domain = CustomDomainResolver.domain
        Map<String, String> params = [:]
        Map<String, String> query = [:]

        try{
            def apiResponse= restKuorumApiService.put(
                    RestKuorumApiService.ApiMethod.DOMAIN,
                    params,
                    query,
                    domainRDTO,
                    new TypeReference<DomainRSDTO>(){})
            DomainRSDTO domain
            if (apiResponse.data){
                domain = apiResponse.data
            }
            lessCompilerService.compileCssForDomain(domain)
            return domain;
        }catch (Exception e){
            log.warn("Error updating config. [Excp: ${e.getMessage()}")
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
                    new TypeReference<List<DomainRSDTO>>(){})
            return apiResponse.data;
        }catch (Exception e){
            log.warn("Error recovering domains")
            return null;
        }
    }

}
