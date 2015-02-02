package kuorum.project

import grails.converters.JSON
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import kuorum.RegionService
import kuorum.core.model.ProjectStatusType
import kuorum.core.model.VoteType
import kuorum.helper.Helper
import kuorum.users.GamificationService
import kuorum.users.KuorumUser
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(ProjectService)
@Mock([Project, ProjectVote, KuorumUser])
class ProjectServiceSpec extends Specification {


    GamificationService gamificationServiceMock = Mock(GamificationService)
    RegionService regionService = Mock(RegionService)

    def setup() {
        service.gamificationService = gamificationServiceMock
        service.regionService = regionService

        Project.metaClass.static.getCollection = {->
            [findOne: {
                delegate.findWhere(it) as JSON

            },
                    update: { filter, updateData ->
                        Project project = Project.get(filter._id)
                        def yesInc = updateData.'$inc'.'peopleVotes.yes'
                        def noInc = updateData.'$inc'.'peopleVotes.no'
                        def absInc = updateData.'$inc'.'peopleVotes.abs'
                        if (yesInc) project.peopleVotes.yes++
                        if (noInc) project.peopleVotes.no++
                        if (absInc) project.peopleVotes.abs++
                        project.save()
                        //post as JSON
                    }
            ]
        }
        Project.metaClass.refresh = {->
            //REFRESH FAILS with null pointer
        }

        regionService.isRelevantRegionForUser(_, _) >> { user, region -> true }
        regionService.findUserRegion(_) >> { user ->
            return Helper.creteDefaultRegion();
        }
    }


    @Unroll
    void "test voteProject voting #votes"() {
        given: "A project and a user voting #votes"
        Project project = Helper.createDefaultProject("#project").save()
        KuorumUser user = Helper.createDefaultUser("email@email.com").save()
        when: "User votes the project"
        ProjectVote projectVote
        (0..numVotes - 1).each {
            projectVote = service.voteProject(project, user, votes[it])
        }
        then: "All ok"
        projectVote.voteType == votes.last()
        ProjectVote.count() == 1
        1 * gamificationServiceMock.projectVotedAward(user, project)
        switch (votes.last()) {
            case VoteType.ABSTENTION: project.peopleVotes.abs = 1; break;
            case VoteType.POSITIVE: project.peopleVotes.abs = 1; break;
            case VoteType.NEGATIVE: project.peopleVotes.abs = 1; break;
        }
        where:
        numVotes | votes
        1        | [VoteType.POSITIVE]
        2        | [VoteType.POSITIVE, VoteType.POSITIVE]
        2        | [VoteType.POSITIVE, VoteType.NEGATIVE]
    }

    @Unroll
    void "check status project for date: #date and final status: #status"() {
        given: "a project"
        Project project = Helper.createDefaultProject("#project").save()

        when: "check the status of the projects"
        service.checkProjectsStatus(date)

        then: "status has been changed or is the same"
        project.status == status

        where:
        date            || status
        new Date()      || ProjectStatusType.OPEN
        new Date() + 11 || ProjectStatusType.CLOSE
    }
}
