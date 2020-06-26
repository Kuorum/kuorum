package kuorum

import kuorum.core.customDomain.CustomDomainResolver
import kuorum.register.KuorumUserSession
import org.slf4j.MDC

class LogsContextFilters {

    def springSecurityService

    def filters = {
        logFilter(controller:'*', action:'*') {
            before = {
                MDC.put("domain", CustomDomainResolver.domain)
                if (springSecurityService.isLoggedIn() && springSecurityService.principal?.id){
                    KuorumUserSession user = springSecurityService.principal
                    MDC.put("userAlias", user.alias)
                }
            }
            afterView = { Exception e ->
                MDC.clear();
            }
        }
    }
}
