package kuorum.post

import kuorum.KuorumFile
import kuorum.core.annotations.MongoUpdatable
import kuorum.core.annotations.Updatable
import kuorum.core.model.CommitmentType
import kuorum.core.model.PostType
import kuorum.project.Project
import kuorum.users.KuorumUser
import kuorum.users.PersonalData
import org.bson.types.ObjectId

@MongoUpdatable
class Post {

    ObjectId id
    KuorumUser owner
    PersonalData ownerPersonalData
    /**
     * Politician who has defended the post
     */
    KuorumUser defender
    PersonalData defenderPersonalData

    /**
     * Date when the defender defends the post
     */
    Date defenderDate
    Project project
    @Updatable String title
    @Updatable String text
    @Updatable Integer pdfPage
    Integer numVotes
    Integer numClucks
    @Updatable PostType postType
    @Updatable URL shortUrl
    Boolean victory = false
    CommitmentType commitmentType
    Date dateCreated
    List<Sponsor> sponsors = []
    List<PostComment> comments = []
    List<PostComment> debates = []
    Boolean published = false

    @Updatable
    KuorumFile multimedia

    //static hasMany = [sponsors:Sponsor]
    static embedded = ['sponsors', 'comments', 'debates','multimedia','ownerPersonalData', 'defenderPersonalData']

    static constraints = {
        numVotes min:0
        numClucks min:0
        defender nullable:true
        defenderPersonalData nullable: true
        defenderDate nullable: true, validator:{val, obj ->
            if (obj.defender && !val){
                'defenderWithoutCreationDate'
            }
        }
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
        commitmentType nullable:true
    }

    String toString(){
        "${title} (${id})"
    }
}
