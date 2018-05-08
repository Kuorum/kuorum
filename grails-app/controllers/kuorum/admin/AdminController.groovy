package kuorum.admin

import grails.plugin.springsecurity.annotation.Secured
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.domain.DomainService
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
import org.kuorum.rest.model.admin.AdminConfigMailingRDTO
import org.kuorum.rest.model.domain.DomainRDTO
import org.kuorum.rest.model.domain.DomainRSDTO
import org.kuorum.rest.model.domain.SocialRDTO
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

//    AmazonFileService amazonFileService;
//
//    FaviconService faviconService
//
//    UnzipService unzipService

    DomainResourcesService domainResourcesService


//    def afterInterceptor = [action: this.&prepareMenuData]
//    protected prepareMenuData = {model, modelAndView ->
    def afterInterceptor = { model, modelAndView ->

    }

    def index() {
        redirect mapping:'adminDomainConfig'
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


    def editLogo() {
//        redirect mapping:'adminDomainConfigUploadLogo'
//        DomainRDTO domainRDTO = new DomainRDTO()
//        EditLogoCommand command = new EditLogoCommand(domainRDTO)
//        EditProfilePicturesCommand command = new EditProfilePicturesCommand(user)
//        [command: command]
    }

//    def updateLogo(EditLogoCommand command){
//        DomainRDTO domainRDTO = new DomainRDTO()
////        if (command.hasErrors()){
////            render view:"editLogo", model: [command:command,domainRDTO:domainRDTO]
////            return
////        }
//        prepareDomainLogo(domainRDTO,command, fileService)
//        domainService.updateConfig(domainRDTO)
//        flash.message=message(code:'admin.menu.domainConfig.uploadLogo.success')
//        redirect mapping:'adminDomainConfigUploadLogo'
//
////        KuorumUser user = params.user
////        if (command.hasErrors()){
////            render view:"editPictures", model: [command:command,user:user]
////            return
////        }
////        prepareUserImages(user,command, fileService)
//        kuorumUserService.updateUser(user)
////        flash.message=message(code:'profile.editUser.success')
////        redirect mapping:'profilePictures'
//
//    }


    def uploadLogo() {

        CommonsMultipartFile customLogo = request.getFile('logo')
        try {
            if (customLogo && !customLogo.empty) {

                domainResourcesService.uploadLogoFile(customLogo.getInputStream())
                flash.message = message(code: 'admin.menu.domainConfig.uploadLogo.success')
            } else {
                flash.message = message(code: 'admin.menu.domainConfig.uploadLogo.unsuccess')
            }
            redirect mapping: 'adminDomainConfig'
        }catch (Exception e) {
            log.error(e)
        }
//        try {
//            if (customLogo && !customLogo.empty) {
//                //Añadir carpeta temporal para manejo de archivos
//                Path temp = Files.createTempDirectory("logoTemp");
//                File file = new File("/${temp}/logo.png")
//                File fileZip = new File("/${temp}/favicon.zip")
////                Path temp = Files.createTempDirectory("favicon");
////                File temp = File.createTempFile("temp", Long.toString(System.nanoTime()));
//                customLogo.transferTo(file)
//                DomainRSDTO domain = domainService.getConfig(CustomDomainResolver.domain)
//                String amazonLogoUrl = amazonFileService.uploadDomainLogo(file, domain.domain)
//                String zipFileUrlRaw = faviconService.sendIcon(amazonLogoUrl)
//
//                try {
//                    RESTClient http = new RESTClient('https://realfavicongenerator.net/files')
//                    http.ignoreSSLIssues()
//                    def response = http.get(path:zipFileUrlRaw,
//                            headers: ["User-Agent": "Kuorum Web"],
//                            query:[:])
//                    final ByteArrayInputStream responseStream = (ByteArrayInputStream) response.data
//                    IOUtils.copy(responseStream, new FileOutputStream(fileZip));
//                    responseStream.close()
//                }catch (Exception e){
//                    log.error(e)
//
//                    //TODO: HANDLE ERROR
//                }
////                unzipService.unzipFile("/tmp/favicon.zip","/home/guille/Escritorio/favicon")
////                String tempFolder = temp;
//                unzipService.unzipFile(fileZip,temp)
//
//                List<File> files = Arrays.asList(temp.toFile().listFiles());
////                List<File> files = Arrays.asList(tempFiles.listFiles());
//                for (File f : files) {
//                    amazonFileService.uploadDomainFaviconFile(f, domain.domain)
//                }
//                flash.message = message(code: 'admin.menu.domainConfig.uploadLogo.success')
//            } else {
//                flash.message = message(code: 'admin.menu.domainConfig.uploadLogo.unsuccess')
//            }
//            redirect mapping: 'adminDomainConfig'
//        }
//        catch (Exception e) {
//            log.error("Your exception message goes here", e)
//        }
//        redirect mapping: 'adminDomainConfig'
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
