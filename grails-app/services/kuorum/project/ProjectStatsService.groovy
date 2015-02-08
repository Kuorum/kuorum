package kuorum.project

import grails.transaction.Transactional
import kuorum.Region
import kuorum.core.model.Gender
import kuorum.core.model.project.ProjectBasicStats
import kuorum.core.model.project.ProjectRegionStats
import kuorum.core.model.VoteType
import kuorum.post.Post

@Transactional
class ProjectStatsService {

    def grailsApplication

    ProjectBasicStats calculateProjectStats(Project project){
        //TODO: Cache this method
        ProjectBasicStats projectStats = new ProjectBasicStats()
        Post post = Post.findByProject(project,[max: 1, sort: "dateCreated", order: "desc", offset: 0])
        projectStats.lastActivity = post?.dateCreated

        projectStats.numUsers = Post.collection.count([project:project.id,published:true])
        projectStats.nomVotesToBePublic = grailsApplication.config.kuorum.milestones.postVotes.publicVotes
        projectStats.numPublicPosts = Post.collection.count([project:project.id,published:true, numVotes:['$gt':projectStats.nomVotesToBePublic]])
        if (project.peopleVotes?.total >0){
            projectStats.percentagePositiveVotes = project.peopleVotes.yes / project.peopleVotes.total
            projectStats.percentageNegativeVotes = project.peopleVotes.no / project.peopleVotes.total
            projectStats.percentageAbsVotes  = project.peopleVotes.abs / project.peopleVotes.total
        }else{
            projectStats.percentagePositiveVotes = 0
            projectStats.percentageNegativeVotes = 0
            projectStats.percentageAbsVotes  = 0
        }
        projectStats
    }

    ProjectRegionStats calculateRegionStats(Project project){
        calculateRegionStats(project, project.region)
    }

    ProjectRegionStats calculateRegionStats(Project project, Region region){
        //TODO: Cache this method
        ProjectRegionStats stats = new ProjectRegionStats(region: region, genderVotes: [:], totalVotes: new AcumulativeVotes())
        Gender.values().each {gender ->
            AcumulativeVotes votes = calculateRegionStatsByGender(project, region, gender)
            stats.genderVotes.put(gender, votes)
        }
        stats.totalVotes = stats.genderVotes.inject(new AcumulativeVotes()) {total,values ->
            total.yes += values.value.yes
            total.no += values.value.no
            total.abs += values.value.abs
            total
        }
        AcumulativeVotes noRegionVotes = new AcumulativeVotes()
        noRegionVotes.yes = project.peopleVotes.yes - stats.totalVotes.yes
        noRegionVotes.no = project.peopleVotes.no - stats.totalVotes.no
        noRegionVotes.abs = project.peopleVotes.abs - stats.totalVotes.abs
        noRegionVotes.numPosts = project.peopleVotes.numPosts - stats.totalVotes.numPosts
        stats.noRegionVotes = noRegionVotes
        stats
    }

    AcumulativeVotes calculateRegionStatsByGender(Project project, Region region, Gender gender){
        //TODO: Cache this method
        String regexPrvince = "^${region.iso3166_2}.*"
        def votes = ProjectVote.collection.aggregate(
                [$match : [project:project.id, 'personalData.gender': gender.toString(),'personalData.provinceCode': [$regex:regexPrvince]]],
                [$group :[_id:'$voteType',quantity:[$sum:1]]]
        )

        HashMap<VoteType, Long> mapResults = [:]
        votes.results().each{
            mapResults.put(VoteType.valueOf(it._id),it.quantity)
        }

        AcumulativeVotes acumulativeVotes = new AcumulativeVotes(
                abs : mapResults[VoteType.ABSTENTION]?:0,
                yes : mapResults[VoteType.POSITIVE]?:0,
                no : mapResults[VoteType.NEGATIVE]?:0
        )

        def posts = Post.collection.count([project:project.id, 'ownerPersonalData.gender': gender.toString(),'ownerPersonalData.provinceCode': [$regex:regexPrvince]])
        acumulativeVotes.numPosts = posts
        acumulativeVotes
    }

    HashMap<String, AcumulativeVotes> calculateProjectStatsPerSubRegions(Project project, Region region){
        //TODO: Cache this method
        def lengthCode = "${region.iso3166_2}-XX".length()
        String regexPrvince = "^${region.iso3166_2}-.*"
        def votes = ProjectVote.collection.aggregate(
                [$match : [project:project.id, 'personalData.provinceCode': [$regex:regexPrvince]]],
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
