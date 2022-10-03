package kuorum.core.navigation.cache

import grails.plugin.springsecurity.SpringSecurityService
import kuorum.register.KuorumUserSession

abstract class AbstractUrlKeyCacheImpl implements ServletResponseCache {

    SpringSecurityService springSecurityService

    String buildKey(URL url) {
        if (springSecurityService.isLoggedIn()) {
            return url.toString() + ((KuorumUserSession) springSecurityService.principal).id
        } else {
            return url.toString();
        }
    }
}
