package kuorum.mail

import grails.transaction.Transactional
import kuorum.core.model.AvailableLanguage
import kuorum.core.model.CommissionType
import kuorum.core.model.OfferType
import kuorum.core.model.PostType
import kuorum.project.Project
import kuorum.post.Cluck
import kuorum.post.Post
import kuorum.post.PostComment
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

    def sendSavedProjectToRelatedUsers(List <KuorumUser> listUsers, Project project){
        def requiredBindings = [
                projectName: project.shortName,
                projectOwner: project.owner,
                commissionType: project.commissions,
        ]

        def globalBindings=[
                projectYoutube: project.urlYoutube?.url,
                projectLink: generateLink('projectShow', project.encodeAsLinkProperties()),
                projectImage: project.image?.url
        ]

        listUsers.each{
            MailUserData mailUserData = new MailUserData(user:it, bindings:requiredBindings)
            MailData mailData = new MailData(fromName:DEFAULT_SENDER_NAME , mailType: MailType.PROJECT_CREATED_NOTIFICATION, globalBindings: globalBindings, userBindings: [mailUserData])
            mandrillAppService.sendTemplate(mailData)
        }
    }

    def sendFeedbackMail(KuorumUser user, String feedback, boolean userDeleted = false){
        def bindings = [
                feedbackUserLink:generateLink("userShow",user.encodeAsLinkProperties()),
                feedbackUser:user.name,
                feedbackText:feedback,
                userDeleted:userDeleted
        ]
        MailUserData mailUserData = new MailUserData(user:getFeedbackUser())
        MailData mailData = new MailData(fromName:user.name, mailType: MailType.FEEDBACK, globalBindings: bindings, userBindings: [mailUserData])
        mandrillAppService.sendTemplate(mailData)
    }

    def sendPoliticianSubscriptionToAdmins(KuorumUser user, OfferType offerType){
        def bindings = [
                userLink:generateLink("userShow",user.encodeAsLinkProperties()),
                user:user.name,
                offerType:messageSource.getMessage("${OfferType.canonicalName}.${offerType}",null,"", new Locale("ES_es")),
                totalPrice:offerType.finalPrice.toString()
        ]
        KuorumUser adminUser = getPurchaseUser();
        MailUserData mailUserData = new MailUserData(user:adminUser)
        MailData mailData = new MailData(fromName:adminUser.name, mailType: MailType.POLITICIAN_SUBSCRIPTION, globalBindings: bindings, userBindings: [mailUserData])
        mandrillAppService.sendTemplate(mailData)
    }
    def sendPoliticianSubscription(KuorumUser user, OfferType offerType){
        def bindings = [
                userLink:generateLink("userShow",user.encodeAsLinkProperties()),
                user:user.name,
                offerType:messageSource.getMessage("${OfferType.canonicalName}.${offerType}",null,"", new Locale("ES_es")),
                totalPrice:offerType.finalPrice.toString()
        ]
        MailUserData mailUserData = new MailUserData(user:user)
        MailData mailData = new MailData(fromName:DEFAULT_SENDER_NAME, mailType: MailType.NOTIFICATION_OFFER_PURCHASED, globalBindings: bindings, userBindings: [mailUserData])
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

    private KuorumUser getFeedbackUser(){
        //Chapu
        KuorumUser user = new KuorumUser(
                name: "Feedback",
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

    def sendChangeEmailRequested(KuorumUser user, String newEmail){
        def bindings = [newEmailAccount:newEmail]
        MailUserData mailUserData = new MailUserData(user:user, bindings:bindings)
        MailData mailData = new MailData(fromName:DEFAULT_SENDER_NAME,mailType: MailType.REGISTER_CHANGE_EMAIL_REQUESTED, userBindings: [mailUserData])
        mandrillAppService.sendTemplate(mailData)
    }

    def sendChangeEmailVerification(KuorumUser user, String resetLink){
        def bindings = [confirmationLink:resetLink]
        MailUserData mailUserData = new MailUserData(user:user, bindings:bindings)
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

    def sendVictoryNotificationUsers(Post post, Set<MailUserData> notificationUsers){
        def globalBindings = [
                defender:post.defender.name,
                defenderLink:generateLink("userShow",post.defender.encodeAsLinkProperties()),
                debateOwner:post.owner.name,
                postName:post.title,
                postLink:generateLink("postShow", post.encodeAsLinkProperties()),
                postOwner: post.owner.name,
                postOwnerLink: generateLink("userShow",post.owner.encodeAsLinkProperties())
        ]

        MailData mailNotificationsData = new MailData()
        mailNotificationsData.mailType = MailType.NOTIFICATION_VICTORY_USERS
        mailNotificationsData.globalBindings=globalBindings
        mailNotificationsData.userBindings = notificationUsers.asList().collect{postType:messageSource.getMessage("${PostType.canonicalName}.${PostType.PURPOSE}",null,"", it.user.language.locale)}
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

    def sendVictoryNotificationDefender(Post post){
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
        mailNotificationsData.mailType = MailType.NOTIFICATION_VICTORY_DEFENDER
        mailNotificationsData.globalBindings=globalBindings
        mailNotificationsData.userBindings = [new MailUserData(user:post.defender)]
        mailNotificationsData.fromName = prepareFromName(post.owner.name)
        mandrillAppService.sendTemplate(mailNotificationsData)
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
        mailNotificationsData.userBindings = notificationUsers.asList().collect{postType:messageSource.getMessage("${PostType.canonicalName}.${PostType.PURPOSE}",null,"", it.user.language.locale)}
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

    def verifyUser(KuorumUser user){
        mailingListUpdateUser(user)
        sendUserAccountConfirmed(user)
    }

    def mailingListUpdateUser(KuorumUser user){
        try{
            mailchimpService.addSubscriber(user)
        }catch(Exception e){
           log.error("Error actualizando usuario ${user} en MailChimp", e)
        }

        try{
            indexSolrService.index(user)
        }catch(Exception e){
            log.error("Error indexando usuario",e)
        }
    }



    protected String generateLink(String mapping, linkParams) {
        grailsLinkGenerator.link(mapping:mapping,absolute: true,  params: linkParams)
    }

    protected String prepareFromName(String name){
        "$name $DEFAULT_VIA"
    }

}
