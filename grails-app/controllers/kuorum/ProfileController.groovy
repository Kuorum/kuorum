package kuorum

import grails.plugin.springsecurity.annotation.Secured
import kuorum.core.model.Gender
import kuorum.core.model.Studies
import kuorum.core.model.WorkingSector
import kuorum.users.KuorumUser
import kuorum.users.OrganizationData
import kuorum.users.PersonData
import kuorum.web.commands.profile.EditUserProfileCommand

@Secured(['IS_AUTHENTICATED_REMEMBERED'])
class ProfileController {

    def springSecurityService
    def regionService
    def kuorumUserService

    def editUser() {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        EditUserProfileCommand command = new EditUserProfileCommand()
        command.gender = user.personalData?.gender
        command.postalCode = user.personalData?.postalCode
        command.year =  user.personalData?.birthday?user.personalData.birthday[Calendar.YEAR]:null
        command.month = user.personalData?.birthday?user.personalData.birthday[Calendar.MONTH]+1:null
        command.day =   user.personalData?.birthday?user.personalData.birthday[Calendar.DAY_OF_MONTH]:null
        command.name = user.name
        command.workingSector = user.personalData?.workingSector
        command.studies = user.personalData?.studies
        command.bio = user.bio
        [command: command, user:user]
    }

    def editUserSave(EditUserProfileCommand command) {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        if (command.hasErrors()){
            render view:"editUser", model: [command:command,user:user]
            return
        }
        Region country = Region.findByIso3166_2("EU-ES") //ESPAÑA
        Region province = regionService.findProvinceByPostalCode(country, command.postalCode)
        if (!province){
            command.errors.rejectValue("postalCode", "notExists")
            render view:"editUser", model: [command:command,user:user]
            return
        }
        if (Gender.ORGANIZATION.equals(command.gender)){
            user.personalData.gender = Gender.ORGANIZATION
        }else{
            user.personalData.birthday = command.date
            user.personalData.gender = command.gender
            user.personalData.postalCode = command.postalCode
            user.personalData.provinceCode = province.iso3166_2
            user.personalData.province = province
            user.personalData.workingSector = command.workingSector
            user.personalData.studies = command.studies
        }
        user.name = command.name
        user.bio = command.bio
        kuorumUserService.updateUser(user)
        redirect mapping:'editUser'
    }
    def changePassword() {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        [user:user]
    }

    def changeEmail() {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        [user:user]
    }

    def socialNetworks() {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        [user:user]
    }

    def configurationEmails() {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        [user:user]
    }

    def showFavoritesPosts() {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        [user:user]
    }

    def showUserPosts() {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        [user:user]
    }

    def kuorumStore() {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        [user:user]
    }

    def userNotifications() {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        [user:user]
    }

    def userMessages() {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        [user:user]
    }

}
