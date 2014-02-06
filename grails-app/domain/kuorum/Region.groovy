package kuorum

import org.bson.types.ObjectId

class Region {

    ObjectId id
    String name
    Region superRegion
    String iso3166_2

    static constraints = {
        iso3166_2 blank: false,
                  matches: '([A-Z]{2}-)*[A-Z]{2}', // ES-PV España - Pais Vasco
                  validator: { val, obj ->
                      if (obj.superRegion && !val.startsWith("${obj.superRegion.iso3166_2}-")) {
                          return ['incorrectMaterializedPath']
                      }
                  }
        superRegion nullable:true
    }

    String toString(){
        name
    }

    boolean equals(Object object){
        iso3166_2.equals(object?.iso3166_2)
    }
}
