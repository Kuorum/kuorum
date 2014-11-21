package kuorum.notifications

import kuorum.post.Post
import kuorum.users.KuorumUser

/**
 * Represents the post owner notification when an user writes a comment on the post
 */
class CommentMyPostNotification extends CommentNotification{
    static constraints = {
    }
}
