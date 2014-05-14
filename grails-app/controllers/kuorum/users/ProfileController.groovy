package kuorum.users

import grails.plugin.springsecurity.annotation.Secured
import kuorum.KuorumFile
import kuorum.core.model.Gender
import kuorum.core.model.UserType
import kuorum.core.model.gamification.GamificationAward
import kuorum.post.Post
import kuorum.web.commands.profile.ChangePasswordCommand
import kuorum.web.commands.profile.EditUserProfileCommand
import kuorum.web.commands.profile.MailNotificationsCommand
import org.bson.types.ObjectId

@Secured(['IS_AUTHENTICATED_REMEMBERED'])
class ProfileController {

    def springSecurityService
    def fileService
    def passwordEncoder
    def regionService
    def kuorumUserService
    def postService
    def gamificationService

    def afterInterceptor = [action: this.&prepareMenuData]

    private prepareMenuData(model) {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        model.menu = [
                activeNotifications:23,
                unpublishedPosts:2,
                unreadMessages:3
        ]
    }

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
        command.photoId = user.avatar?.id?.toString()
        [command: command, user:user]
    }

    def editUserSave(EditUserProfileCommand command) {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        if (command.hasErrors()){
            render view:"editUser", model: [command:command,user:user]
            return
        }
        prepareUserStep1(user,command)
        prepareUserStep2(user,command)
        kuorumUserService.updateUser(user)
        flash.message=message(code:'profile.editUser.success')
        redirect mapping:'profileEditUser'
    }

    protected prepareUserStep1(KuorumUser user, def command){
        user.name = command.name
        PersonalData personalData = null
        if (Gender.ORGANIZATION.equals(command.gender)){
            personalData = new OrganizationData()
            personalData.isPoliticalParty = false
            personalData.gender = Gender.ORGANIZATION
            personalData.country = command.country
        }else{
            personalData = new PersonData()
            personalData.birthday = command.date
            personalData.gender = command.gender
            personalData.postalCode = command.postalCode
            personalData.provinceCode = command.province.iso3166_2
            personalData.province = command.province
        }

        user.personalData = personalData
        if (Gender.ORGANIZATION.equals(command.gender)){
            user.personalData.userType = UserType.ORGANIZATION
            kuorumUserService.convertAsOrganization(user)
        }
    }

    protected prepareUserStep2(KuorumUser user, def command){
        user.personalData.workingSector = command.workingSector
        user.bio = command.bio
        user.personalData.studies =  command.studies
        if (command.photoId){
            KuorumFile avatar = KuorumFile.get(new ObjectId(command.photoId))
            avatar.alt = user.name
            avatar.save()
            user.avatar = avatar
            fileService.convertTemporalToFinalFile(avatar)
            fileService.deleteTemporalFiles(user)
        }
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
            command.errors.rejectValue("originalPassword","${ChangePasswordCommand.class.name}.originalPassword.notCorrectPassword")
            render view:"changePassword", model: [command:command,user:user]
            return
        }
        user.password = springSecurityService.encodePassword(command.password)
        kuorumUserService.updateUser(user)
        flash.message=message(code:'profile.changePassword.success')
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

    def deleteAccount(){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        [user:user]
        render "MANDA UN EMAIL"
    }

}
