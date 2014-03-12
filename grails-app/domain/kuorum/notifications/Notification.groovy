package kuorum.notifications

import kuorum.mail.MailType
import kuorum.users.KuorumUser
import org.bson.types.ObjectId

class Notification {

    ObjectId id
    KuorumUser kuorumUser
    Date dateCreated

    //If no mailType, no send mail
    MailType mailType
    static constraints = {
        mailType  nullable: true
    }
}
