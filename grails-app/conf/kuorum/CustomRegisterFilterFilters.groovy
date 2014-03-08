package kuorum

import kuorum.users.KuorumUser

class CustomRegisterFilterFilters {

    def springSecurityService

    private static final STEP1_FIELDS=['birthday','postalCode','gender']

    def filters = {
        all(controller:'customRegister', invert: true) {
            before = {
                if (springSecurityService.isLoggedIn()){
                    KuorumUser kuorumUser = KuorumUser.get(springSecurityService.principal.id)
                    if (STEP1_FIELDS.find{field -> kuorumUser.personalData."$field" == null}){
                        redirect(mapping: 'customRegisterStep1')
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
