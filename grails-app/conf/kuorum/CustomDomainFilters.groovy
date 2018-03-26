package kuorum

import com.fasterxml.jackson.core.type.TypeReference
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.files.LessCompilerService
import kuorum.util.rest.RestKuorumApiService

class CustomDomainFilters {

    RestKuorumApiService restKuorumApiService;
    LessCompilerService lessCompilerService

    def filters = {
        all(controller:'error', invert: true) {
            before = {
                URL url = new URL(request.getRequestURL().toString());
                if (url.getHost() == "127.0.0.1"){
                    // Debug on idea via apache using proxy always is 127.0.0.1
                    url = new URL("http://local2.kuorum.org/kuorum")
                }
                CustomDomainResolver.setUrl(url, request.getContextPath())
                Map<String, String> params = [:]
                Map<String, String> query = [domainName:CustomDomainResolver.domain]

                try{
                    def response= restKuorumApiService.get(
                            RestKuorumApiService.ApiMethod.DOMAIN_TOKEN,
                            params,
                            query,
                            new TypeReference<String>(){})
                    String token
                    if (response.data){
                        token = org.apache.commons.io.IOUtils.toString(response.data)
                    }
                    CustomDomainResolver.setApiToken(token)
                }catch (Exception e){
                    log.warn("Domain not found: ${CustomDomainResolver.domain}")
                    redirect(controller:'error',action:'notFound')
                }


            }
            after = { Map model ->

            }
            afterView = { Exception e ->
//                CustomDomainResolver.clear()
            }
        }
    }
}
