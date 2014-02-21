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
    Boolean victory = false
    Date dateCreated
    List<Sponsor> sponsors = []

    /**
     * First cluck / owners cluck: Is going to be used to reference the sponsors and politician supports.
     */
    Cluck cluck

    //static hasMany = [sponsors:Sponsor]
    static embedded = ['sponsors']

    static constraints = {
        numVotes min:0
        numClucks min:0
        photo nullable:true
        cluck nullable:true
    }

    String toString(){
        "${title} (${id})"
    }
}
