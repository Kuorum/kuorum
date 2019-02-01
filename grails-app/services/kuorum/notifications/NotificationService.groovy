package kuorum.notifications

import com.fasterxml.jackson.core.type.TypeReference
import grails.transaction.Transactional
import kuorum.core.model.search.SearchNotifications
import kuorum.register.KuorumUserSession
import kuorum.users.KuorumUser
import kuorum.util.rest.RestKuorumApiService
import org.codehaus.groovy.grails.web.mapping.LinkGenerator
import org.kuorum.rest.model.kuorumUser.BasicDataKuorumUserRSDTO
import org.kuorum.rest.model.kuorumUser.config.NotificationConfigRDTO
import org.kuorum.rest.model.notification.NotificationPageRSDTO

@Transactional
class NotificationService {

    def kuorumMailService

    LinkGenerator grailsLinkGenerator

    RestKuorumApiService restKuorumApiService

    /**
     * Returns all notifications
     * @param user
     * @return
     */
    NotificationPageRSDTO findUserNotifications(SearchNotifications searchNotifications){
        Map<String, String> params = [userId: searchNotifications.user.id.toString()]
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
    void markUserNotificationsAsChecked(KuorumUserSession user){
        Map<String, String> params = [userId: user.id.toString()]
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

    void sendPoliticianContactNotification(BasicDataKuorumUserRSDTO userContacted, KuorumUserSession user, String message, String cause){
        kuorumMailService.sendPoliticianContact(userContacted, user, message, cause)
        //kuorumMailService.sendPoliticianContactKuorumNotification(userContacted, user, message, cause) // MandrillApp API problems
    }

    NotificationConfigRDTO getNotificationsConfig(KuorumUserSession user){
        Map<String, String> params = [userId: user.id.toString()]
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
        Map<String, String> params = [userId: user.id.toString()]
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

    void sendWelcomeRegister(KuorumUserSession user){
        kuorumMailService.sendWelcomeRegister(user)
    }
}
