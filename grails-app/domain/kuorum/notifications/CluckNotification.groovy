package kuorum.notifications

import kuorum.mail.MailType

class CluckNotification extends Notification{

    MailType mailType = MailType.NOTIFICATION_CLUCK
    static constraints = {
    }
}
