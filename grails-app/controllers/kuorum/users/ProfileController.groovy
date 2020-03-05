package kuorum.users

import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.annotation.Secured
import grails.plugin.springsecurity.ui.RegistrationCode
import grails.plugin.springsecurity.ui.ResetPasswordCommand
import kuorum.KuorumFile
import kuorum.causes.CausesService
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.core.exception.KuorumException
import kuorum.core.model.Gender
import kuorum.core.model.UserType
import kuorum.files.FileService
import kuorum.mail.KuorumMailService
import kuorum.notifications.NotificationService
import kuorum.register.KuorumUserSession
import kuorum.register.RegisterService
import kuorum.users.extendedPoliticianData.ProfessionalDetails
import kuorum.web.commands.profile.*
import kuorum.web.commands.profile.politician.PoliticianCausesCommand
import kuorum.web.commands.profile.politician.RelevantEventsCommand
import org.bson.types.ObjectId
import org.codehaus.groovy.runtime.InvokerHelper
import org.kuorum.rest.model.domain.SocialRDTO
import org.kuorum.rest.model.kuorumUser.KuorumUserRSDTO
import org.kuorum.rest.model.kuorumUser.UserDataRDTO
import org.kuorum.rest.model.kuorumUser.config.NotificationConfigRDTO
import org.kuorum.rest.model.kuorumUser.config.NotificationMailConfigRDTO
import org.kuorum.rest.model.notification.campaign.config.NewsletterConfigRQDTO
import org.kuorum.rest.model.notification.campaign.config.NewsletterConfigRSDTO
import org.kuorum.rest.model.tag.CauseRSDTO
import payment.campaign.NewsletterService

import java.util.regex.Matcher
import java.util.regex.Pattern

@Secured(['IS_AUTHENTICATED_REMEMBERED'])
class ProfileController {

    def springSecurityService
    FileService fileService
    def passwordEncoder
    def regionService
    KuorumUserService kuorumUserService
    def postService
    NotificationService notificationService
    KuorumMailService kuorumMailService
    RegisterService registerService
    NewsletterService newsletterService
    CausesService causesService
    PoliticianService politicianService
    Pattern pattern
    Matcher matcher

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
//        KuorumUser user = params.user
    }

//    @Secured("IS_AUTHENTICATED_FULLY")
    def editAccountDetails(){
        KuorumUser user = params.user
        AccountDetailsCommand command = new AccountDetailsCommand(user)
//        command.homeRegion = regionService.findUserRegion(user)
        [command:command, requirePassword: registerService.isPasswordSetByUser(user)]
    }

