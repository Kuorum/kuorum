package kuorum.files

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import kuorum.KuorumFile
import kuorum.core.FileGroup
import kuorum.helper.Helper
import kuorum.users.KuorumUser
import org.springframework.mock.web.MockMultipartFile
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(FileService)
@Mock([KuorumUser, KuorumFile])
class FileServiceSpec extends Specification {


    def setup() {
        grailsApplication.config.kuorum.upload.serverPath = "/tmp/kuorumTest"
        grailsApplication.config.grails.serverURL = "http://127.0.0.1:8080"
        grailsApplication.config.kuorum.upload.relativeUrlPath="/uploadedFiles"
    }

    def cleanup() {
        File tmpDir = new File("${grailsApplication.config.kuorum.upload.serverPath}")
        deleteDirectory(tmpDir)
    }

    @Unroll
    void "test uploading temporal[#temporal] file #fileName (size= #size)"() {
        given: "An input stream"
        def data =""
        (0..size).each{data += "Fichero subido"}
        def mockFile = new MockMultipartFile(fileName, 'tempFile','jpg/jpeg', data as byte[])
        KuorumUser user = Helper.createDefaultUser("email@email.com").save()
        when:"trying to save"
        KuorumFile kuorumTempFile = service.uploadTemporalFile(mockFile.inputStream,user, mockFile.getName(), FileGroup.USER_AVATAR)
        File tempFile = new File("${kuorumTempFile.storagePath}/${kuorumTempFile.fileName}")

        KuorumFile finalKuorumFile
        File finalFile
        if (!temporal){
            finalKuorumFile = service.convertTemporalToFinalFile(kuorumTempFile)
            finalFile = new File("${finalKuorumFile.storagePath}/${kuorumTempFile.fileName}")
        }

        then:
        kuorumTempFile != null
        kuorumTempFile.fileGroup == FileGroup.USER_AVATAR
        if (temporal){
            kuorumTempFile.storagePath.startsWith("${grailsApplication.config.kuorum.upload.serverPath}${service.TMP_PATH}")
            kuorumTempFile.url.startsWith("${grailsApplication.config.grails.serverURL}${grailsApplication.config.kuorum.upload.relativeUrlPath}")
            kuorumTempFile.storagePath.split("/").last().split("\\.").first() == kuorumTempFile.id.toString()
            kuorumTempFile.url.split("/").last() == kuorumTempFile.fileName
            tempFile.exists()
        }else{
            finalKuorumFile.temporal == Boolean.FALSE
            File tmpDir = new File("${grailsApplication.config.kuorum.upload.serverPath}")
            !tmpDir.exists()
            !tempFile.exists()
            finalFile.exists()
            finalKuorumFile.storagePath.startsWith("${grailsApplication.config.kuorum.upload.serverPath}")
            !finalKuorumFile.storagePath.startsWith("${grailsApplication.config.kuorum.upload.serverPath}${service.TMP_PATH}")
            finalKuorumFile.storagePath.split("/").last().split("\\.").first() == finalKuorumFile.id.toString()

        }
        where:
        fileName        | size | temporal
        "fileName.jpg"  | 1    | Boolean.TRUE
        "fileName.jpg"  | 1    | Boolean.FALSE
        "file.gif"      | 1    | Boolean.TRUE
    }


    @Unroll
    void "test deleting temporal files with #numValidFiles valid files of #totalFiles total files"() {
        given: "Upload some temporaly files"
        KuorumUser user = Helper.createDefaultUser("email@email.com").save()
        KuorumUser controlUser = Helper.createDefaultUser("control@email.com").save()

        def data = "file data"

        if (controlFiles > 0)
            (1..controlFiles).each{
                def mockFile = new MockMultipartFile("controlFile${it}.jpg", 'tempFile','jpg/jpeg', data as byte[])
                KuorumFile kuorumTempFile = service.uploadTemporalFile(mockFile.inputStream,controlUser, mockFile.getName(), FileGroup.USER_AVATAR)
            }

        if (totalFiles > 0)
            (1..totalFiles).each{
                def mockFile = new MockMultipartFile("file${it}.jpg", 'tempFile','jpg/jpeg', data as byte[])
                KuorumFile kuorumTempFile = service.uploadTemporalFile(mockFile.inputStream,user, mockFile.getName(), FileGroup.USER_AVATAR)
            }

        if (numValidFiles > 0)
            (1..numValidFiles).each{
                KuorumFile kuorumFinalFile = KuorumFile.findByUserIdAndTemporal(user.id,Boolean.TRUE)
                service.convertTemporalToFinalFile(kuorumFinalFile)
            }


        when:"deleting temporal files"
        service.deleteTemporalFiles(user)
        then:
        KuorumFile.findAllByUserIdAndTemporal(user.id, Boolean.TRUE).size() == 0
        KuorumFile.findAllByUserId(controlUser.id).size() == controlFiles
        KuorumFile.findAllByUserIdAndTemporal(user.id, Boolean.FALSE).size() == numValidFiles
        KuorumFile.count() == numValidFiles + controlFiles
        File tmpDir = new File("${grailsApplication.config.kuorum.upload.serverPath}${service.TMP_PATH}")
        if (controlFiles > 0)
            tmpDir.exists()
        else
            !tmpDir.exists()

        where:
        numValidFiles | totalFiles  | controlFiles
        0             | 0           | 0
        0             | 0           | 1
        1             | 1           | 1
        1             | 1           | 0
        1             | 2           | 1
        2             | 3           | 1
        2             | 3           | 0
    }

    void "test deleting non existing file"(){
        given:"Non  exisiting file"
        KuorumUser user = Helper.createDefaultUser("email@email.com").save()
        KuorumFile file = new KuorumFile(
                user:user,
                temporal:Boolean.TRUE,
                storagePath:"/tmp",
                fileName:"noExists.txt",
                url:"http://url.com",
                fileGroup:FileGroup.LAW_IMAGE)
        file.save()
        when:"Is deleted"
        service.deleteFile(file)
        then:"ALL OK"
        true
    }

    static private boolean deleteDirectory(File path) {
        if( path.exists() ) {
            File[] files = path.listFiles();
            for(int i=0; i<files.length; i++) {
                if(files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                }
                else {
                    files[i].delete();
                }
            }
        }
        return( path.delete() );
    }
}
