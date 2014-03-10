package kuorum.notifications

import kuorum.mail.MailType

class DebateAlertNotification extends DebateNotification{

    MailType mailType = MailType.NOTIFICATION_DEBATE_AUTHOR//MailType.ALERT_FIRST_DEBATE

    static constraints = {
    }
}
