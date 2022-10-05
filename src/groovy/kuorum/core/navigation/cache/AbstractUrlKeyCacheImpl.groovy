package kuorum.core.navigation.cache

import grails.plugin.springsecurity.SpringSecurityService
import kuorum.register.KuorumUserSession

abstract class AbstractUrlKeyCacheImpl implements ServletResponseCache {

    AbstractUrlKeyCacheImpl(){}

    AbstractUrlKeyCacheImpl(SpringSecurityService springSecurityService) {
        this.springSecurityService = springSecurityService
    }

    SpringSecurityService springSecurityService

    String buildKey(URL url) {
        buildKey(url, null)
    }

    String buildKey(URL url, Locale locale) {
        String key = url.toString()
        key += localeInfo(locale)
        key += securityInfo()
        return key
    }

    private String securityInfo() {
            return springSecurityService.isLoggedIn()?((KuorumUserSession) springSecurityService.principal).id:""
    }

    private String localeInfo(Locale locale) {
            return locale!=null?locale.getLanguage():""
    }
}
