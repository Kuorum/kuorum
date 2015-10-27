package kuorum.files

import grails.plugin.fixtures.FixtureLoader
import grails.plugin.springsecurity.SpringSecurityUtils
import kuorum.FileController
import kuorum.Region
import kuorum.users.KuorumUser
import org.springframework.mock.web.MockMultipartFile
import spock.lang.Shared
import spock.lang.Specification

class FileControllerIntegrationSpec extends Specification{

    @Shared
    FileController fileController

    @Shared
    FixtureLoader fixtureLoader

    void setupSpec(){
        Region.collection.getDB().dropDatabase()
        fileController = new FileController()
    }

    void "upload a PDF file" () {
        given: "a mock multipart file"
        MockMultipartFile testFile = new MockMultipartFile('qqfile', 'testPDF.pdf', 'application/pdf', '123' as byte[])
        fileController.request.addFile(testFile)
        and:"A DDBB loaded"
        fixtureLoader.load("testBasicData")

        and: "a user"
        KuorumUser user = KuorumUser.findByEmail("patxi@example.com")

        when: "upload a PDF file"
        SpringSecurityUtils.doWithAuth(user.email) {
            fileController.uploadPDF()
        }

        then: "the url of the file, id and status 200"
        fileController.response.json
        fileController.response.json.fileId
        fileController.response.json.absolutePathPDF
        fileController.response.json.status == 200
    }
}
