package kuorum.notifications

import kuorum.mail.MailType
import kuorum.post.Post
import kuorum.users.KuorumUser

class CluckNotification extends Notification{

    final MailType mailType = MailType.NOTIFICATION_CLUCK
    Post post
    KuorumUser clucker

    static constraints = {
    }
}
