package kuorum.admin

import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.annotation.Secured
import kuorum.mail.MailchimpService
import kuorum.project.Project
import kuorum.users.KuorumUser
import kuorum.users.PoliticianService
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest

@Secured(['ROLE_ADMIN'])
class AdminController {

    def indexSolrService
    def springSecurityService
    MailchimpService mailchimpService

    PoliticianService politicianService

//    def afterInterceptor = [action: this.&prepareMenuData]
//    protected prepareMenuData = {model, modelAndView ->
    def afterInterceptor = { model, modelAndView ->

    }

    def index() {
        log.info("Index admin")
        render view: '/admin/index', model:[]
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
        politicianService.asyncUploadPoliticianCSV(springSecurityService.currentUser,uploadedFile.inputStream)
        flash.message = "CSV ${uploadedFile.originalFilename} uploaded. An email will be sent at the end of the process"
        redirect(mapping:"adminSearcherIndex")
//        render view: "csvPoliticiansLoaded", model: [politiciansOk:politiciansOk,politiciansWrong:politiciansWrong, fileName:uploadedFile.getOriginalFilename()]
    }



}
