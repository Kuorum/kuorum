package kuorum.users

import grails.plugin.springsecurity.annotation.Secured
import grails.plugin.springsecurity.ui.RegistrationCode
import grails.plugin.springsecurity.ui.ResetPasswordCommand
import kuorum.KuorumFile
import kuorum.causes.CausesService
import kuorum.core.model.Gender
import kuorum.core.model.UserType
import kuorum.files.FileService
import kuorum.mail.KuorumMailAccountService
import kuorum.mail.MailType
import kuorum.notifications.Notification
import kuorum.register.RegisterService
import kuorum.users.extendedPoliticianData.ProfessionalDetails
import kuorum.web.commands.profile.*
import kuorum.web.commands.profile.politician.PoliticianCausesCommand
import kuorum.web.commands.profile.politician.ProfessionalDetailsCommand
import kuorum.web.commands.profile.politician.QuickNotesCommand
import kuorum.web.commands.profile.politician.RelevantEventsCommand
import org.bson.types.ObjectId
import org.kuorum.rest.model.notification.MailsMessageRSDTO
import org.kuorum.rest.model.tag.CauseRSDTO

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
    RegisterService registerService
    CausesService causesService;
    PoliticianService politicianService

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
            redirect mapping:'profileEditAccountDetails'
            return
        }
        user.language = command.language
        user.name = command.name
        if (!user.personalData){
            user.personalData = new  PersonData();
        }
        user.personalData.phonePrefix = command.phonePrefix
        user.personalData.telephone = command.phone
        user.personalData.province = command.homeRegion
        user.timeZone = TimeZone.getTimeZone(command.timeZoneId)
        kuorumUserService.updateUser(user);
        if (user.email != command.email){
            def changeMailData = changeEmail(user, command.email)
            String urlResend = g.createLink(mapping: 'profileChangeEmailResend', params: [email:command.email])
            flash.error = g.message(code:'kuorum.web.commands.profile.AccountDetailsCommand.logic.confirmationMailChanged', args: [urlResend], encodeAs: "raw")
        }else{
            flash.message=g.message(code:'kuorum.web.commands.profile.AccountDetailsCommand.logic.updateSuccess')
        }
        redirect mapping:"profileEditAccountDetails"

    }

    def updateUserEmail(){
        KuorumUser user = params.user
        String email = params.email
        def emailPattern = /[_A-Za-z0-9-]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})/
        if (email ==~ emailPattern){
            if (user.email != email){
                def changeMailData = changeEmail(user, email)
                flash.error = g.message(code:'kuorum.web.commands.profile.AccountDetailsCommand.logic.confirmationMailChanged', encodeAs: "raw")
            }
            redirect mapping:"profileEditAccountDetails"
        }else{
            render "Not valid email"
        }
    }

    def editCauses(){
        KuorumUser politician = params.user
        List<CauseRSDTO> causes = causesService.findDefendedCauses(politician)
        PoliticianCausesCommand command = new PoliticianCausesCommand(politician, causes.collect{it.name})
        [command:command]
    }

    def updateCauses(PoliticianCausesCommand command){
        KuorumUser user = params.user
        if (command.hasErrors() || !user ){
            render view:"editCauses", model:[command:command]
            return;
        }
        politicianService.updatePoliticianCauses(user, command.causes)
        flash.message=message(code:'profile.editUser.success')
        redirect mapping:'profileCauses', params: user.encodeAsLinkProperties()
    }

    private def changeEmail(KuorumUser user, String email){
        def registrationCode = new RegistrationCode(username: user.email)
        registrationCode[DYNAMIC_ATTRIBUTE_NEW_EMAIL]=email
        if (!registrationCode.save()) {
            return null;
        }
        log.info("Solicitud de cambio de email del usuario ${user.email} con el token ${registrationCode.token}" )
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
        EditUserProfileCommand command = new EditUserProfileCommand(user)

        [command: command, user:user]
    }

    def editUserSave(EditUserProfileCommand command) {
        KuorumUser user = params.user
        if (command.hasErrors()){
            render view:"editUser", model: [command:command,user:user]
            return
        }
        prepareUserEditProfile(user,command)
        prepareUserImages(user,command, fileService)
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

    public static void prepareUserEditProfile(KuorumUser user, EditUserProfileCommand command){
        PersonalData personalData = null;
        user.bio = command.bio
        if (Gender.ORGANIZATION.equals(command.gender)){
            personalData = new OrganizationData()
            personalData.enterpriseSector = command.enterpriseSector
            user.userType = UserType.ORGANIZATION
            personalData.gender = Gender.ORGANIZATION
        }else{
            personalData = new PersonData(user.personalData?.properties)
            if (user.userType==UserType.POLITICIAN || user.userType==user.userType.CANDIDATE){
                if (!user.professionalDetails){
                    user.professionalDetails = new ProfessionalDetails()
                }
                user.professionalDetails.position = command.position
                user.professionalDetails.politicalParty = command.politicalParty
            }else{
                personalData.birthday = command.birthday
                personalData.studies =  command.studies
                personalData.workingSector =  command.workingSector
                user.userType = UserType.PERSON
            }
            personalData.gender = command.gender
        }
        if (user.personalData){
            //Datos no sobreescribibles
            personalData.provinceCode= user.personalData.provinceCode
            personalData.province= user.personalData.province
            personalData.country= user.personalData.country
        }
        user.personalData = personalData
    }

    public static  void prepareUserImages(KuorumUser user, EditUserProfileCommand command, FileService fileService){

        if (command.photoId && (!user.avatar || user.avatar.id.toString() != command.photoId)){
            KuorumFile avatar = KuorumFile.get(new ObjectId(command.photoId))
            avatar.alt = user.name
            fileService.deleteKuorumFile(user.avatar)
            avatar = fileService.convertTemporalToFinalFile(avatar)
            avatar.save(flush: true)
            user.avatar = avatar
        }
        if (command.imageProfile && (!user.imageProfile  || user.imageProfile.id.toString() != command.imageProfile)){
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
        if (!registerService.isPasswordSetByUser(user)){
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
        SocialNetworkCommand command = new SocialNetworkCommand(user)

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
        flash.message = g.message(code: 'kuorum.web.commands.profile.SocialNetworkCommand.save.success')
        redirect mapping:'profileSocialNetworks'
    }

    def configurationEmails() {
        KuorumUser user = params.user
        MailNotificationsCommand command = new MailNotificationsCommand()
        command.availableMails = user.availableMails?.collect{it.mailGroup}.unique()?:[]
        [user:user, command: command]
    }
    def configurationEmailsSave(MailNotificationsCommand command) {
        KuorumUser user = params.user
        if (command.hasErrors()){
            render view:"configurationEmails", model: [command:command,user:user]
            return
        }
        user.availableMails = MailType.values().findAll{command.availableMails?.contains(it.mailGroup)}
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

    def editNews(){
        KuorumUser user = params.user
        [command:new RelevantEventsCommand(politician:user, politicianRelevantEvents: user.relevantEvents?.reverse()?:[])]
    }

    def updateNews(RelevantEventsCommand command){
        command.politicianRelevantEvents = command.politicianRelevantEvents.findAll{it}
        KuorumUser user = params.user
        if (!command.validate() || !user ){
            render view:"/profile/editRelevantEvents", model:[command:command]
            return;
        }
        politicianService.updatePoliticianRelevantEvents(params.user, command.politicianRelevantEvents)
        flash.message=message(code:'profile.editUser.success')
        redirect mapping:'profileNews'
    }

    def editQuickNotes(){
        KuorumUser politician = params.user
        QuickNotesCommand command = new QuickNotesCommand(politician)
        [command:command]
    }

    def updateQuickNotes(QuickNotesCommand command){
        KuorumUser user = params.user
        if (command.hasErrors() || !user ){
            render view:"editQuickNotes", model:[command:command]
            return;
        }
        politicianService.updatePoliticianQuickNotes(user, command.politicianExtraInfo, command.institutionalOffice, command.politicalOffice)
        flash.message=message(code:'profile.editUser.success')
        redirect mapping:'profileQuickNotes', params: user.encodeAsLinkProperties()
    }


    def editProfessionalDetails(){
        KuorumUser politician = params.user
        ProfessionalDetailsCommand command = new ProfessionalDetailsCommand(politician)
        [command:command]
    }

    def updateProfessionalDetails(ProfessionalDetailsCommand command){
        KuorumUser user = params.user
        if (command.hasErrors() || !user ){
            render view:"/profile/editProfessionalDetails", model:[command:command]
            return;
        }
        politicianService.updatePoliticianProfessionalDetails(user, command)
        flash.message=message(code:'profile.editUser.success')
        redirect mapping:'profileProfessionalDetails', params: user.encodeAsLinkProperties()
    }
}
