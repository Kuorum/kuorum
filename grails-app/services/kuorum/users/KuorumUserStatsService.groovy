package kuorum.users

import grails.transaction.Transactional
import kuorum.Region
import kuorum.core.model.Gender
import kuorum.core.model.Law.LawRegionStats
import kuorum.core.model.UserType
import kuorum.law.AcumulativeVotes

@Transactional
class KuorumUserStatsService {


    LawRegionStats calculateStats(Region region){
        //TODO: Cache this method
        LawRegionStats stats = new LawRegionStats(region: region, genderVotes: [:], totalVotes: new AcumulativeVotes())
        Gender.values().each {gender ->
            AcumulativeVotes votes = calculateRegionStatsByGender(region, gender)
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

    AcumulativeVotes calculateRegionStatsByGender(Region region, Gender gender){
        //TODO: Cache this method
        String regexPrvince = "^${region.iso3166_2}.*"
        def activeUsers = KuorumUser.collection.count([enabled:true, accountLocked:false, 'personalData.provinceCode':[$regex:regexPrvince], 'personalData.gender':gender.toString(), userType:[$ne:UserType.POLITICIAN.toString()]])
//        def notActiveUsers = KuorumUser.collection.count([enabled:true, accountLocked:true, 'personalData.provinceCode':[$regex:regexPrvince]])
//        def notActiveUsers = calculateNotConfirmedUsers()
        def deletedUsers = KuorumUser.collection.count([enabled:false, accountLocked:false, 'personalData.provinceCode':[$regex:regexPrvince], 'personalData.gender':gender.toString(), userType:[$ne:UserType.POLITICIAN.toString()]])
        new AcumulativeVotes(
                abs : 0,
                yes : activeUsers,
                no : deletedUsers
        )
    }

    Long calculateNotConfirmedUsers(){
        KuorumUser.collection.count([enabled:true, accountLocked:true, userType:[$ne:UserType.POLITICIAN.toString()]]) // No tienen provincia fijada
    }

    HashMap<String, AcumulativeVotes> calculateStatsPerSubRegions(Region region){
        //TODO: Cache this method
        def lengthCode = "${region.iso3166_2}-XX".length()
        String regexPrvince = "^${region.iso3166_2}-.*"
        def votes = KuorumUser.collection.aggregate(
                [$match : ['personalData.provinceCode': [$regex:regexPrvince],userType:[$ne:UserType.POLITICIAN.toString()]]],
                [$project:[gender:'$personalData.gender', provinceCode:['$substr' : ['$personalData.provinceCode',0, lengthCode]]]],
                [$group :[_id:[gender:'$gender', region:'$provinceCode'],quantity:[$sum:1]]]
        )
        HashMap<String, AcumulativeVotes> res = [:]
        Region.findAllBySuperRegion(region).each{
            res.put(it.iso3166_2, new AcumulativeVotes())
        }
        votes.results().each{
            if (it._id.gender == Gender.ORGANIZATION.toString()){
                res[it._id.region].abs = it.quantity
            }else if (it._id.gender == Gender.MALE.toString()){
                res[it._id.region].yes = it.quantity
            }else if (it._id.gender == Gender.FEMALE.toString()){
                res[it._id.region].no = it.quantity
            }
        }
        res
    }
}
