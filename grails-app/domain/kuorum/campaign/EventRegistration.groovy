package kuorum.campaign

import kuorum.core.model.AvailableLanguage
import org.bson.types.ObjectId

class EventRegistration {

    ObjectId id
    Date dateCreated
    String alias
    String name
    String email
    AvailableLanguage language
    ObjectId userId
    Long debateId
    Long postId

    static constraints = {
    }

    static mapping = {
        debateId index:true
//        postId index:true
    }

}