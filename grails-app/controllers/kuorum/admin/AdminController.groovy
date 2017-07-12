package kuorum.admin

import grails.plugin.springsecurity.annotation.Secured
import kuorum.core.model.UserType
import kuorum.mail.KuorumMailAccountService
import kuorum.mail.MailchimpService
import kuorum.register.RegisterService
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserAudit
import kuorum.users.KuorumUserService
import kuorum.users.PoliticianService
import kuorum.web.admin.KuorumUserEmailSenderCommand
import kuorum.web.admin.KuorumUserRightsCommand
import org.kuorum.rest.model.notification.KuorumMailAccountDetailsRSDTO
import org.kuorum.rest.model.notification.campaign.config.NewsletterConfigRSDTO
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest
import payment.campaign.MassMailingService

@Secured(['ROLE_ADMIN'])
class AdminController {

    def indexSolrService
    def springSecurityService
    MailchimpService mailchimpService

    KuorumMailAccountService kuorumMailAccountService
    KuorumUserService kuorumUserService

    MassMailingService massMailingService;

    PoliticianService politicianService

    RegisterService registerService

//    def afterInterceptor = [action: this.&prepareMenuData]
//    protected prepareMenuData = {model, modelAndView ->
    def afterInterceptor = { model, modelAndView ->

    }

    def index() {
        log.info("Index admin")
        render view: '/admin/index', model:[]
    }

    def solrIndex(){
        [res:[:]]
    }

    def updateMailChimp(){
        KuorumUser loggedUser = KuorumUser.get(springSecurityService.principal.id)
        mailchimpService.updateAllUsers(loggedUser)
        flash.message="Se ha puesto en marcha el proceso de actualización de mail chimp. Recibirá un email cuando termine"
        redirect mapping:"adminPrincipal"
    }

    def fullIndex(){
        def res = indexSolrService.fullIndex()
        render view: '/admin/solrIndex', model:[res:res]
    }

    def uploadPoliticianCsv(){
        MultipartFile uploadedFile = ((MultipartHttpServletRequest) request).getFile('filecsv')
        if (uploadedFile.empty) {
            flash.message = 'file cannot be empty'
            render(view: 'solrIndex')
            return
        }
        KuorumUser loggedUser = KuorumUser.get(springSecurityService.principal.id)
        politicianService.asyncUploadPoliticianCSV(loggedUser,uploadedFile.inputStream)
        flash.message = "CSV ${uploadedFile.originalFilename} uploaded. An email will be sent at the end of the process"
        redirect(mapping:"adminSearcherIndex")
//        render view: "csvPoliticiansLoaded", model: [politiciansOk:politiciansOk,politiciansWrong:politiciansWrong, fileName:uploadedFile.getOriginalFilename()]
    }

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
        if (user.authorities.collect{it.authority}.contains("ROLE_POLITICIAN")) {
            user.userType = UserType.POLITICIAN
            user.personalData.userType = UserType.POLITICIAN
        }else if(user.authorities.collect{it.authority}.contains("ROLE_CANDIDATE")){
            user.userType = UserType.CANDIDATE
            user.personalData.userType = UserType.CANDIDATE
        }else{
            user.userType = UserType.PERSON
            user.personalData.userType = UserType.PERSON
        }
        if (command.relevance){
            kuorumUserService.updateUserRelevance(user, command.relevance)
        }
        user = kuorumUserService.updateUser(user);

        flash.message =message(code:'admin.editUser.success', args: [user.name])

        redirect(mapping:'editorAdminUserRights', params:user.encodeAsLinkProperties())
    }

    def editorsMonitoring(){
        [audits: KuorumUserAudit.findAllByDateCreatedGreaterThan(new Date()-31, [sort: "id", order: "desc"])]
    }

    def editUserEmailSender(String userAlias){
        Boolean requestState;
        KuorumUser user = kuorumUserService.findByAlias(userAlias)
        KuorumUserEmailSenderCommand command = new KuorumUserEmailSenderCommand()
        NewsletterConfigRSDTO configRSDTO = massMailingService.findNewsletterConfig(user);
        command.user = user;
        command.emailSender = configRSDTO.getEmailSender();
        requestState = configRSDTO.getEmailSenderRequested();
        [
                command: command,
                requestState: requestState
        ]
    }

    def updateUserEmailSender(KuorumUserEmailSenderCommand command){
        KuorumUser user = command.user;
        NewsletterConfigRSDTO configRSDTO = massMailingService.findNewsletterConfig(user);
        configRSDTO.setEmailSender(command.emailSender);

        massMailingService.updateNewsletterConfig(user, configRSDTO);

        redirect(mapping:'editorAdminEmailSender', params:user.encodeAsLinkProperties())
    }

}
