package kuorum

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import kuorum.core.FileGroup
import kuorum.files.FileService
import kuorum.users.KuorumUser
import org.bson.types.ObjectId
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest

import javax.servlet.http.HttpServletRequest

class FileController {

    FileService fileService
    def springSecurityService

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def uploadImage() {
        def fileData = getFileData(request)
        FileGroup fileGroup = FileGroup.valueOf(params.fileGroup)
//        FileGroup fileGroup = FileGroup.PROJECT_IMAGE
        if(!fileGroup){
            //TODO: ESTO ESTA MAL (Por defecto no es POST_IMAGE)
            fileGroup = FileGroup.POST_IMAGE
        }
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)

        KuorumFile kuorumFile = fileService.uploadTemporalFile(fileData.inputStream, user, fileData.fileName, fileGroup)

        render ([absolutePathImg:kuorumFile.url, fileId:kuorumFile.id.toString(), status:200] as JSON)
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def uploadPDF() {
        def fileData = getFileData(request)
        FileGroup fileGroup = FileGroup.PDF
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        KuorumFile kuorumFile = fileService.uploadTemporalFile(fileData.inputStream, user, fileData.fileName, fileGroup)

        render ([absolutePathPDF:kuorumFile.url, fileId:kuorumFile.id.toString(), status:200] as JSON)
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def uploadCampaignImages() {
        def fileData = getFileData(request)
        FileGroup fileGroup = FileGroup.CUSTOM_TEMPLATE_IMAGE
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        String campaignId= params.campaignId
        String path = "${user.id}/${campaignId}"
        KuorumFile kuorumFile = fileService.uploadTemporalFile(fileData.inputStream, user, fileData.fileName, fileGroup, path)
        kuorumFile = fileService.convertTemporalToFinalFile(kuorumFile)

        render ([success:true,absolutePath:kuorumFile.url, fileId:kuorumFile.id.toString(), status:200] as JSON)
//        render ([success:true] as JSON)
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def getCampaignImages() {
        String campaignId= params.campaignId
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        String path = "${user.id}/${campaignId}"
        List<KuorumFile> files = fileService.listFilesFromPath(FileGroup.CUSTOM_TEMPLATE_IMAGE, path)

        render (files.collect{kf->
            [name:kf.originalName, uuid:kf.id.toString(), thumbnailUrl:kf.url]
        } as JSON)
    }

        @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def cropImage() {
        KuorumFile kuorumFile = KuorumFile.get(new ObjectId(params.fileId))
        Double x = Double.parseDouble(params.x)
        Double y = Double.parseDouble(params.y)
        Double height = Double.parseDouble(params.height)
        Double width = Double.parseDouble(params.width)

        //TODO: Seguridad. Ahora todo el mundo puede hacer crop de cualquier foto

        KuorumFile cropedFile = fileService.cropImage(kuorumFile,x,y,height,width)
        render ([absolutePathImg:cropedFile.url, fileId:cropedFile.id.toString(), status:200] as JSON)
    }

    private def getFileData(HttpServletRequest request) {
        if (request instanceof MultipartHttpServletRequest) {
            MultipartFile uploadedFile = ((MultipartHttpServletRequest) request).getFile('qqfile')

            return [inputStream:uploadedFile.inputStream, fileName:uploadedFile.originalFilename]
        }
        return [inputStream:request.inputStream, fileName:params.qqfile]
    }
}
