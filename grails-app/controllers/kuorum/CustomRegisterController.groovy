package kuorum

import grails.plugin.springsecurity.annotation.Secured
import kuorum.core.model.Gender
import kuorum.core.model.UserType
import kuorum.users.KuorumUser
import kuorum.users.OrganizationData
import kuorum.users.PersonData
import kuorum.users.PersonalData
import kuorum.web.commands.customRegister.Step1Command
import kuorum.web.commands.customRegister.Step2Command
import kuorum.web.commands.customRegister.Step3Command
import kuorum.web.commands.customRegister.Step4Command
import org.bson.types.ObjectId

class CustomRegisterController {

    def regionService
    def springSecurityService
    def kuorumUserService
    def kuorumMailService

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
        [command: command]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def step1Save(Step1Command command){
        if (command.hasErrors()){
            render view:"step1", model: [command:command]
            return
        }
        //TODO: Change on other country
        Region country = Region.findByIso3166_2("EU-ES") //ESPAÑA
        Region province = regionService.findProvinceByPostalCode(country, command.postalCode)
        if (!province){
            command.errors.rejectValue("postalCode", "notExists")
            render view:"step1", model: [command:command]
            return
        }
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        PersonalData personalData = null
        if (Gender.ORGANIZATION.equals(command.gender)){
            personalData = new OrganizationData()
            personalData.isPoliticalParty = false
            personalData.gender = Gender.ORGANIZATION
        }else{
            personalData = new PersonData()
            personalData.birthday = command.date
            personalData.gender = command.gender
            personalData.postalCode = command.postalCode
            personalData.provinceCode = province.iso3166_2
            personalData.province = province
        }

        user.personalData = personalData
        if (Gender.ORGANIZATION.equals(command.gender)){
            user.personalData.userType = UserType.ORGANIZATION
            kuorumUserService.convertAsOrganization(user)
        }

        user.save(flush:true, failOnError: true)

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
        [command: command]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def step2Save(Step2Command command){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        user.personalData.workingSector = command.workingSector
        user.bio = command.bio
        user.personalData.studies =  command.studies
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
        List<KuorumUser> recommendedUsers = kuorumUserService.recommendedUsers(user)
        [command: command, recommendedUsers:recommendedUsers]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def step4Save(Step4Command command){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        if (command.hasErrors()){
            List<KuorumUser> recommendedUsers = kuorumUserService.recommendedUsers(user)
            render view:"step4", model: [command: command, recommendedUsers:recommendedUsers]
            return
        }
        command.recommendedUsers.each {userId ->
            KuorumUser following = KuorumUser.get(new ObjectId(userId))
            kuorumUserService.createFollower(user, following)
        }
        redirect mapping:'customRegisterStep5'
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def step5(){
        log.info("Custom register finished")
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        kuorumMailService.verifyUser(user)
        redirect mapping:'home'
    }
}
