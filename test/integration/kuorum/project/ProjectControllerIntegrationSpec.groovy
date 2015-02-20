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
    def redirectMap, renderMap

    void setupSpec(){
        projectController = new ProjectController()
        projectController.metaClass.redirect = { Map map ->
            redirectMap = map
        }

        projectController.metaClass.render = { Map map ->
            renderMap = map
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
        project?.updates?.remove(project?.updates?.last())
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
            projectController.addProjectUpdate(projectUpdateCommand, user)
        }

        then: "The project update has not been added"
        renderMap.view == "/project/createProjectUpdate"
        renderMap.model.errors['description'] == message

        where:
        description || message
        'a' * 501   || ''
        null        || ''
    }


    void " Try to show a project without being authenticated" () {
        given: "a project with published to false"
        Project project = IntegrationHelper.createDefaultProject("#PruebaSalenda")
        project.published = false
        project.save(flush:true)
        when: "Add an update to the project"
        projectController.show(project.hashtag)

        then: "The project update has not been added"
        projectController
        projectController.response
        projectController.response.status
        projectController.response.status == 401

        cleanup:
        project?.delete(flush: true)
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
}
