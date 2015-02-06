package kuorum.project

import grails.plugin.springsecurity.SpringSecurityUtils
import kuorum.Institution
import kuorum.PoliticalParty
import kuorum.Region
import kuorum.core.model.CommissionType
import kuorum.core.model.ProjectStatusType
import kuorum.core.model.project.ProjectUpdate
import kuorum.helper.IntegrationHelper
import kuorum.users.KuorumUser
import kuorum.util.Order
import spock.lang.Ignore
import spock.lang.IgnoreRest
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by iduetxe on 4/03/14.
 */
class ProjectServiceIntegrationSpec extends Specification {


    def projectService
    def fixtureLoader

    def setup() {
        Region.collection.getDB().dropDatabase()
        fixtureLoader.load("testBasicData")
    }

    void "test update a project"() {
        given: "A project"
        Project project = Project.findByHashtag("#leyAborto")
        when: "Updating the project"
        Region region = Region.findByIso3166_2("EU-ES")
        Institution institution = Institution.findAllByRegion(region).first()
        PoliticalParty politicalParty = PoliticalParty.list().find { it.institution == institution }
        String realName = "realName"
        String shortName = "shortName"
        def commissions = [CommissionType.AGRICULTURE, CommissionType.BUDGETS, CommissionType.CONSTITUTIONAL]

        project.shortName = shortName
        project.region = region
        project.institution = institution
        project.politicalParty = politicalParty
        project.realName = realName
        project.commissions = commissions
        Project projectSaved = projectService.updateProject(project)
        then: "Project is update properly"

        projectSaved.shortName == shortName
        projectSaved.region == region
        projectSaved.region.iso3166_2 == region.iso3166_2
        projectSaved.region.id == region.id
        projectSaved.realName == realName
        projectSaved.commissions == commissions
        projectSaved.politicalParty == politicalParty
        projectSaved.institution == institution
        projectSaved.institution.name == institution.name
        Project.withNewSession {
            Project projectRecovered = Project.findByHashtag("#leyAborto")
            projectRecovered.shortName == shortName
            projectRecovered.region == region
            projectRecovered.region.iso3166_2 == region.iso3166_2
            projectRecovered.region.id == region.id
            projectRecovered.realName == realName
            projectRecovered.commissions == commissions
            projectRecovered.institution == institution
            projectRecovered.institution.name == institution.name
//            projectRecovered.politicalParty.name == politicalParty.name
//            projectRecovered.politicalParty == politicalParty
        }
    }

    @Unroll
    void "check status project for date: #date and final status: #status"() {
        given: "a project"
        Project project = Project.findByHashtag("#leyAborto")

        when: "check the status of the projects"
        projectService.checkProjectsStatus(date)

        then: "status has been changed or is the same"
        project.status == status

        where:
        date            || status
        new Date()      || ProjectStatusType.OPEN
        new Date() + 11 || ProjectStatusType.CLOSE
    }


    void "Add an update to a project"() {
        given: "A project and projectUpdate"
        Project project = Project.findByHashtag("#leyAborto")
        ProjectUpdate projectUpdate = new ProjectUpdate(description: 'prueba')

        when: "Add a update to the project"
        Map result = projectService.addProjectUpdate(projectUpdate, project)

        then: "The result message is OK and it has a projectUpdate object"
        result
        result.message == 'OK'
        result.projectUpdate
        result.projectUpdate.description == 'prueba'

        cleanup:
        project.updates.remove(projectUpdate)
    }

    @Ignore('Complete the test when the result message for errors is completed')
    @Unroll
    void "Add a update, with errors, to a project"() {
        given: "A project and and projectUpdate"
        Project project = Project.findByHashtag("#leyAborto")
        ProjectUpdate projectUpdate = new ProjectUpdate(description: description)

        when: "Add a update to the project"
        Map result = projectService.addProjectUpdate(projectUpdate, project)

        then: "The result message is the error message"
        result
        result.message == message

        where:
        description || message
        'a' * 501   || ''
        null        || ''
    }

