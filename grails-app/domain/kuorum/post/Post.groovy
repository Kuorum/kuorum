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
    List<PostComment> comments = []
    List<PostComment> debates = []

    /**
     * First firstCluck / owners firstCluck: Is going to be used to reference the sponsors and politician supports.
     */
    Cluck firstCluck

    //static hasMany = [sponsors:Sponsor]
    static embedded = ['sponsors', 'comments', 'debates']

    static constraints = {
        numVotes min:0
        numClucks min:0
        photo nullable:true
        firstCluck nullable:true
    }

    String toString(){
        "${title} (${id})"
    }
}
