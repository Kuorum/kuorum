package kuorum.users

import grails.transaction.Transactional
import kuorum.core.model.Gender
import kuorum.core.model.UserType
import kuorum.core.model.project.ProjectStats
import kuorum.project.AcumulativeVotes
import kuorum.util.rest.RestKuorumApiService

@Transactional(readOnly = true)
class KuorumUserStatsService {

    RestKuorumApiService restKuorumApiService

    ProjectStats calculateStats() {
        // TODO: Cache this method
        ProjectStats stats = new ProjectStats(genderVotes: [:], totalVotes: new AcumulativeVotes())
        Gender.values().each {gender ->
            AcumulativeVotes votes = calculateStatsByGender(gender)
            stats.genderVotes.put(gender, votes)
        }
        stats.totalVotes = stats.genderVotes.inject(new AcumulativeVotes()) {total,values ->
            total.yes += values.value.yes
            total.no += values.value.no
            total.abs += values.value.abs
            total
        }
        stats
    }

    AcumulativeVotes calculateStatsByGender(Gender gender) {
        // TODO: Cache this method
        def activeUsers = KuorumUser.collection.count([enabled:true, accountLocked:false, 'personalData.gender':gender.toString(), userType:[$ne:UserType.POLITICIAN.toString()]])
//        def notActiveUsers = KuorumUser.collection.count([enabled:true, accountLocked:true, 'personalData.provinceCode':[$regex:regexPrvince]])
//        def notActiveUsers = calculateNotConfirmedUsers()
        def deletedUsers = KuorumUser.collection.count([enabled:false, accountLocked:false, 'personalData.gender':gender.toString(), userType:[$ne:UserType.POLITICIAN.toString()]])
        new AcumulativeVotes(
                abs : 0,
                yes : activeUsers,
                no : deletedUsers
        )
    }

    Long calculateNotConfirmedUsers() {
        KuorumUser.collection.count([enabled:true, accountLocked:true, userType:[$ne:UserType.POLITICIAN.toString()]]) // No tienen provincia fijada
    }
}
