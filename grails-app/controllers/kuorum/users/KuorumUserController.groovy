package kuorum.users

import org.bson.types.ObjectId

class KuorumUserController {

    static scaffold = true

    def show(String id){
        KuorumUser user = KuorumUser.get(new ObjectId(id))
        [user:user]
    }

    def showCitizen(String id){
        KuorumUser user = KuorumUser.get(new ObjectId(id))
        render (view:"show", model:[user:user])
    }

    def showOrganization(String id){
        KuorumUser user = KuorumUser.get(new ObjectId(id))
        render (view:"show", model:[user:user])
    }

    def showPolitician(String id){
        KuorumUser user = KuorumUser.get(new ObjectId(id))
        render (view:"show", model:[user:user])
    }


}
