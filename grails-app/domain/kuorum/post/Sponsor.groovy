package kuorum.post

import kuorum.users.KuorumUser
import org.bson.types.ObjectId

class Sponsor {

    Double amount

    /**
     * It is a trick for embedded object
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

    String toString(){
        "$kuorumUser ($amuount)"
    }

    boolean equals(def o){
        kuorumUserId == o?.kuorumUserId
    }
}
