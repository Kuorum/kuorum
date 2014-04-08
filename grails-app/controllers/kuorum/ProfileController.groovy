package kuorum

import grails.plugin.springsecurity.annotation.Secured
import kuorum.core.model.Gender
import kuorum.core.model.gamification.GamificationAward
import kuorum.post.Post
import kuorum.users.KuorumUser
import kuorum.web.commands.profile.ChangePasswordCommand
import kuorum.web.commands.profile.EditUserProfileCommand
import kuorum.web.commands.profile.MailNotificationsCommand

@Secured(['IS_AUTHENTICATED_REMEMBERED'])
class ProfileController {

    def springSecurityService
    def passwordEncoder
    def regionService
    def kuorumUserService
    def postService
    def gamificationService

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
        redirect mapping:'profileEditUser'
    }
    def changePassword() {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        [user:user, command: new ChangePasswordCommand()]
    }
    def changePasswordSave(ChangePasswordCommand command) {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        if (command.hasErrors()){
            render view:"changePassword", model: [command:command,user:user]
            return
        }
        if (!passwordEncoder.isPasswordValid(user.password ,command.originalPassword, null)){
            command.errors.rejectValue("originalPassword","notCorrectPassword")
            render view:"changePassword", model: [command:command,user:user]
            return
        }
        user.password = springSecurityService.encodePassword(command.password)
        kuorumUserService.updateUser(user)
        redirect mapping:'profileChangePass'
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
        MailNotificationsCommand command = new MailNotificationsCommand(availableMails:user.availableMails)
        command.availableMails = user.availableMails?:[]
        [user:user, command: command]
    }
    def configurationEmailsSave(MailNotificationsCommand command) {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        if (command.hasErrors()){
            render view:"configurationEmails", model: [command:command,user:user]
            return
        }
        user.availableMails = command.availableMails
        kuorumUserService.updateUser(user)
        redirect mapping:'profileEmailNotifications'
    }

    def showFavoritesPosts() {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        List<Post> favorites = postService.favoritesPosts(user)
        [user:user, favorites: favorites]
    }

    def showUserPosts() {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)

        [user:user]
    }

    def kuorumStore() {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        [user:user]
    }
    def kuorumStoreActivateAward() {
        GamificationAward award = GamificationAward.valueOf(params.award)
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        if (gamificationService.canActivateAward(user,award)){
            gamificationService.activateAward(user,award)
            render "AJAX activated ${award}"
        }else{
            render "AJAX NOT ALLOWED"
        }
    }

    def kuorumStoreBuyAward() {
        GamificationAward award = GamificationAward.valueOf(params.award)
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        if (gamificationService.canBuyAward(user,award)){
            gamificationService.buyAward(user,award)
            render "AJAX  buyed ${award}"
        }else{
            render "AJAX  no hay dinerito"
        }
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
