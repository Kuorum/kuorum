package kuorum.post

import kuorum.users.KuorumUser
import org.bson.types.ObjectId

class PostComment {

    String text
    Date dateCreated
    Boolean moderated = Boolean.FALSE
    Boolean deleted = Boolean.FALSE

    /**
     * KuorumUsers which are vote positively the comment
     */
    List<ObjectId> positiveVotes = [] //ObjectId is the IDs of users instead embedded KuorumUser. If embed user, gorm doesn't work properly
    /**
     * KuorumUsers which are vote negatively the comment
     */
    List<ObjectId> negativeVotes = [] //ObjectId is the IDs of users instead embedded KuorumUser. If embed user, gorm doesn't work properly

    /*
     * It is a trick for embedded this object
     */
    transient KuorumUser kuorumUser
    ObjectId kuorumUserId


    void setKuorumUser(KuorumUser kuorumUser){
        kuorumUserId = kuorumUser.id
        this.kuorumUser = kuorumUser
    }

    KuorumUser getKuorumUser(){
        if (!kuorumUser)
            kuorumUser = KuorumUser.get(kuorumUserId)
        kuorumUser
    }

    static constraints = {
    }
}
