package kuorum.notifications

import kuorum.mail.MailType
import kuorum.post.Post
import kuorum.users.KuorumUser

class DebateNotification extends Notification{

    MailType mailType = MailType.NOTIFICATION_DEBATE
    KuorumUser politician
    Post post

    static constraints = {
    }
}
