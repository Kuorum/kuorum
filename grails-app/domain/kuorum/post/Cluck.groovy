package kuorum.post

import kuorum.law.Law
import kuorum.users.KuorumUser
import org.bson.types.ObjectId

class Cluck {

    ObjectId id
    /**
     * The user who has created the cluck
     */
    KuorumUser owner
    /**
     * The user who has created the post clucked
     */
    KuorumUser postOwner

    Date dateCreated

    Law law

    Post post

    static constraints = {
        owner index:true
        law index:true
    }

    static mapping = {
        sort id:"desc"
    }
}
