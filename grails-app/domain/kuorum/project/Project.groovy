package kuorum.project

import grails.validation.Validateable
import kuorum.KuorumFile
import kuorum.core.annotations.MongoUpdatable
import kuorum.core.annotations.Updatable
import kuorum.core.model.CommissionType
import kuorum.core.model.ProjectStatusType
import kuorum.core.model.project.ProjectBasicStats
import kuorum.users.KuorumUser
import org.bson.types.ObjectId

@Deprecated
@MongoUpdatable
class Project {

    ObjectId id
    String hashtag
    @Updatable String shortName
    @Updatable String realName
    @Updatable String description
    @Updatable List<CommissionType> commissions = []
    @Updatable KuorumFile image
    @Updatable ProjectStatusType status = ProjectStatusType.OPEN
    @Deprecated
    @Updatable URL shortUrl
    @Updatable Boolean availableStats
    @Updatable Integer relevance = -1

    Date dateCreated
    Boolean published = Boolean.FALSE
    Date publishDate
    Date lastUpdated
    AcumulativeVotes peopleVotes = new AcumulativeVotes()

    // New fields for Project
    @Updatable Date deadline
    @Updatable KuorumFile urlYoutube
    @Updatable KuorumFile pdfFile
    KuorumUser owner
    List<ProjectUpdate> updates = []

    ProjectBasicStats projectBasicStats;

    static embedded = ['peopleVotes','image','updates' ]

    static constraints = {
        hashtag matches: '#[a-zA-Z0-9]+', nullable: false, unique: true, minSize: 1, maxSize: 17
        shortName nullable: false, minSize: 1, maxSize: 107
        commissions nullable: false, minSize:1, maxSize: 4
        realName nullable:true
        description nullable:false, minSize: 1

        image nullable:true, validator: { val, obj ->
            if (!val && !obj.urlYoutube) {
                return ['imageOrUrlYoutubeRequired']
            }
        }
        publishDate nullable:true

        //New validations for Project
//        deadline nullable: false, validator: { val, obj ->
//            if (val < new Date().clearTime()) {
//                return ['deadlineLessThanToday']
//            } else if (val.clearTime() > new Date().clearTime() + 120) {
//                return ['deadlineGreaterThan120Days']
//            }
//        }
        urlYoutube nullable: true, validator: { val, obj ->
            if (!val && !obj.image) {
                return ['imageOrUrlYoutubeRequired']
            }
        }
        pdfFile nullable: true
        owner nullable: false
        updates nullable: true
        shortUrl nullable:true

    }

    static mapping = {
        hashtag index:true, indexAttributes: [unique:true]
    }

    static transients = ['lastUpdate', 'percentagePositiveVotes', 'percentageNegativeVotes', 'percentageAbsVotes', 'numPublicPost']

    Date getLastUpdate(){
        this.updates.sort{it.dateCreated}.last().dateCreated
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
        indexMetaData.put("ownerName", this.owner.name)
        this[INDEX_META_DATA_FIELD] = indexMetaData
    }

    String toString(){
        "${hashtag} (${id})"
    }
}

public class AcumulativeVotes {
    Long yes = 0
    Long no = 0
    Long abs = 0
    Long numPosts = 0

    Long getTotal(){
        yes+no+abs
    }
}

@Validateable
@Deprecated
class ProjectUpdate {

    String description
    KuorumFile image
    KuorumFile urlYoutube
    Date dateCreated

    static embedded = ['image','urlYoutube' ]

    static constraints = {
        description maxSize:500
        image nullable: true
        urlYoutube nullable: true
        dateCreated nullable: false
    }
}