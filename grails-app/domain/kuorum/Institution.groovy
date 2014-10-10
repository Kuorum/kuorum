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
        "${name} (${region})"
    }

    boolean equals(Object object){
        object && (
                id && id.equals(object?.id) ||
                name && name.equals(object.name) && region && region.equals(object.region)
        )
    }

    //Overrides dynamic finder because not works properly with embedded object
    // -- Fast approach --: There will be few institution per region
    static def findAllByRegion(Region region) {
        def res = Institution.collection.find(['region._id':region.id],[_id:1])
        List<Region> regions = res.collect{Institution.get(it._id)}
        regions
    }
}
