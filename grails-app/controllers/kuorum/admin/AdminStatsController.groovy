package kuorum.admin

import grails.converters.JSON
import kuorum.Region
import kuorum.core.model.ProjectStatusType
import kuorum.core.model.UserType
import kuorum.core.model.project.ProjectStats
import kuorum.project.Project
import kuorum.post.Post
import kuorum.users.KuorumUser

class AdminStatsController {

    def kuorumUserStatsService

    def stats(String hashtag) {
        ProjectStats stats = kuorumUserStatsService.calculateStats()
        def totalStats = [
            totalUsers:KuorumUser.count(),
            activeUsers:KuorumUser.countByAccountLockedAndEnabledAndUserTypeNotEqual(false, true, UserType.POLITICIAN),
            notConfirmedUsers:KuorumUser.countByAccountLocked(true),
            deleteUsers :KuorumUser.countByAccountLockedAndEnabledAndUserTypeNotEqual(false, false, UserType.POLITICIAN),
            activePoliticians:KuorumUser.countByAccountLockedAndEnabledAndUserType(false, true, UserType.POLITICIAN),
            inactivePoliticians:KuorumUser.countByAccountLockedAndEnabledAndUserType(false, true, UserType.POLITICIAN),
            totalProjects: Project.count(),
            openProjects: Project.countByStatus(ProjectStatusType.OPEN),
            closeProjects: Project.countByStatusNotEqual(ProjectStatusType.OPEN),
            numPosts:Post.count(),
            numVictories:Post.countByVictory(true)
        ]
        [stats:stats, totalStats:totalStats]
    }

    def statsDataPieChart() {
        ProjectStats stats = kuorumUserStatsService.calculateStats()
        render stats as JSON
    }

}
