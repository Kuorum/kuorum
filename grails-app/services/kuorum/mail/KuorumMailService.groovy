package kuorum.mail

import com.ecwid.mailchimp.MailChimpClient
import com.ecwid.mailchimp.MailChimpMethod
import com.ecwid.mailchimp.method.v2_0.lists.Email
import com.ecwid.mailchimp.method.v2_0.lists.SubscribeMethod
import grails.transaction.Transactional
import kuorum.core.exception.KuorumException
import kuorum.core.exception.KuorumExceptionData
import kuorum.core.exception.KuorumExceptionUtil
import kuorum.post.Cluck
import kuorum.post.Post
import kuorum.users.KuorumUser
import org.codehaus.groovy.grails.web.mapping.LinkGenerator
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

    LinkGenerator grailsLinkGenerator


    def registerUser(KuorumUser user, def bindings){
        MailUserData mailUserData = new MailUserData(user:user, bindings:bindings)
        MailData mailData = new MailData(mailType: MailType.REGISTER_VERIFY_ACCOUNT, userBindings: [mailUserData])
        sendTemplate(mailData)

    }

    def sendCluckNotificationMail(Cluck cluck){
        String userLink = generateLink("userShow",[id:cluck.owner.id.toString()])
        MailUserData mailUserData = new MailUserData(user:cluck.postOwner, bindings:[fname:cluck.postOwner.name,postName:cluck.post.title])
        MailData mailData = new MailData()
        mailData.mailType = MailType.NOTIFICATION_CLUCK
        mailData.globalBindings=[cluckUserName:cluck.owner.name, cluckUserLink:userLink]
        mailData.userBindings = [mailUserData]
        sendTemplate(mailData)
    }

    def sendFollowerNotificationMail(KuorumUser follower, KuorumUser following){
        String userLink = generateLink("userShow",[id:follower.id.toString()])
        MailUserData mailUserData = new MailUserData(user:following, bindings:[])
        MailData mailData = new MailData()
        mailData.mailType = MailType.NOTIFICATION_FOLLOWER
        mailData.globalBindings=[followerName:follower.name, followerLink:userLink]
        mailData.userBindings = [mailUserData]
        sendTemplate(mailData)
    }

    def sendPublicMilestoneNotificationMail(Post post){
        String postLink = generateLink("${post.postType}Show",[postId:post.id.toString()])
        MailUserData mailUserData = new MailUserData(user:post.owner, bindings:[])
        MailData mailData = new MailData()
        mailData.mailType = MailType.NOTIFICATION_PUBLIC_MILESTONE
        mailData.globalBindings=[postName:post.title, numVotes:post.numVotes, postLink:postLink]
        mailData.userBindings = [mailUserData]
        sendTemplate(mailData)
    }

    def sendDebateNotificationMail(Post post, List<MailUserData> userMailData){
        KuorumUser politician = post.debates[0].kuorumUser
        def globalBindings = [
                debateOwner:post.owner.name,
                postName:post.title,
                politicianName:politician.name,
                message:post.debates[0].text,
                politicianLink:generateLink("userShow",[id:politician.id]),
                postLink:generateLink("${post.postType}Show", [postId:post.id])]

        MailData mailData = new MailData()
        mailData.mailType = MailType.NOTIFICATION_DEBATE
        mailData.globalBindings=globalBindings
        mailData.userBindings = userMailData
        sendTemplate(mailData)

        MailUserData mailUserData = new MailUserData(user:post.owner, bindings:[])
        MailData mailDataAlert = new MailData()
        mailDataAlert.mailType = MailType.ALERT_DEBATE
        mailDataAlert.globalBindings=globalBindings
        mailDataAlert.userBindings =[mailUserData]
        sendTemplate(mailDataAlert)
    }

    private void sendTemplate(MailData mailData) {

        if (!mailData.validate()){
            throw KuorumExceptionUtil.createExceptionFromValidatable(mailData, "Faltan datos para mandar este email")
        }

        def rest = new grails.plugins.rest.client.RestBuilder()
        String apiKey = MANDRIL_APIKEY

        def globalMergeVars = mailData.mailType.globalBindings.collect{ field ->
            ({
                name = "${field}"
                content = "${mailData.globalBindings[field]}"
            })
        }

        def mergeVars = mailData.userBindings.collect{MailUserData mailUserData ->
            def userVars = []
            mailData.mailType.requiredBindings.each{field ->
                userVars << ({
                    name = "${field}"
                    content = "${mailUserData.bindings[field]}"
                })
            }
            ({
                rcpt= "${mailUserData.user.email}"
                vars = userVars
            })
        }
        def to2 = []
        mailData.userBindings.each{MailUserData mailUserData ->
            //Check if the user has active the email
            if (!(mailData.mailType.configurable && !mailUserData.user.availableMails.contains(mailData.mailType))){
                to2 << ({
                    email = "${mailUserData.user.email}"
                    name = "${mailUserData.user.name}"
                    type = "to"
                })
            }

        }

        def resp = rest.post('https://mandrillapp.com/api/1.0/messages/send-template.json'){

            contentType "application/json; charset=UTF-8"
            accept "application/json"
            json {
                key = "${apiKey}"
                template_name = mailData.mailType.nameTemplate
                template_content = [{
                    name = "NO_SE_USA"
                    content = "XXXXX"
                },{
                    name = "NO_SE_USA_2"
                    content = "XXXXXX"
                }]
                message = {
                    //html = "<p>Contenido HTML que creo que no sirve</p>"
                    //text= "Verifica tu cuenta"
                    //subject= "Verifica tu cuenta"
                    //from_email= "info@kuorum.org"
                    //from_name= "Kuorum"
                    to = to2
//                    to= [{
//                        email ="${mailData.userBindings[0].user.email}"
//                        name = "${mailData.userBindings[0].user.name}"
//                        type = "to"
//                    }]
                    //headers =[
                    //        "Reply-To":"info@kuorum.org"
                    //]
//                    important =true
//                        bcc_address = "iduetxe@gmail.com"
                    global_merge_vars = globalMergeVars
//                    global_merge_vars = [{
//                        name = "merge1"
//                        content = "<a href='kuorum.org'> Kuorum </a>"
//                    }
//                    ]
                    merge_vars = mergeVars
//                    merge_vars=[{
//                        rcpt= "${mailData.user.email}"
//                        vars= [
//                                {
//                                    name = "fname"
//                                    content = "${mailData.user.name}"
//                                },
//                                {
//                                    name = "verifyLink"
//                                   content = "${mailData.bindings.verifyLink}"
//                                }
//                        ]
//                    }]
                    tags= [
                            mailData.mailType.tagTemplate
                    ]
                    subaccount ="kuorumComunication"
                    google_analytics_domains=[
                            "kuorum.org"
                    ]
//                        google_analytics_campaign="message.from_email@example.com"
                    metadata=[[
                            "website": "kuorum.org"
                    ]]
//                    attachments= [[
//                            type: "text/plain",
//                            name: "myfile.txt",
//                            content: "ZXhhbXBsZSBmaWxl"
//                    ]]
                }
//                async= true
            }
        }

        if (resp.responseEntity.statusCode != HttpStatus.OK){
            log.error("Error enviando emai '${mailData.mailType}' ")
            KuorumExceptionData error = new KuorumExceptionData(code:"error.mail.sendingMail")
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

    protected String generateLink(String mapping, linkParams) {
        grailsLinkGenerator.link(mapping:mapping,absolute: true,  params: linkParams)
    }
}
