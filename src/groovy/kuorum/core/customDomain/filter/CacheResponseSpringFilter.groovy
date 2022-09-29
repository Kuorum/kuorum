package kuorum.core.customDomain.filter

import grails.plugin.springsecurity.SpringSecurityService
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.domain.DomainService
import kuorum.register.KuorumUserSession
import org.kuorum.rest.model.domain.DomainRSDTO
import org.springframework.web.filter.GenericFilterBean

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletResponse

class CacheResponseSpringFilter extends GenericFilterBean {

    DomainService domainService

    SpringSecurityService springSecurityService

    // TODO: CHANGE CACHE
    HashMap<String, String> fakeCache = new HashMap<>();

    void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        URL url = new URL(request.getRequestURL().toString())
        String rawHTML = getCachedWeb(url);
        if (rawHTML && isURLCacheable(URL)) {
            // TODO: RECOVER DATA FROM CACHE AND FILL THE RESPONSE
        } else {
            filterChain.doFilter(request, response)
            saveCache(response)
        }
    }

    Boolean isURLCacheable(URL url) {
        // TODO: THINK WHICH URLS SHOULD BE CACHED
        return true
    }

    String getCachedWeb(URL url) {
        String urlKey = buildKey(url)
        if (fakeCache.containsKey(urlKey)) {
            return fakeCache.get(urlKey);
        } else {
            return null;
        }
    }

    String buildKey(URL url) {
        if (springSecurityService.isLoggedIn()) {
            return url.toString() + ((KuorumUserSession) springSecurityService.principal).id
        } else {
            return url.toString();
        }
    }

    void saveCache(URL url, ServletResponse response) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        if (isURLCacheable(url)) {
            // TODO: RECOVER DATA FROM RESPONSE AND SAVE RESPONSE

        }
    }

}
