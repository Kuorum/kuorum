package kuorum.law

import kuorum.Institution
import kuorum.KuorumFile
import kuorum.ParliamentaryGroup
import kuorum.Region
import kuorum.core.annotations.MongoUpdatable
import kuorum.core.annotations.Updatable
import kuorum.core.model.CommissionType
import kuorum.core.model.LawStatusType
import org.bson.types.ObjectId

@MongoUpdatable
class Law {

    ObjectId id
    String hashtag
    @Updatable String shortName
    @Updatable String realName
    @Updatable String description
    @Updatable String introduction
    @Updatable List<CommissionType> commissions = []
    @Updatable Region region
    @Updatable Institution institution
    @Updatable ParliamentaryGroup parliamentaryGroup
    @Updatable KuorumFile image
    @Updatable LawStatusType status = LawStatusType.OPEN
    @Updatable URL urlPdf
    @Updatable URL shortUrl
    @Updatable Boolean availableStats
    @Updatable Integer relevance = -1;

    Date dateCreated
    Boolean published = Boolean.FALSE
    Date publishDate
    AcumulativeVotes peopleVotes = new AcumulativeVotes()


    static embedded = ['region','peopleVotes','image' ]

    static constraints = {
        hashtag matches: '#[a-zA-Z0-9]+', nullable: false, unique: true
        shortName nullable: false
        commissions nullable: false, minSize:1
        realName nullable:false
        description nullable:false
        introduction nullable:false
        region  nullable:false
        institution nullable:false, validator: { val, obj ->
            if (obj.region && obj.region != val.region) {
                return ['notSameRegionAsInstitution']
            }
        }
        urlPdf nullable:true, url:true
        //TODO: image no es nullable
        image nullable:true
        publishDate nullable:true
        parliamentaryGroup nullable:true
    }

    static List<Law> findAllByPublishedAndRegion(Boolean published, Region region){
        Law.collection.find(['region._id':region.id, 'published':published],[_id:1]).collect{Law.get(it._id)}
    }

    static Long countByPublishedAndRegion(Boolean published, Region region){
        Law.collection.count(['region._id':region.id, 'published':published])
    }

    static mapping = {
        hashtag index:true, indexAttributes: [unique:true]
    }


    String toString(){
        "${hashtag} (${id})"
    }
}

public class AcumulativeVotes {
    Long yes = 0
    Long no = 0
    Long abs = 0

    Long getTotal(){
        yes+no+abs
    }
}