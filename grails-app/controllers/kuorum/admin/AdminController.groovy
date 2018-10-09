package kuorum.admin

import grails.plugin.springsecurity.annotation.Secured
import kuorum.KuorumFile
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.domain.DomainService
import kuorum.files.AmazonFileService
import kuorum.files.DomainResourcesService
import kuorum.files.FileService
import kuorum.mail.KuorumMailAccountService
import kuorum.mail.MailchimpService
import kuorum.register.RegisterService
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserService
import kuorum.web.admin.KuorumUserEmailSenderCommand
import kuorum.web.admin.KuorumUserRightsCommand
import kuorum.web.admin.domain.DomainConfigCommand
import kuorum.web.admin.domain.DomainLandingCommand
import kuorum.web.admin.domain.EditLegalInfoCommand
import kuorum.web.commands.LinkCommand
import kuorum.web.commands.domain.EditDomainCarouselPicturesCommand
import org.kuorum.rest.model.admin.AdminConfigMailingRDTO
import org.kuorum.rest.model.domain.*
import org.kuorum.rest.model.notification.campaign.config.NewsletterConfigRSDTO
import org.springframework.web.multipart.commons.CommonsMultipartFile
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

    FileService fileService

    DomainResourcesService domainResourcesService

    AmazonFileService amazonFileService


