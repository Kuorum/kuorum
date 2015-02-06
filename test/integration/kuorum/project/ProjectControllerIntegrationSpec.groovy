package kuorum.project

import grails.plugin.springsecurity.SpringSecurityUtils
import kuorum.Institution
import kuorum.KuorumFile
import kuorum.core.FileGroup
import kuorum.core.FileType
import kuorum.core.exception.KuorumException
import kuorum.core.model.CommissionType
import kuorum.core.model.ProjectStatusType
import kuorum.helper.IntegrationHelper
import kuorum.users.KuorumUser
import kuorum.web.commands.ProjectCommand
import kuorum.web.commands.ProjectUpdateCommand
import org.codehaus.groovy.grails.commons.GrailsApplication
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class ProjectControllerIntegrationSpec extends Specification{

    GrailsApplication grailsApplication

    @Shared
    ProjectController projectController

    @Shared
    def renderMap

    @Shared
    def redirectMap

    def setup() {
        projectController = new ProjectController()
        ProjectController.metaClass.render = { Map map ->
            renderMap = map
        }
        ProjectController.metaClass.redirect = { Map map ->
            redirectMap = map
        }
    }

    void "create new project" () {
        given: "A user"
        KuorumUser user = KuorumUser.findByEmail("politician@example.com")

        when: "Create the project"
        Map response
        SpringSecurityUtils.doWithAuth(user.email) {
            response = projectController.create()
        }

        then: "Status 200, the status of the command object is OPEN and the region of the command is the same as user"
        projectController.response.status == 200
        response.command
        response.command.status == ProjectStatusType.OPEN
        response.command.region == user.politicianOnRegion
    }

    void "save a new project" () {
        given: "A user"
        KuorumUser user = KuorumUser.findByEmail("politician@example.com")

        and: "A command object of project"
        ProjectCommand projectCommand = createProjectCommand(user)

        when: "Save the project"
        def result
        SpringSecurityUtils.doWithAuth(user.email) {
            result = projectController.save(projectCommand)
        }

        then: "Redirects to projectShow, hashtag is 'test', institutionName is 'parlamento-espanol' and commission is 'justicia'"
        redirectMap
        redirectMap.mapping == 'projectShow'
        redirectMap.params
        redirectMap.params.hashtag == 'test'
        redirectMap.params.institutionName == 'parlamento-espanol'
        redirectMap.params.commission == 'justicia'

        cleanup:
        Project.findByHashtag('#test').delete(flush: true)
    }

    @Ignore("Checking the exception doesn't work.")
    void "save a new project with a different region from the user" () {
        given: "A user"
        KuorumUser user = KuorumUser.findByEmail("politician@example.com")

        and: "A command object of project. The institution is changed and is different from the user"
        ProjectCommand projectCommand = createProjectCommand(user)
        projectCommand.institution = Institution.findByName("Parlamento europeo")

        expect: "Throws exception"
        SpringSecurityUtils.doWithAuth(user.email) {
            shouldFail(KuorumException) {
                projectController.save(projectCommand)
            }
        }
    }

    void "create new projectUpdate" () {
        given: "A user and a project"
        KuorumUser user = KuorumUser.findByEmail("politician@example.com")
        Project project = Project.findByHashtag("#leyAborto")

        when: "Create the projectUpdate"
        Map response
        SpringSecurityUtils.doWithAuth(user.email) {
            response = projectController.createProjectUpdate(project.hashtag)
        }

        then: "Status 200, the status of the command object is OPEN and the region of the command is the same as user"
        projectController.response.status == 200
        response.projectUpdateCommand
    }

    void "Add a update to a project" () {
        given: "A user, a project and a projectUpdateCommand"
        KuorumUser user = KuorumUser.findByEmail("politician@example.com")
        Project project = Project.findByHashtag("#leyAborto")
        ProjectUpdateCommand projectUpdateCommand = new ProjectUpdateCommand(description: 'prueba')
        projectController.params.hashtag = project.hashtag

        when: "Add an update to the project"
        SpringSecurityUtils.doWithAuth(user.email) {
            projectController.addProjectUpdate(projectUpdateCommand)
        }

        then: "The project update has been added"
        redirectMap
        redirectMap.mapping == 'projectShow'
        redirectMap.params.hashtag == 'leyAborto'

        cleanup:
        project.updates.remove(project.updates.last())
    }

    @Ignore('Complete the test when the result message for errors is completed')
    @Unroll
    void "Add a update, with errors, to a project" () {
        given: "A user, a project and a projectUpdateCommand"
        KuorumUser user = KuorumUser.findByEmail("politician@example.com")
        Project project = Project.findByHashtag("#leyAborto")
        ProjectUpdateCommand projectUpdateCommand = new ProjectUpdateCommand(description: description)
        projectController.params.hashtag = project.hashtag

        when: "Add an update to the project"
        SpringSecurityUtils.doWithAuth(user.email) {
            projectController.addProjectUpdate(projectUpdateCommand)
        }

        then: "The project update has not been added"
        renderMap.view == "/project/createProjectUpdate"
        renderMap.model.errors['description'] == message

        where:
        description || message
        'a' * 501   || ''
        null        || ''
    }

    @Unroll
    void "order the projects by #sortAttr if the param published doesn't exist, so we will have all the projects in desc order"() {
        given:"new projects. Inside is the user projectOwner@example.com"
        List <Project> listProjects= [], resultProjectsOrderedByMethod= []
        4.times{
            listProjects << IntegrationHelper.createDefaultProject("#hashtag${it}").save(flush:true)
        }
        KuorumUser user = listProjects.first()?.owner

        and:"params to order"
        projectController.params.sort = "${sortAttr}"
        projectController.params.order = 'desc'
        projectController.params.offset = '0'
        projectController.params.max = '10'

        when:"search the projects in a user session"
        SpringSecurityUtils.doWithAuth(user.email) {
            resultProjectsOrderedByMethod = (projectController.ajaxShowProjectListOfUsers().model.projects).toArray().toList()
        }

        and:"order the projects by sort"
        List <Project> projectsOrderedBySort = listProjects.sort{it."$sortAttr"}.reverse()

        then:"we compare the result of ordering the issues by groovy method and by our created method giving it the params to the ajaxShowProjectListOfUsers method"
        projectsOrderedBySort == resultProjectsOrderedByMethod
        renderMap
        renderMap.template
        renderMap.template == 'projects'
        renderMap.model
        renderMap.model.projects
        renderMap.model.projects.toArray().toList() == resultProjectsOrderedByMethod.toArray().toList()

        cleanup:
        listProjects*.delete(flush:true)
        KuorumUser.findByEmail(user?.email)?.delete(flush:true)

        where: "we give the value key to the sortAttr"
        sortAttr  << ["dateCreated"/*, "peopleVotes"*/]
    }

    private createProjectCommand(KuorumUser kuorumUser){
        ProjectCommand projectCommand = new ProjectCommand()
        projectCommand.region = kuorumUser.politicianOnRegion
        projectCommand.hashtag = '#test'
        projectCommand.shortName = 'test'
        projectCommand.description = 'test'
        projectCommand.commissions = [CommissionType.JUSTICE]
        projectCommand.deadline = new Date() + 10
        projectCommand.institution = Institution.findByName("Parlamento Español (España)")
        projectCommand.owner = kuorumUser

        KuorumFile image = new KuorumFile(
                user: kuorumUser,
                local: false,
                temporal: false,
                fileName: "${grailsApplication.config.grails.serverURL}/static/images/img-post.jpg",
                url: "${grailsApplication.config.grails.serverURL}/static/images/img-post.jpg",
                fileGroup: FileGroup.PROJECT_IMAGE,
                fileType: FileType.IMAGE,
                originalName: 'test'
        ).save()

        KuorumFile pdfFile = new KuorumFile(
                user: kuorumUser,
                local: false,
                temporal: false,
                fileName: "test.pdf",
                url: "http://kuorum.org",
                fileGroup: FileGroup.PDF,
                originalName: 'test'
        ).save()

        projectCommand.photoId = image.id
        projectCommand.pdfFileId = pdfFile.id

        projectCommand
    }


    void "order the projects by #sortAttr"() {
        given:"new projects. Inside is the user projectOwner@example.com"
        List <Project> listProjects= [], resultProjectsOrderedByMethod= []
        4.times{
            listProjects << IntegrationHelper.createDefaultProject("#hashtag${it}").save(flush:true)
        }
        KuorumUser user = listProjects.first()?.owner

        and:"params to order"
        projectController.params.sort = "${sortAttr}"
        projectController.params.order = 'desc'
        projectController.params.offset = '0'
        projectController.params.max = '10'

        when:"search the projects in a user session"
        SpringSecurityUtils.doWithAuth(user.email) {
            resultProjectsOrderedByMethod = (projectController.ajaxShowProjectListOfUsers().model.projects).toArray().toList()
        }

        and:"order the projects by sort"
        List <Project> projectsOrderedBySort = listProjects.sort{it."$sortAttr"}.reverse()

        then:"we compare the result of ordering the issues by groovy method and by our created method giving it the params to the ajaxShowProjectListOfUsers method"
        projectsOrderedBySort == resultProjectsOrderedByMethod
        renderMap
        renderMap.template
        renderMap.template == 'projects'
        renderMap.model
        renderMap.model.projects.toArray().toList() == resultProjectsOrderedByMethod.toArray().toList()

        cleanup:
        listProjects*.delete(flush:true)
        KuorumUser.findByEmail(user?.email)?.delete(flush:true)

        where: "we give the value key to the sortAttr"
        sortAttr  << ["dateCreated"/*, "peopleVotes"*/]
    }
}
