package kuorum

import grails.plugin.springsecurity.SpringSecurityUtils
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.register.KuorumUserSession
import org.kuorum.rest.model.domain.DomainRSDTO

class CustomRegisterFilterFilters {

    def springSecurityService

    private static final STEP1_FIELDS=['alias']

    def filters = {
        customRegisterFilter(controller:'customRegister|logout|error|register|search|layouts', invert: true) {
            before = {
                if (springSecurityService.isLoggedIn() && springSecurityService.principal?.id){
                    KuorumUserSession user = springSecurityService.principal
                    if (STEP1_FIELDS.find{field -> !user."$field"}){
//                        It is for redirect when the user has not complete profile
                        redirect(mapping: 'customProcessRegisterStep2')

                        return false
                    }
                }

            }
            after = { Map model ->

            }
            afterView = { Exception e ->

            }
        }
    }
}
