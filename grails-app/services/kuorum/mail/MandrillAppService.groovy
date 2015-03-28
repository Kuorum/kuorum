package kuorum.mail

import com.microtripit.mandrillapp.lutung.MandrillApi
import com.microtripit.mandrillapp.lutung.controller.MandrillMessagesApi
import com.microtripit.mandrillapp.lutung.view.MandrillMessage
import grails.transaction.Transactional
import kuorum.core.exception.KuorumExceptionUtil
import org.springframework.beans.factory.annotation.Value

@Transactional
class MandrillAppService {

    @Value('${mail.mandrillapp.key}')
    String MANDRIL_APIKEY

    //UNTESTED - Is not possible to test if the mail has been sent. Only if not fails
    void sendTemplate(MailData mailData, Boolean async = false) {

        if (!mailData.validate()){
            throw KuorumExceptionUtil.createExceptionFromValidatable(mailData, "Faltan datos para mandar este email")
        }

        List<MandrillMessage.Recipient> recipients = createRecipients(mailData)
        List<MandrillMessage.MergeVar> globalMergeVars = createGlobalMergeVars(mailData)
        List<MandrillMessage.MergeVarBucket> mergeVars = createMergeVars(mailData)


        MandrillMessage message = new MandrillMessage()
        message.fromName = mailData.fromName
        message.to = recipients
        message.globalMergeVars = globalMergeVars
        message.mergeVars = mergeVars
        message.tags = [mailData.mailType.tagTemplate]
        message.googleAnalyticsDomains = ["kuorum.org"]
        message.metadata = ["website":"kuorum.org"]

        MandrillApi mandrillApi = new MandrillApi(MANDRIL_APIKEY);
        MandrillMessagesApi messagesApi = mandrillApi.messages()
        messagesApi.sendTemplate(mailData.mailType.nameTemplate,[:],message, async)



    }

    private List<MandrillMessage.Recipient> createRecipients(MailData mailData){
        List<MandrillMessage.Recipient> recipients = []
        mailData.userBindings.each{MailUserData mailUserData ->
            if (!mailUserData.user.availableMails){
                //Log for finding a bug
                log.warn("El usuario ${mailUserData.user} tiene a null los availableMails")
            }
            //Check if the user has active the email
            if (!mailData.mailType.configurable || (mailUserData.user.availableMails && mailUserData.user.availableMails.contains(mailData.mailType))){
                MandrillMessage.Recipient recipient =new MandrillMessage.Recipient()
                recipient.email= mailUserData.user.email
                recipient.name = mailUserData.user.name
                recipient.type = MandrillMessage.Recipient.Type.BCC
                recipients << recipient
            }else{
                log.info("No se ha mandado el mail ${mailData.mailType} a ${mailUserData.user.email} debido a: [configurable: ${!mailData.mailType.configurable}, availableMailsNull:${!mailUserData.user.availableMails}, mailDesactivadoPorUser: ${mailUserData.user.availableMails?.contains(mailData.mailType)}]")
            }
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
            userVars.vars = userVars.vars.plus(new MandrillMessage.MergeVar(
                    name:"language",
                    content: mailUserData.user.language.locale.language
            ))
            userVars
        }
    }
}
