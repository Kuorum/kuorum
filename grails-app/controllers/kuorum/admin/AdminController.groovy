package kuorum.admin

import com.mongodb.BasicDBObject
import com.mongodb.DBObject
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.annotation.Secured
import kuorum.core.model.UserType
import kuorum.mail.MailchimpService
import kuorum.project.Project
import kuorum.users.KuorumUser
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest

@Secured(['ROLE_ADMIN'])
class AdminController {

    def indexSolrService
    def springSecurityService
    MailchimpService mailchimpService

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

    def updateMailChimp(){
        mailchimpService.updateAllUsers()
        render "UPDATE ALL USERS TO MAILCHIMP"
    }

    def fullIndex(){
        def res = indexSolrService.fullIndex()
        render view: '/admin/solrIndex', model:[res:res]
    }

    def uploadPoliticianCsv(){
        MultipartFile uploadedFile = ((MultipartHttpServletRequest) request).getFile('filecsv')
        if (uploadedFile.empty) {
            flash.message = 'file cannot be empty'
            render(view: 'solrIndex')
            return
        }
        Reader reader = new InputStreamReader(uploadedFile.inputStream);
        def lines = com.xlson.groovycsv.CsvParser.parseCsv(reader)
        for(line in lines) {
            println "$line.Name $line.Lastname"
        }
        flash.message = "CSV ${uploadedFile.originalFilename} Processed"
        redirect(mapping:"adminSearcherIndex")
    }
}