//    @Secured(["IS_AUTHENTICATED_FULLY"])
    def updateAccountDetails(AccountDetailsCommand command){
        KuorumUser user = params.user
        if (command.hasErrors()){
            render view: "editAccountDetails", model:[command:command, requirePassword: registerService.isPasswordSetByUser(user)]
            return
        }
        user = kuorumUserService.updateAlias(user, command.alias)
        if (!user){
            flash.error = g.message(code:'kuorum.web.commands.profile.AccountDetailsCommand.logic.aliasError')
            redirect mapping:'profileEditAccountDetails'
            return
        }
        user.language = command.language
        user.name = command.name.encodeAsRemovingHtmlTags()
        user.surname = command.surname?.encodeAsRemovingHtmlTags()?:""
        if (!user.personalData){
            user.personalData = new  PersonalData()
        }
        user.personalData.phonePrefix = command.phonePrefix
        user.personalData.telephone = command.phone
        user.personalData.provinceCode = command.homeRegion?.iso3166?:null
        user.timeZone = command.timeZoneId ? TimeZone.getTimeZone(command.timeZoneId) : null
        kuorumUserService.updateUser(user)
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
        KuorumUserSession politician = springSecurityService.principal
        List<CauseRSDTO> causes = causesService.findSupportedCauses(politician)
        PoliticianCausesCommand command = new PoliticianCausesCommand(causes.collect{it.name})
        [command:command]
    }

    def updateCauses(PoliticianCausesCommand command){
        KuorumUserSession user = springSecurityService.principal
        if (command.hasErrors()){
            render view:"editCauses", model:[command:command]
            return
        }
        politicianService.updateUserCauses(user, command.causes)
        flash.message=message(code:'profile.editUser.success')
        redirect mapping:'profileCauses', params: user.encodeAsLinkProperties()
    }

    private def changeEmail(KuorumUser user, String email){
        def registrationCode = new RegistrationCode(username: user.email)
        registrationCode[DYNAMIC_ATTRIBUTE_NEW_EMAIL]=email
        if (!registrationCode.save()) {
            return null
        }
        log.info("Solicitud de cambio de email del usuario ${user.email} con el token ${registrationCode.token}" )
        String url = generateLink('profileChangeEmailConfirm', [t: registrationCode.token])
        String emailToShow = email.replaceAll(/[\.|@]/,'<span>$0</span>')
        kuorumMailService.sendChangeEmailRequested(user, emailToShow)
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
            user = KuorumUser.findByEmailAndDomain(registrationCode.username, CustomDomainResolver.domain)
            if (!user) {
                flash.error = message(code: 'spring.security.ui.register.badCode')
                redirect mapping:"home"
                return
            }
            String email = registrationCode[DYNAMIC_ATTRIBUTE_NEW_EMAIL]
            kuorumUserService.updateEmail(user, email)
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
        kuorumUserService.updateUser(user)
        flash.message=message(code:'profile.editUser.success')
        redirect mapping:'profileEditUser'
    }

    def editPictures(){
        KuorumUser user = params.user
        EditProfilePicturesCommand command = new EditProfilePicturesCommand(user)

        [command: command]
    }

    def updatePictures(EditProfilePicturesCommand command){
        KuorumUser user = params.user
        if (command.hasErrors()){
            render view:"editPictures", model: [command:command,user:user]
            return
        }
        prepareUserImages(user,command, fileService)
        kuorumUserService.updateUser(user)
        flash.message=message(code:'profile.editUser.success')
        redirect mapping:'profilePictures'

    }

    static void prepareUserEditProfile(KuorumUser user, EditUserProfileCommand command){
        PersonalData personalData = null
        user.bio = command.bio
        if (Gender.ORGANIZATION.equals(command.gender)){
            personalData = new PersonalData()
            user.userType = UserType.ORGANIZATION
            personalData.gender = Gender.ORGANIZATION
        }else{
            personalData = new PersonalData(user.personalData?.properties)
            if (!user.professionalDetails){
                user.professionalDetails = new ProfessionalDetails()
            }
            personalData.birthday = command.birthday
            user.userType = UserType.PERSON
            personalData.gender = command.gender
        }
        if (user.personalData){
            //Datos no sobreescribibles
            personalData.provinceCode= user.personalData.provinceCode
        }
        user.personalData = personalData
    }

    static  void prepareUserImages(KuorumUser user, EditProfilePicturesCommand command, FileService fileService){

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
            user.imageProfile = imageProfile
            fileService.deleteKuorumFile(user.imageProfile)
            fileService.convertTemporalToFinalFile(imageProfile)
            imageProfile.save()

        }

        fileService.deleteTemporalFiles(user)
    }

    def changePassword() {
        KuorumUser user = params.user
        if (!registerService.isPasswordSetByUser(user)){
            redirect mapping:"profileSetPass"
            return
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
        KuorumUserSession loggedUser = springSecurityService.principal
        KuorumUserRSDTO user = kuorumUserService.findUserRSDTO(loggedUser)
        SocialNetworkCommand command = new SocialNetworkCommand(user)
        [command: command]
    }

    def socialNetworksSave(SocialNetworkCommand command) {
        if (command.hasErrors()){
            render (view:'socialNetworks', model:[command: command])
            return
        }
        SocialRDTO social = new SocialRDTO()
        command.properties.each {
            if (it.key!= "class" && social.hasProperty(it.key))
                social."${it.key}" = it.value
        }
        KuorumUserSession loggedUser = springSecurityService.principal
        UserDataRDTO userDataRDTO = kuorumUserService.mapUserToRDTO(loggedUser)
        userDataRDTO.socialLinks = social
        kuorumUserService.updateKuorumUser(loggedUser, userDataRDTO)
        flash.message = g.message(code: 'kuorum.web.commands.profile.SocialNetworkCommand.save.success')
        redirect mapping:'profileSocialNetworks'
    }

    def configurationEmails() {
        KuorumUserSession loggedUser = springSecurityService.principal
        NotificationConfigRDTO notificationConfig = notificationService.getNotificationsConfig(loggedUser)
        MailNotificationsCommand command = new MailNotificationsCommand()
        command.mentions = notificationConfig.mailConfig.mentions
        command.followNew = notificationConfig.mailConfig.followNew
        command.proposalComment = notificationConfig.mailConfig.proposalComment
        command.proposalLike = notificationConfig.mailConfig.proposalLike
        command.proposalPinned = notificationConfig.mailConfig.proposalPinned
        command.proposalNew = notificationConfig.mailConfig.proposalNew
        command.proposalNewOwner = notificationConfig.mailConfig.proposalNewOwner
        command.postLike = notificationConfig.mailConfig.postLike
        command.debateNewOwner = notificationConfig.mailConfig.debateNewOwner
        command.debateNewCause = notificationConfig.mailConfig.debateNewCause
        command.postNewOwner = notificationConfig.mailConfig.postNewOwner
        command.postNewCause = notificationConfig.mailConfig.postNewCause
        command.eventNewOwner = notificationConfig.mailConfig.eventNewOwner
        command.eventNewCause = notificationConfig.mailConfig.eventNewCause
        command.participatoryBudgetNewOwner = notificationConfig.mailConfig.participatoryBudgetNewOwner
        command.participatoryBudgetNewCause = notificationConfig.mailConfig.participatoryBudgetNewCause
        command.districtProposalNewOwner = notificationConfig.mailConfig.districtProposalNewOwner
        command.districtProposalNewCause = notificationConfig.mailConfig.districtProposalNewCause
        command.districtProposalParticipatoryBudgetOwner = notificationConfig.mailConfig.districtProposalParticipatoryBudgetOwner
        command.districtProposalSupport = notificationConfig.mailConfig.districtProposalSupport
        command.districtProposalVote = notificationConfig.mailConfig.districtProposalVote
        command.petitionSign = notificationConfig.mailConfig.petitionSign
        command.petitionNewOwner = notificationConfig.mailConfig.petitionNewOwner
        command.petitionNewCause = notificationConfig.mailConfig.petitionNewCause
        command.surveyNewOwner = notificationConfig.mailConfig.surveyNewOwner
        command.surveyNewCause = notificationConfig.mailConfig.surveyNewCause
        [command: command]
    }
    def configurationEmailsSave(MailNotificationsCommand command) {
        KuorumUserSession loggedUser = springSecurityService.principal
        if (command.hasErrors()){
            render view:"configurationEmails", model: [command:command]
            return
        }
        NotificationConfigRDTO notificationConfig = new NotificationConfigRDTO()
        notificationConfig.setMailConfig(new NotificationMailConfigRDTO())
        notificationConfig.mailConfig.mentions = command.mentions
        notificationConfig.mailConfig.followNew = command.followNew
        notificationConfig.mailConfig.proposalComment = command.proposalComment
        notificationConfig.mailConfig.proposalLike = command.proposalLike
        notificationConfig.mailConfig.proposalPinned = command.proposalPinned
        notificationConfig.mailConfig.proposalNew = command.proposalNew
        notificationConfig.mailConfig.proposalNewOwner = command.proposalNewOwner
        notificationConfig.mailConfig.debateNewOwner = command.debateNewOwner
        notificationConfig.mailConfig.debateNewCause = command.debateNewCause
        notificationConfig.mailConfig.postNewOwner = command.postNewOwner
        notificationConfig.mailConfig.postNewCause = command.postNewCause
        notificationConfig.mailConfig.postLike = command.postLike
        notificationConfig.mailConfig.eventNewOwner = command.eventNewOwner
        notificationConfig.mailConfig.eventNewCause = command.eventNewCause
        notificationConfig.mailConfig.surveyNewOwner = command.surveyNewOwner
        notificationConfig.mailConfig.participatoryBudgetNewOwner = command.participatoryBudgetNewOwner
        notificationConfig.mailConfig.participatoryBudgetNewCause = command.participatoryBudgetNewCause
        notificationConfig.mailConfig.districtProposalNewOwner = command.districtProposalNewOwner
        notificationConfig.mailConfig.districtProposalNewCause = command.districtProposalNewCause
        notificationConfig.mailConfig.districtProposalParticipatoryBudgetOwner = command.districtProposalParticipatoryBudgetOwner
        notificationConfig.mailConfig.districtProposalSupport = command.districtProposalSupport
        notificationConfig.mailConfig.districtProposalVote = command.districtProposalVote
        notificationConfig.mailConfig.petitionSign = command.petitionSign
        notificationConfig.mailConfig.petitionNewCause = command.petitionNewCause
        notificationConfig.mailConfig.petitionNewOwner = command.petitionNewOwner
        notificationConfig.mailConfig.surveyNewOwner = command.surveyNewOwner
        notificationConfig.mailConfig.surveyNewCause = command.surveyNewCause
        notificationService.saveNotificationsConfig(loggedUser, notificationConfig)
        flash.message = message(code:'profile.emailNotifications.success')
        redirect mapping:'profileEmailNotifications'
    }

    def deleteAccount(){
        [command: new DeleteAccountCommand()]
    }

    def deleteAccountPost(DeleteAccountCommand command){
        KuorumUserSession loggedUser = springSecurityService.principal
        if (command.hasErrors()){
            render view:'deleteAccount', model:[command:command]
            return
        }
        kuorumMailService.sendFeedbackMail(loggedUser, command.explanation, command.forever)
        if (command.forever){
            kuorumUserService.deleteAccount(loggedUser)
            flash.message=message(code:'profile.deleteAccount.deleteForever.success')
            redirect(mapping: 'logout')
        }else{
            flash.message=message(code:'profile.deleteAccount.oneChance.success')
            redirect(mapping: 'home')
        }
    }


    def editNews(){
        KuorumUser user = params.user
        [command:new RelevantEventsCommand(politicianRelevantEvents: user.relevantEvents?.reverse()?:[])]
    }

    def updateNews(RelevantEventsCommand command){
        command.politicianRelevantEvents = command.politicianRelevantEvents.findAll{it}
        KuorumUser user = params.user
        if (!command.validate() || !user ){
            render view:"/profile/editRelevantEvents", model:[command:command]
            return
        }
        politicianService.updateNews(params.user, command.politicianRelevantEvents)
        flash.message=message(code:'profile.editUser.success')
        redirect mapping:'profileNews'
    }

    def editNewsletterConfig(){
        KuorumUserSession user = springSecurityService.principal
        NewsletterConfigRSDTO config = newsletterService.findNewsletterConfig(user)
        NewsletterConfigCommand command = new NewsletterConfigCommand()
        use(InvokerHelper) {
            command.setProperties(config.properties)
        }
        [command:command]
    }

    def updateNewsletterConfig(NewsletterConfigCommand command){
        if (command.hasErrors()){
            render(view: 'editNewsletterConfig', model: [command: command])
            return
        }
        KuorumUserSession user = springSecurityService.principal
        NewsletterConfigRSDTO configRSDTO = newsletterService.findNewsletterConfig(user)
        NewsletterConfigRQDTO config = new NewsletterConfigRQDTO()
        use(InvokerHelper) {
            config.setProperties(command.properties)
        }
        config.setEmailSenderRequested(configRSDTO.getEmailSenderRequested())
        newsletterService.updateNewsletterConfig(user,config)
        flash.message="Success"
        redirect(mapping:'profileNewsletterConfig')
    }

    def domainUserValidChecker(){

        if (SpringSecurityUtils.ifAnyGranted('ROLE_USER_VALIDATED')){
            render ([validated:true, success:true, pendingValidations:[]] as JSON)
        }else{
            KuorumUserSession sessionUser = springSecurityService.principal
            render ([validated:false, success:true,
                     pendingValidations:[
                             censusValidation: sessionUser.censusValid,
                             phoneValidation: sessionUser.phoneValid

                     ]
            ] as JSON)
        }
    }

    def validateUser(DomainValidationCommand domainValidationCommand){
        if (domainValidationCommand.hasErrors()){
            render ([validated:false, success: false, msg:message(error:  domainValidationCommand.getErrors().getAllErrors().get(0))] as JSON)
            return
        }
        KuorumUserSession userSession = springSecurityService.principal
        Boolean isValidated = kuorumUserService.userDomainValidation(userSession, domainValidationCommand.ndi, domainValidationCommand.postalCode, domainValidationCommand.birthDate)
        userSession = springSecurityService.principal // Refresh user info
        render ([
                success: userSession.censusValid,
                validated:isValidated,
                msg:userSession.censusValid?"Success validation":g.message(code:'kuorum.web.commands.profile.DomainValidationCommand.validationError'),
                pendingValidations:[
                        censusValidation: userSession.censusValid,
                        phoneValidation:userSession.phoneValid

                ]] as JSON)
    }

    def validateUserPhoneSendSMS(DomainUserPhoneValidationCommand domainUserPhoneValidationCommand){
        KuorumUserSession userSession = springSecurityService.principal
        try{
            String hash = kuorumUserService.sendSMSWithValidationCode(userSession, domainUserPhoneValidationCommand.phoneNumber)
            render ([validated: false, success:true, hash:hash] as JSON)
        }catch(Exception e){
            Exception realCause = e?.cause?.cause?:null
            render ([validated: false, success:false, hash:null, msg:realCause?.getMessage()] as JSON)
        }
    }

    def validateUserPhone(DomainUserPhoneCodeValidationCommand domainUserPhoneValidationCommand){
        if (domainUserPhoneValidationCommand.hasErrors()){
            render ([validated: false, success: false, msg:message(error:  domainUserPhoneValidationCommand.getErrors().getAllErrors().get(0))] as JSON)
            return
        }
        KuorumUserSession userSession = springSecurityService.principal
        Boolean isValidated = kuorumUserService.userPhoneDomainValidation(userSession, domainUserPhoneValidationCommand.phoneHash, domainUserPhoneValidationCommand.phoneCode)
        userSession = springSecurityService.principal // Refresh user info
        render ([
                success: userSession.phoneValid,
                validated: isValidated,
                msg:userSession.phoneValid?"Success validation":g.message(code:'kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand.validationError'),
                pendingValidations:[
                        censusValidation:userSession.censusValid,
                        phoneValidation:userSession.phoneValid

                ]] as JSON)
    }
}
