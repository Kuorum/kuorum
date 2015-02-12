package kuorum.users

import grails.plugin.springsecurity.SpringSecurityUtils
import kuorum.Institution
import kuorum.KuorumFile
import kuorum.core.FileGroup
import kuorum.core.FileType
import kuorum.core.exception.KuorumException
import kuorum.core.model.CommissionType
import kuorum.core.model.ProjectStatusType
import kuorum.core.model.search.Pagination
import kuorum.helper.IntegrationHelper
import kuorum.project.Project
import kuorum.project.ProjectController
import kuorum.web.commands.ProjectCommand
import kuorum.web.commands.ProjectUpdateCommand
import org.codehaus.groovy.grails.commons.GrailsApplication
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class ToolsControllerIntegrationSpec extends Specification{

    GrailsApplication grailsApplication

    @Shared
    ToolsController toolsController

    @Shared
    def renderMap

    @Shared
    def redirectMap

    def setup() {
        toolsController = new ToolsController()
        toolsController.metaClass.render = { Map map ->
            renderMap = map
        }
        toolsController.metaClass.redirect = { Map map ->
            redirectMap = map
        }
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
        toolsController.params.sort = "${sortAttr}"
        toolsController.params.order = 'desc'
        Pagination pagination = new Pagination(offset: 0, max: 10)

        when:"search the projects in a user session"
        SpringSecurityUtils.doWithAuth(user.email) {
            resultProjectsOrderedByMethod = (toolsController.ajaxShowProjectListOfUsers(pagination).model.projects).toArray().toList()
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

    void "order the projects by #sortAttr"() {
        given:"new projects. Inside is the user projectOwner@example.com"
        List <Project> listProjects= [], resultProjectsOrderedByMethod= []
        4.times{
            listProjects << IntegrationHelper.createDefaultProject("#hashtag${it}").save(flush:true)
        }
        KuorumUser user = listProjects.first()?.owner

        and:"params to order"
        toolsController.params.sort = "${sortAttr}"
        toolsController.params.order = 'desc'
        Pagination pagination = new Pagination(offset: 0, max: 10)

        when:"search the projects in a user session"
        SpringSecurityUtils.doWithAuth(user.email) {
            resultProjectsOrderedByMethod = (toolsController.ajaxShowProjectListOfUsers(pagination).model.projects).toArray().toList()
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
