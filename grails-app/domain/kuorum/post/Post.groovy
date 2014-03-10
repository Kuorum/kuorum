package kuorum.post

import kuorum.core.annotations.MongoUpdatable
import kuorum.core.annotations.Updatable
import kuorum.core.model.PostType
import kuorum.law.Law
import kuorum.users.KuorumUser
import org.bson.types.ObjectId

@MongoUpdatable
class Post {

    ObjectId id
    KuorumUser owner
    /**
     * Politician who has supported the post
     */
    KuorumUser supportedBy
    Law law
    @Updatable String title
    @Updatable String text
    @Updatable String photo
    Integer numVotes
    Integer numClucks
    @Updatable PostType postType
    Boolean victory = false
    Date dateCreated
    List<Sponsor> sponsors = []
    List<PostComment> comments = []
    List<PostComment> debates = []
    Boolean published = false

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
        politician nullable:true
    }

    String toString(){
        "${title} (${id})"
    }
}
