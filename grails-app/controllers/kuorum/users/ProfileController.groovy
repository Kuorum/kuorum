package kuorum.users

import grails.plugin.springsecurity.annotation.Secured
import grails.plugin.springsecurity.ui.RegistrationCode
import grails.plugin.springsecurity.ui.ResetPasswordCommand
import kuorum.KuorumFile
import kuorum.core.model.Gender
import kuorum.core.model.UserType
import kuorum.files.FileService
import kuorum.mail.KuorumMailAccountService
import kuorum.notifications.Notification
import kuorum.register.FacebookAuthService
import kuorum.register.GoogleOAuthService
import kuorum.web.commands.profile.*
import org.bson.types.ObjectId
import org.kuorum.rest.model.notification.MailsMessageRSDTO

@Secured(['IS_AUTHENTICATED_REMEMBERED'])
class ProfileController {

    def springSecurityService
    FileService fileService
    def passwordEncoder
    def regionService
    KuorumUserService kuorumUserService
    def postService
    def gamificationService
    def notificationService
    def kuorumMailService
    KuorumMailAccountService kuorumMailAccountService

    def beforeInterceptor ={
        if (springSecurityService.isLoggedIn()){//Este if es para la confirmacion del email
            KuorumUser user
            if (params.userId){
                user = KuorumUser.get(params.userId)
            }else{
                user = KuorumUser.get(springSecurityService.principal.id)
            }
            params.user = user
        }
    }
    def afterInterceptor = [action: this.&prepareMenuData]

    protected prepareMenuData(model) {
        KuorumUser user = params.user
        model.menu = [
                activeNotifications:Notification.countByKuorumUserAndIsAlertAndIsActive(user, true, true),
                unpublishedPosts:postService.numUnpublishedUserPosts(user),
                favorites: postService.favoritesPosts(user).size(),
                unreadMessages:3
        ]
    }

//    @Secured("IS_AUTHENTICATED_FULLY")
    def editAccountDetails(){
        KuorumUser user = params.user
        AccountDetailsCommand command = new AccountDetailsCommand(user)
        [command:command]
    }

//    @Secured(["IS_AUTHENTICATED_FULLY"])
    def updateAccountDetails(AccountDetailsCommand command){
        if (command.hasErrors()){
            render view: "editAccountDetails", model:[command:command]
            return;
        }
        KuorumUser user = params.user
        user = kuorumUserService.updateAlias(user, command.alias)
        if (!user){
            flash.error = g.message(code:'kuorum.web.commands.profile.AccountDetailsCommand.logic.aliasError')
            render view: "editAccountDetails", model:[command:new AccountDetailsCommand(user)]
            return
        }
        user.language = command.language
        user.name = command.name
        if (!user.personalData){
            user.personalData = new  PersonData();
        }
        user.personalData.phonePrefix = command.phonePrefix
        user.personalData.telephone = command.phone
        kuorumUserService.updateUser(user);
        if (user.email != command.email){
            def changeMailData = changeEmail(user, command.email)
            flash.error = g.message(code:'kuorum.web.commands.profile.AccountDetailsCommand.logic.confirmationMailChanged')
        }else{
            flash.message=g.message(code:'kuorum.web.commands.profile.AccountDetailsCommand.logic.updateSuccess')
        }
        redirect mapping:"profileEditAccountDetails"

    }

    private def changeEmail(KuorumUser user, String email){
        def registrationCode = new RegistrationCode(username: user.email)
        registrationCode[DYNAMIC_ATTRIBUTE_NEW_EMAIL]=email
        if (!registrationCode.save()) {
            return null;
        }
        log.info("Solicitud de cambio de email del usuario ${user.email} con el tocken ${registrationCode.token}" )
        String url = generateLink('profileChangeEmailConfirm', [t: registrationCode.token])

        kuorumMailService.sendChangeEmailRequested(user, email)
        kuorumMailService.sendChangeEmailVerification(user,url, email)
        return [url:url]
    }

    private static final DYNAMIC_ATTRIBUTE_NEW_EMAIL = 'newEmail'

