package kuorum.project

import kuorum.core.model.VoteType
import kuorum.users.KuorumUser
import kuorum.users.PersonalData
import org.bson.types.ObjectId

@Deprecated
class ProjectVote {
    ObjectId id
    KuorumUser kuorumUser
    PersonalData personalData
    Project project
    VoteType voteType
    Date dateCreated
    static embedded = ['personalData']

    static constraints = {
    }
}
