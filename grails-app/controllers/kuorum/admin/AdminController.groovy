package kuorum.admin

import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.annotation.Secured
import kuorum.project.Project
import kuorum.users.KuorumUser

@Secured(['ROLE_ADMIN'])
class AdminController {

    def indexSolrService
    def springSecurityService

//    def afterInterceptor = [action: this.&prepareMenuData]
//    protected prepareMenuData = {model, modelAndView ->
    def afterInterceptor = { model, modelAndView ->
        def menu = []
        if (SpringSecurityUtils.ifAnyGranted('ROLE_ADMIN')){
            menu = [
                    unpublishedProjects: Project.countByPublished(false)
            ]
        }else if (SpringSecurityUtils.ifAnyGranted('ROLE_POLITICIAN')){
            KuorumUser user = springSecurityService.currentUser
            menu = [
                    unpublishedProjects: Project.findAllByPublishedAndRegion(false, user.personalData.province).size()
            ]
        }
        model.menu = menu
    }

    def index() {
        log.info("Index admin")
        render view: '/admin/index', model:[menu:[unpublishedProjects: Project.countByPublished(false)]]
    }

    def solrIndex(){
        [res:[:]]
    }

    def fullIndex(){
        def res = indexSolrService.fullIndex()
        render view: '/admin/solrIndex', model:[res:res]
    }
}
