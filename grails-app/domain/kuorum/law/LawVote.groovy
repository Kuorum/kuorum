package kuorum.law

import kuorum.core.model.VoteType
import kuorum.users.KuorumUser
import kuorum.users.PersonalData

class LawVote {
    KuorumUser kuorumUser
    PersonalData personalData
    Law law
    VoteType voteType
    Date dateCreated
    static embedded = ['personalData']

    static constraints = {
    }
}
