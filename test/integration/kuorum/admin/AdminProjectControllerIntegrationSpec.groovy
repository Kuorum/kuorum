package kuorum.admin

import grails.plugin.springsecurity.SpringSecurityUtils
import kuorum.Institution
import kuorum.KuorumFile
import kuorum.core.FileGroup
import kuorum.core.FileType
import kuorum.core.exception.KuorumException
import kuorum.core.model.CommissionType
import kuorum.core.model.ProjectStatusType
import kuorum.project.Project
import kuorum.users.KuorumUser
import kuorum.web.commands.ProjectCommand
import org.codehaus.groovy.grails.commons.GrailsApplication
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Specification

class AdminProjectControllerIntegrationSpec extends Specification{

    GrailsApplication grailsApplication

    @Shared
    AdminProjectController adminProjectController

    @Shared
    def redirectMap, renderMap

    void setupSpec(){
        adminProjectController = new AdminProjectController()
        adminProjectController.metaClass.redirect = { Map map ->
            redirectMap = map
        }

        adminProjectController.metaClass.render = { Map map ->
            renderMap = map
        }
    }

    @Ignore
    void "create new project" () {
        given: "A user"
        KuorumUser user = KuorumUser.findByEmail("politician@example.com")

        when: "Create the project"
        Map response
        SpringSecurityUtils.doWithAuth(user.email) {
            response = adminProjectController.createProject()
        }

        then: "Status 200, the status of the command object is OPEN and the region of the command is the same as user"
        adminProjectController.response.status == 200
        response.command
        response.command.status == ProjectStatusType.OPEN
        response.command.region == user.politicianOnRegion
    }

    @Ignore
    void "save a new project with a different region from the user" () {
        given: "A user"
        KuorumUser user = KuorumUser.findByEmail("politician@example.com")

        and: "A command object of project. The institution is changed and is different from the user"
        ProjectCommand projectCommand = createCommand(user)
        projectCommand.institution = Institution.findByName("Parlamento europeo")

        expect: "Throws exception"
        SpringSecurityUtils.doWithAuth(user.email) {
            shouldFail(KuorumException) {
                adminProjectController.saveProject(projectCommand)


            }
        }
    }

    @Ignore
    void "save a new project" () {
        given: "A user"
        KuorumUser user = KuorumUser.findByEmail("politician@example.com")

        and: "A command object of project. The institution is changed and is different from the user"
        ProjectCommand projectCommand = createCommand(user)

        when: "Save the project"
        def result
        SpringSecurityUtils.doWithAuth(user.email) {
            result = adminProjectController.saveProject(projectCommand)
        }

        then: "Redirects to projectShow, hashtag is 'test', institutionName is 'parlamento-madrileno' and commission is 'justicia'"
        redirectMap
        redirectMap.mapping == 'projectShow'
        redirectMap.params
        redirectMap.params.hashtag == 'test'
        redirectMap.params.institutionName == 'parlamento-madrileno'
        redirectMap.params.commission == 'justicia'

        cleanup:
        Project.findByHashtag('#test').delete(flush: true)
    }

    private createCommand(KuorumUser kuorumUser){
        ProjectCommand projectCommand = new ProjectCommand()
        projectCommand.region = kuorumUser.politicianOnRegion
        projectCommand.hashtag = '#test'
        projectCommand.shortName = 'test'
        projectCommand.description = 'test'
        projectCommand.commissions = [CommissionType.JUSTICE]
        projectCommand.deadline = new Date() + 10
        projectCommand.institution = Institution.findByName("Parlamento Madrileño")
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
            fileGroup: FileGroup.PDF ,
            originalName: 'test'
        ).save()

        projectCommand.photoId = image.id
        projectCommand.pdfFileId = pdfFile.id

        projectCommand
    }
}
