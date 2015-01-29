package kuorum.project

import kuorum.core.model.VoteType
import kuorum.users.KuorumUser
import kuorum.users.PersonalData
import org.bson.types.ObjectId

class ProjectVote {
    ObjectId id
    KuorumUser kuorumUser
    PersonalData personalData
    Project Project
    VoteType voteType
    Date dateCreated
    static embedded = ['personalData']

    static constraints = {
    }
}
