package kuorum.core.customDomain.filter

import com.fasterxml.jackson.core.type.TypeReference
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.util.rest.RestKuorumApiService
import org.springframework.web.filter.GenericFilterBean

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse

class CustomDomainSpringFilter extends GenericFilterBean {

    RestKuorumApiService restKuorumApiService

    void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        URL url = new URL(request.getRequestURL().toString());
        if (url.getHost() == "127.0.0.1"){
            // Debug on idea via apache using proxy always is 127.0.0.1
            url = new URL("http://local2.kuorum.org/kuorum")
        }
        CustomDomainResolver.setUrl(url, request.getContextPath())
        Map<String, String> params = [:]
        Map<String, String> query = [domainName:CustomDomainResolver.domain]

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
            CustomDomainResolver.setApiToken(token)
        }catch (Exception e){
//            log.warn("Domain not found: ${CustomDomainResolver.domain}")
//            redirect(controller:'error',action:'notFound')
        }
        filterChain.doFilter(request, response);

        CustomDomainResolver.clear()
    }

}
