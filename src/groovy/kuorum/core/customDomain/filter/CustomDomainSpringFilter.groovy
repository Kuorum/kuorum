package kuorum.core.customDomain.filter

import kuorum.core.customDomain.CustomDomainResolver
import kuorum.domain.DomainService
import org.kuorum.rest.model.domain.DomainConfigRSDTO
import org.springframework.web.filter.GenericFilterBean

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse

class CustomDomainSpringFilter extends GenericFilterBean {

    DomainService domainService

    void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        URL url = new URL(request.getRequestURL().toString());
        if (url.getHost() == "127.0.0.1"){
            // Debug on idea via apache using proxy always is 127.0.0.1
            logger.warn("Develop mode. Using local.kuorum.org")
            url = new URL("http://local.kuorum.org/kuorum")
        }
        CustomDomainResolver.setUrl(url, request.getContextPath())

        String token = domainService.getToken(CustomDomainResolver.domain)
        DomainConfigRSDTO configRSDTO = domainService.getConfig(CustomDomainResolver.domain)
        //TODO IF TOKEN NULL REDIRECT TO NOT FOUND
        CustomDomainResolver.setApiToken(token)
        CustomDomainResolver.setDomainConfigRSDTO(configRSDTO)
        filterChain.doFilter(request, response);

        CustomDomainResolver.clear()
    }

}
