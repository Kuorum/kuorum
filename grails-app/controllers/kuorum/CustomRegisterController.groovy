package kuorum

import grails.plugin.springsecurity.annotation.Secured
import kuorum.core.model.AvailableLanguage
import kuorum.core.model.Gender
import kuorum.core.model.OfferType
import kuorum.core.model.UserType
import kuorum.dashboard.DashboardService
import kuorum.notifications.Notice
import kuorum.notifications.NoticeType
import kuorum.notifications.NotificationService
import kuorum.register.RegisterService
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserService
import kuorum.users.OrganizationData
import kuorum.users.PoliticianService
import kuorum.web.commands.customRegister.Step2Command
import kuorum.web.commands.customRegister.SubscriptionStep1Command
import kuorum.web.commands.payment.contact.promotionalCode.PromotionalCodeCommand
import kuorum.web.commands.profile.PersonalDataCommand
import kuorum.web.commands.profile.UserRegionCommand
import org.springframework.web.servlet.LocaleResolver
import payment.contact.PromotionalCodeService

class CustomRegisterController {

    def springSecurityService
    KuorumUserService kuorumUserService;
    DashboardService dashboardService
    NotificationService notificationService
    LocaleResolver localeResolver
    RegisterService registerService
    OfferService offerService
    PoliticianService politicianService
    PromotionalCodeService promotionalCodeService

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
            return;
        }
        kuorumUserService.updateAlias(user, command.alias)

//        if (command.userType == UserType.ORGANIZATION){
//            user.personalData = new OrganizationData();
//            user.personalData.gender = Gender.ORGANIZATION
//            user.userType = UserType.ORGANIZATION
//        }else{
            user.personalData = user.personalData?:new kuorum.users.PersonData()
            user.userType = UserType.PERSON
            user.personalData.province = command.homeRegion
//        }
//        user.personalData.phonePrefix = command.phonePrefix
//        user.personalData.telephone = command.phone
        user.language = command.language
//        user.name = command.name
//        user.surname = command.surname

        user.password = registerService.encodePassword(user, command.password)
//        if (command.userType == UserType.POLITICIAN){
//            offerService.purchaseOffer(user, OfferType.BASIC, 0)
//            politicianService.requestAPoliticianAccount(user)
//        }
        kuorumUserService.updateUser(user)
        redirect mapping:"customProcessRegisterStep3"
    }
}
