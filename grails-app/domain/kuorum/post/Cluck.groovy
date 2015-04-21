package kuorum.post

import kuorum.Region
import kuorum.project.Project
import kuorum.users.KuorumUser
import org.bson.types.ObjectId

class Cluck {

    ObjectId id

    CluckAction cluckAction;
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
//    @Deprecated
//    KuorumUser defendedBy

    /**
     * The sponsors who has financed any amount of money
     */
//    @Deprecated
//    List<Sponsor> sponsors =[]

    /**
     * The KuorumUser Ids that are involved in the  post debate
     */
//    @Deprecated
//    List<ObjectId> debateMembers=[]

    /**
     * Represents if is the cluck created by the post owner
     */
    @Deprecated
    Boolean isFirstCluck = Boolean.FALSE

    Project project

    Post post

    Region region

    Date dateCreated
    Date lastUpdated

//    static hasMany = [sponsors:Sponsor]
    static embedded = ['region']

    static constraints = {
        owner index:true
        project index:true
        lastUpdated index:true
//        defendedBy nullable: true,  index:true
    }

    static mapping = {
        autoTimestamp true
        sort lastUpdated:"desc"
    }
}
