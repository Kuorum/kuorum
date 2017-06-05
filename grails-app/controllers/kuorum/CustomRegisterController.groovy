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

//    @Secured(['ROLE_INCOMPLETE_USER', 'ROLE_PASSWORDCHANGED'])
    def countryAndPostalCode(UserRegionCommand command){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)

        if (command.hasErrors()){
            flash.error = message(code:'customRegister.countryAndPostalCode.fail')
            redirect mapping: 'home'
            return
        }

        if(user){
            user.personalData.country = regionService.findCountry(command.province)
            Region province = command.province
            user.personalData.province = province
            user.personalData.provinceCode = province.iso3166_2
            NoticeType noticeType = dashboardService.getNoticesByKuorumUser(user)
            user.notice = new Notice(noticeType: noticeType)
            kuorumUserService.updateUser(user)
        }
        flash.message = message(code:'customRegister.countryAndPostalCode.success')
        redirect mapping: 'home'
    }

    @Secured(['ROLE_INCOMPLETE_USER', 'ROLE_PASSWORDCHANGED', 'ROLE_USER'])
    def ageAndGender(PersonalDataCommand personalDataCommand){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        personalDataCommand.validate()

        if (personalDataCommand.hasErrors()){
            flash.error = message(code:'customRegister.ageAndGender.fail')
            redirect mapping: 'home'
            return
        }

        if(user){
            user.personalData.gender = personalDataCommand.gender
            if (user.personalData.gender != Gender.ORGANIZATION){
                user.personalData.year = personalDataCommand.year
            }
            NoticeType noticeType = dashboardService.getNoticesByKuorumUser(user)
            user.notice = new Notice(noticeType: noticeType)
            kuorumUserService.updateUser(user)
        }
        flash.message = message(code:'customRegister.ageAndGender.success')
        redirect mapping: 'home'
    }

    @Secured(['ROLE_POLITICIAN'])
    def telephone(PersonalDataCommand personalDataCommand){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        personalDataCommand.validate()

        if (personalDataCommand.hasErrors()){
            flash.error = message(code:'customRegister.telephone.fail')
            redirect mapping: 'home'
            return
        }

        if(user){
            user.personalData.telephone = personalDataCommand.telephone
            user.personalData.phonePrefix = personalDataCommand.phonePrefix
            NoticeType noticeType = dashboardService.getNoticesByKuorumUser(user)
            user.notice = new Notice(noticeType: noticeType)
            kuorumUserService.updateUser(user)
        }
        flash.message = message(code:'customRegister.telephone.success')
        redirect mapping: 'home'
    }

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
        redirect mapping:"registerStep3"
    }

    @Secured('IS_AUTHENTICATED_REMEMBERED')
    def step3(){
        KuorumUser user =  KuorumUser.get(springSecurityService.principal.id)
        [user:user, command: new PromotionalCodeCommand()]
    }

    def step3Save(PromotionalCodeCommand command){
        if(!command.promotionalCode){
            redirect(mapping:"dashboard", params: [tour:true])
            return;
        }else{
            if (command.hasErrors()){
                KuorumUser user =  KuorumUser.get(springSecurityService.principal.id)
                render view: "step3", model:[user:user, command: command]
            }else{
                KuorumUser user =  KuorumUser.get(springSecurityService.principal.id)
                promotionalCodeService.setPromotionalCode(user, command.promotionalCode)
                flash.message=g.message(code: 'subscriber.step3.promotionalCode.success')
                redirect(mapping:"dashboard", params: [tour:true])
            }
        }
    }

    def subscriptionStep1(){
        OfferType offerType = params.offerType?OfferType.valueOf(params.offerType):OfferType.BASIC
        Long kpeople = params.kpeople?Long.parseLong(params.kpeople):1;

        // THE USER WAS DOING SOME WIRED AND TRY TO SUBSCRIBE AN ACCOUNT ALREADY LOGGED
        if (springSecurityService.isLoggedIn()){
            KuorumUser user =  KuorumUser.get(springSecurityService.principal.id)
            offerService.purchaseOffer(user, offerType, kpeople)
            flash.message = message(code: 'dashboard.userProfile.advise.politicianRequest.text')
            if(politicianService.isPolitician(user)){
                redirect mapping:'politicianInbox'
            }else{
                politicianService.requestAPoliticianAccount(user)
                redirect mapping:'dashboard'
            }
            return
        }
        //ELSE
        // REAL NEW USER
        Locale locale = localeResolver.resolveLocale(request)
        AvailableLanguage availableLanguage = AvailableLanguage.fromLocaleParam(locale.getLanguage())
        SubscriptionStep1Command command = new SubscriptionStep1Command([kpeople:kpeople, offerType:offerType,language:availableLanguage])
        [command:command]
    }

    def subscriptionStep1Save(SubscriptionStep1Command command){
        if (command.hasErrors()){
            render view:"subscriptionStep1", model:[command:command]
            return;
        }

        KuorumUser user = registerService.registerUser(command)
        kuorumUserService.updateAlias(user, command.alias)
        user.personalData = user.personalData?:new kuorum.users.PersonData()
        user.personalData.phonePrefix = command.phonePrefix
        user.personalData.telephone = command.phone
        user.language = command.language
        user.password = registerService.encodePassword(user, command.password)
        user.userType = UserType.PERSON
        if (command.userType == UserType.POLITICIAN){
            offerService.purchaseOffer(user, command.offerType, command.kpeople)
            politicianService.requestAPoliticianAccount(user)
        }
        kuorumUserService.updateUser(user)
        springSecurityService.reauthenticate(user.email)
        redirect mapping:"registerSubscriptionStep3"

    }

    def subscriptionStep3(){

    }
}
