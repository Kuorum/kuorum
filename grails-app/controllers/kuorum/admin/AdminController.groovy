package kuorum.admin

import grails.plugin.springsecurity.annotation.Secured
import kuorum.law.Law
@Secured(['ROLE_ADMIN'])
class AdminController {

    def indexSolrService

//    def afterInterceptor = [action: this.&prepareMenuData]
//    protected prepareMenuData = {model, modelAndView ->
    def afterInterceptor = { model, modelAndView ->
        model.menu = [
                unpublishedLaws: Law.countByPublished(false)
        ]
    }

    def index() {
        log.info("Index admin")
        render view: '/admin/index', model:[menu:[unpublishedLaws: Law.countByPublished(false)]]
    }

    def solrIndex(){
        [res:[:]]
    }

    def fullIndex(){
        def res = indexSolrService.fullIndex()
        render view: '/admin/solrIndex', model:[res:res]
    }
}
