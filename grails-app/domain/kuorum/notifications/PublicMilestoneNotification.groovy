package kuorum.notifications

import kuorum.mail.MailType

class PublicMilestoneNotification extends MilestoneNotification{

    final MailType mailType = MailType.NOTIFICATION_PUBLIC_MILESTONE

    static constraints = {
    }
}
