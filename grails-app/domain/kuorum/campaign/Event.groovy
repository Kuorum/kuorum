package kuorum.campaign

import org.bson.types.ObjectId

@Deprecated
class Event {

    ObjectId id
    Long debateId
    Long postId
    Date dateTime
    Float latitude
    Float longitude
    Integer zoom
    String localName
    String address

    static constraints = {
    }

    static mapping = {
        debateId index:true, indexAttributes: [unique:true]
//        postId index:true, indexAttributes: [unique:true]
    }

}