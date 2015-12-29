package kuorum.admin

import com.mongodb.BasicDBObject
import com.mongodb.DBObject
import grails.converters.deep.JSON
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.annotation.Secured
import kuorum.KuorumFile
import kuorum.core.model.UserType
import kuorum.files.FileService
import kuorum.mail.MailchimpService
import kuorum.post.Post
import kuorum.project.Project
import kuorum.users.KuorumUser
import kuorum.users.PoliticianService
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

@Secured(['ROLE_ADMIN'])
class AdminController {

    def indexSolrService
    def springSecurityService
    MailchimpService mailchimpService

    PoliticianService politicianService

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
        politicianService.asyncUploadPoliticianCSV(springSecurityService.currentUser,uploadedFile.inputStream)
        flash.message = "CSV ${uploadedFile.originalFilename} uploaded. An email will be sent at the end of the process"
        redirect(mapping:"adminSearcherIndex")
//        render view: "csvPoliticiansLoaded", model: [politiciansOk:politiciansOk,politiciansWrong:politiciansWrong, fileName:uploadedFile.getOriginalFilename()]
    }

    def putImagesOnAmazon(){
        List<String> emails = ["info+davidburrowesmp@kuorum.org","mikemurray287@gmail.com", "mat.nso@gmail.com", "amartosortega@gmail.com","alesanperz+equo@gmail.com","patricia.hernandez@congreso.es","ana.sastre@savethechildren.es", "carmenmartinezten@hotmail.com","yromanglez@gmail.com", "imolero@unicef.es" , "carmen@tetuanvalley.com", "aldobelus@riseup.net", "redes@educo.org","matias.nso@kuorum.org", "info+catsmithmp@kuorum.org", "info+geraintdaviesmp@kuorum.org", "yus_marc17@yahoo.com.mx", "gust.lorusso@gmail.com", "bvallsgu@gmail.com", "contact+1@kuorum.org", "contact+2@kuorum.org", "contact+3@kuorum.org", "contact+4@kuorum.org", "jrbalgete@gmail.com", "movimientosn@gmail.com", "rafa.alvarezluque@gmail.com", "gon.guzman@outlook.com", "sisepuedelaredo@gmail.com", "gabriel.siles-brugge@manchester.ac.uk"]
//        List<String> emails = ["info+davidburrowesmp@kuorum.org"]
        List<String> wrongMails = []
        KuorumUser.findAllByEmailInList(emails).each{
            try{
                if (it.avatar){
                    log.info("Moving avatar ${it.email} => ${it.avatar.url}")
                    it.avatar = moveLocalImageToAmazon(it.avatar)
                }
                if (it.imageProfile){
                    log.info("Moving imgProfile ${it.email} => ${it.imageProfile.url}")
                    it.imageProfile = moveLocalImageToAmazon(it.imageProfile)
                }
                if (!it.save()){
                    wrongMails << [mail:it.email, errors:it.errors]
                }
            }catch (Exception e){
                log.error("Execption moving images to Amazon",e)
                wrongMails << [mail:it.email, errors:[exception:e.getMessage()]]
            }
        }
        render wrongMails as JSON
    }

    FileService fileService
    private KuorumFile moveLocalImageToAmazon(KuorumFile kuorumFile){
        try{
            if (kuorumFile.local){
                URL url = new URL(kuorumFile.url);
                BufferedImage image = ImageIO.read(url);
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ImageIO.write(image, "jpg", os);
                InputStream is = new ByteArrayInputStream(os.toByteArray());
                KuorumFile rest = fileService.uploadTemporalFile(is, kuorumFile.user, kuorumFile.fileName, kuorumFile.fileGroup)
                rest = fileService.convertTemporalToFinalFile(rest);
                fileService.deleteKuorumFile(kuorumFile);
                return rest;
            }else{
                return kuorumFile
            }
        }catch (Exception e){
            log.info("No se ha podido mover la imagen ${kuorumFile.id} del usuario ${kuorumFile.user.email} a amazon")
            fileService.deleteKuorumFile(kuorumFile);
            return null
        }

    }
}
