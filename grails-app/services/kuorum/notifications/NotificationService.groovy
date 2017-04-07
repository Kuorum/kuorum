package kuorum.notifications

import com.fasterxml.jackson.core.type.TypeReference
import grails.transaction.Transactional
import kuorum.OfferPurchased
import kuorum.campaign.PollCampaignVote
import kuorum.core.model.search.SearchNotifications
import kuorum.post.Post
import kuorum.post.PostComment
import kuorum.users.KuorumUser
import kuorum.util.rest.RestKuorumApiService
import org.codehaus.groovy.grails.web.mapping.LinkGenerator
import org.kuorum.rest.model.kuorumUser.config.NotificationConfigRDTO
import org.kuorum.rest.model.notification.NotificationPageRSDTO

@Transactional
class NotificationService {

    def kuorumMailService

    LinkGenerator grailsLinkGenerator

    RestKuorumApiService restKuorumApiService;

    /**
     * Returns all notifications
     * @param user
     * @return
     */
    NotificationPageRSDTO findUserNotifications(SearchNotifications searchNotifications){
        Map<String, String> params = [userAlias: searchNotifications.user.id.toString()]
        Map<String, String> query = [page:searchNotifications.offset, size:searchNotifications.max]
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.ACCOUNT_NOTIFICATIONS,
                params,
                query,
                new TypeReference<NotificationPageRSDTO>(){}
        )

        NotificationPageRSDTO notificationPage = null
        if (response.data) {
            notificationPage = (NotificationPageRSDTO) response.data
        }

        notificationPage
    }

    /**
     * Marks all the notifications as checked by the user
     * @param user
     */
    void markUserNotificationsAsChecked(KuorumUser user){
        Map<String, String> params = [userAlias: user.id.toString()]
        def response = restKuorumApiService.delete(
                RestKuorumApiService.ApiMethod.ACCOUNT_NOTIFICATIONS,
                params,
                [:]
        )

        NotificationPageRSDTO notificationPage = null
        if (response.data) {
            notificationPage = (NotificationPageRSDTO) response.data
        }

        notificationPage
    }

    @Deprecated
    public void sendPollCampaignNotification(PollCampaignVote pollCampaign){
        kuorumMailService.sendPollCampaignMail(pollCampaign)
    }

    public void sendPoliticianContactNotification(KuorumUser politician, KuorumUser user, String message, String cause){
        kuorumMailService.sendPoliticianContact(politician, user, message, cause)
        //kuorumMailService.sendPoliticianContactKuorumNotification(politician, user, message, cause) // MandrillApp API problems
    }

    @Deprecated
    void sendCommentNotifications(Post post, PostComment comment){
        // Does nothing
    }

    NotificationConfigRDTO getNotificationsConfig(KuorumUser user){
        Map<String, String> params = [userAlias: user.id.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.ACCOUNT_NOTIFICATIONS_CONFIG,
                params,
                query,
                new TypeReference<NotificationConfigRDTO>(){}
        )

        NotificationConfigRDTO notificationPage = null
        if (response.data) {
            notificationPage = (NotificationConfigRDTO) response.data
        }

        notificationPage
    }

    void saveNotificationsConfig(KuorumUser user,NotificationConfigRDTO notificationConfigRDTO){
        Map<String, String> params = [userAlias: user.id.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.put(
                RestKuorumApiService.ApiMethod.ACCOUNT_NOTIFICATIONS_CONFIG,
                params,
                query,
                notificationConfigRDTO,
                null
        )

        NotificationConfigRDTO notificationPage = null

    }

    /**
    * A post has been supported by a politician
    */
    @Deprecated
    void sendPostDefendedNotification(Post post){
        // Does nothing
    }

    void sendOfferPurchasedNotification(KuorumUser user, OfferPurchased offerPurchased){
        kuorumMailService.sendPoliticianSubscriptionToAdmins(user,offerPurchased)
    }

    void sendWelcomeRegister(KuorumUser user){
        kuorumMailService.sendWelcomeRegister(user)
    }

    void sendEditorPurchaseNotification(KuorumUser editor){
        kuorumMailService.sendNewEditorRequestToAdmins(editor)
    }
    void sendBetaTesterPurchaseNotification(KuorumUser politician){
        kuorumMailService.sendNewBetaTesterRequestToAdmins(politician)
    }
}
