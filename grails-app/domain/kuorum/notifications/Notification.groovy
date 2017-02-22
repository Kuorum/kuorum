package kuorum.notifications

import kuorum.mail.MailType
import kuorum.users.KuorumUser
import org.bson.types.ObjectId

@Deprecated
class Notification {
/*
  Notification is not abstract because grails(GORM) tries to instantiate Notificationa, and
  then showing _class, cast the object to de corresponding class.
 */
    ObjectId id
    KuorumUser kuorumUser
    Date dateCreated

    /**
     * Representes if this notification is an alert
     */
    Boolean isAlert = Boolean.FALSE
    /**
     * If notification is an alert, represents if it has been done by the user
     */
    Boolean isActive = Boolean.FALSE

    /**
     * If notification is an alert and the user has marked as "Do later"
     */
    Boolean isPostponed = Boolean.FALSE

    //If no mailType, no send mail
    MailType mailType
    static constraints = {
        mailType  nullable: true
    }
    static mapping = {
        compoundIndex kuorumUser:1, dateCreated:-1
//        collection "notification"
    }
}
