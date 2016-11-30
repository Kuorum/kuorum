package kuorum.project

import grails.transaction.Transactional
import kuorum.core.model.Gender
import kuorum.core.model.project.ProjectBasicStats
import kuorum.core.model.VoteType
import kuorum.core.model.project.ProjectStats
import kuorum.post.Post

@Transactional
class ProjectStatsService {

    def grailsApplication

    ProjectBasicStats calculateProjectBasicStats(Project project) {
        // TODO: Cache this method
        ProjectBasicStats projectStats = new ProjectBasicStats()
        Post post = Post.findByProject(project,[max: 1, sort: "dateCreated", order: "desc", offset: 0])
        projectStats.lastActivity = post?.dateCreated

        projectStats.numUsers = Post.collection.count([project:project.id,published:true])
        projectStats.nomVotesToBePublic = grailsApplication.config.kuorum.milestones.postVotes.publicVotes
        projectStats.numPublicPosts = Post.collection.count([project:project.id,published:true])
        if (project.peopleVotes?.total > 0) {
            projectStats.percentagePositiveVotes = project.peopleVotes.yes / project.peopleVotes.total
            projectStats.percentageNegativeVotes = project.peopleVotes.no / project.peopleVotes.total
            projectStats.percentageAbsVotes  = project.peopleVotes.abs / project.peopleVotes.total
        } else {
            projectStats.percentagePositiveVotes = 0
            projectStats.percentageNegativeVotes = 0
            projectStats.percentageAbsVotes  = 0
        }

        projectStats
    }

    ProjectStats calculateProjectStats(Project project) {
        // TODO: Cache this method
        ProjectStats stats = new ProjectStats(genderVotes: [:], totalVotes: new AcumulativeVotes())
        Gender.values().each {gender ->
            AcumulativeVotes votes = calculateStatsByGender(project, gender)
            stats.genderVotes.put(gender, votes)
        }
        stats.totalVotes = stats.genderVotes.inject(new AcumulativeVotes()) {total, values ->
            total.yes += values.value.yes
            total.no += values.value.no
            total.abs += values.value.abs
            total
        }
        stats
    }

    AcumulativeVotes calculateStatsByGender(Project project, Gender gender) {
        // TODO: Cache this method
        def votes = ProjectVote.collection.aggregate(
                [$match : [project: project.id, 'personalData.gender': gender.toString()]],
                [$group : [_id:'$voteType', quantity: [$sum:1]]]
        )

        HashMap<VoteType, Long> mapResults = [:]
        votes.results().each{
            mapResults.put(VoteType.valueOf(it._id), it.quantity)
        }

        AcumulativeVotes acumulativeVotes = new AcumulativeVotes(
                abs : mapResults[VoteType.ABSTENTION]?:0,
                yes : mapResults[VoteType.POSITIVE]?:0,
                no : mapResults[VoteType.NEGATIVE]?:0
        )

        def posts = Post.collection.count([project: project.id, 'ownerPersonalData.gender': gender.toString()])
        acumulativeVotes.numPosts = posts
        acumulativeVotes
    }

}