    @Unroll
    void "order the projects by sort:#sortAttr and order:#orderAttr. Published is #published, so we will have only the projects which have been published #published"() {
        given:"new projects. From position 0 to 2 will have published to true, because by default, it is false"
        List <Project> listProjects= []
        Map params = [sort:sortAttr, order:orderAttr, published:published, offset:offset, max:max]
        5.times{
            Project project = IntegrationHelper.createDefaultProject("#hashtag${it}")
            project.peopleVotes = new AcumulativeVotes(yes:2*it, no:1*it, abs: 3*it, numPosts: 1+5*it).save(flush:true)
            listProjects << project.save(flush: true)
        }
        listProjects[0..2]*.published = true
        listProjects*.save(flush: true)

        KuorumUser user = listProjects.first()?.owner

        when:"we call the search method without params"
        Map result = projectService.search(user, params.sort , params.order , params.published, params.offset, params.max)

        and:"order the projects by sort"
        List <Project> projectsOrderedBySort = listProjects.sort(sortInTest)
        if(published != null){
            projectsOrderedBySort = projectsOrderedBySort.findAll{it.published == published}
        }

        then:"we compare both ordered lists. Moreover, we will have only projects with published to false"
        result
        result.projects
        result.projects.size() ==  projectsOrderedBySort.size()
        result.projects == projectsOrderedBySort

        if(result.projects.size() > (offset + max)){
            result.projects == listProjects[offset..(offset + max)]
        }else if(result.projects.size() > offset){
            result.projects == listProjects[offset..-1]
        }else{
           !result.projects
        }

        cleanup:
        listProjects*.delete(flush:true)
        KuorumUser.findByEmail(user?.email)?.delete(flush:true)

        where: "we give the value key to the sortAttr"
        sortAttr        | orderAttr  | published | sortInTest                                  | offset | max
        'dateCreated'   | Order.ASC  | true      | { it.dateCreated }                          | 0      | 10
        'dateCreated'   | Order.ASC  | false     | { it.dateCreated }                          | 3      | 5
        'dateCreated'   | Order.ASC  | null      | { it.dateCreated }                          | 5      | 30
        'dateCreated'   | Order.DESC | true      | { a, b -> b.dateCreated <=> a.dateCreated } | 0      | 10
        'dateCreated'   | Order.DESC | false     | { a, b -> b.dateCreated <=> a.dateCreated } | 3      | 5
        'dateCreated'   | Order.DESC | null      | { a, b -> b.dateCreated <=> a.dateCreated } | 5      | 30
        'peopleVotes'   | Order.ASC  | true      | { it.peopleVotes.total }                    | 0      | 10
        'peopleVotes'   | Order.ASC  | false     | { it.peopleVotes.total }                    | 3      | 5
        'peopleVotes'   | Order.ASC  | null      | { it.peopleVotes.total }                    | 5      | 30
        'peopleVotes'   | Order.DESC | true      | { -it.peopleVotes.total }                   | 0      | 10
        'peopleVotes'   | Order.DESC | false     | { -it.peopleVotes.total }                   | 3      | 5
        'peopleVotes'   | Order.DESC | null      | { -it.peopleVotes.total }                   | 5      | 30
        'peopleVoteYes' | Order.ASC  | true      | {
            it.peopleVotes.total ? (it.peopleVotes.yes * 100) / it.peopleVotes.total : 0
        }                                                                                      | 0      | 10
        'peopleVoteYes' | Order.ASC  | false     | {
            it.peopleVotes.total ? (it.peopleVotes.yes * 100) / it.peopleVotes.total : 0
        }                                                                                      | 3      | 5
        'peopleVoteYes' | Order.ASC  | null      | {
            it.peopleVotes.total ? (it.peopleVotes.yes * 100) / it.peopleVotes.total : 0
        }                                                                                      | 5      | 30
        'peopleVoteYes' | Order.DESC | true      | {
            -(it.peopleVotes.total ? (it.peopleVotes.yes * 100) / it.peopleVotes.total : 0)
        }                                                                                      | 0      | 10
        'peopleVoteYes' | Order.DESC | false     | {
            -(it.peopleVotes.total ? (it.peopleVotes.yes * 100) / it.peopleVotes.total : 0)
        }                                                                                      | 3      | 5
        'peopleVoteYes' | Order.DESC | null      | {
            -(it.peopleVotes.total ? (it.peopleVotes.yes * 100) / it.peopleVotes.total : 0)
        }                                                                                      | 5      | 30
        'numPosts'      | Order.ASC  | true      | { it.peopleVotes.numPosts }                 | 0      | 10
        'numPosts'      | Order.ASC  | false     | { it.peopleVotes.numPosts }                 | 3      | 5
        'numPosts'      | Order.ASC  | null      | { it.peopleVotes.numPosts }                 | 5      | 30
        'numPosts'      | Order.DESC | true      | { -it.peopleVotes.numPosts }                | 0      | 10
        'numPosts'      | Order.DESC | false     | { -it.peopleVotes.numPosts }                | 3      | 5
        'numPosts'      | Order.DESC | null      | { -it.peopleVotes.numPosts }                | 5      | 30
    }