    @Secured("IS_AUTHENTICATED_ANONYMOUSLY")
    def changeEmailConfirm(){

        String token = params.t

        RegistrationCode registrationCode = token ? RegistrationCode.findByToken(token) : null
        if (!registrationCode) {
            flash.error = message(code: 'spring.security.ui.register.badCode')
            redirect mapping:"home"
            return
        }

        KuorumUser user
        // TODO to ui service
        RegistrationCode.withTransaction { status ->
            user = KuorumUser.findByEmail(registrationCode.username)
            if (!user) {
                flash.error = message(code: 'spring.security.ui.register.badCode')
                redirect mapping:"home"
                return
            }
            user.email = registrationCode[DYNAMIC_ATTRIBUTE_NEW_EMAIL]
            user.save(flush:true)
            registrationCode.delete()
        }

        if (!user) {
            flash.error = message(code: 'spring.security.ui.register.badCode')
            redirect mapping:"home"
            return
        }
        springSecurityService.reauthenticate user.email
        flash.message = message(code: 'profile.changeEmail.success', args: [user.email])
        redirect mapping:"home"
    }

    private String generateLink(String urlMapping, linkParams) {
        createLink(absolute: true,
                mapping:urlMapping,
                params: linkParams)
    }

    def editUser() {
        KuorumUser user = params.user
        EditUserProfileCommand command = new EditUserProfileCommand()
        command.gender = user.personalData?.gender
        command.postalCode = user.personalData?.postalCode
        command.homeRegion = user.personalData.province
        command.year =  user.personalData?.year
//        command.country =  user.personalData?.country
//        command.month = user.personalData?.birthday?user.personalData.birthday[Calendar.MONTH]+1:null
//        command.day =   user.personalData?.birthday?user.personalData.birthday[Calendar.DAY_OF_MONTH]:null
        command.alias = user.alias
        command.name = user.name
        command.language = user.language
        command.phonePrefix = user.personalData?.phonePrefix
        command.telephone = user.personalData?.telephone
        if (user.userType == UserType.ORGANIZATION){
            command.enterpriseSector = user.personalData?.enterpriseSector
        }else{
            command.workingSector = user.personalData?.workingSector
            command.studies = user.personalData?.studies
        }
        command.bio = user.bio
        command.photoId = user.avatar?.id?.toString()
        command.imageProfile = user.imageProfile?.id?.toString()
        [command: command, user:user]
    }

    def editUserSave(EditUserProfileCommand command) {
        KuorumUser user = params.user
        if (command.hasErrors()){
            render view:"editUser", model: [command:command,user:user]
            return
        }
        if (!user.alias){
            user.alias = command.alias
        }
        prepareUserStep1(user,command)
        prepareUserStep2(user,command)
        user.language = command.language
        user.personalData.phonePrefix = command.phonePrefix
        user.personalData.telephone = command.telephone
        kuorumUserService.updateUser(user)
        flash.message=message(code:'profile.editUser.success')
        redirect mapping:'profileEditUser'
    }

    def editCommissions () {
        KuorumUser user = params.user
        EditCommissionsProfileCommand command = new EditCommissionsProfileCommand()
        command.commissions = user.relevantCommissions
        [command: command, user:user]
    }

    def editCommissionsSave (EditCommissionsProfileCommand command) {
        KuorumUser user = params.user
        if (command.hasErrors()){
            render view:"editUser", model: [command:command,user:user]
            return
        }
        user.relevantCommissions = command.commissions
        kuorumUserService.updateUser(user)
        flash.message=message(code:'profile.editUser.success')
        redirect mapping:'profileEditCommissions'
    }

    protected prepareUserStep1(KuorumUser user, def command){
        user.name = command.name
        PersonalData personalData = null;
        if (Gender.ORGANIZATION.equals(command.gender)){
            personalData = new OrganizationData()
            personalData.enterpriseSector = command.enterpriseSector
            user.userType = UserType.ORGANIZATION
        }else{
            personalData = new PersonData(year: user.personalData?.year)
            if (user.userType!=UserType.POLITICIAN)
                user.userType = UserType.PERSON
            else{
                personalData.userType=UserType.POLITICIAN
            }
            personalData.year = command.year
        }
        if (user.personalData){
            //Datos no sobreescribibles
            personalData.postalCode= user.personalData.postalCode
            personalData.provinceCode= user.personalData.provinceCode
            personalData.province= user.personalData.province
            personalData.country= user.personalData.country
        }
        personalData.gender = command.gender

        if (command.homeRegion && !personalData.province){
            personalData.provinceCode = command.homeRegion.iso3166_2
            personalData.province = command.homeRegion
        }
        user.personalData = personalData
        if (Gender.ORGANIZATION.equals(command.gender)){
            user.personalData.userType = UserType.ORGANIZATION
            kuorumUserService.convertAsOrganization(user)
        }
    }

