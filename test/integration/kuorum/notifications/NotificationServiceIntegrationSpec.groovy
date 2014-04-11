package kuorum.notifications

import kuorum.core.model.search.Pagination
import kuorum.law.Law
import kuorum.users.KuorumUser
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by iduetxe on 25/03/14.
 */
class NotificationServiceIntegrationSpec extends Specification{

    def fixtureLoader
    def notificationService

    def setup(){
        Notification.collection.getDB().dropDatabase()
        fixtureLoader.load("testBasicData")
    }

    @Unroll
    void "test find #numNotificationFound user notifications and notifications days are #rangeDate"(){
        given: "User and one notification per day"
        KuorumUser user = KuorumUser.findByEmail("peter@example.com")
        Law law = Law.findByHashtag("#codigoPenal")

        rangeDate.each {
            LawClosedNotification notification = new LawClosedNotification(law:law)
            notification.kuorumUser = user
            notification.save(flush: true, failOnError: true)
            notification.dateCreated = (new Date() + it)
            notification.save(flush: true, failOnError: true)
        }
        Pagination pagination = new Pagination(max:numResults, offset:offset)
        when:
        notificationService.markUserNotificationsAsChecked(user)
        List<Notification> notifications = notificationService.findUserNotificationsNotChecked(user,pagination)
        then:
        notifications.size() == numNotificationFound
        if (notifications.size())
            notifications.first().dateCreated >= notifications.last().dateCreated
        where:
        rangeDate | numNotificationFound | numResults | offset
        (-5..1)   |   1                  | 5          | 0
        (-5..5)   |   5                  | 5          | 0
        (-5..10)  |   5                  | 5          | 5
        (-5..-1)  |   0                  | 5          | 0
        (-5..5)   |   0                  | 5          | 5
    }
}
