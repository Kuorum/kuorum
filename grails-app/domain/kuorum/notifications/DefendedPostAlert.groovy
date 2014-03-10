package kuorum.notifications

import kuorum.mail.MailType

class DefendedPostAlert extends  DefendedPostNotification{

    MailType mailType = MailType.NOTIFICATION_DEFENDED_AUTHOR

    static constraints = {
    }
}
