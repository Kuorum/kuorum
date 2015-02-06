package kuorum.project

import groovy.time.TimeCategory
import kuorum.Institution
import kuorum.KuorumFile
import kuorum.PoliticalParty
import kuorum.Region
import kuorum.core.annotations.MongoUpdatable
import kuorum.core.annotations.Updatable
import kuorum.core.model.CommissionType
import kuorum.core.model.ProjectStatusType
import kuorum.core.model.project.ProjectUpdate
import kuorum.core.model.project.ProjectRegionStats
import kuorum.users.KuorumUser
import org.bson.types.ObjectId

@MongoUpdatable
class Project {

    def projectStatsService

    ObjectId id
    String hashtag
    @Updatable String shortName
    @Updatable String realName
    @Updatable String description
    @Updatable String introduction
    @Updatable List<CommissionType> commissions = []
    @Updatable Region region
    @Updatable Institution institution
    @Updatable PoliticalParty politicalParty
    @Updatable KuorumFile image
    @Updatable ProjectStatusType status = ProjectStatusType.OPEN
    @Updatable URL shortUrl
    @Updatable Boolean availableStats
    @Updatable Integer relevance = -1

    Date dateCreated
    Boolean published = Boolean.FALSE
    Date publishDate
    AcumulativeVotes peopleVotes = new AcumulativeVotes()

    //New fields for Project
    @Updatable Date deadline
    @Updatable KuorumFile urlYoutube
    @Updatable KuorumFile pdfFile
    KuorumUser owner
    @Updatable List<ProjectUpdate> updates = []


    static embedded = ['region','peopleVotes','image' ]

    static constraints = {
        hashtag matches: '#[a-zA-Z0-9]+', nullable: false, unique: true, minSize: 1, maxSize: 17
        shortName nullable: false, minSize: 1, maxSize: 107
        commissions nullable: false, minSize:1, maxSize: 4
        realName nullable:true
        description nullable:false, minSize: 1, maxSize: 5000
        introduction nullable:true
        region  nullable:false
        institution nullable:false, validator: { val, obj ->
            if (obj.region && obj.region != val.region) {
                return ['notSameRegionAsInstitution']
            }
        }
        image nullable:true, validator: { val, obj ->
            if (!val && !obj.urlYoutube) {
                return ['imageOrUrlYoutubeRequired']
            }
        }
        publishDate nullable:true
        politicalParty nullable:true

        //New validations for Project
        deadline nullable: false, validator: { val, obj ->
            if (val < new Date().clearTime()) {
                return ['deadlineLessThanToday']
            } else if (val.clearTime() > new Date().clearTime() + 120) {
                return ['deadlineGreaterThan120Days']
            }
        }
        urlYoutube nullable: true, validator: { val, obj ->
            if (!val && !obj.image) {
                return ['imageOrUrlYoutubeRequired']
            }
        }
        pdfFile nullable: true
        owner nullable: false
        updates nullable: true
    }

    static List<Project> findAllByPublishedAndRegion(Boolean published, Region region){
        Project.collection.find(['region._id':region.id, 'published':published],[_id:1]).collect{Project.get(it._id)}
    }

    static Long countByPublishedAndRegion(Boolean published, Region region){
        Project.collection.count(['region._id':region.id, 'published':published])
    }

    static mapping = {
        hashtag index:true, indexAttributes: [unique:true]
    }

    static transients = ['votesInRegion','lastUpdate','timeToDeadline']

    Long getVotesInRegion(){
        ProjectRegionStats projectRegionStats = projectStatsService.calculateRegionStats(this)
        projectRegionStats.totalVotes.total
    }

    String getLastUpdate(){
        ProjectUpdate projectUpdate = this.updates.sort{it.dateCreated}.first()
        TimeCategory.minus(new  Date(), projectUpdate.dateCreated.clearTime())
    }

    String getTimeToDeadline(){
        TimeCategory.minus(this.deadline.clearTime(),this.dateCreated.clearTime())
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