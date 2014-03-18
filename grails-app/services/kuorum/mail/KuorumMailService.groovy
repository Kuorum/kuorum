package kuorum.mail

import com.ecwid.mailchimp.MailChimpClient
import com.ecwid.mailchimp.MailChimpException
import com.ecwid.mailchimp.method.v2_0.lists.Email
import com.ecwid.mailchimp.method.v2_0.lists.SubscribeMethod
import grails.transaction.Transactional
import kuorum.core.exception.KuorumException
import kuorum.core.exception.KuorumExceptionData
import kuorum.core.exception.KuorumExceptionUtil
import kuorum.core.model.PostType
import kuorum.post.Cluck
import kuorum.post.Post
import kuorum.users.KuorumUser
import org.codehaus.groovy.grails.web.mapping.LinkGenerator
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus

@Transactional
class KuorumMailService {

    @Value('${mail.mandrillapp.key}')
    String MANDRIL_APIKEY

    @Value('${mail.mailChimp.key}')
    String MAILCHIMP_APIKEY

    @Value('${mail.mailChimp.listId}')
    String MAILCHIMP_LIST_ID

    String DEFAULT_SENDER_NAME="Kuorum"
    String DEFAULT_VIA="via Kuorum.org"

    LinkGenerator grailsLinkGenerator
    MessageSource messageSource


    def sendRegisterUser(KuorumUser user, String confirmationLink){
        def bindings = [confirmationLink:confirmationLink]
        MailUserData mailUserData = new MailUserData(user:user, bindings:bindings)
        MailData mailData = new MailData(fromName:DEFAULT_SENDER_NAME,mailType: MailType.REGISTER_VERIFY_ACCOUNT, userBindings: [mailUserData])
        sendTemplate(mailData)
    }

    def sendUserAccountConfirmed(KuorumUser user){
        def bindings = []
        MailUserData mailUserData = new MailUserData(user:user)
        MailData mailData = new MailData(fromName:DEFAULT_SENDER_NAME,mailType: MailType.REGISTER_ACCOUNT_COMPLETED, userBindings: [mailUserData])
        sendTemplate(mailData)
    }

    def sendCluckNotificationMail(Cluck cluck){
        String userLink = generateLink("userShow",[id:cluck.owner.id.toString()])
        MailUserData mailUserData = new MailUserData(user:cluck.postOwner)
        MailData mailData = new MailData()
        mailData.mailType = MailType.NOTIFICATION_CLUCK
        mailData.globalBindings=[clucker:cluck.owner.name, cluckerLink:userLink,postName:cluck.post.title]
        mailData.userBindings = [mailUserData]
        mailData.fromName = prepareFromName(cluck.owner.name)
        sendTemplate(mailData)
    }

    def sendFollowerNotificationMail(KuorumUser follower, KuorumUser following){
        String userLink = generateLink("userShow",[id:follower.id.toString()])
        MailUserData mailUserData = new MailUserData(user:following, bindings:[])
        MailData mailData = new MailData()
        mailData.mailType = MailType.NOTIFICATION_FOLLOWER
        mailData.globalBindings=[follower:follower.name, followerLink:userLink]
        mailData.userBindings = [mailUserData]
        mailData.fromName = prepareFromName(follower.name)
        sendTemplate(mailData)
    }

    def sendPublicMilestoneNotificationMail(Post post){
        String postLink = generateLink("${post.postType}Show",[postId:post.id.toString()])
        def bindings = [mailType:messageSource.getMessage("${PostType.canonicalName}.${post.postType}",null,"", user.language.locale)]
        MailUserData mailUserData = new MailUserData(user:post.owner, bindings:bindings)
        MailData mailData = new MailData()
        mailData.mailType = MailType.NOTIFICATION_PUBLIC_MILESTONE
        mailData.globalBindings=[postName:post.title, numVotes:post.numVotes, postLink:postLink]
        mailData.userBindings = [mailUserData]
        mailData.fromName = DEFAULT_SENDER_NAME
        sendTemplate(mailData)
    }

    def sendDebateNotificationMailAuthor(Post post){
        KuorumUser debateOwner = post.debates.last().kuorumUser

        MailUserData mailUserData = new MailUserData(user:post.owner, bindings:[])
        def globalBindings = [
                postType:messageSource.getMessage("${PostType.canonicalName}.${post.postType}",null,"", new Locale("ES_es")),
                debateOwner:debateOwner.name,
                debateOwnerLink:generateLink("userShow",[id:debateOwner.id]),
                postName:post.title,
                postOwner:post.owner.name,
                postOwnerLink:generateLink("userShow",[id:post.owner.id]),
                postLink:generateLink("${post.postType}Show", [postId:post.id]),
                message:post.last().text
                ]

        MailData mailNotificationsData = new MailData()
        mailNotificationsData.mailType = MailType.NOTIFICATION_DEBATE_POLITICIAN
        mailNotificationsData.globalBindings=globalBindings
        mailNotificationsData.userBindings = [mailUserData]
        mailNotificationsData.fromName = prepareFromName(debateOwner.name)
        sendTemplate(mailNotificationsData)

    }

