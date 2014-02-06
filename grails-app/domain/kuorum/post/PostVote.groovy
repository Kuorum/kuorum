package kuorum.post

import kuorum.users.PersonalData
import kuorum.users.KuorumUser

/**
 * Storage the person vote of each post
 */
class PostVote {

    Post post
    KuorumUser user
    PersonalData personalData

    static embedded = ['personalData']

    static constraints = {
    }
}
