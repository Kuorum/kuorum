package kuorum.law

import grails.transaction.Transactional
import kuorum.Region
import kuorum.core.model.Gender
import kuorum.core.model.Law.LawBasicStats
import kuorum.core.model.Law.LawRegionStats
import kuorum.core.model.VoteType
import kuorum.post.Post

@Transactional
class LawStatsService {

    LawBasicStats calculateLawStats(Law law){
        //TODO: Cache this method
        LawBasicStats lawStats = new LawBasicStats()
        Post post = Post.findByLaw(law,[max: 1, sort: "dateCreated", order: "desc", offset: 0])
        lawStats.lastActivity = post?.dateCreated

        lawStats.numPosts = Post.collection.count([law:law.id,published:true])
        lawStats.numPostsWithManyVotes = Post.collection.count([law:law.id,published:true, numVotes:['$gt':10]])

        lawStats
    }

    LawRegionStats calculateRegionStats(Law law, Region region){
        //TODO: Cache this method
        LawRegionStats stats = new LawRegionStats(region: region, genderVotes: [:], totalVotes: new AcumulativeVotes())
        Gender.values().each {gender ->
            AcumulativeVotes votes = calculateRegionStatsByGender(law, region, gender)
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

    AcumulativeVotes calculateRegionStatsByGender(Law law, Region region, Gender gender){
        //TODO: Cache this method
        String regexPrvince = "^${region.iso3166_2}.*"
        def votes = LawVote.collection.aggregate(
                [$match : [law:law.id, 'personalData.gender': gender.toString(),'personalData.provinceCode': [$regex:regexPrvince]]],
                [$group :[_id:'$voteType',quantity:[$sum:1]]]
        )

        HashMap<VoteType, Long> mapResults = [:]
        votes.results().each{
            mapResults.put(VoteType.valueOf(it._id),it.quantity)
        }

        new AcumulativeVotes(
                abs : mapResults[VoteType.ABSTENTION]?:0,
                yes : mapResults[VoteType.POSITIVE]?:0,
                no : mapResults[VoteType.NEGATIVE]?:0
        )
    }

    HashMap<String, AcumulativeVotes> calculateLawStatsPerSubRegions(Law law, Region region){
        //TODO: Cache this method
        def lengthCode = "${region.iso3166_2}-XX".length()
        String regexPrvince = "^${region.iso3166_2}-.*"
        def votes = LawVote.collection.aggregate(
                [$match : [law:law.id, 'personalData.provinceCode': [$regex:regexPrvince]]],
                [$project:[voteType:'$voteType', provinceCode:['$substr' : ['$personalData.provinceCode',0, lengthCode]]]],
                [$group :[_id:[voteType:'$voteType', region:'$provinceCode'],quantity:[$sum:1]]]
        )
        HashMap<String, AcumulativeVotes> res = [:]
        Region.findAllBySuperRegion(region).each{
            res.put(it.iso3166_2, new AcumulativeVotes())
        }
        votes.results().each{
            if (it._id.voteType == VoteType.ABSTENTION.toString()){
                res[it._id.region].abs = it.quantity
            }else if (it._id.voteType == VoteType.POSITIVE.toString()){
                res[it._id.region].yes = it.quantity
            }else if (it._id.voteType == VoteType.NEGATIVE.toString()){
                res[it._id.region].no = it.quantity
            }
        }
        res
    }
}
