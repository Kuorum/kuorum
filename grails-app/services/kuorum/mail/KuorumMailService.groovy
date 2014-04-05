package kuorum.mail

import grails.transaction.Transactional
import kuorum.core.model.CommissionType
import kuorum.core.model.PostType
import kuorum.law.Law
import kuorum.post.Cluck
import kuorum.post.Post
import kuorum.solr.IndexSolrService
import kuorum.users.KuorumUser
import org.codehaus.groovy.grails.web.mapping.LinkGenerator
import org.springframework.context.MessageSource

@Transactional
class KuorumMailService {

    String DEFAULT_SENDER_NAME="Kuorum"
    String DEFAULT_VIA="via Kuorum.org"

    LinkGenerator grailsLinkGenerator
    MandrillAppService mandrillAppService
    MailchimpService mailchimpService
    MessageSource messageSource
    IndexSolrService indexSolrService


    def sendRegisterUser(KuorumUser user, String confirmationLink){
        def bindings = [confirmationLink:confirmationLink]
        MailUserData mailUserData = new MailUserData(user:user, bindings:bindings)
        MailData mailData = new MailData(fromName:DEFAULT_SENDER_NAME,mailType: MailType.REGISTER_VERIFY_EMAIL, userBindings: [mailUserData])
        mandrillAppService.sendTemplate(mailData)
    }

    def sendRegisterUserViaRRSS(KuorumUser user){
        MailUserData mailUserData = new MailUserData(user:user, bindings:[:])
        MailData mailData = new MailData(fromName:DEFAULT_SENDER_NAME,mailType: MailType.REGISTER_RRSS, userBindings: [mailUserData])
        mandrillAppService.sendTemplate(mailData)
    }

    def sendResetPasswordMail(KuorumUser user, String resetLink){
        def bindings = [resetPasswordLink:resetLink]
        MailUserData mailUserData = new MailUserData(user:user, bindings:bindings)
        MailData mailData = new MailData(fromName:DEFAULT_SENDER_NAME,mailType: MailType.REGISTER_RESET_PASSWORD, userBindings: [mailUserData])
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
        MailUserData mailUserData = new MailUserData(user:cluck.postOwner)
        MailData mailData = new MailData()
        mailData.mailType = MailType.NOTIFICATION_CLUCK
        mailData.globalBindings=[clucker:cluck.owner.name, cluckerLink:userLink,postName:cluck.post.title]
        mailData.userBindings = [mailUserData]
        mailData.fromName = prepareFromName(cluck.owner.name)
        mandrillAppService.sendTemplate(mailData)
    }

    def sendFollowerNotificationMail(KuorumUser follower, KuorumUser following){
        String userLink = generateLink("userShow",follower.encodeAsLinkProperties())
        MailUserData mailUserData = new MailUserData(user:following, bindings:[])
        MailData mailData = new MailData()
        mailData.mailType = MailType.NOTIFICATION_FOLLOWER
        mailData.globalBindings=[follower:follower.name, followerLink:userLink]
        mailData.userBindings = [mailUserData]
        mailData.fromName = prepareFromName(follower.name)
        mandrillAppService.sendTemplate(mailData)
    }

    def sendPublicMilestoneNotificationMail(Post post){
        String postLink = generateLink("postShow", post.encodeAsLinkProperties())
        def bindings = [mailType:messageSource.getMessage("${PostType.canonicalName}.${post.postType}",null,"", user.language.locale)]
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

        MailUserData mailUserData = new MailUserData(user:post.owner, bindings:[])
        def globalBindings = [
                postType:messageSource.getMessage("${PostType.canonicalName}.${post.postType}",null,"", new Locale("ES_es")),
                debateOwner:debateOwner.name,
                debateOwnerLink:generateLink("userShow",debateOwner.encodeAsLinkProperties()),
                postName:post.title,
                postOwner:post.owner.name,
                postOwnerLink:generateLink("userShow", post.owner.encodeAsLinkProperties()),
                postLink:generateLink("postShow", post.encodeAsLinkProperties()),
                message:post.last().text
                ]

        MailData mailNotificationsData = new MailData()
        mailNotificationsData.mailType = MailType.NOTIFICATION_DEBATE_POLITICIAN
        mailNotificationsData.globalBindings=globalBindings
        mailNotificationsData.userBindings = [mailUserData]
        mailNotificationsData.fromName = prepareFromName(debateOwner.name)
        mandrillAppService.sendTemplate(mailNotificationsData)

    }

