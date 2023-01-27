package kuorum

import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import kuorum.core.model.UserType
import kuorum.register.RegisterService
import kuorum.users.CookieUUIDService
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserService
import kuorum.web.commands.customRegister.Step2Command
import payment.contact.CensusService

class CustomRegisterController {

    SpringSecurityService springSecurityService
    KuorumUserService kuorumUserService
    CookieUUIDService cookieUUIDService
    CensusService censusService;
    RegisterService registerService

    def afterInterceptor = {}

    @Secured('IS_AUTHENTICATED_REMEMBERED')
    def step2(){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        String recommendedAlias = kuorumUserService.generateValidAlias(user.name, true)
        [command:new Step2Command(user,recommendedAlias)]
    }


    @Secured('IS_AUTHENTICATED_REMEMBERED')
    def step2Save(Step2Command  command){
        KuorumUser user =  KuorumUser.get(springSecurityService.principal.id)
        if (command.hasErrors()){
            render view: "step2", model:[command:command]
            return
        }
        kuorumUserService.updateAlias(user, command.alias)

        user.personalData = user.personalData?:new kuorum.users.PersonalData()
        user.userType = UserType.PERSON
        user.personalData.provinceCode = command.homeRegion?.iso3166?:null
        user.language = command.language
        user.personalData.gender = command.gender

//        user.password = registerService.encodePassword(user, command.password)
        kuorumUserService.updateUser(user)
        redirect mapping:'customProcessRegisterStep3';
    }

    @Secured('IS_AUTHENTICATED_REMEMBERED')
    def step3(){
        [:]
    }
}
