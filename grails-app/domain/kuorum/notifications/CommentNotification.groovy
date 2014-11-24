package kuorum.notifications

import kuorum.post.Post
import kuorum.users.KuorumUser

class CommentNotification extends Notification{

    KuorumUser commentWriter
    Post post
    String text

    static constraints = {
    }
}
