package kuorum

import kuorum.core.model.UserType
import kuorum.users.KuorumUser

class CustomRegisterFilterFilters {

    def springSecurityService

//    private static final STEP1_FIELDS=['birthday','postalCode','gender']
    private static final STEP1_FIELDS=[]

    def filters = {
        all(controller:'customRegister|logout|error', invert: true) {
            before = {
                if (springSecurityService.isLoggedIn()){
                    KuorumUser kuorumUser = KuorumUser.get(springSecurityService.principal.id)
                    if (kuorumUser.userType != UserType.ORGANIZATION && STEP1_FIELDS.find{field -> kuorumUser.personalData."$field" == null}){
                        //OLD ACTION - DEPRECATED. It is for redirect when the user has not complete profile
                        redirect(mapping: 'home')
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
