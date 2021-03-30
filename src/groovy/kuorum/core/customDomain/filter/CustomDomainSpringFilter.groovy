package kuorum.core.customDomain.filter

import kuorum.core.customDomain.CustomDomainResolver
import kuorum.domain.DomainService
import org.kuorum.rest.model.domain.DomainRSDTO
import org.springframework.web.filter.GenericFilterBean

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse

class CustomDomainSpringFilter extends GenericFilterBean {

    DomainService domainService

    void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        URL url = new URL(request.getRequestURL().toString())
        if (url.getHost() == "127.0.0.1"){
            // Debug on idea via apache using proxy always is 127.0.0.1
            url = new URL("https://local.kuorum.org/kuorum")
            logger.info("Develop mode. Using ${url.toString()}")
        }
        DomainRSDTO configRSDTO = null;
        try{
            CustomDomainResolver.setUrl(url, request.getContextPath())
            configRSDTO = domainService.getConfig(CustomDomainResolver.domain)
        }catch (Exception e){
            logger.info("Domain ${url.getHost()} not found due to an exception")
            logger.debug("Domain ${url.getHost()} not found due to an exception", e)
        }
        if (!configRSDTO){
            int errorCode = 402
            logger.info("Domain not found: ${request.getContextPath()}. Sending ${errorCode} code")
            response.sendError(errorCode)
        }else{
            CustomDomainResolver.setDomainRSDTO(configRSDTO)
            filterChain.doFilter(request, response)
            CustomDomainResolver.clear()
        }
    }

}
