package kuorum.law

import kuorum.Institution
import kuorum.Region
import kuorum.core.model.CommissionType
import org.bson.types.ObjectId

class Law {

    ObjectId id
    String hashtag
    String shortName
    String realName
    String description
    String introduction
    List<CommissionType> commissions = []
    Region region
    Boolean open = Boolean.TRUE
    Date dateCreated

    static embedded = ['region' ]

    static constraints = {
        hashtag matches: '#[a-zA-Z0-9]+', nullable: false
        shortName nullable: false
        commissions nullable: false
    }

    static mapping = {
        hashtag index:true, indexAttributes: [unique:true]
    }


    String toString(){
        "${hashtag} (${id})"
    }
}