    def sendDebateNotificationMailPolitician(Post post,Set<MailUserData> politiciansData){
        KuorumUser debateOwner = post.debates.last().kuorumUser
        def globalBindings = [
                postType:messageSource.getMessage("${PostType.canonicalName}.${post.postType}",null,"", new Locale("ES_es")),
                debateOwner:debateOwner.name,
                debateOwnerLink:generateLink("userShow",[id:debateOwner.id]),
                postName:post.title,
                postOwner:post.owner.name,
                postOwnerLink:generateLink("userShow",[id:post.owner.id]),
                postLink:generateLink("${post.postType}Show", [postId:post.id]),
                message:post.last().text
        ]
        MailData mailNotificationsData = new MailData()
        mailNotificationsData.mailType = MailType.NOTIFICATION_DEBATE_POLITICIAN
        mailNotificationsData.globalBindings=globalBindings
        mailNotificationsData.userBindings = politiciansData.asList()
        mailNotificationsData.fromName = prepareFromName(debateOwner.name)
        sendTemplate(mailNotificationsData)
    }

    def sendDebateNotificationMailInterestedUsers(Post post, Set<MailUserData> notificationUsers){
        KuorumUser debateOwner = post.debates.last().kuorumUser
        def globalBindings = [
                postType:messageSource.getMessage("${PostType.canonicalName}.${post.postType}",null,"", new Locale("ES_es")),
                debateOwner:debateOwner.name,
                debateOwnerLink:generateLink("userShow",[id:debateOwner.id]),
                postName:post.title,
                postOwner:post.owner.name,
                postOwnerLink:generateLink("userShow",[id:post.owner.id]),
                postLink:generateLink("${post.postType}Show", [postId:post.id]),
                message:post.last().text
        ]

        MailData mailNotificationsData = new MailData()
        mailNotificationsData.mailType = MailType.NOTIFICATION_DEBATE_USERS
        mailNotificationsData.globalBindings=globalBindings
        mailNotificationsData.userBindings = notificationUsers.asList()
        mailNotificationsData.fromName = prepareFromName(debateOwner.name)
        sendTemplate(mailNotificationsData)
    }

    def sendPostDefendedNotificationMailAuthor(Post post){
        MailUserData mailUserData = new MailUserData(user:post.owner, bindings:[])
        def globalBindings = [
                postType:messageSource.getMessage("${PostType.canonicalName}.${post.postType}",null,"", new Locale("ES_es")),
                defender:post.defender.name,
                defenderLink:generateLink("userShow",[id:post.defender.id]),
                debateOwner:post.owner.name,
                postName:post.title,
                postLink:generateLink("${post.postType}Show", [postId:post.id]),
                postOwner: post.owner.name,
                postOwnerLink: generateLink("userShow",[id:post.owner.id])

        ]

        MailData mailNotificationsData = new MailData()
        mailNotificationsData.mailType = MailType.NOTIFICATION_DEFENDED_AUTHOR
        mailNotificationsData.globalBindings=globalBindings
        mailNotificationsData.userBindings = [mailUserData]
        mailNotificationsData.fromName = prepareFromName(post.defender.name)
        sendTemplate(mailNotificationsData)
    }

    def sendPostDefendedNotificationMailPoliticians(Post post,Set<MailUserData> politiciansData){
        def globalBindings = [
                postType:messageSource.getMessage("${PostType.canonicalName}.${post.postType}",null,"", new Locale("ES_es")),
                defender:post.defender.name,
                defenderLink:generateLink("userShow",[id:post.defender.id]),
                debateOwner:post.owner.name,
                postName:post.title,
                postLink:generateLink("${post.postType}Show", [postId:post.id]),
                postOwner: post.owner.name,
                postOwnerLink: generateLink("userShow",[id:post.owner.id])

        ]

        MailData mailNotificationsData = new MailData()
        mailNotificationsData.mailType = MailType.NOTIFICATION_DEFENDED_POLITICIANS
        mailNotificationsData.globalBindings=globalBindings
        mailNotificationsData.userBindings = politiciansData.asList()
        mailNotificationsData.fromName = prepareFromName(post.defender.name)
        sendTemplate(mailNotificationsData)
    }

