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
@Deprecated
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
    Integer numVotes
    Integer numClucks
    @Updatable URL shortUrl
    Boolean victory = false
    CommitmentType commitmentType
    Date dateCreated
    Date lastUpdated
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
        multimedia nullable:true
        shortUrl nullable:true
        commitmentType nullable:true
    }


    def beforeInsert() {
        prepareIndexMetaData()
    }

    def beforeUpdate() {
        prepareIndexMetaData()
    }

    private static final String INDEX_META_DATA_FIELD = "indexMetaData"
    private void prepareIndexMetaData(){
        def indexMetaData = [:]
        indexMetaData.put("hashtag",this.project.hashtag)
        indexMetaData.put("ownerName",this.owner.name)
        indexMetaData.put("commissions",this.project.commissions.collect{it.toString()}) //Serializing error if not "toString"
        this[INDEX_META_DATA_FIELD] = indexMetaData
    }

    String toString(){
        "${title} (${id})"
    }

}
