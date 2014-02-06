package kuorum.post

import kuorum.users.KuorumUser

class Cluck {

    /**
     * The user who has created the cluck
     */
    KuorumUser owner
    /**
     * The user who has created the post clucked
     */
    KuorumUser postOwner

    Post post

    static constraints = {
    }
}
