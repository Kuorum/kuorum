package kuorum.core.navigation.cache

import grails.plugin.springsecurity.SpringSecurityService
import kuorum.register.KuorumUserSession

import javax.servlet.http.HttpServletRequest

abstract class AbstractHttpRequestKeyCacheImpl implements ServletResponseCache {

    AbstractHttpRequestKeyCacheImpl(){}

    AbstractHttpRequestKeyCacheImpl(SpringSecurityService springSecurityService) {
        this.springSecurityService = springSecurityService
    }

    SpringSecurityService springSecurityService

    String buildKey(HttpServletRequest request) {
        buildKey(url, null)
    }

    String buildKey(HttpServletRequest request, Locale locale) {
        String key = request.getLocalAddr() + request.getContextPath()
        key += clean(request.getServletPath())
        key += securityInfo()
        key += localeInfo(locale)
        return key
    }

    private String securityInfo() {
            return springSecurityService.isLoggedIn()?((KuorumUserSession) springSecurityService.principal).id:""
    }

    protected String localeInfo(Locale locale) {
            return locale!=null?locale.getLanguage():""
    }

}