    def sendPostDefendedNotificationMailDefender(Post post){
        MailUserData mailUserData = new MailUserData(user:post.defender, bindings:[])
        def globalBindings = [
                postType:messageSource.getMessage("${PostType.canonicalName}.${post.postType}",null,"", new Locale("ES_es")),
                defender:post.defender.name,
                defenderLink:generateLink("userShow",[id:post.defender.id]),
                debateOwner:post.owner.name,
                postName:post.title,
                postLink:generateLink("${post.postType}Show", [postId:post.id]),
                postOwner: post.owner.name,
                postOwnerLink: generateLink("userShow",[id:post.owner.id])
        ]

        MailData mailNotificationsData = new MailData()
        mailNotificationsData.mailType = MailType.NOTIFICATION_DEFENDED_BY_POLITICIAN
        mailNotificationsData.globalBindings=globalBindings
        mailNotificationsData.userBindings = [mailUserData]
        mailNotificationsData.fromName = DEFAULT_SENDER_NAME
        sendTemplate(mailNotificationsData)
    }

    def sendPostDefendedNotificationMailInterestedUsers(Post post, Set<MailUserData> notificationUsers){
        def globalBindings = [
                postType:messageSource.getMessage("${PostType.canonicalName}.${post.postType}",null,"", new Locale("ES_es")),
                defender:post.defender.name,
                defenderLink:generateLink("userShow",[id:post.defender.id]),
                debateOwner:post.owner.name,
                postName:post.title,
                postLink:generateLink("${post.postType}Show", [postId:post.id]),
                postOwner: post.owner.name,
                postOwnerLink: generateLink("userShow",[id:post.owner.id])
        ]

        MailData mailNotificationsData = new MailData()
        mailNotificationsData.mailType = MailType.NOTIFICATION_DEFENDED_USERS
        mailNotificationsData.globalBindings=globalBindings
        mailNotificationsData.userBindings = notificationUsers.asList()
        mailNotificationsData.fromName = prepareFromName(post.defender.name)
        sendTemplate(mailNotificationsData)
    }

    def sendVictoryNotificationUsers(Post post, Set<MailUserData> notificationUsers){
        def globalBindings = [
                postType:messageSource.getMessage("${PostType.canonicalName}.${post.postType}",null,"", new Locale("ES_es")),
                defender:post.defender.name,
                defenderLink:generateLink("userShow",[id:post.defender.id]),
                debateOwner:post.owner.name,
                postName:post.title,
                postLink:generateLink("${post.postType}Show", [postId:post.id]),
                postOwner: post.owner.name,
                postOwnerLink: generateLink("userShow",[id:post.owner.id])
        ]

        MailData mailNotificationsData = new MailData()
        mailNotificationsData.mailType = MailType.NOTIFICATION_VICTORY_USERS
        mailNotificationsData.globalBindings=globalBindings
        mailNotificationsData.userBindings = notificationUsers.asList()
        mailNotificationsData.fromName = prepareFromName(post.owner.name)
        sendTemplate(mailNotificationsData)
    }

    def sendVictoryNotificationDefender(Post post){
        def globalBindings = [
                postType:messageSource.getMessage("${PostType.canonicalName}.${post.postType}",null,"", new Locale("ES_es")),
                defender:post.defender.name,
                defenderLink:generateLink("userShow",[id:post.defender.id]),
                debateOwner:post.owner.name,
                postName:post.title,
                postLink:generateLink("${post.postType}Show", [postId:post.id]),
                postOwner: post.owner.name,
                postOwnerLink: generateLink("userShow",[id:post.owner.id])
        ]

        MailData mailNotificationsData = new MailData()
        mailNotificationsData.mailType = MailType.NOTIFICATION_VICTORY_DEFENDER
        mailNotificationsData.globalBindings=globalBindings
        mailNotificationsData.userBindings = [new MailUserData(user:post.defender, bindings:[])]
        mailNotificationsData.fromName = prepareFromName(post.owner.name)
        sendTemplate(mailNotificationsData)
    }


    //UNTESTED - Is not possible to test if the mail has been sent. Only if not fails
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
                    from_name= "${mailData.fromName}"
                    to = to2
//                    to= [{
//                        email ="${mailData.userBindings[0].user.email}"
//                        name = "${mailData.userBindings[0].user.name}"
//                        type = "to"
//                    }]
                    //headers =[
                    //        "Reply-To":"info@kuorum.org"
                    //]
//                    headers = [
//                            "X-MC-SigningDomain":"Kuorum.org"
//                    ]
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
        sendUserAccountConfirmed(user)
    }

    def addSubscriber(KuorumUser user){
// reuse the same MailChimpClient object whenever possible
        try {
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
        }catch(MailChimpException mailChimpException){
            log.error("No se ha podido añadir al usuario ${user.email} a mailchimp debido a que MailChimp se ha negado",mailChimpException)
        }catch(Exception e){
            log.error("No se ha podido añadir al usuario ${user.email} a mailchimp debido a una excepcion",e)
        }

    }

    protected String generateLink(String mapping, linkParams) {
        grailsLinkGenerator.link(mapping:mapping,absolute: true,  params: linkParams)
    }

    protected String prepareFromName(String name){
        "$name $DEFAULT_VIA"
    }

}
