package kuorum.post

import kuorum.users.PersonalData
import kuorum.users.KuorumUser
import org.bson.types.ObjectId

/**
 * Storage the person vote of each post
 */
class PostVote {
    ObjectId id
    Post post
    KuorumUser user
    PersonalData personalData

    static embedded = ['personalData']

    static constraints = {
    }
}
