package kuorum.notifications

import kuorum.post.Post
import kuorum.users.KuorumUser

class DebateNotification extends Notification{

    Boolean isFirstDebate = false
    KuorumUser debateWriter
    Post post
    Integer idDebate

    static constraints = {
    }
}
