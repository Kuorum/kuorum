package kuorum.users

import org.bson.types.ObjectId

class KuorumUserController {

    static scaffold = true

    def show(String id){
        KuorumUser user = KuorumUser.get(new ObjectId(id))
        [user:user]
    }
}
