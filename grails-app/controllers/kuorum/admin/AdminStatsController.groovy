package kuorum.admin

import grails.converters.JSON
import kuorum.Region
import kuorum.core.model.Law.LawRegionStats
import kuorum.core.model.LawStatusType
import kuorum.core.model.UserType
import kuorum.law.AcumulativeVotes
import kuorum.law.Law
import kuorum.post.Post
import kuorum.users.KuorumUser

class AdminStatsController {

    def kuorumUserStatsService

    def stats(String hashtag){

        Region spain = Region.findByIso3166_2("EU-ES")
        LawRegionStats stats = kuorumUserStatsService.calculateStats(spain)
        def totalStats = [
            totalUsers:KuorumUser.count(),
            activeUsers:KuorumUser.countByAccountLockedAndEnabledAndUserTypeNotEqual(false, true, UserType.POLITICIAN),
            notConfirmedUsers:KuorumUser.countByAccountLocked(true),
            deleteUsers :KuorumUser.countByAccountLockedAndEnabledAndUserTypeNotEqual(false, false, UserType.POLITICIAN),
            activePoliticians:KuorumUser.countByAccountLockedAndEnabledAndUserType(false, true, UserType.POLITICIAN),
            inactivePoliticians:KuorumUser.countByAccountLockedAndEnabledAndUserType(false, true, UserType.POLITICIAN),
            totalLaws: Law.count(),
            openLaws: Law.countByStatus(LawStatusType.OPEN),
            closeLaws: Law.countByStatusNotEqual(LawStatusType.OPEN),
            numPosts:Post.count(),
            numVictories:Post.countByVictory(true)
        ]
        [stats:stats, region:spain, totalStats:totalStats]
    }

    def statsDataMap(String hashtag){
        Region spain = Region.findByIso3166_2("EU-ES")
        HashMap<String, AcumulativeVotes> statsPerProvince = kuorumUserStatsService.calculateStatsPerSubRegions(spain)
        def map = [:]
        statsPerProvince.each {k,v ->
//            def max = [v.yes,v.no, v.abs].max()
//            if (v.yes == max)
//                map.put(k,1)
//            else if (v.no == max)
//                map.put(k,2)
//            else
//                map.put(k,3)
            map.put(k,3)
        }
        render ([votation:[results:map]] as JSON)
    }

    def statsDataPieChart(){
        Region region = Region.findByIso3166_2(params.regionIso3166)
        LawRegionStats stats = kuorumUserStatsService.calculateStats(region)
        render stats as JSON
    }
}
