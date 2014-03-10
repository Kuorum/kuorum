package kuorum.notifications

import kuorum.mail.MailType
import kuorum.post.Post
import kuorum.users.KuorumUser

class VictoryNotification extends Notification{

    MailType mailType = MailType.NOTIFICATION_VICTORY
    Post post
    KuorumUser politician

    static constraints = {
    }
}
