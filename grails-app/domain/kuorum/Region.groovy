package kuorum

import org.bson.types.ObjectId

class Region {

    ObjectId id
    String name
    Region superRegion
    String iso3166_2
    List<String> postalCodes

    static constraints = {
        iso3166_2 blank: false,
                  unique: true,
                  matches: '([A-Z]{2}-)*[A-Z]{2}', // ES-PV España - Pais Vasco
                  validator: { val, obj ->
                      if (obj.superRegion && !val.startsWith("${obj.superRegion.iso3166_2}-")) {
                          return ['incorrectMaterializedPath']
                      }
                  }
        superRegion nullable:true
        postalCodes nullable:true
    }

    String toString(){
        name
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof Region)) return false

        Region region = (Region) o

        if (iso3166_2 != region.iso3166_2) return false

        return true
    }

    int hashCode() {
        return iso3166_2.hashCode()
    }
}
