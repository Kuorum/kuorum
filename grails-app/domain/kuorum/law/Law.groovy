package kuorum.law

import kuorum.Institution
import kuorum.Region
import kuorum.core.annotations.MongoUpdatable
import kuorum.core.annotations.Updatable
import kuorum.core.model.CommissionType
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
    Boolean open = Boolean.TRUE
    Date dateCreated
    Boolean published = Boolean.FALSE

    static embedded = ['region' ]

    static constraints = {
        hashtag matches: '#[a-zA-Z0-9]+', nullable: false
        shortName nullable: false
        commissions nullable: false
        realName nullable:false
        description nullable:false
        introduction nullable:false
        institution nullable:false
        region  nullable:false, validator: { val, obj ->
            if (val != obj.institution.region) {
                return ['notSameRegionAsInstitution']
            }
        }
    }

    static mapping = {
        hashtag index:true, indexAttributes: [unique:true]
    }


    String toString(){
        "${hashtag} (${id})"
    }
}
