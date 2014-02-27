package kuorum.notifications

import kuorum.post.Post

class MilestoneNotification extends Notification{

    Post post
    Long numVotes

    static constraints = {
    }
}
