package kuorum.notifications

import kuorum.core.model.VoteType
import kuorum.users.KuorumUser

class CommentVotedNotification extends CommentNotification{

    KuorumUser votingUser
    VoteType voteType

    static constraints = {
    }
}
