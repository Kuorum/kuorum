package kuorum.notifications

import kuorum.post.Post
import kuorum.users.KuorumUser

class DefendedPostNotification extends Notification{

    KuorumUser defender
    Post post

    static constraints = {
    }
}
