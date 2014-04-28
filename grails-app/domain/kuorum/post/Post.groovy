package kuorum.post

import kuorum.KuorumFile
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
     * Politician who has defended the post
     */
    KuorumUser defender
    Law law
    @Updatable String title
    @Updatable String text
    @Updatable Integer pdfPage
    Integer numVotes
    Integer numClucks
    @Updatable PostType postType
    @Updatable URL shortUrl
    Boolean victory = false
    Date dateCreated
    List<Sponsor> sponsors = []
    List<PostComment> comments = []
    List<PostComment> debates = []
    Boolean published = false

    @Updatable
    KuorumFile multimedia

    /**
     * First firstCluck / owners firstCluck: Is going to be used to reference the sponsors and politician supports.
     */
    Cluck firstCluck

    //static hasMany = [sponsors:Sponsor]
    static embedded = ['sponsors', 'comments', 'debates','multimedia']

    static constraints = {
        numVotes min:0
        numClucks min:0
        firstCluck nullable:true
        defender nullable:true
//        defender nullable:true, validator:{val, obj ->
//            if (obj.victory && !defender){
//                return "victoryPostWithoutDefender"
//            }
//        }
        text nullable: false, blank: false
        title nullable:false, blank: false
        postType nullable:false
        multimedia nullable:true
        pdfPage nullable:true
        shortUrl nullable:true
    }

    String toString(){
        "${title} (${id})"
    }
}
