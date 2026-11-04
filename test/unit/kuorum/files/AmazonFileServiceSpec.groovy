package kuorum.files

import com.amazonaws.AmazonClientException
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.CopyObjectRequest
import com.amazonaws.services.s3.model.DeleteObjectRequest
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import kuorum.AmazonS3KeyCodec
import kuorum.KuorumFile
import kuorum.core.FileGroup
import kuorum.core.FileType
import kuorum.core.exception.KuorumException
import kuorum.util.rest.RestKuorumApiService
import org.bson.types.ObjectId
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionStatus
import spock.lang.Specification

/**
 * Temporal files must end in a permanent S3 key. The upload endpoint of the backend always stores them under "tmp/",
 * which is purged, so the groups without a dedicated promotion endpoint have to be moved with an S3 copy.
 */
@TestMixin(GrailsUnitTestMixin)
class AmazonFileServiceSpec extends Specification {

    static final String TMP_KEY = "tmp/domain/user/logo.png"

    AmazonS3 s3
    RestKuorumApiService restKuorumApiService
    AmazonFileService service
    File serverPath

    def setup() {
        mockCodec(AmazonS3KeyCodec)
        KuorumFile.metaClass.static.findByStoragePath = { String storagePath -> null } // there is no GORM here
        serverPath = File.createTempFile("kuorumFiles", "")
        serverPath.delete()
        serverPath.mkdirs()
        config.kuorum.amazon.bucketName = "bucket"
        config.grails.resources.mappers.amazoncdn.host = "https://cdn.test"
        config.kuorum.upload.serverPath = serverPath.absolutePath

        s3 = Mock(AmazonS3)
        restKuorumApiService = Mock(RestKuorumApiService)
        AmazonS3 amazonS3 = s3
        service = new AmazonFileService() {
            @Override
            protected AmazonS3 buildAmazonClient() { amazonS3 }
        }
        service.grailsApplication = grailsApplication
        service.restKuorumApiService = restKuorumApiService
        service.transactionManager = Stub(PlatformTransactionManager) { getTransaction(_) >> Stub(TransactionStatus) } // the service is transactional
    }

    def cleanup() {
        GroovySystem.metaClassRegistry.removeMetaClass(KuorumFile)
        serverPath.deleteDir()
    }

    private KuorumFile temporalFile(FileGroup group, ObjectId userId = new ObjectId()) {
        KuorumFile file = new KuorumFile(
                id: new ObjectId(), userId: userId, fileGroup: group, fileName: "logo.png", originalName: "logo.png",
                relativePath: "/user", temporal: true, local: false, fileType: FileType.AMAZON_IMAGE,
                storagePath: "/${TMP_KEY}", url: "https://cdn.test/${TMP_KEY}", urlThumb: "https://cdn.test/${TMP_KEY}"
        )
        // there is no GORM here: a successful save returns the instance (called with and without arguments)
        file.metaClass.save = { Map args -> delegate }
        file.metaClass.save = { -> delegate }
        return file
    }

    void "a key that already existed (a slide being replaced) is never deleted when the save fails"() {
        given:
        KuorumFile file = temporalFile(FileGroup.DOMAIN_SLIDE_IMAGE)
        file.metaClass.save = { Map args -> null }

        when:
        boolean moved = service.copyAmazonFileFromTemporal(file, "domains/demo/slide1.png")

        then:
        1 * s3.doesObjectExist("bucket", "domains/demo/slide1.png") >> true
        1 * s3.copyObject(_ as CopyObjectRequest)
        0 * s3.deleteObject(_)
        !moved
        file.temporal == true
    }

    void "the domain carousel flow still moves the file to its fixed key and returns true"() {
        given:
        KuorumFile file = temporalFile(FileGroup.DOMAIN_SLIDE_IMAGE)

        when:
        boolean moved = service.copyAmazonFileFromTemporal(file, "domains/demo/slide1.png")

        then:
        1 * s3.doesObjectExist("bucket", "domains/demo/slide1.png") >> true
        1 * s3.copyObject({ CopyObjectRequest r -> r.sourceKey == TMP_KEY && r.destinationKey == "domains/demo/slide1.png" })
        1 * s3.deleteObject({ DeleteObjectRequest r -> r.key == TMP_KEY })
        moved
        file.storagePath == "domains/demo/slide1.png"
        file.temporal == false
    }

}
