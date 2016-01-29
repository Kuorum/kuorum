package kuorum.admin

import grails.converters.deep.JSON
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.annotation.Secured
import kuorum.KuorumFile
import kuorum.files.FileService
import kuorum.mail.MailchimpService
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
        List<String> hashtags = [
                "#evalAmbiental",
                "#losToros",
                "#antiFracking",
                "#accionExterior",
                "#transparencia",
                "#seguridadPrivada",
                "#trabajoDiscap",
                "#autoridadFiscal",
                "#cajasAhorros",
                "#sectorElectrico",
                "#codigoPenal",
                "#pensiones",
                "#ecografias",
                "#empresassociales",
                "#aborto",
                "#abdicacion",
                "#tabaco",
                "#despidoIRPF",
                "#reformaFiscal",
                "#perrosYGatos",
                "#biblioNacional",
                "#PGE2015",
                "#fundaciones",
                "#sefardies",
                "#movSostenible",
                "#pobrezaInfantil",
                "#partidos",
                "#altoCargo",
                "#normaTributaria",
                "#SDG",
                "#solucionesjustas",
                "#participacion",
                "#antiConsultas",
                "#sayNoToFracking"]


        def wrongProjects = []
        Project.findAllByHashtagInList(hashtags).each{Project it ->
            try{
                if (it.image){
                    log.info("Moving avatar ${it.hashtag} => ${it.image.url}")
                    it.image = moveLocalImageToAmazon(it.image, it.hashtag)
                }

                if (!it.save()){
                    wrongProjects << [mail:it.hashtag, errors:it.errors]
                }
            }catch (Exception e){
                log.error("Execption moving images to Amazon",e)
                wrongProjects << [mail:it.hashtag, errors:[exception:e.getMessage()]]
            }
        }
        render wrongProjects as JSON
    }

    FileService fileService
    private KuorumFile moveLocalImageToAmazon(KuorumFile kuorumFile, String hashtag){
        try{
            if (kuorumFile.local){
                URL url = new URL(kuorumFile.url.replaceAll("https://kuorum.org","http://51.254.117.194"));
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
            log.info("No se ha podido mover la imagen ${kuorumFile.id} del projecto ${hashtag} a amazon: ${e.getMessage()}")
            fileService.deleteKuorumFile(kuorumFile);
            return null
        }

    }

}
