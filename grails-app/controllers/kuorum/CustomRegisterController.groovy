package kuorum

import grails.plugin.springsecurity.annotation.Secured
import kuorum.core.model.Gender
import kuorum.users.KuorumUser
import kuorum.users.OrganizationData
import kuorum.users.PersonData
import kuorum.users.PersonalData
import kuorum.web.commands.customRegister.Step1Command

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


        kuorumUserService.updatePersonalData(user, personalData)

        redirect mapping:'customRegisterStep2'
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def step2(){
        log.info("Custom register paso2")
    }
}
