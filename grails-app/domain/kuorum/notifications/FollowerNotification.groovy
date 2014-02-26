package kuorum.notifications

import kuorum.mail.MailType
import kuorum.users.KuorumUser

class FollowerNotification extends Notification{

    KuorumUser follower
    MailType mailType = MailType.NOTIFICATION_FOLLOWER

    static constraints = {
    }
}
