package kuorum.admin

import grails.plugin.springsecurity.annotation.Secured
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.core.model.UserType
import kuorum.domain.DomainService
import kuorum.mail.KuorumMailAccountService
import kuorum.mail.MailchimpService
import kuorum.register.RegisterService
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserService
import kuorum.web.admin.KuorumUserEmailSenderCommand
import kuorum.web.admin.KuorumUserRightsCommand
import kuorum.web.admin.domain.DomainConfigCommand
import org.kuorum.rest.model.admin.AdminConfigMailingRDTO
import org.kuorum.rest.model.domain.DomainRDTO
import org.kuorum.rest.model.domain.DomainRSDTO
import org.kuorum.rest.model.domain.SocialRDTO
import org.kuorum.rest.model.notification.KuorumMailAccountDetailsRSDTO
import org.kuorum.rest.model.notification.campaign.config.NewsletterConfigRSDTO
import payment.campaign.NewsletterService

@Secured(['ROLE_ADMIN'])
class AdminController {

    def indexSolrService
    def springSecurityService
    MailchimpService mailchimpService

    KuorumMailAccountService kuorumMailAccountService
    KuorumUserService kuorumUserService

    NewsletterService newsletterService;

    DomainService domainService

    RegisterService registerService

//    def afterInterceptor = [action: this.&prepareMenuData]
//    protected prepareMenuData = {model, modelAndView ->
    def afterInterceptor = { model, modelAndView ->

    }

    def index() {
        log.info("Index admin")
        render view: '/admin/index', model:[]
    }

    def domainConfig(){
        DomainRSDTO domainRSDTO = domainService.getConfig(CustomDomainResolver.domain)
        DomainConfigCommand domainConfigCommand = new DomainConfigCommand();
        domainConfigCommand.name = domainRSDTO.name
        domainConfigCommand.language = domainRSDTO.language
        domainConfigCommand.slogan = domainRSDTO.slogan
        domainConfigCommand.subtitle = domainRSDTO.subtitle
        domainConfigCommand.mainColor = domainRSDTO.mainColor
        domainConfigCommand.mainColorShadowed = domainRSDTO.mainColorShadowed
        domainConfigCommand.secondaryColor = domainRSDTO.secondaryColor
        domainConfigCommand.secondaryColorShadowed = domainRSDTO.secondaryColorShadowed
        domainConfigCommand.facebook = domainRSDTO.social?.facebook
        domainConfigCommand.twitter = domainRSDTO.social?.twitter
        domainConfigCommand.linkedIn = domainRSDTO.social?.linkedIn
        domainConfigCommand.googlePlus = domainRSDTO.social?.googlePlus
        domainConfigCommand.instagram = domainRSDTO.social?.instagram
        domainConfigCommand.youtube = domainRSDTO.social?.youtube
        [command:domainConfigCommand]

    }

    def domainConfigSave(DomainConfigCommand command){
        if (command.hasErrors()){
            render view:'domainConfig', model:[command:command]
            return;
        }
        DomainRDTO domainRDTO = new DomainRDTO()
        domainRDTO.name = command.name
        domainRDTO.language = command.language
        domainRDTO.slogan = command.slogan
        domainRDTO.subtitle = command.subtitle
        domainRDTO.mainColor = command.mainColor
        domainRDTO.mainColorShadowed = command.mainColorShadowed
        domainRDTO.secondaryColor = command.secondaryColor
        domainRDTO.secondaryColorShadowed = command.secondaryColorShadowed
        domainRDTO.social = new SocialRDTO()
        domainRDTO.social.facebook = command.facebook
        domainRDTO.social.twitter = command.twitter
        domainRDTO.social.linkedIn = command.linkedIn
        domainRDTO.social.googlePlus = command.googlePlus
        domainRDTO.social.instagram = command.instagram
        domainRDTO.social.youtube = command.youtube
        domainService.updateConfig(domainRDTO)
        flash.message ="Success"
        redirect mapping:'adminDomainConfig'
    }

    @Secured(['ROLE_ADMIN', 'ROLE_SUPER_ADMIN'])
    def solrIndex(){
    }

    @Secured(['ROLE_SUPER_ADMIN'])
    def updateMailChimp(){
        KuorumUser loggedUser = KuorumUser.get(springSecurityService.principal.id)
        mailchimpService.updateAllUsers(loggedUser)
        flash.message="Se ha puesto en marcha el proceso de actualización de mail chimp. Recibirá un email cuando termine"
        redirect mapping:"adminPrincipal"
    }

    @Secured(['ROLE_SUPER_ADMIN'])
    def fullIndex(){
        def res = indexSolrService.fullIndex()
        render view: '/admin/solrIndex'
    }

    @Secured(['ROLE_SUPER_ADMIN'])
    def editUserRights(String userAlias){
        KuorumUser user = kuorumUserService.findByAlias(userAlias)
        KuorumUserRightsCommand command = new KuorumUserRightsCommand()
        command.user = user
        command.active = user.enabled
        KuorumMailAccountDetailsRSDTO account = kuorumMailAccountService.getAccountDetails(user)
        command.emailAccountActive = account?.active?:false
        command.authorities = user.authorities
        command.relevance = kuorumUserService.getUserRelevance(user)
        [command:command]
    }

    @Secured(['ROLE_SUPER_ADMIN'])
    def updateUserRights( KuorumUserRightsCommand command){

        if (command.hasErrors()){
            render view:"editUserRights", model:[command:command]
            return
        }
        KuorumUser user = command.user
        if (command.emailAccountActive){
            kuorumMailAccountService.activateAccount(user)
        }else{
            kuorumMailAccountService.deleteAccount(user)
        }
        user.enabled = command.active?:false
        user.authorities = command.authorities
        if (command.password){
            user.password = registerService.encodePassword(user, command.password)
        }

        user.userType = UserType.PERSON

        if (command.relevance){
            kuorumUserService.updateUserRelevance(user, command.relevance)
        }
        user = kuorumUserService.updateUser(user);

        flash.message =message(code:'admin.editUser.success', args: [user.name])

        redirect(mapping:'editorAdminUserRights', params:user.encodeAsLinkProperties())
    }

    @Secured(['ROLE_SUPER_ADMIN'])
    @Deprecated
    def editUserEmailSender(String userAlias){
        Boolean requestState;
        KuorumUser user = kuorumUserService.findByAlias(userAlias)
        KuorumUserEmailSenderCommand command = new KuorumUserEmailSenderCommand()
        NewsletterConfigRSDTO configRSDTO = newsletterService.findNewsletterConfig(user);
        command.user = user;
        command.emailSender = configRSDTO.getEmailSender();
        requestState = configRSDTO.getEmailSenderRequested();
        [
                command: command,
                requestState: requestState
        ]
    }

    @Secured(['ROLE_SUPER_ADMIN'])
    @Deprecated
    def updateUserEmailSender(KuorumUserEmailSenderCommand command){
        KuorumUser user = command.user;
        AdminConfigMailingRDTO adminRDTO = new AdminConfigMailingRDTO();
        adminRDTO.setEmailSender(command.emailSender);

        newsletterService.updateNewsletterConfig(user, adminRDTO);

        redirect(mapping:'editorAdminEmailSender', params:user.encodeAsLinkProperties())
    }

}
