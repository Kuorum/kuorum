package kuorum.notifications

import kuorum.mail.MailType

class DebateAlertNotification extends DebateNotification{

    MailType mailType = MailType.ALERT_DEBATE

    static constraints = {
    }
}
