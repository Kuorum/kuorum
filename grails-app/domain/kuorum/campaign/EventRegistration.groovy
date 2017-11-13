package kuorum.campaign

import org.bson.types.ObjectId

class EventRegistration {

    ObjectId id
    Date dateCreated
    String alias
    String name
    ObjectId userId
    Long debateId
    Long postId

    static constraints = {
    }

    static mapping = {
        debateId index:true
        postId index:true
    }

}