package kuorum.notifications

import kuorum.mail.MailType
import kuorum.users.KuorumUser

class Notification {

    KuorumUser kuorumUser
    Date dateCreated
    MailType mailType
    static constraints = {
        mailType  nullable: true
    }
}
