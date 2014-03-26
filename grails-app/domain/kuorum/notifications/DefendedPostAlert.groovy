package kuorum.notifications

import kuorum.mail.MailType

class DefendedPostAlert extends  DefendedPostNotification{

    Boolean isAlert = Boolean.TRUE
    Boolean isActive = Boolean.TRUE

    MailType mailType = MailType.NOTIFICATION_DEFENDED_AUTHOR

    static constraints = {
    }
}
