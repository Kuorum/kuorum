package kuorum

import org.bson.types.ObjectId

class ParliamentaryGroup {

    ObjectId id
    String name
    Institution institution

    //Desnorm from institution: is for make easier searchs
    Region region

    static embedded = [ 'institution', 'region' ]

    static constraints = {
        name blank: false
        institution nullable:false
        region  validator: { val, obj ->
            if (val != obj.institution.region) {
                return ['notSameRegionAsInstitution']
            }
        }
    }

    String toString(){
        "$name (${region.name})"
    }

    boolean equals(Object object){
        id.equals(object?.id)
    }
}