    @Unroll
    void "order the projects by sort:#sortAttr and order:#orderAttr. Published is #published, but there are not projects"() {
        given:"new projects. From position 0 to 2 will have published to true, because by default, it is false"
        List <Project> listProjects= []
        Map params = [sort:sortAttr, order:orderAttr, published:published, offset:offset, max:max]
        KuorumUser user = KuorumUser.findByEmail("projectowner@example.com") ?: IntegrationHelper.createDefaultUser("projectOwner@example.com").save(flush:true)


        when:"we call the search method without params"
        Map result = projectService.search(user, params.sort , params.order , params.published, params.offset, params.max)

        then:"we compare both ordered lists. Moreover, we will have only projects with published to false"
        result
        !result.projects

        cleanup:
        KuorumUser.findByEmail(user?.email)?.delete(flush:true)

        where: "we give the value key to the sortAttr"
        sortAttr        | orderAttr  | published | sortInTest                                  | offset | max
        'dateCreated'   | Order.ASC  | true      | { it.dateCreated }                          | 0      | 10
        'dateCreated'   | Order.ASC  | false     | { it.dateCreated }                          | 3      | 5
        'dateCreated'   | Order.ASC  | null      | { it.dateCreated }                          | 5      | 30
        'dateCreated'   | Order.DESC | true      | { a, b -> b.dateCreated <=> a.dateCreated } | 0      | 10
        'dateCreated'   | Order.DESC | false     | { a, b -> b.dateCreated <=> a.dateCreated } | 3      | 5
        'dateCreated'   | Order.DESC | null      | { a, b -> b.dateCreated <=> a.dateCreated } | 5      | 30
        'peopleVotes'   | Order.ASC  | true      | { it.peopleVotes.total }                    | 0      | 10
        'peopleVotes'   | Order.ASC  | false     | { it.peopleVotes.total }                    | 3      | 5
        'peopleVotes'   | Order.ASC  | null      | { it.peopleVotes.total }                    | 5      | 30
        'peopleVotes'   | Order.DESC | true      | { -it.peopleVotes.total }                   | 0      | 10
        'peopleVotes'   | Order.DESC | false     | { -it.peopleVotes.total }                   | 3      | 5
        'peopleVotes'   | Order.DESC | null      | { -it.peopleVotes.total }                   | 5      | 30
        'peopleVoteYes' | Order.ASC  | true      | {
            it.peopleVotes.total ? (it.peopleVotes.yes * 100) / it.peopleVotes.total : 0
        }                                                                                      | 0      | 10
        'peopleVoteYes' | Order.ASC  | false     | {
            it.peopleVotes.total ? (it.peopleVotes.yes * 100) / it.peopleVotes.total : 0
        }                                                                                      | 3      | 5
        'peopleVoteYes' | Order.ASC  | null      | {
            it.peopleVotes.total ? (it.peopleVotes.yes * 100) / it.peopleVotes.total : 0
        }                                                                                      | 5      | 30
        'peopleVoteYes' | Order.DESC | true      | {
            -(it.peopleVotes.total ? (it.peopleVotes.yes * 100) / it.peopleVotes.total : 0)
        }                                                                                      | 0      | 10
        'peopleVoteYes' | Order.DESC | false     | {
            -(it.peopleVotes.total ? (it.peopleVotes.yes * 100) / it.peopleVotes.total : 0)
        }                                                                                      | 3      | 5
        'peopleVoteYes' | Order.DESC | null      | {
            -(it.peopleVotes.total ? (it.peopleVotes.yes * 100) / it.peopleVotes.total : 0)
        }                                                                                      | 5      | 30
        'numPosts'      | Order.ASC  | true      | { it.peopleVotes.numPost }                  | 0      | 10
        'numPosts'      | Order.ASC  | false     | { it.peopleVotes.numPost }                  | 3      | 5
        'numPosts'      | Order.ASC  | null      | { it.peopleVotes.numPost }                  | 5      | 30
        'numPosts'      | Order.DESC | true      | { -it.peopleVotes.numPost }                 | 0      | 10
        'numPosts'      | Order.DESC | false     | { -it.peopleVotes.numPost }                 | 3      | 5
        'numPosts'      | Order.DESC | null      | { -it.peopleVotes.numPost }                 | 5      | 30
    }

}
