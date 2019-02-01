package kuorum.mail

import grails.transaction.Transactional
import kuorum.core.model.AvailableLanguage
import kuorum.register.KuorumUserSession
import kuorum.solr.IndexSolrService
import kuorum.users.KuorumUser
import org.codehaus.groovy.grails.web.mapping.LinkGenerator
import org.kuorum.rest.model.kuorumUser.BasicDataKuorumUserRSDTO
import org.kuorum.rest.model.notification.mail.sent.MailTypeRSDTO
import org.kuorum.rest.model.notification.mail.sent.SendMailRSDTO
import org.kuorum.rest.model.notification.mail.sent.SentUserMailRSDTO
import org.springframework.context.MessageSource

@Transactional
class KuorumMailService {

    String DEFAULT_SENDER_NAME="Kuorum.org"
    String DEFAULT_VIA="via Kuorum.org"

    LinkGenerator grailsLinkGenerator
    MandrillAppService mandrillAppService
    MessageSource messageSource
    IndexSolrService indexSolrService
    def grailsApplication

    def sendBatchMail(KuorumUser user, String rawMail, String subject){
        MailUserData mailUserData = new MailUserData(user:user)
        MailData mailData = new MailData(fromName:DEFAULT_SENDER_NAME , mailType: MailType.BATCH_PROCESS, globalBindings: [body: rawMail, subject:subject], userBindings: [mailUserData])
        mandrillAppService.sendTemplate(mailData)
    }

    def sendFeedbackMail(KuorumUser user, String feedback, boolean userDeleted = false){
        def bindings = [
                feedbackUserLink:generateLink("userShow",user.encodeAsLinkProperties()),
                feedbackUser:user.name,
                feedbackText:feedback,
                userDeleted:userDeleted
        ]
        MailUserData mailUserData = new MailUserData(user:getPurchaseUser())
        MailData mailData = new MailData(fromName:user.name, mailType: MailType.FEEDBACK, globalBindings: bindings, userBindings: [mailUserData])
        mandrillAppService.sendTemplate(mailData)
    }

    def sendWelcomeRegister(KuorumUser user){
        def bindings = [
                user:user.name
        ]
        MailUserData mailUserData = new MailUserData(user:user)
        MailData mailData = new MailData(fromName:DEFAULT_SENDER_NAME, mailType: MailType.REGISTER_WELLCOME, globalBindings: bindings, userBindings: [mailUserData])
        mandrillAppService.sendTemplate(mailData)
    }

    private KuorumUser getFeedbackUser(String name = "Feedback"){
        //Chapu
        KuorumUser user = new KuorumUser(
                name: name,
                email: "${grailsApplication.config.kuorum.contact.feedback}",
                language: AvailableLanguage.es_ES
        )
    }

    private KuorumUser getPurchaseUser(){
        //Chapu
        KuorumUser user = new KuorumUser(
                name: "${grailsApplication.config.kuorum.purchase.userName}",
                email: "${grailsApplication.config.kuorum.purchase.email}",
                language: AvailableLanguage.es_ES
        )
    }

    private KuorumUser buildMailUser(String name, String email, AvailableLanguage language){
        //Chapu
        KuorumUser user = new KuorumUser(
                name: name,
                email: email,
                language: language
        )
    }

    def sendRegisterUser(KuorumUser user, String confirmationLink){
        def bindings = [confirmationLink:confirmationLink]
        MailUserData mailUserData = new MailUserData(user:user, bindings:bindings)
        MailData mailData = new MailData(fromName:DEFAULT_SENDER_NAME,mailType: MailType.REGISTER_VERIFY_EMAIL, userBindings: [mailUserData])
        mandrillAppService.sendTemplate(mailData)
    }

    def sendResetPasswordMail(KuorumUser user, String resetLink){
        def bindings = [resetPasswordLink:resetLink]
        MailUserData mailUserData = new MailUserData(user:user, bindings:bindings)
        MailData mailData = new MailData(fromName:DEFAULT_SENDER_NAME,mailType: MailType.REGISTER_RESET_PASSWORD, userBindings: [mailUserData])
        mandrillAppService.sendTemplate(mailData)
    }

    def sendRequestACustomDomainAdmin(KuorumUser userRequestingDomain){
        String rawMessage = """
        <h1> Custom domain requested </h1>
        <ul>
            <li>Name: ${userRequestingDomain.name}</li>
            <li>Alias: <a href="${generateLink("userShow", userRequestingDomain.encodeAsLinkProperties())}" target="_blank">${userRequestingDomain.alias}</a></li>
            <li>Email: ${userRequestingDomain.email}</li>
            <li>Phone: ${userRequestingDomain.personalData?.telephone?:'--'}</li>
            <li>Lang: ${userRequestingDomain.language}</li>
        </ul>
        """
        sendBatchMail(getFeedbackUser("CUSTOM DOMAIN"), rawMessage, "Requested a custom domain: ${userRequestingDomain.name}")
    }

    def sendChangeEmailRequested(KuorumUser user, String newEmail){
        def bindings = [newEmailAccount:newEmail]
        MailUserData mailUserData = new MailUserData(user:user, bindings:bindings)
        MailData mailData = new MailData(fromName:DEFAULT_SENDER_NAME,mailType: MailType.REGISTER_CHANGE_EMAIL_REQUESTED, userBindings: [mailUserData])
        mandrillAppService.sendTemplate(mailData)
    }

    def sendChangeEmailVerification(KuorumUser user, String resetLink, String newMail){
        def bindings = [confirmationLink:resetLink]
        KuorumUser futureUser = new KuorumUser(user.properties)
        futureUser.email = newMail
        MailUserData mailUserData = new MailUserData(user:futureUser, bindings:bindings)
        MailData mailData = new MailData(fromName:DEFAULT_SENDER_NAME,mailType: MailType.REGISTER_CHANGE_EMAIL_VERIFY, userBindings: [mailUserData])
        mandrillAppService.sendTemplate(mailData)
    }

    def sendPoliticianContact(BasicDataKuorumUserRSDTO userContacted, KuorumUserSession user, String message, String cause){
        String contactLink = generateLink("userShow",user.encodeAsLinkProperties())
        def bindings = [:]
        SendMailRSDTO mailDataRSDTO = new SendMailRSDTO();
        mailDataRSDTO.mailType = MailTypeRSDTO.NOTIFICATION_CONTACT
        mailDataRSDTO.globalBindings = [
                "contactName":user.name,
                "contactLink":contactLink,
                "contactMessage":message,
                "causeName":cause
        ]
        SentUserMailRSDTO sentUserMailRSDTO = new SentUserMailRSDTO()
        sentUserMailRSDTO.name=userContacted.name
        sentUserMailRSDTO.email=userContacted.email
        sentUserMailRSDTO.lang=AvailableLanguage.valueOf(userContacted.language.toString()).locale.language?:"en"
        sentUserMailRSDTO.bindings=bindings
        mailDataRSDTO.destinations = [sentUserMailRSDTO]
        mandrillAppService.sendTemplate(mailDataRSDTO)
    }


    protected String generateLink(String mapping, linkParams) {
        grailsLinkGenerator.link(mapping:mapping,absolute: true,  params: linkParams)
    }

    protected String prepareFromName(String name){
        "$name $DEFAULT_VIA"
    }

}
