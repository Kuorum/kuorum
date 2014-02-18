package kuorum.post

import kuorum.core.model.PostType
import kuorum.law.Law
import kuorum.users.KuorumUser
import org.bson.types.ObjectId

class Post {

    ObjectId id
    KuorumUser owner
    Law law
    String title
    String text
    String photo
    Integer numVotes
    Integer numClucks
    PostType postType
    Date dateCreated

    static constraints = {
        numVotes min:0
        numClucks min:0
        photo nullable:true
    }

    String toString(){
        "${title} (${id})"
    }
}
