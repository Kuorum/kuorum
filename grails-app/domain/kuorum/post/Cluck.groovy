package kuorum.post

import kuorum.law.Law
import kuorum.users.KuorumUser
import org.bson.types.ObjectId

class Cluck {

    ObjectId id
    /**
     * The user who has created the firstCluck
     */
    KuorumUser owner
    /**
     * The user who has created the post clucked
     */
    KuorumUser postOwner

    /**
     *The politician user who has supported the Cluck
     */
    KuorumUser defendedBy

    /**
     * The sponsors who has financed any amount of money
     */
    List<Sponsor> sponsors =[]

    /**
     * Represents if is the cluck created by the post owner
     */
    Boolean isFirstCluck = Boolean.FALSE

    Law law

    Post post

    Date dateCreated
    Date lastUpdated

    static hasMany = [sponsors:Sponsor]
    static embedded = ['sponsors']

    static constraints = {
        owner index:true
        law index:true
        lastUpdated index:true
        defendedBy nullable: true,  index:true
    }

    static mapping = {
        autoTimestamp true
        sort lastUpdated:"desc"
    }
}
