package kuorum.post

import kuorum.users.KuorumUser
import org.bson.types.ObjectId

class PostComment {

    String text
    Date dateCreated

    /**
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
