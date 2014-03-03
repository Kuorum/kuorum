package kuorum.notifications

import kuorum.mail.MailType
import kuorum.post.Post
import kuorum.users.KuorumUser

class DebateNotification extends Notification{

    MailType mailType = MailType.NOTIFICATION_MORE_DEBATE //MailType.NOTIFICATION_FIRST_DEBATE
    Boolean isFirstDebate = false
    KuorumUser politician
    Post post

    static constraints = {
    }
}
