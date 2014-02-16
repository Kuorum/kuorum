package kuorum.mail

import com.ecwid.mailchimp.MailChimpClient
import com.ecwid.mailchimp.MailChimpMethod
import com.ecwid.mailchimp.method.v2_0.lists.Email
import com.ecwid.mailchimp.method.v2_0.lists.SubscribeMethod
import grails.transaction.Transactional
import kuorum.core.exception.KuorumException
import kuorum.core.exception.KuorumExceptionData
import kuorum.users.KuorumUser
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus

@Transactional
class KuorumMailService {

    @Value('${mail.mandrillapp.key}')
    String MANDRIL_APIKEY

    @Value('${mail.mailChimp.key}')
    String MAILCHIMP_APIKEY

    @Value('${mail.mailChimp.listId}')
    String MAILCHIMP_LIST_ID

    def registerUser(MailData mailData) {

        def rest = new grails.plugins.rest.client.RestBuilder()
        String apiKey = MANDRIL_APIKEY

        //TODO: PEnsar como poner bonito y reusable este código
        def resp = rest.post('https://mandrillapp.com/api/1.0/messages/send-template.json'){

            contentType "application/json; charset=UTF-8"
            accept "application/json"
            json {
                key = "${apiKey}"
                template_name = "validationEmail"
                template_content = [{
                    name = "fname"
                    content = "${mailData.user.name}"
                },{
                    name = "app_button"
                    content = "${mailData.bindings.validateLink}"
                }]
                message = {
                    html = "<p>Contenido HTML que creo que no sirve</p>"
                    text= "Verifica tu cuenta"
                    subject= "Verifica tu cuenta"
                    from_email= "info@kuorum.org"
                    from_name= "Kuorum"
                    to= [{
                        email ="${mailData.user.email}"
                        name = "${mailData.user.name} ${mailData.user.surname}"
                        type = "to"
                    }]
                    headers =[
                            "Reply-To":"info@kuorum.org"
                    ]
//                    important =true
//                        bcc_address = "iduetxe@gmail.com"
                    global_merge_vars = [{
                        name = "merge1"
                        content = "<a href='kuorum.org'> Kuorum </a>"
                    }
                    ]
                    merge_vars=[{
                        rcpt= "${mailData.user.email}"
                        vars= [
                                {
                                    name = "fname"
                                    content = "${mailData.user.name}"
                                },
                                {
                                    name = "verifyLink"
                                    content = "${mailData.bindings.verifyLink}"
                                }
                        ]
                    }]
                    tags= [
                            "registerUser"
                    ]
                    subaccount ="kuorumComunication"
                    google_analytics_domains=[
                            "kuorum.org"
                    ]
//                        google_analytics_campaign="message.from_email@example.com"
                    metadata=[[
                            "website": "kuorum.org"
                    ]]
                    attachments= [[
                            type: "text/plain",
                            name: "myfile.txt",
                            content: "ZXhhbXBsZSBmaWxl"
                    ]]
                }
//                async= true
            }
        }

        if (resp.responseEntity.statusCode != HttpStatus.OK){
            log.error("Error enviando email de registro de '${mailData.user.name}' con el mail ${mailData.user.email}")
            KuorumExceptionData error = new KuorumExceptionData(code:"error.mail.sendRegisterUser")
            throw new KuorumException(resp.responseEntity.toString(),error)
        }
        resp


    }

    def verifyUser(KuorumUser user){

        addSubscriber(user)
    }

    def addSubscriber(KuorumUser user){
// reuse the same MailChimpClient object whenever possible
        MailChimpClient mailChimpClient = new MailChimpClient();

        Email mailChimpEmail = new Email()
        mailChimpEmail.email = user.email

        // Subscribe a person
        SubscribeMethod subscribeMethod = new SubscribeMethod();
        subscribeMethod.apikey = MAILCHIMP_APIKEY;
        subscribeMethod.id = MAILCHIMP_LIST_ID;
        subscribeMethod.email = mailChimpEmail
        subscribeMethod.double_optin = false;
        subscribeMethod.update_existing = true;
        subscribeMethod.send_welcome = false;
        subscribeMethod.merge_vars = new MailChimpMergeVars(user.email, user.name, user?.surname);
        mailChimpClient.execute(subscribeMethod);

        log.info(" Se ha añadido correctamente el usuario $user con mail $user.email a MailChimp");

        /*
        // check his status
        MemberInfoMethod memberInfoMethod = new MemberInfoMethod();
        memberInfoMethod.apikey = apikey;
        memberInfoMethod.id = listId;
        memberInfoMethod.emails = Arrays.asList(subscribeMethod.email);

        MemberInfoResult memberInfoResult = mailChimpClient.execute(memberInfoMethod);
        MemberInfoData data = memberInfoResult.data.get(0);
        System.out.println(data.email+"'s status is "+data.status);
*/
        // Close http-connection when the MailChimpClient object is not needed any longer.
        // Generally the close method should be called from a "finally" block.
        mailChimpClient.close();

    }
}