    protected prepareUserStep2(KuorumUser user, def command){
        user.bio = command.bio
        if (user.userType!=UserType.ORGANIZATION){
            user.personalData.studies =  command.studies
            user.personalData.workingSector =  command.workingSector
        }else{
            user.personalData.enterpriseSector =  command.enterpriseSector
        }
        if (command.photoId){
            KuorumFile avatar = KuorumFile.get(new ObjectId(command.photoId))
            avatar.alt = user.name
            fileService.deleteKuorumFile(user.avatar)
            avatar = fileService.convertTemporalToFinalFile(avatar)
            avatar.save(flush: true)
            user.avatar = avatar
        }
        if (command.hasProperty('imageProfile') && command.imageProfile){
            KuorumFile imageProfile = KuorumFile.get(new ObjectId(command.imageProfile))
            imageProfile.alt = user.name
            imageProfile.save()
            user.imageProfile = imageProfile
            fileService.convertTemporalToFinalFile(imageProfile)
        }

        fileService.deleteTemporalFiles(user)
    }

    def changePassword() {
        KuorumUser user = params.user
        if (!user.password || user.password.startsWith(FacebookAuthService.PASSWORD_PREFIX) || user.password.startsWith(GoogleOAuthService.PASSWORD_PREFIX)){
            redirect mapping:"profileSetPass";
            return;
        }
        [user:user, command: new ChangePasswordCommand()]
    }
    def changePasswordSave(ChangePasswordCommand command) {
        KuorumUser user = params.user
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

    def setPassword() {
        KuorumUser user = params.user
        [user:user, command: new ResetPasswordCommand()]
    }

    def setPasswordSave(ResetPasswordCommand command){
        KuorumUser user = params.user
        if (command.hasErrors()){
            render view:"setPassword", model: [command:command,user:user]
            return
        }
        user.password = springSecurityService.encodePassword(command.password)
        kuorumUserService.updateUser(user)
        flash.message=message(code:'profile.changePassword.success')
        redirect mapping:'profileChangePass'
    }

    def socialNetworks() {
        KuorumUser user = params.user
        SocialNetworkCommand command = new SocialNetworkCommand()
        command.properties.each {
            if (it.key!= "class" && user.socialLinks.hasProperty(it.key))
                command."$it.key" = user.socialLinks."${it.key}"
        }
        [user:user, command: command]
    }

    def socialNetworksSave(SocialNetworkCommand command) {
        KuorumUser user = params.user
        if (command.hasErrors()){
            render (view:'socialNetworks', model:[user:user, command: command])
            return
        }
        command.properties.each {
            if (it.key!= "class" && user.socialLinks.hasProperty(it.key))
                user.socialLinks."${it.key}" = it.value
        }
        kuorumUserService.updateUser(user)
        flash.message = "OK"
        redirect mapping:'profileSocialNetworks'
    }

    def configurationEmails() {
        KuorumUser user = params.user
        MailNotificationsCommand command = new MailNotificationsCommand(availableMails:user.availableMails)
        command.availableMails = user.availableMails?:[]
        [user:user, command: command]
    }
    def configurationEmailsSave(MailNotificationsCommand command) {
        KuorumUser user = params.user
        if (command.hasErrors()){
            render view:"configurationEmails", model: [command:command,user:user]
            return
        }
        user.availableMails = command.availableMails
        kuorumUserService.updateUser(user)
        flash.message = message(code:'profile.emailNotifications.success')
        redirect mapping:'profileEmailNotifications'
    }

    def userMessages() {
        KuorumUser user = params.user
        [user:user]
    }

    def deleteAccount(){
        KuorumUser user = params.user
        [user:user, command: new DeleteAccountCommand()]
    }

    def deleteAccountPost(DeleteAccountCommand command){
        KuorumUser user = params.user
        if (command.hasErrors()){
            render view:'deleteAccount', model:[user:user, command:command]
            return;
        }
        kuorumMailService.sendFeedbackMail(user, command.explanation, command.forever)
        if (command.forever){
            kuorumUserService.deleteAccount(user)
            flash.message=message(code:'profile.deleteAccount.deleteForever.success')
            redirect(mapping: 'logout')
        }else{
            flash.message=message(code:'profile.deleteAccount.oneChance.success')
            redirect(mapping: 'home')
        }
    }

    def showUserEmails(){
        MailsMessageRSDTO mails = kuorumMailAccountService.getUserMails(params.user)
        [mails:mails]
    }
}
