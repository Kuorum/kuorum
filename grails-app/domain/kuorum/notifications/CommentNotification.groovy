package kuorum.notifications

import kuorum.post.Post
import kuorum.users.KuorumUser

class CommentNotification extends Notification{

    // TODO: Think how to take this configuration out from here using a cleaner method
    static final Integer TEXT_BRIEF_SIZE = 20

    KuorumUser commentWriter
    Post post
    String text

    static constraints = {
    }
}
