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
        file.id = new ObjectId() // the map constructor does not bind the id of a domain class
        // there is no GORM here: a successful save returns the instance (called with and without arguments)
        file.metaClass.save = { Map args -> delegate }
        file.metaClass.save = { -> delegate }
        return file
    }

    void "a group without dedicated endpoint is moved out of tmp with an S3 copy to its own final key"() {
        given:
        KuorumFile file = temporalFile(FileGroup.ASSOCIATION_IMAGE_PROFILE)
        String expectedKey = "ProjectFiles/${file.userId}/${file.id}/logo.png"

        when:
        KuorumFile result = service.convertTemporalToFinalFile(file)

        then:
        1 * s3.doesObjectExist("bucket", expectedKey)
        1 * s3.copyObject({ CopyObjectRequest r -> r.sourceKey == TMP_KEY && r.destinationKey == expectedKey })

        then: "the tmp object is deleted only after the copy and the save"
        1 * s3.deleteObject({ DeleteObjectRequest r -> r.key == TMP_KEY })
        0 * s3._
        0 * restKuorumApiService._

        and:
        result.url == "https://cdn.test/${expectedKey}"
        result.urlThumb == result.url
        !result.url.contains("/tmp/")
        result.storagePath == expectedKey
        result.temporal == false
        result.local == false
        result.fileType == FileType.AMAZON_IMAGE
    }

    void "the final key is different for two users uploading a file with the same name"() {
        given:
        KuorumFile fileUser1 = temporalFile(FileGroup.ASSOCIATION_IMAGE_PROFILE)
        KuorumFile fileUser2 = temporalFile(FileGroup.ASSOCIATION_IMAGE_PROFILE)

        expect:
        fileUser1.fileName == fileUser2.fileName
        service.generateFinalAmazonKey(fileUser1) != service.generateFinalAmazonKey(fileUser2)
    }

    void "the final key is deterministic and built with the id of the file, so a retry copies onto the same key"() {
        given:
        KuorumFile file = temporalFile(FileGroup.ASSOCIATION_IMAGE_PROFILE)

        expect:
        file.id != null
        service.generateFinalAmazonKey(file) == service.generateFinalAmazonKey(file)
        service.generateFinalAmazonKey(file) == "ProjectFiles/${file.userId}/${file.id}/logo.png"
    }

    void "two files of the same user and name get different final keys"() {
        given:
        ObjectId userId = new ObjectId()
        KuorumFile file1 = temporalFile(FileGroup.ASSOCIATION_IMAGE_PROFILE, userId)
        KuorumFile file2 = temporalFile(FileGroup.ASSOCIATION_IMAGE_PROFILE, userId)

        expect:
        service.generateFinalAmazonKey(file1) != service.generateFinalAmazonKey(file2)
    }

    void "if the S3 copy fails the file stays temporal and the tmp object is not deleted"() {
        given:
        KuorumFile file = temporalFile(FileGroup.ASSOCIATION_IMAGE_PROFILE)
        boolean saved = false
        file.metaClass.save = { Map args -> saved = true; delegate }

        when:
        service.convertTemporalToFinalFile(file)

        then:
        1 * s3.copyObject(_ as CopyObjectRequest) >> { throw new AmazonClientException("S3 is down") }
        0 * s3.deleteObject(_)
        thrown(KuorumException)

        and:
        file.temporal == true
        file.url == "https://cdn.test/${TMP_KEY}"
        file.storagePath == "/${TMP_KEY}"
        !saved
    }

    void "if the save returns null (validation error) the final copy is rolled back and the file keeps its temporal state"() {
        given:
        KuorumFile file = temporalFile(FileGroup.ASSOCIATION_IMAGE_PROFILE)
        file.metaClass.save = { Map args -> null }
        String expectedKey = "ProjectFiles/${file.userId}/${file.id}/logo.png"

        when:
        service.convertTemporalToFinalFile(file)

        then:
        1 * s3.copyObject(_ as CopyObjectRequest)
        1 * s3.deleteObject({ DeleteObjectRequest r -> r.key == expectedKey })
        0 * s3.deleteObject({ DeleteObjectRequest r -> r.key == TMP_KEY })
        thrown(KuorumException)

        and:
        file.temporal == true
        file.url == "https://cdn.test/${TMP_KEY}"
        file.storagePath == "/${TMP_KEY}"
    }

    void "if the save throws the final copy is rolled back too"() {
        given:
        KuorumFile file = temporalFile(FileGroup.ASSOCIATION_IMAGE_PROFILE)
        file.metaClass.save = { Map args -> throw new RuntimeException("Mongo is down") }
        String expectedKey = "ProjectFiles/${file.userId}/${file.id}/logo.png"

        when:
        service.convertTemporalToFinalFile(file)

        then:
        1 * s3.copyObject(_ as CopyObjectRequest)
        1 * s3.deleteObject({ DeleteObjectRequest r -> r.key == expectedKey })
        0 * s3.deleteObject({ DeleteObjectRequest r -> r.key == TMP_KEY })
        thrown(KuorumException)
        file.temporal == true
    }

    void "a failing delete of the tmp object does not fail a promotion that already succeeded"() {
        given:
        KuorumFile file = temporalFile(FileGroup.ASSOCIATION_IMAGE_PROFILE)

        when:
        KuorumFile result = service.convertTemporalToFinalFile(file)

        then:
        1 * s3.copyObject(_ as CopyObjectRequest)
        1 * s3.deleteObject({ DeleteObjectRequest r -> r.key == TMP_KEY }) >> { throw new AmazonClientException("gone") }
        notThrown(Exception)
        result.temporal == false
        !result.url.contains("/tmp/")
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

    void "a file that is not temporal is returned untouched"() {
        given:
        KuorumFile file = temporalFile(FileGroup.ASSOCIATION_IMAGE_PROFILE)
        file.temporal = false

        when:
        KuorumFile result = service.convertTemporalToFinalFile(file)

        then:
        0 * s3._
        0 * restKuorumApiService._
        result.url == "https://cdn.test/${TMP_KEY}"
    }

    void "a group with a dedicated endpoint is still promoted by the backend, not by an S3 copy"() {
        given:
        KuorumFile file = temporalFile(FileGroup.USER_AVATAR)
        file.metaClass.getUser = { -> [id: file.userId] }
        File local = new File("${service.calculateLocalStoragePath(file)}/${file.fileName}")
        local.parentFile.mkdirs()
        local.text = "image"

        when:
        KuorumFile result = service.convertTemporalToFinalFile(file)

        then:
        1 * restKuorumApiService.putFile(RestKuorumApiService.ApiMethod.USER_IMG_AVATAR, [userId: file.userId.toString()], [:], _ as File, "logo.png") >> "https://cdn.test/UsersFiles/final.png"
        0 * s3.copyObject(_)
        result.url == "https://cdn.test/UsersFiles/final.png"
        result.temporal == false
    }

}
