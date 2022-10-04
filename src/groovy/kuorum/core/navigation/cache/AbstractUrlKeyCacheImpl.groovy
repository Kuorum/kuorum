package kuorum.core.navigation.cache

import grails.plugin.springsecurity.SpringSecurityService
import kuorum.register.KuorumUserSession

abstract class AbstractUrlKeyCacheImpl implements ServletResponseCache {

    AbstractUrlKeyCacheImpl(SpringSecurityService springSecurityService) {
        this.springSecurityService = springSecurityService
    }

    //TODO If cache only for don't logged, this go out
    SpringSecurityService springSecurityService

    String buildKey(URL url) {
        buildKey(url, null)
    }

    String buildKey(URL url, Locale locale) {
        String key = url.toString()
        key += localeInfo(locale)
        key += securityInfo(key)
        return key
    }

    private String securityInfo(String key) {
        if (springSecurityService.isLoggedIn()) {
            return ((KuorumUserSession) springSecurityService.principal).id
        }
        return ""
    }

    private String localeInfo(Locale locale) {
        if(locale!=null){
            return locale.getLanguage().toString()
        }
        return ""
    }
}
