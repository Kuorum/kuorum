package kuorum.mail

import grails.transaction.Transactional
import kuorum.campaign.PollCampaignVote
import kuorum.core.model.AvailableLanguage
import kuorum.core.model.ContactSectorType
import kuorum.solr.IndexSolrService
import kuorum.users.KuorumUser
import org.codehaus.groovy.grails.web.mapping.LinkGenerator
import org.springframework.context.MessageSource

@Transactional
class KuorumMailService {

    String DEFAULT_SENDER_NAME="Kuorum.org"
    String DEFAULT_VIA="via Kuorum.org"

    LinkGenerator grailsLinkGenerator
    MandrillAppService mandrillAppService
    MailchimpService mailchimpService
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
        MailData mailData = new MailData(fromName:DEFAULT_SENDER_NAME, mailType: MailType.NOTIFICATION_OFFER_PURCHASED, globalBindings: bindings, userBindings: [mailUserData])
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

    def sendRegisterUserViaRRSS(KuorumUser user, String provider){
        MailUserData mailUserData = new MailUserData(user:user, bindings:[:])
        MailData mailData = new MailData(fromName:DEFAULT_SENDER_NAME,mailType: MailType.REGISTER_RRSS, userBindings: [mailUserData])
        mailData.globalBindings=[provider:provider]
        mandrillAppService.sendTemplate(mailData)
    }

    def sendResetPasswordMail(KuorumUser user, String resetLink){
        def bindings = [resetPasswordLink:resetLink]
        MailUserData mailUserData = new MailUserData(user:user, bindings:bindings)
        MailData mailData = new MailData(fromName:DEFAULT_SENDER_NAME,mailType: MailType.REGISTER_RESET_PASSWORD, userBindings: [mailUserData])
        mandrillAppService.sendTemplate(mailData)
    }

    def sendRequestADemo(String name, String email, AvailableLanguage language){
        def bindings = [:]
        KuorumUser user = buildMailUser(name, email, language)
        MailUserData mailUserData = new MailUserData(user:user, bindings:bindings)
        MailData mailData = new MailData(fromName:DEFAULT_SENDER_NAME,mailType: MailType.REGISTER_REQUEST_DEMO, userBindings: [mailUserData])
        mandrillAppService.sendTemplate(mailData)
    }
    def sendRequestADemoAdmin(
            String name,
            String surname,
            String email,
            String enterprise,
            ContactSectorType sector,
            String phone,
            String comment,
            AvailableLanguage language){
        String rawMessage = """
        <h1> Demo requested </h1>
        <ul>
            <li>Name: $name ${surname? "${surname} || $surname, $name" :''} </li>
            <li>Email: $email</li>
            <li>Enterprise: $enterprise ${sector?"(${sector})":''} </li>
            <li>Phone: $phone</li>
            <li>Lang: $language</li>
            <li>Comment: <p>${comment?:''}</p></li>
        </ul>
        """
        sendBatchMail(getFeedbackUser("DEMO"), rawMessage, "Requested a demo: ${name}");
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
        sendBatchMail(getFeedbackUser("CUSTOM DOMAIN"), rawMessage, "Requested a custom domain: ${userRequestingDomain.name}");
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

    def sendPoliticianContact(KuorumUser politician, KuorumUser user, String message, String cause){
        String contactLink = generateLink("userShow",user.encodeAsLinkProperties())
        String politicianLink = generateLink("userShow",politician.encodeAsLinkProperties())
        def bindings = [:]
        MailUserData mailUserData = new MailUserData(user:politician,bindings:bindings)
        MailData mailData = new MailData()
        mailData.mailType = MailType.NOTIFICATION_CONTACT
        mailData.globalBindings=[
                "contactName":user.name,
                "contactLink":contactLink,
                "contactMessage":message,
                "causeName":cause
        ]
        mailData.userBindings = [mailUserData]
        mailData.fromName = prepareFromName(user.name)
        mandrillAppService.sendTemplate(mailData)
    }

    def sendPollCampaignMail(PollCampaignVote pollCampaing){
        String politicianLink = generateLink("userShow",pollCampaing.politician.encodeAsLinkProperties())
        Map<String, String> bindings = [politician:pollCampaing.politician.name,politicianLink:politicianLink]
        for (String value : pollCampaing.campaign.values){
            if (pollCampaing.values.contains(value)){
                bindings.put(value, "OK")
            }
        }
        MailUserData mailUserData = new MailUserData()
        mailUserData.user = new KuorumUser(name:"", email: pollCampaing.userEmail)
        MailData mailData = new MailData()
        mailData.mailType = MailType.CAMPAIGN_POLL_THANK_YOU
        mailData.globalBindings=bindings
        mailData.userBindings = [mailUserData]
        mailData.fromName = DEFAULT_SENDER_NAME
        mandrillAppService.sendTemplate(mailData)
    }

    def mailingListUpdateUser(KuorumUser user){
        try{
            mailchimpService.addSubscriber(user)
        }catch(Exception e){
           log.error("Error actualizando usuario ${user} en MailChimp", e)
        }
    }



    protected String generateLink(String mapping, linkParams) {
        grailsLinkGenerator.link(mapping:mapping,absolute: true,  params: linkParams)
    }

    protected String prepareFromName(String name){
        "$name $DEFAULT_VIA"
    }

}
