package kuorum

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import kuorum.core.FileGroup
import kuorum.files.FileService
import kuorum.files.LocalFileService
import kuorum.register.KuorumUserSession
import org.apache.commons.codec.binary.Base64
import org.bson.types.ObjectId
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest
import payment.campaign.CampaignService
import payment.campaign.NewsletterService
import payment.campaign.SurveyService
import payment.contact.ContactService

import javax.servlet.http.HttpServletRequest

class FileController {

    FileService fileService

    LocalFileService localFileService;
    def springSecurityService
    CampaignService campaignService
    NewsletterService newsletterService
    ContactService contactService
    SurveyService surveyService

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def uploadImage() {
        def fileData = getFileData(request)
        FileGroup fileGroup = FileGroup.valueOf(params.fileGroup)
//        FileGroup fileGroup = FileGroup.PROJECT_IMAGE
        if(!fileGroup){
            //TODO: ESTO ESTA MAL (Por defecto no es POST_IMAGE)
            fileGroup = FileGroup.POST_IMAGE
        }
        KuorumUserSession user = springSecurityService.principal

        KuorumFile kuorumFile = fileService.uploadTemporalFile(fileData.inputStream, user, fileData.fileName, fileGroup)

        render ([absolutePathImg:kuorumFile.url, fileId:kuorumFile.id.toString(), status:200, aspectRatio:fileGroup.aspectRatio] as JSON)
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def uploadPDF() {
        def fileData = getFileData(request)
        FileGroup fileGroup = FileGroup.PDF
        KuorumUserSession user = springSecurityService.principal
        KuorumFile kuorumFile = fileService.uploadTemporalFile(fileData.inputStream, user, fileData.fileName, fileGroup)

        render ([absolutePathPDF:kuorumFile.url, fileId:kuorumFile.id.toString(), status:200] as JSON)
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def uploadCampaignFile() {
        def fileData = getFileData(request)
        File file = File.createTempFile("campaignFile", "kuorum")
        localFileService.upload(fileData.inputStream,file);
        String fileUrl = campaignService.uploadFile(springSecurityService.principal,Long.parseLong(params.campaignId), file, fileData.fileName);
        file.delete();
//        FileGroup fileGroup = FileGroup.PDF
//        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
//        KuorumFile kuorumFile = fileService.uploadTemporalFile(fileData.inputStream, user, fileData.fileName, fileGroup)

        render ([fileUrl: fileUrl, fileName: fileData.fileName, status:200, success:true] as JSON)
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def deleteCampaignFile() {
        def fileName = params.fileName
        campaignService.deleteFile(springSecurityService.principal, Long.parseLong(params.campaignId), fileName)
        render ([fileUrl: "#FILE", fileName: fileName, status:200, success:true] as JSON)
    }


    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def uploadContactFile() {
        def fileData = getFileData(request)
        File file = File.createTempFile("contactFile", "kuorum")
        localFileService.upload(fileData.inputStream,file);
        String fileUrl = contactService.uploadFile(springSecurityService.principal,Long.parseLong(params.contactId), file, fileData.fileName);
        file.delete();
//        FileGroup fileGroup = FileGroup.PDF
//        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
//        KuorumFile kuorumFile = fileService.uploadTemporalFile(fileData.inputStream, user, fileData.fileName, fileGroup)

        render ([fileUrl: fileUrl, fileName: fileData.fileName, status:200, success:true] as JSON)
    }


    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def deleteContactFile() {
        def fileName = params.fileName
        contactService.deleteFile(springSecurityService.principal, Long.parseLong(params.contactId), fileName)
        render ([fileUrl: "#FILE", fileName: fileName, status:200, success:true] as JSON)
    }


    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def uploadQuestionOptionFile() {
        def fileData = getFileData(request)
        File file = File.createTempFile("contactFile", "kuorum")
        localFileService.upload(fileData.inputStream,file);
        String fileUrl
        try {
            fileUrl = surveyService.uploadQuestionOptionFile(
                    springSecurityService.principal,
                    params.userAlias,
                    Long.parseLong(params.surveyId),
                    Long.parseLong(params.questionId),
                    Long.parseLong(params.questionOptionId),
                    file,
                    fileData.fileName);
        }finally{
            file.delete();
        }
//        FileGroup fileGroup = FileGroup.PDF
//        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
//        KuorumFile kuorumFile = fileService.uploadTemporalFile(fileData.inputStream, user, fileData.fileName, fileGroup)

        render ([fileUrl: fileUrl, fileName: fileData.fileName, status:200, success:true] as JSON)
    }


    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def deleteQuestionOptionFile() {
        def fileName = params.fileName
        surveyService.deleteQuestionOptionFile(
                springSecurityService.principal,
                params.userAlias,
                Long.parseLong(params.surveyId),
                Long.parseLong(params.questionId),
                Long.parseLong(params.questionOptionId),
                fileName);
        render ([fileUrl: "#FILE", fileName: fileName, status:200, success:true] as JSON)
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def uploadCampaignImages() {
        def fileData = getFileData(request, 'miufile')
        FileGroup fileGroup = FileGroup.CUSTOM_TEMPLATE_IMAGE
        KuorumUserSession user = springSecurityService.principal
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
        KuorumUserSession user = springSecurityService.principal
        String path = "${user.id}/${campaignId}"
        List<KuorumFile> files = fileService.listFilesFromPath(FileGroup.CUSTOM_TEMPLATE_IMAGE, path)

        render (files.collect{kf->
            [name:kf.originalName, uuid:kf.id.toString(), thumbnailUrl:kf.url]
        } as JSON)
    }


    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def uploadNewsletterAttachedFile() {
        def fileData = getFileData(request)
        File file = File.createTempFile("campaignFile", "kuorum")
        localFileService.upload(fileData.inputStream,file);
        String fileUrl = newsletterService.uploadFile(springSecurityService.principal,Long.parseLong(params.campaignId), file, fileData.fileName);
        file.delete();
//        FileGroup fileGroup = FileGroup.PDF
//        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
//        KuorumFile kuorumFile = fileService.uploadTemporalFile(fileData.inputStream, user, fileData.fileName, fileGroup)

        render ([fileUrl: fileUrl, fileName: fileData.fileName, status:200, success:true] as JSON)
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def deleteNewsletterAttachedFile() {
        def fileName = params.fileName
        newsletterService.deleteFile(springSecurityService.principal, Long.parseLong(params.campaignId), fileName)
        render ([fileUrl: "#FILE", fileName: fileName, status:200, success:true] as JSON)
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
        render ([absolutePathImg:cropedFile.url, fileId:cropedFile.id.toString(), status:200,aspectRatio:cropedFile.fileGroup.aspectRatio] as JSON)
    }

    private def getFileData(HttpServletRequest request) {
        getFileData(request, 'qqfile')
    }
    private def getFileData(HttpServletRequest request, paramFileName) {
        if (request instanceof MultipartHttpServletRequest) {
            MultipartFile uploadedFile = ((MultipartHttpServletRequest) request).getFile(paramFileName)

            return [inputStream:uploadedFile.inputStream, fileName:uploadedFile.originalFilename]
        }
        return [inputStream:request.inputStream, fileName:params.qqfile]
    }
}
