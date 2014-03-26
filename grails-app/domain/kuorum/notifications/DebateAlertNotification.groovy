package kuorum.notifications

import kuorum.mail.MailType

class DebateAlertNotification extends DebateNotification{

    Boolean isAlert = Boolean.TRUE
    Boolean isActive = Boolean.TRUE

    MailType mailType = MailType.NOTIFICATION_DEBATE_AUTHOR//MailType.ALERT_FIRST_DEBATE

    static constraints = {
    }
}
