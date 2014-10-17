package kuorum

import org.bson.types.ObjectId

class PoliticalParty {

    ObjectId id
    String name


    static constraints = {
        name blank: false
    }

    String toString(){
        "$name"
    }

    boolean equals(Object object){
        id.equals(object?.id)
    }
}
