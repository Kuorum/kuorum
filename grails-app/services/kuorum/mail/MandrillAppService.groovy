package kuorum.mail

import com.microtripit.mandrillapp.lutung.MandrillApi
import com.microtripit.mandrillapp.lutung.controller.MandrillMessagesApi
import com.microtripit.mandrillapp.lutung.view.MandrillMessage
import com.microtripit.mandrillapp.lutung.view.MandrillMessageStatus
import grails.transaction.Transactional
import kuorum.core.model.AvailableLanguage
import kuorum.register.KuorumUserSession
import kuorum.users.KuorumUser
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.notification.mail.sent.SendMailRSDTO
import org.kuorum.rest.model.notification.mail.sent.SentUserMailRSDTO
import org.springframework.beans.factory.annotation.Value

@Transactional
class MandrillAppService {


    RestKuorumApiService restKuorumApiService

    @Value('${mail.mandrillapp.key}')
    String MANDRIL_APIKEY

    //UNTESTED - Is not possible to test if the mail has been sent. Only if not fails
    @Deprecated
    void sendTemplate(MailData mailData, Boolean async = true) {

        if (!mailData.validate()) {
            log.error("Faltan datos para mandar este email. Errors:  ${mailData.errors.allErrors}")
            return
        }

        if (!mailData.mailType.mailTypeRSDTO) {
            sendViaMandrillApp(mailData, async)
        } else {
            sendTemplateViaKuorumServices(mailData)
        }
    }

    void sendTemplate(SendMailRSDTO mailData, KuorumUserSession sender = null) {
        sendTemplateViaKuorumServices(mailData, sender)
    }
    private sendViaMandrillApp(MailData mailData,Boolean async = true){
        List<MandrillMessage.Recipient> recipients = createRecipients(mailData)
        List<MandrillMessage.MergeVar> globalMergeVars = createGlobalMergeVars(mailData)
        List<MandrillMessage.MergeVarBucket> mergeVars = createMergeVars(mailData)


        MandrillMessage message = new MandrillMessage()
        message.fromName = mailData.fromName
        message.to = recipients
        message.globalMergeVars = globalMergeVars
        message.mergeVars = mergeVars
//        message.tags = [mailData.mailType.mailGroup.toString()]
        message.googleAnalyticsDomains = ["kuorum.org"]
        message.metadata = ["website":"kuorum.org"]

        MandrillApi mandrillApi = new MandrillApi(MANDRIL_APIKEY)
        MandrillMessagesApi messagesApi = mandrillApi.messages()
        MandrillMessageStatus[] statuses =  messagesApi.sendTemplate(mailData.mailType.nameTemplate,[:],message, async)
        statuses.each { MandrillMessageStatus status ->
            switch (status.status){
                case "queued":
                    log.warn("The mail was queued")
                    break
                case "sent":
                    break
                case "rejected":
                case "invalid":
                default:
                    log.warn("Mandrillapp not sent the email ${status.email}. Cause ${status.status}: ${status.rejectReason}")
                    break
            }
        }



    }

    private List<MandrillMessage.Recipient> createRecipients(MailData mailData){
        List<MandrillMessage.Recipient> recipients = []
        mailData.userBindings.each{MailUserData mailUserData ->
            MandrillMessage.Recipient recipient =new MandrillMessage.Recipient()
            recipient.email= mailUserData.user.email
            recipient.name = mailUserData.user.name
            recipient.type = MandrillMessage.Recipient.Type.BCC
            recipients << recipient
        }
        recipients
    }
    private List<MandrillMessage.MergeVar> createGlobalMergeVars(mailData){

        mailData.mailType.globalBindings.collect{ field ->
            new MandrillMessage.MergeVar(
                    name:field,
                    content: mailData.globalBindings[field]
            )
        }

    }

    private List<MandrillMessage.MergeVarBucket> createMergeVars(mailData){
        mailData.userBindings.collect{MailUserData mailUserData ->
            MandrillMessage.MergeVarBucket userVars = new MandrillMessage.MergeVarBucket()

            userVars.rcpt = mailUserData.user.email
            userVars.vars = mailData.mailType.requiredBindings.collect{field ->
                new MandrillMessage.MergeVar(
                        name:field,
                        content: mailUserData.bindings[field]
                )
            }
            String lang = "en" //All mails are in english or spanish
            if (mailUserData.user.language == AvailableLanguage.es_ES){
                lang = "es"
            }
            userVars.vars = userVars.vars.plus(new MandrillMessage.MergeVar(
                    name:"language",
//                    content: mailUserData.user.language.locale.language
                    content: lang
            ))
            userVars
        }
    }

    private void sendTemplateViaKuorumServices(MailData mailData,KuorumUserSession sender = null){
        SendMailRSDTO sendMailRSDTO = new SendMailRSDTO()
        sendMailRSDTO.globalBindings = mailData.globalBindings
        sendMailRSDTO.mailType = mailData.mailType.mailTypeRSDTO
        sendMailRSDTO.destinations = mailData.userBindings.collect{
            SentUserMailRSDTO userMailRSDTO = new SentUserMailRSDTO()
            userMailRSDTO.bindings = it.bindings
            userMailRSDTO.lang = it.user.language?.locale?.language?:"en"
            userMailRSDTO.name = it.user.name
            userMailRSDTO.email =it.user.email
            return userMailRSDTO
        }
        sendTemplateViaKuorumServices(sendMailRSDTO, sender)
    }
    private void sendTemplateViaKuorumServices(SendMailRSDTO mailData, KuorumUserSession sender = null){
        Map params = [:]
        RestKuorumApiService.ApiMethod apiMethod = RestKuorumApiService.ApiMethod.ADMIN_MAILS_SEND
        if (sender){
            params.put("userId",sender.id.toString())
            apiMethod = RestKuorumApiService.ApiMethod.ACCOUNT_MAILS_SEND
        }
        restKuorumApiService.post(
                apiMethod,
                params,
                [:],
                mailData,
                null)
    }

}