    def sendDebateNotificationMailPolitician(Post post,Set<MailUserData> politiciansData){
        KuorumUser debateOwner = post.debates.last().kuorumUser
        def globalBindings = [
                postType:messageSource.getMessage("${PostType.canonicalName}.${post.postType}",null,"", new Locale("ES_es")),
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

    def sendDebateNotificationMailInterestedUsers(Post post, Set<MailUserData> notificationUsers){
        KuorumUser debateOwner = post.debates.last().kuorumUser
        def globalBindings = [
                postType:messageSource.getMessage("${PostType.canonicalName}.${post.postType}",null,"", new Locale("ES_es")),
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
        mailNotificationsData.userBindings = notificationUsers.asList()
        mailNotificationsData.fromName = prepareFromName(debateOwner.name)
        mandrillAppService.sendTemplate(mailNotificationsData)
    }

    def sendPostDefendedNotificationMailAuthor(Post post){
        MailUserData mailUserData = new MailUserData(user:post.owner, bindings:[])
        def globalBindings = [
                postType:messageSource.getMessage("${PostType.canonicalName}.${post.postType}",null,"", new Locale("ES_es")),
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
                postType:messageSource.getMessage("${PostType.canonicalName}.${post.postType}",null,"", new Locale("ES_es")),
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
        MailUserData mailUserData = new MailUserData(user:post.defender, bindings:[])
        def globalBindings = [
                postType:messageSource.getMessage("${PostType.canonicalName}.${post.postType}",null,"", new Locale("ES_es")),
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
                postType:messageSource.getMessage("${PostType.canonicalName}.${post.postType}",null,"", new Locale("ES_es")),
                defender:post.defender.name,
                defenderLink:generateLink("userShow",post.defender.encodeAsLinkProperties()),
                debateOwner:post.owner.name,
                postName:post.title,
                postLink:generateLink("postShow", post.encodeAsLinkProperties()),
                postOwner: post.owner.name,
                postOwnerLink: generateLink("userShow",post.owner.encodeAsLinkProperties())
        ]

        MailData mailNotificationsData = new MailData()
        mailNotificationsData.mailType = MailType.NOTIFICATION_DEFENDED_USERS
        mailNotificationsData.globalBindings=globalBindings
        mailNotificationsData.userBindings = notificationUsers.asList()
        mailNotificationsData.fromName = prepareFromName(post.defender.name)
        mandrillAppService.sendTemplate(mailNotificationsData)
    }

    def sendVictoryNotificationUsers(Post post, Set<MailUserData> notificationUsers){
        def globalBindings = [
                postType:messageSource.getMessage("${PostType.canonicalName}.${post.postType}",null,"", new Locale("ES_es")),
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
        mailNotificationsData.userBindings = notificationUsers.asList()
        mailNotificationsData.fromName = prepareFromName(post.owner.name)
        mandrillAppService.sendTemplate(mailNotificationsData)
    }

    def sendPromotedPostMailUsers(Post post, KuorumUser sponsor, Set<MailUserData> notificationUsers){
        MailData mailNotificationsData = new MailData()
        mailNotificationsData.mailType = MailType.PROMOTION_USERS
        mailNotificationsData.globalBindings=globalBindingsForPromotedMails(post,sponsor)
        mailNotificationsData.userBindings = notificationUsers.asList()
        mailNotificationsData.fromName = prepareFromName(sponsor.name)
        mandrillAppService.sendTemplate(mailNotificationsData)
    }

    def sendPromotedPostMailOwner(Post post, KuorumUser sponsor){
        MailUserData mailUserData = new MailUserData(user:post.owner, bindings:[:])
        MailData mailNotificationsData = new MailData()
        mailNotificationsData.mailType = MailType.PROMOTION_OWNER
        mailNotificationsData.globalBindings=globalBindingsForPromotedMails(post,sponsor)
        mailNotificationsData.userBindings = [mailUserData]
        mailNotificationsData.fromName = prepareFromName(sponsor.name)
        mandrillAppService.sendTemplate(mailNotificationsData)
    }

    def sendPromotedPostMailSponsor(Post post, KuorumUser sponsor){
        MailUserData mailUserData = new MailUserData(user:sponsor, bindings:[:])
        MailData mailNotificationsData = new MailData()
        mailNotificationsData.mailType = MailType.PROMOTION_SPONSOR
        mailNotificationsData.globalBindings=globalBindingsForPromotedMails(post,sponsor)
        mailNotificationsData.userBindings = [mailUserData]
        mailNotificationsData.fromName = DEFAULT_SENDER_NAME
        mandrillAppService.sendTemplate(mailNotificationsData)
    }
    private def globalBindingsForPromotedMails(Post post, KuorumUser sponsor){
        Law law = post.law
        String commissionName = messageSource.getMessage("${CommissionType.canonicalName}.${law.commissions.first()}",null,"otros", new Locale("ES_es"))
        String postTypeName =   messageSource.getMessage("${PostType.canonicalName}.${post.postType}",null,"", new Locale("ES_es"))
        [
                postType:postTypeName,
                postName:post.title,
                postLink:generateLink("postShow", post.encodeAsLinkProperties()),
                promoter:sponsor.name,
                promoterLink:generateLink("userShow",sponsor.encodeAsLinkProperties()),
                postOwner: post.owner.name,
                postOwnerLink: generateLink("userShow",post.owner.encodeAsLinkProperties()),
                hashtag:post.law.hashtag,
                hashtagLink:generateLink("lawShow",law.encodeAsLinkProperties())
        ]
    }

    def sendVictoryNotificationDefender(Post post){
        def globalBindings = [
                postType:messageSource.getMessage("${PostType.canonicalName}.${post.postType}",null,"", new Locale("ES_es")),
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
        mailNotificationsData.userBindings = [new MailUserData(user:post.defender, bindings:[])]
        mailNotificationsData.fromName = prepareFromName(post.owner.name)
        mandrillAppService.sendTemplate(mailNotificationsData)
    }


    def verifyUser(KuorumUser user){
        mailchimpService.addSubscriber(user)
        sendUserAccountConfirmed(user)
        indexSolrService.index(user)
    }



    protected String generateLink(String mapping, linkParams) {
        grailsLinkGenerator.link(mapping:mapping,absolute: true,  params: linkParams)
    }

    protected String prepareFromName(String name){
        "$name $DEFAULT_VIA"
    }

}
