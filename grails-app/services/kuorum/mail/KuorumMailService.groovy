package kuorum.mail

import grails.transaction.Transactional
import kuorum.OfferPurchased
import kuorum.campaign.PollCampaignVote
import kuorum.core.model.*
import kuorum.post.Cluck
import kuorum.post.Post
import kuorum.post.PostComment
import kuorum.project.Project
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

    def sendPoliticianSubscriptionToAdmins(KuorumUser user, OfferPurchased offerPurchased){
        def bindings = [
                userLink:generateLink("userShow",user.encodeAsLinkProperties()),
                user:user.name,
                offerType:messageSource.getMessage("${OfferType.canonicalName}.${offerPurchased.offerType}",null,"", new Locale("ES_es")),
                totalPrice:offerPurchased.offerType.price * offerPurchased.kPeople
        ]
        KuorumUser adminUser = getPurchaseUser();
        MailUserData mailUserData = new MailUserData(user:adminUser)
        MailData mailData = new MailData(fromName:adminUser.name, mailType: MailType.POLITICIAN_SUBSCRIPTION, globalBindings: bindings, userBindings: [mailUserData])
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

    def sendNewEditorRequestToAdmins(KuorumUser editor){
        def bindings = [
                userLink:generateLink("userShow",editor.encodeAsLinkProperties()),
                user:editor.name
        ]
        MailUserData mailUserData = new MailUserData(user:getFeedbackUser())
        MailData mailData = new MailData(fromName:DEFAULT_SENDER_NAME, mailType: MailType.EDITOR_SUBSCRIPTION, globalBindings: bindings, userBindings: [mailUserData])
        mandrillAppService.sendTemplate(mailData)
    }

    def sendNewBetaTesterRequestToAdmins(KuorumUser politicianBetaTester){
        def bindings = [
                userLink:generateLink("userShow",politicianBetaTester.encodeAsLinkProperties()),
                user:politicianBetaTester.name
        ]
        MailUserData mailUserData = new MailUserData(user:getFeedbackUser())
        MailData mailData = new MailData(fromName:DEFAULT_SENDER_NAME, mailType: MailType.POLITICIAN_SUBSCRIPTION_TESTER, globalBindings: bindings, userBindings: [mailUserData])
        mandrillAppService.sendTemplate(mailData)
    }

    def sendVictoryToAdmins(KuorumUser user, Post post, Boolean victoryOk){
        KuorumUser admin = getFeedbackUser()
        def globalBindings = [
                postType:messageSource.getMessage("${PostType.canonicalName}.${PostType.PURPOSE}",null,"", admin.language.locale),
                defender:post.defender.name,
                defenderLink:generateLink("userShow",post.defender.encodeAsLinkProperties()),
                postName:post.title,
                postLink:generateLink("postShow", post.encodeAsLinkProperties()),
                postOwner: post.owner.name,
                postOwnerLink: generateLink("userShow",post.owner.encodeAsLinkProperties()),
                victoryOk:victoryOk
        ]
        MailUserData mailUserData = new MailUserData(user:admin)
        MailData mailData = new MailData(fromName:user.name, mailType: MailType.FEEDBACK_VICTORY, globalBindings: globalBindings, userBindings: [mailUserData])
        mandrillAppService.sendTemplate(mailData)
    }

    private KuorumUser getFeedbackUser(String name = "Feedback"){
        //Chapu
        KuorumUser user = new KuorumUser(
                name: name,
                email: "${grailsApplication.config.kuorum.contact.feedback}",
                availableMails: MailType.values(),
                language: AvailableLanguage.es_ES
        )
    }

    private KuorumUser getPurchaseUser(){
        //Chapu
        KuorumUser user = new KuorumUser(
                name: "${grailsApplication.config.kuorum.purchase.userName}",
                email: "${grailsApplication.config.kuorum.purchase.email}",
                availableMails: MailType.values(),
                language: AvailableLanguage.es_ES
        )
    }

    private KuorumUser buildMailUser(String name, String email, AvailableLanguage language){
        //Chapu
        KuorumUser user = new KuorumUser(
                name: name,
                email: email,
                availableMails: MailType.values(),
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
            Sector sector,
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
            <li>Enterprise: ${userRequestingDomain.organization}</li>
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

    def sendUserAccountConfirmed(KuorumUser user){
        def bindings = []
        MailUserData mailUserData = new MailUserData(user:user)
        MailData mailData = new MailData(fromName:DEFAULT_SENDER_NAME,mailType: MailType.REGISTER_ACCOUNT_COMPLETED, userBindings: [mailUserData])
        mandrillAppService.sendTemplate(mailData)
    }

    def sendCluckNotificationMail(Cluck cluck){
        String userLink = generateLink("userShow",cluck.owner.encodeAsLinkProperties())
        String postLink = generateLink("postShow",cluck.post.encodeAsLinkProperties())
        def bindings = [
                postType:messageSource.getMessage("${PostType.canonicalName}.${PostType.PURPOSE}",null,"", cluck.postOwner.language.locale)
        ]
        MailUserData mailUserData = new MailUserData(user:cluck.postOwner,bindings:bindings)
        MailData mailData = new MailData()
        mailData.mailType = MailType.NOTIFICATION_CLUCK
        mailData.globalBindings=[
                clucker:cluck.owner.name,
                cluckerLink:userLink,
                postName:cluck.post.title,
                postLink:postLink]
        mailData.userBindings = [mailUserData]
        mailData.fromName = prepareFromName(cluck.owner.name)
        mandrillAppService.sendTemplate(mailData)
    }

    def sendPoliticianContactKuorumNotification(KuorumUser politician, KuorumUser user, String message, String cause){
        // FAST FIX FOR CONTROLING CONTACT BUTTON (Sending an email to Kuorum)
        String userLink = generateLink('userShow',user.encodeAsLinkProperties())
        String politicianLink = generateLink('userShow',politician.encodeAsLinkProperties())
        String rawMessage = """
        <h1> <a href='${userLink}'>${user.name}</a> has sent a contact mail to <a href="${politicianLink}">${politician.name}</a> </h1>
        <h2> Message related with ${cause}</h2>
        <p> ${message} </p>
        """
        sendBatchMail(getFeedbackUser(), rawMessage, "${user.name} has contacted with ${politician.name}")
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

    def sendFollowerNotificationMail(KuorumUser follower, KuorumUser following){
        String userLink = generateLink("userShow",follower.encodeAsLinkProperties())
        MailUserData mailUserData = new MailUserData(user:following)
        MailData mailData = new MailData()
        mailData.mailType = MailType.NOTIFICATION_FOLLOWER
        mailData.globalBindings=[follower:follower.name, followerLink:userLink]
        mailData.userBindings = [mailUserData]
        mailData.fromName = prepareFromName(follower.name)
        mandrillAppService.sendTemplate(mailData)
    }

    def sendPublicMilestoneNotificationMail(Post post){
        String postLink = generateLink("postShow", post.encodeAsLinkProperties())
        def bindings = [postType: messageSource.getMessage("${PostType.canonicalName}.${PostType.PURPOSE}",null,"", post.owner.language.locale)]
        MailUserData mailUserData = new MailUserData(user:post.owner, bindings:bindings)
        MailData mailData = new MailData()
        mailData.mailType = MailType.NOTIFICATION_PUBLIC_MILESTONE
        mailData.globalBindings=[postName:post.title, numVotes:post.numVotes, postLink:postLink]
        mailData.userBindings = [mailUserData]
        mailData.fromName = DEFAULT_SENDER_NAME
        mandrillAppService.sendTemplate(mailData)
    }

    def sendDebateNotificationMailAuthor(Post post){
        KuorumUser debateOwner = post.debates.last().kuorumUser

        MailUserData mailUserData = new MailUserData(user:post.owner)
        def globalBindings = [
                postType:messageSource.getMessage("${PostType.canonicalName}.${PostType.PURPOSE}",null,"", post.owner.language.locale),
                debateOwner:debateOwner.name,
                debateOwnerLink:generateLink("userShow",debateOwner.encodeAsLinkProperties()),
                postName:post.title,
                postOwner:post.owner.name,
                postOwnerLink:generateLink("userShow", post.owner.encodeAsLinkProperties()),
                postLink:generateLink("postShow", post.encodeAsLinkProperties()),
                message:post.last().text
                ]

        MailData mailNotificationsData = new MailData()
        mailNotificationsData.mailType = MailType.NOTIFICATION_DEBATE_AUTHOR
        mailNotificationsData.globalBindings=globalBindings
        mailNotificationsData.userBindings = [mailUserData]
        mailNotificationsData.fromName = prepareFromName(debateOwner.name)
        mandrillAppService.sendTemplate(mailNotificationsData)

    }

    def sendDebateNotificationMailPolitician(Post post,Set<MailUserData> politiciansData){
        KuorumUser debateOwner = post.debates.last().kuorumUser
        def globalBindings = [
                postType:messageSource.getMessage("${PostType.canonicalName}.${PostType.PURPOSE}",null,"", debateOwner.language.locale),
                debateOwner:debateOwner.name,
                debateOwnerLink:generateLink("userShow", debateOwner.encodeAsLinkProperties()),
                postName:post.title,
                postOwner:post.owner.name,
                postOwnerLink:generateLink("userShow",post.owner.encodeAsLinkProperties()),
                postLink:generateLink("postShow", post.encodeAsLinkProperties()),
                message:post.last().text
        ]
        MailData mailNotificationsData = new MailData()
        mailNotificationsData.mailType = MailType.NOTIFICATION_DEBATE_POLITICIAN
        mailNotificationsData.globalBindings=globalBindings
        mailNotificationsData.userBindings = politiciansData.asList()
        mailNotificationsData.fromName = prepareFromName(debateOwner.name)
        mandrillAppService.sendTemplate(mailNotificationsData)
    }

    def sendDebateNotificationMailInterestedUsers(final Post post, Set<MailUserData> notificationUsers){
        KuorumUser debateOwner = post.debates.last().kuorumUser
        def globalBindings = [
                debateOwner:debateOwner.name,
                debateOwnerLink:generateLink("userShow",debateOwner.encodeAsLinkProperties()),
                postName:post.title,
                postOwner:post.owner.name,
                postOwnerLink:generateLink("userShow",post.owner.encodeAsLinkProperties()),
                postLink:generateLink("postShow", post.encodeAsLinkProperties()),
                message:post.last().text
        ]

        MailData mailNotificationsData = new MailData()
        mailNotificationsData.mailType = MailType.NOTIFICATION_DEBATE_USERS
        mailNotificationsData.globalBindings=globalBindings
        mailNotificationsData.userBindings = notificationUsers.collect{it.bindings.put("postType", messageSource.getMessage("${PostType.canonicalName}.${PostType.PURPOSE}",null,"", it.user.language.locale)); it}
        mailNotificationsData.fromName = prepareFromName(debateOwner.name)
        mandrillAppService.sendTemplate(mailNotificationsData)
    }

    def sendPostDefendedNotificationMailAuthor(Post post){
        MailUserData mailUserData = new MailUserData(user:post.owner)
        def globalBindings = [
                postType:messageSource.getMessage("${PostType.canonicalName}.${PostType.PURPOSE}",null,"", post.owner.language.locale),
                defender:post.defender.name,
                defenderLink:generateLink("userShow",post.defender.encodeAsLinkProperties()),
                debateOwner:post.owner.name,
                postName:post.title,
                postLink:generateLink("postShow", post.encodeAsLinkProperties()),
                postOwner: post.owner.name,
                postOwnerLink: generateLink("userShow",post.owner.encodeAsLinkProperties())

        ]

        MailData mailNotificationsData = new MailData()
        mailNotificationsData.mailType = MailType.NOTIFICATION_DEFENDED_AUTHOR
        mailNotificationsData.globalBindings=globalBindings
        mailNotificationsData.userBindings = [mailUserData]
        mailNotificationsData.fromName = prepareFromName(post.defender.name)
        mandrillAppService.sendTemplate(mailNotificationsData)
    }

    def sendPostDefendedNotificationMailPoliticians(Post post,Set<MailUserData> politiciansData){
        def globalBindings = [
                postType:messageSource.getMessage("${PostType.canonicalName}.${PostType.PURPOSE}",null,"", new Locale("ES_es")),
                defender:post.defender.name,
                defenderLink:generateLink("userShow",post.defender.encodeAsLinkProperties()),
                debateOwner:post.owner.name,
                postName:post.title,
                postLink:generateLink("postShow", post.encodeAsLinkProperties()),
                postOwner: post.owner.name,
                postOwnerLink: generateLink("userShow",post.owner.encodeAsLinkProperties())

        ]

        MailData mailNotificationsData = new MailData()
        mailNotificationsData.mailType = MailType.NOTIFICATION_DEFENDED_POLITICIANS
        mailNotificationsData.globalBindings=globalBindings
        mailNotificationsData.userBindings = politiciansData.asList()
        mailNotificationsData.fromName = prepareFromName(post.defender.name)
        mandrillAppService.sendTemplate(mailNotificationsData)
    }

    def sendPostDefendedNotificationMailDefender(Post post){
        MailUserData mailUserData = new MailUserData(user:post.defender)
        def globalBindings = [
                postType:messageSource.getMessage("${PostType.canonicalName}.${PostType.PURPOSE}",null,"", post.defender.language.locale),
                defender:post.defender.name,
                defenderLink:generateLink("userShow",post.defender.encodeAsLinkProperties()),
                debateOwner:post.owner.name,
                postName:post.title,
                postLink:generateLink("postShow", post.encodeAsLinkProperties()),
                postOwner: post.owner.name,
                postOwnerLink: generateLink("userShow",post.owner.encodeAsLinkProperties())
        ]

        MailData mailNotificationsData = new MailData()
        mailNotificationsData.mailType = MailType.NOTIFICATION_DEFENDED_BY_POLITICIAN
        mailNotificationsData.globalBindings=globalBindings
        mailNotificationsData.userBindings = [mailUserData]
        mailNotificationsData.fromName = DEFAULT_SENDER_NAME
        mandrillAppService.sendTemplate(mailNotificationsData)
    }

    def sendPostDefendedNotificationMailInterestedUsers(Post post, Set<MailUserData> notificationUsers){
        def globalBindings = [
                postType:messageSource.getMessage("${PostType.canonicalName}.${PostType.PURPOSE}",null,"", new Locale("ES_es")),
                defender:post.defender.name,
                defenderLink:generateLink("userShow",post.defender.encodeAsLinkProperties()),
                postName:post.title,
                postLink:generateLink("postShow", post.encodeAsLinkProperties()),
                postOwner: post.owner.name,
                postOwnerLink: generateLink("userShow",post.owner.encodeAsLinkProperties())
        ]

        MailData mailNotificationsData = new MailData()
        mailNotificationsData.mailType = MailType.NOTIFICATION_DEFENDED_USERS
        mailNotificationsData.globalBindings=globalBindings
        mailNotificationsData.userBindings = notificationUsers.asList()
        mailNotificationsData.fromName = prepareFromName(post.owner.name)
        mandrillAppService.sendTemplate(mailNotificationsData)
    }

    private def globalBindingsForPromotedMails(Post post, KuorumUser sponsor){
        Project project = post.project
        String commissionName = messageSource.getMessage("${CommissionType.canonicalName}.${project.commissions.first()}",null,"otros", new Locale("ES_es"))
        [
                postName:post.title,
                postLink:generateLink("postShow", post.encodeAsLinkProperties()),
                promoter:sponsor.name,
                promoterLink:generateLink("userShow",sponsor.encodeAsLinkProperties()),
                postOwner: post.owner.name,
                postOwnerLink: generateLink("userShow",post.owner.encodeAsLinkProperties()),
                hashtag:post.project.hashtag,
                hashtagLink:generateLink("projectShow",project.encodeAsLinkProperties())
        ]
    }

    def sendCommentedPostNotificationUsers(Post post, PostComment postComment, Set<MailUserData> notificationUsers){
        def globalBindings = [
                comment:postComment.text,
                commenterLink:generateLink("userShow",postComment.kuorumUser.encodeAsLinkProperties()),
                commenter:postComment.kuorumUser.name,
                postName:post.title,
                postLink:generateLink("postShow", post.encodeAsLinkProperties()),
                postOwner: post.owner.name,
                postOwnerLink: generateLink("userShow",post.owner.encodeAsLinkProperties())
        ]

        MailData mailNotificationsData = new MailData()
        mailNotificationsData.mailType = MailType.NOTIFICATION_COMMENTED_POST_USERS
        mailNotificationsData.globalBindings=globalBindings
        mailNotificationsData.userBindings = notificationUsers.asList().collect{it.bindings.put("postType",messageSource.getMessage("${PostType.canonicalName}.${PostType.PURPOSE}",null,"", it.user.language.locale)); it}
        mailNotificationsData.fromName = prepareFromName(postComment.kuorumUser.name)
        mandrillAppService.sendTemplate(mailNotificationsData)
    }

    def sendCommentedPostNotificationOwner(Post post, PostComment postComment){
        def globalBindings = [
                postType:messageSource.getMessage("${PostType.canonicalName}.${PostType.PURPOSE}",null,"",post.owner.language.locale),
                comment:postComment.text,
                commenterLink:generateLink("userShow",postComment.kuorumUser.encodeAsLinkProperties()),
                commenter:postComment.kuorumUser.name,
                postName:post.title,
                postLink:generateLink("postShow", post.encodeAsLinkProperties()),
                postOwner: post.owner.name,
                postOwnerLink: generateLink("userShow",post.owner.encodeAsLinkProperties())
        ]

        MailUserData mailUserData = new MailUserData(user:post.owner, bindings:[:])
        MailData mailNotificationsData = new MailData()
        mailNotificationsData.mailType = MailType.NOTIFICATION_COMMENTED_POST_OWNER
        mailNotificationsData.globalBindings=globalBindings
        mailNotificationsData.userBindings = [mailUserData]
        mailNotificationsData.fromName = prepareFromName(postComment.kuorumUser.name)
        mandrillAppService.sendTemplate(mailNotificationsData)
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
