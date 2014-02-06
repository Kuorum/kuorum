package kuorum

import org.bson.types.ObjectId

/**
 * Represents the different places where the politician celebrate meetings
 * to make decisions -Parlament, Senate -
 *
 * Parlament of Catalonia, Parlament of Spain, Senate of spain
 *
 */
class Institution {

    ObjectId id
    String name
    Region region

    static embedded = [ 'region' ]

    static constraints = {
        name blank: false, nullable:false
    }

    String toString(){
        name
    }

    boolean equals(Object object){
        object && (
                id && id.equals(object?.id) ||
                name && name.equals(object.name) && region && region.equals(object.region)
        )
    }
}
