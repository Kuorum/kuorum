package kuorum

import grails.plugin.springsecurity.annotation.Secured
import kuorum.core.model.search.Pagination
import kuorum.dashboard.DashboardService
import kuorum.notifications.Notice
import kuorum.notifications.NoticeType
import kuorum.users.KuorumUser
import kuorum.users.ProfileController
import kuorum.users.RoleUser
import kuorum.web.commands.customRegister.Step1Command
import kuorum.web.commands.customRegister.Step2Command
import kuorum.web.commands.customRegister.Step3Command
import kuorum.web.commands.customRegister.Step4Command
import kuorum.web.commands.profile.PersonalDataCommand
import org.bson.types.ObjectId

class CustomRegisterController extends  ProfileController{

    def kuorumMailService
    DashboardService dashboardService


    def afterInterceptor = {}

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def step1() {
        log.info("Custom register paso1")
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        Step1Command command = new Step1Command()
        command.gender = user.personalData?.gender
        command.postalCode = user.personalData?.postalCode
        command.year =  user.personalData?.birthday?user.personalData?.birthday[Calendar.YEAR]:null
        command.month = user.personalData?.birthday?user.personalData?.birthday[Calendar.MONTH]:null
        command.day =   user.personalData?.birthday?user.personalData?.birthday[Calendar.DAY_OF_MONTH]:null
        command.name = user.name
        [command: command]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def step1Save(Step1Command command){
        if (command.hasErrors()){
            render view:"step1", model: [command:command]
            return
        }
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        prepareUserStep1(user, command)
        user.save(flush:true, failOnError: true)
        kuorumMailService.mailingListUpdateUser(user)

        redirect mapping:'customRegisterStep2'
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def step2(){
        log.info("Custom register paso2")
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        Step2Command command = new Step2Command()
        command.workingSector = user.personalData?.workingSector
        command.bio = user.bio
        command.studies =  user.personalData?.studies
        command.photoId = user.avatar?.id?.toString()
        command.enterpriseSector = user.personalData?.enterpriseSector
        [command: command, user:user]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def step2Save(Step2Command command){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        prepareUserStep2(user, command)

        user.save()
        redirect mapping:'customRegisterStep3'
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def step3(){
        log.info("Custom register paso3")
        Step3Command command = new Step3Command()
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        command.relevantCommissions = user.relevantCommissions
        [command: command]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def step3Save(Step3Command command){
        if (command.hasErrors()){
            render view:"step3", model: [command:command]
            return
        }
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        user.relevantCommissions = command.relevantCommissions
        user.save()
        redirect mapping:'customRegisterStep4'
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def step4(){
        log.info("Custom register paso4")
        Step4Command command = new Step4Command()
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
//        List<KuorumUser> recommendedUsers = kuorumUserService.recommendedUsers(user, new Pagination(max:12))
        List<KuorumUser> recommendedUsers = step4RecommendedUsers(user)

        [command: command, recommendedUsers:recommendedUsers]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def step4Save(Step4Command command){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        if (command.hasErrors()){
//            List<KuorumUser> recommendedUsers = kuorumUserService.recommendedUsers(user)
            List<KuorumUser> recommendedUsers = step4RecommendedUsers(user)
            render view:"step4", model: [command: command, recommendedUsers:recommendedUsers]
            return
        }
        command.recommendedUsers.each {userId ->
            KuorumUser following = KuorumUser.get(new ObjectId(userId))
            kuorumUserService.createFollower(user, following)
        }
        redirect mapping:'customRegisterStep5'
    }

    private List<KuorumUser> step4RecommendedUsers(KuorumUser user){
        List<KuorumUser> recommendedUsers = []
        def organizations = kuorumUserService.recommendOrganizations(user, new Pagination(max:4))
        def persons = kuorumUserService.recommendPersons(user, new Pagination(max:12))
        def politicians = kuorumUserService.recommendPoliticians(user, new Pagination(max:4))
        persons = persons.take(persons.size() - politicians.size())
        persons = persons.take(persons.size() - organizations.size())

        recommendedUsers += organizations
        recommendedUsers += persons
        recommendedUsers += politicians
        recommendedUsers
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def step5(){
        log.info("Custom register finished")
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        user.authorities = [RoleUser.findByAuthority("ROLE_USER")]
        user.save()
        kuorumMailService.verifyUser(user)
//        redirect mapping:'home'
        redirect mapping:'tourStart'
    }

    @Secured(['ROLE_INCOMPLETE_USER', 'ROLE_PASSWORDCHANGED'])
    def countryAndPostalCode(PersonalDataCommand personalDataCommand){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        personalDataCommand.validate()

        if (personalDataCommand.hasErrors()){
            flash.error = message(code:'customRegister.countryAndPostalCode.fail')
            redirect mapping: 'home'
            return
        }

        if(user){
            user.personalData.country = personalDataCommand.country
            Region province = regionService.findRegionOrProvinceByPostalCode(personalDataCommand.country, personalDataCommand.postalCode)
            user.personalData.province = province
            user.personalData.provinceCode = province.iso3166_2
            user.personalData.postalCode = personalDataCommand.postalCode
            NoticeType noticeType = dashboardService.getNoticesByKuorumUser(user)
            user.notice = new Notice(noticeType: noticeType)
            user.save(flush: true)
        }
        flash.message = message(code:'customRegister.countryAndPostalCode.success')
        redirect mapping: 'home'
    }

    @Secured(['ROLE_INCOMPLETE_USER', 'ROLE_PASSWORDCHANGED'])
    def ageAndGender(PersonalDataCommand personalDataCommand){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        personalDataCommand.validate()

        if (personalDataCommand.hasErrors()){
            flash.error = message(code:'customRegister.ageAndGender.fail')
            redirect mapping: 'home'
            return
        }

        if(user){
            user.personalData.year = personalDataCommand.year
            user.personalData.gender = personalDataCommand.gender
            NoticeType noticeType = dashboardService.getNoticesByKuorumUser(user)
            user.notice = new Notice(noticeType: noticeType)
            user.save(flush: true)
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
            user.personalData.telephone = "$params.phonePrefix $personalDataCommand.telephone"
            NoticeType noticeType = dashboardService.getNoticesByKuorumUser(user)
            user.notice = new Notice(noticeType: noticeType)
            user.save(flush: true)
        }
        flash.message = message(code:'customRegister.telephone.success')
        redirect mapping: 'home'
    }


}
