package kuorum

import grails.plugin.springsecurity.SpringSecurityUtils
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.register.KuorumUserSession
import org.kuorum.rest.model.domain.DomainRSDTO

class CustomRegisterFilterFilters {

    def springSecurityService

    private static final STEP1_FIELDS=['alias']

    def filters = {
        all(controller:'customRegister|logout|error|register|search|layouts', invert: true) {
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

        configFomain(controller: 'admin|logout|error|register|layouts|file', invert:true){
            before = {
                if (springSecurityService.isLoggedIn() && SpringSecurityUtils.ifAnyGranted("ROLE_ADMIN")){
                    DomainRSDTO domainRSDTO = CustomDomainResolver.domainRSDTO
                    if (domainRSDTO.version == 0){
//                      Domain is not configured
                        log.info("Domain is not configured. Redirecting ...")
                        redirect(mapping: 'adminDomainRegisterStep1')

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
