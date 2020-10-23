package kuorum.core.customDomain.filter

import grails.plugin.springsecurity.SpringSecurityService
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.register.KuorumUserSession
import org.slf4j.MDC
import org.springframework.web.filter.GenericFilterBean

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse

class KuorumLogSpringFilter extends GenericFilterBean {


    private static final String MDC_KEY_DOMAIN="domain";
    private static final String MDC_KEY_DOMAIN_USER="userEmail";
    private static final String MDC_KEY_DOMAIN_ANONYMOUS="Anonymous";

    SpringSecurityService springSecurityService

    void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        URL url = new URL(request.getRequestURL().toString())
        MDC.put(MDC_KEY_DOMAIN, CustomDomainResolver.getDomain());
        if (springSecurityService.isLoggedIn()){
            KuorumUserSession userSession = springSecurityService.getPrincipal();
            MDC.put(MDC_KEY_DOMAIN_USER, userSession.getEmail());
        }else{
            MDC.put(MDC_KEY_DOMAIN_USER, MDC_KEY_DOMAIN_ANONYMOUS);
        }
        logger.info("Requested: ${url}")
        try{
            filterChain.doFilter(request, response);
        }finally {
            MDC.clear();
        }
    }

}
