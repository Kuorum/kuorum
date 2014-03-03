package kuorum.notifications

import kuorum.mail.MailType

class DebateAlertNotification extends DebateNotification{

    MailType mailType = MailType.ALERT_MORE_DEBATE//MailType.ALERT_FIRST_DEBATE

    static constraints = {
    }
}
