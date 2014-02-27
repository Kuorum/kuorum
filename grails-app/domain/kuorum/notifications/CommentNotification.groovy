package kuorum.notifications

import kuorum.post.Post
import kuorum.users.KuorumUser

class CommentNotification extends Notification{

    KuorumUser tertullian
    Post post

    static constraints = {
    }
}