//    def afterInterceptor = [action: this.&prepareMenuData]
//    protected prepareMenuData = {model, modelAndView ->
    def afterInterceptor = { model, modelAndView ->

    }

    @Secured(['ROLE_ADMIN'])
    def index() {
        redirect mapping:'adminDomainConfig'
    }

    @Secured(['ROLE_ADMIN'])
    def domainConfig(){
        DomainRSDTO domainRSDTO = domainService.getConfig(CustomDomainResolver.domain)
        DomainConfigCommand domainConfigCommand = new DomainConfigCommand();
        domainConfigCommand.name = domainRSDTO.name
        domainConfigCommand.validation = domainRSDTO.validation
        domainConfigCommand.language = domainRSDTO.language
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

    @Secured(['ROLE_ADMIN'])
    def domainConfigSave(DomainConfigCommand command){
        if (command.hasErrors()){
            render view:'domainConfig', model:[command:command]
            return;
        }
        DomainRDTO domainRDTO = getPopulatedDomainRDTO()
        domainRDTO.name = command.name
        domainRDTO.validation = command.validation?:false
        domainRDTO.language = command.language
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

    @Secured(['ROLE_ADMIN'])
    def editLandingInfo(){
        DomainRSDTO domainRSDTO = domainService.getConfig(CustomDomainResolver.domain)
        DomainLandingCommand domainLandingCommand = new DomainLandingCommand();
        domainLandingCommand.slogan = domainRSDTO.slogan
        domainLandingCommand.subtitle = domainRSDTO.subtitle
        domainLandingCommand.domainDescription = domainRSDTO.domainDescription
        domainLandingCommand.footerLinks = domainRSDTO.footerLinks.collect{new LinkCommand(title: it.key, url: it.value)}
        [command:domainLandingCommand]
    }

    @Secured(['ROLE_ADMIN'])
    def editLandingInfoSave(DomainLandingCommand command){
        if (command.hasErrors()){
            render view:'editLandingInfo', model:[command:command]
            return;
        }

        DomainRDTO domainRDTO = getPopulatedDomainRDTO()
        domainRDTO.slogan = command.slogan
        domainRDTO.subtitle = command.subtitle
        domainRDTO.domainDescription = command.domainDescription
        domainRDTO.footerLinks = command.footerLinks?.findAll{it}?.collectEntries {[(it.title): it.url]}?:null



        domainService.updateConfig(domainRDTO)
        flash.message ="Success"
        redirect mapping:'adminDomainConfigLanding'
    }

    private DomainRDTO getPopulatedDomainRDTO(){
        DomainRSDTO domainRSDTO = domainService.getConfig(CustomDomainResolver.domain)
        DomainRDTO domainRDTO = new DomainRDTO()
        domainRDTO.name = domainRSDTO.name
        domainRDTO.validation = domainRSDTO.validation
        domainRDTO.language = domainRSDTO.language
        domainRDTO.mainColor = domainRSDTO.mainColor
        domainRDTO.mainColorShadowed = domainRSDTO.mainColorShadowed
        domainRDTO.secondaryColor = domainRSDTO.secondaryColor
        domainRDTO.secondaryColorShadowed = domainRSDTO.secondaryColorShadowed
        domainRDTO.social = domainRSDTO.social
        domainRDTO.slogan = domainRSDTO.slogan
        domainRDTO.subtitle = domainRSDTO.subtitle
        domainRDTO.domainDescription= domainRSDTO.domainDescription
        domainRDTO.footerLinks = domainRSDTO.footerLinks

        return domainRDTO;
    }

    @Secured(['ROLE_ADMIN'])
    def editLegalInfo() {
        DomainLegalInfoRSDTO domainLegalInfoRDSTO = domainService.getLegalInfo(CustomDomainResolver.domain)
        EditLegalInfoCommand editLegalInfoCommand = new EditLegalInfoCommand();
        editLegalInfoCommand.address = domainLegalInfoRDSTO?.address
        editLegalInfoCommand.city = domainLegalInfoRDSTO?.city
        editLegalInfoCommand.country = domainLegalInfoRDSTO?.country
        editLegalInfoCommand.domainOwner = domainLegalInfoRDSTO?.domainOwner
        editLegalInfoCommand.fileName = domainLegalInfoRDSTO?.fileName
        editLegalInfoCommand.filePurpose = domainLegalInfoRDSTO?.filePurpose
        editLegalInfoCommand.fileResponsibleEmail = domainLegalInfoRDSTO?.fileResponsibleEmail
        editLegalInfoCommand.fileResponsibleName = domainLegalInfoRDSTO?.fileResponsibleName
        [command:editLegalInfoCommand]
    }

    @Secured(['ROLE_ADMIN'])
    def updateLegalInfo(EditLegalInfoCommand command) {
        if (command.hasErrors()){
            render view:'editLegalInfo', model:[command:command]
            return;
        }
        DomainLegalInfoRDTO domainLegalInfoRDTO = new DomainLegalInfoRDTO()
        domainLegalInfoRDTO.address = command.address
        domainLegalInfoRDTO.city = command.city
        domainLegalInfoRDTO.country = command.country
        domainLegalInfoRDTO.domainOwner = command.domainOwner
        domainLegalInfoRDTO.fileName = command.fileName
        domainLegalInfoRDTO.filePurpose = command.filePurpose
        domainLegalInfoRDTO.fileResponsibleEmail = command.fileResponsibleEmail
        domainLegalInfoRDTO.fileResponsibleName = command.fileResponsibleName
        domainService.updateLegalInfo(domainLegalInfoRDTO)
        flash.message ="Success"
        redirect mapping:'adminDomainConfigLegalInfo'
    }

    @Secured(['ROLE_SUPER_ADMIN'])
    def editLogo() {
    }

    @Secured(['ROLE_SUPER_ADMIN'])
    def uploadLogo() {

        CommonsMultipartFile customLogo = request.getFile('logo')
        try {
            if (customLogo && !customLogo.empty) {
                domainResourcesService.uploadLogoFile(customLogo.getInputStream())
                flash.message = message(code: 'admin.menu.domainConfig.uploadLogo.success')
            } else {
                flash.error = message(code: 'admin.menu.domainConfig.uploadLogo.unsuccess')
            }
            redirect mapping: 'adminDomainConfig'
        }catch (Exception e) {
            log.error(e)
        }
    }

    @Secured(['ROLE_SUPER_ADMIN'])
    def editCarousel() {
        String domain = CustomDomainResolver.domain
        KuorumFile slide1 = domainResourcesService.getSlidePath(domain,1)
        KuorumFile slide2 = domainResourcesService.getSlidePath(domain,2)
        KuorumFile slide3 = domainResourcesService.getSlidePath(domain,3)
        EditDomainCarouselPicturesCommand command = new EditDomainCarouselPicturesCommand()
        command.slideId1 = slide1?.id?.toString()?:null;
        command.slideId2 = slide2?.id?.toString()?:null;
        command.slideId3 = slide3?.id?.toString()?:null;
        [command: command]
    }

    @Secured(['ROLE_SUPER_ADMIN'])
    def uploadCarousel(EditDomainCarouselPicturesCommand command) {
        if (command.hasErrors()){
            render view: "editCarousel", model: [command:command]
        }
        else {
            String domain = CustomDomainResolver.domain
            KuorumFile slideFile1 = KuorumFile.get(command.slideId1)
            KuorumFile slideFile2 = KuorumFile.get(command.slideId2)
            KuorumFile slideFile3 = KuorumFile.get(command.slideId3)
            domainResourcesService.uploadCarouselImages(slideFile1, slideFile2, slideFile3, domain)
            flash.message = "Sus imágenes se subieron correctamente"
            redirect mapping: 'adminDomainConfig'
        }
    }


    @Secured(['ROLE_SUPER_ADMIN'])
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
        command.relevance = kuorumUserService.getUserRelevance(user)
        [command: command]
    }

    @Secured(['ROLE_SUPER_ADMIN'])
    def updateUserRights(KuorumUserRightsCommand command) {

        if (command.hasErrors()) {
            render view: "editUserRights", model: [command: command]
            return
        }
        KuorumUser user = command.user
        user.enabled = command.active?:false
        if (command.relevance){
            kuorumUserService.updateUserRelevance(user, command.relevance)
        }
        user = kuorumUserService.updateUser(user);

        flash.message = message(code: 'admin.editUser.success', args: [user.name])

        redirect(mapping: 'editorAdminUserRights', params: user.encodeAsLinkProperties())
    }

    @Secured(['ROLE_SUPER_ADMIN'])
    @Deprecated
    def editUserEmailSender(String userAlias) {
        Boolean requestState;
        KuorumUser user = kuorumUserService.findByAlias(userAlias)
        KuorumUserEmailSenderCommand command = new KuorumUserEmailSenderCommand()
        NewsletterConfigRSDTO configRSDTO = newsletterService.findNewsletterConfig(user);
        command.user = user;
        command.emailSender = configRSDTO.getEmailSender();
        requestState = configRSDTO.getEmailSenderRequested();
        [
                command     : command,
                requestState: requestState
        ]
    }

    @Secured(['ROLE_SUPER_ADMIN'])
    @Deprecated
    def updateUserEmailSender(KuorumUserEmailSenderCommand command) {
        KuorumUser user = command.user;
        AdminConfigMailingRDTO adminRDTO = new AdminConfigMailingRDTO();
        adminRDTO.setEmailSender(command.emailSender);

        newsletterService.updateNewsletterConfig(user, adminRDTO);

        redirect(mapping: 'editorAdminEmailSender', params: user.encodeAsLinkProperties())
    }

}
