package kuorum.post

import kuorum.users.KuorumUser
import kuorum.users.PersonalData
import org.bson.types.ObjectId

/**
 * Storage the person vote of each post
 */
class PostVote {
    ObjectId id
    Post post
    KuorumUser user
    PersonalData personalData
    Date dateCreated
    Boolean anonymous = Boolean.FALSE

    static embedded = ['personalData']

    static constraints = {

    }

    static mapping = {
        compoundIndex post:1, user:-1
    }
}
