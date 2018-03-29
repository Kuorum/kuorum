package kuorum.domain

import com.fasterxml.jackson.core.type.TypeReference
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.domain.DomainConfigRSDTO

class DomainService {

    RestKuorumApiService restKuorumApiService;

    String getToken(String domain){
        Map<String, String> params = [:]
        Map<String, String> query = [domainName:domain]
        try{
            def apiResponse= restKuorumApiService.get(
                    RestKuorumApiService.ApiMethod.DOMAIN_TOKEN,
                    params,
                    query,
                    new TypeReference<String>(){})
            String token
            if (apiResponse.data){
                token = org.apache.commons.io.IOUtils.toString(apiResponse.data)
            }
            return token;
        }catch (Exception e){
            log.warn("Domain not found: ${domain}")
            return null;
        }
    }

    DomainConfigRSDTO getConfig(String domain){
        Map<String, String> params = [:]
        Map<String, String> query = [domainName:domain]

        try{
            def apiResponse= restKuorumApiService.get(
                    RestKuorumApiService.ApiMethod.DOMAIN_CONFIG,
                    params,
                    query,
                    new TypeReference<DomainConfigRSDTO>(){})
            DomainConfigRSDTO config
            if (apiResponse.data){
                config = apiResponse.data
            }
            return config;
        }catch (Exception e){
            log.warn("Domain not found: ${domain}")
            return null;
        }
    }

    List<String> findAllDomains(){
        Map<String, String> params = [:]
        Map<String, String> query = [:]

        try{
            def apiResponse= restKuorumApiService.get(
                    RestKuorumApiService.ApiMethod.DOMAIN,
                    params,
                    query,
                    new TypeReference<List<String>>(){})
            return apiResponse.data;
        }catch (Exception e){
            log.warn("Error recovering domains")
            return null;
        }
    }

}
