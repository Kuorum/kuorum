package kuorum

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import kuorum.core.FileGroup
import kuorum.users.KuorumUser
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest

import javax.servlet.http.HttpServletRequest

class FileController {

    def fileService
    def springSecurityService

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def uploadImage() {
        def fileData = getFileData(request)
//        FileGroup fileGroup = FileGroup.valueOf(params.fileGroup)
        FileGroup fileGroup = FileGroup.LAW_IMAGE
        if(!fileGroup){
            //TODO: ESTO ESTA MAL (Por defecto no es POST_IMAGE)
            fileGroup = FileGroup.POST_IMAGE
        }
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)

        KuorumFile kuorumFile = fileService.uploadTemporalFile(fileData.inputStream, user, fileData.fileName, fileGroup)

        render ([absolutePathImg:kuorumFile.url, fileId:kuorumFile.id, status:200] as JSON)
    }

    private def getFileData(HttpServletRequest request) {
        if (request instanceof MultipartHttpServletRequest) {
            MultipartFile uploadedFile = ((MultipartHttpServletRequest) request).getFile('qqfile')

            return [inputStream:uploadedFile.inputStream, fileName:uploadedFile.name]
        }
        return [inputStream:request.inputStream, fileName:params.qqfile]
    }
}
