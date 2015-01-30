package kuorum.project

import grails.test.mixin.TestFor
import kuorum.Institution
import kuorum.KuorumFile
import kuorum.Region
import kuorum.core.FileGroup
import kuorum.core.model.CommissionType
import kuorum.helper.Helper
import kuorum.users.KuorumUser
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Project)
class ProjectSpec extends Specification {


    @Shared Region europe = new Region(name: "Europe", iso3166_2: "EU")
    @Shared Region spain = new Region(name: "Spain", iso3166_2: "EU-ES", superRegion: europe)

    @Shared Institution parliamentEurope = new Institution(name: "Parlamenteo Europeo", region: europe)
    @Shared Institution parliamentSpain = new Institution(name: "Parlamenteo Europeo", region: spain)

    @Shared KuorumFile pdfFile
    @Shared KuorumFile urlYoutube

    def setup() {
        mockForConstraintsTests(Project, [new Project()])
        mockForConstraintsTests(KuorumFile, [new KuorumFile()])
        KuorumUser user = Helper.createDefaultUser("email@email.com")
        pdfFile = new KuorumFile(
                fileGroup: FileGroup.PDF,
                temporal: true,
                user: user,
                url: "http://kuorum.org",
                local: true,
                storagePath: "/tmp",
                fileName: "test.pdf"
        )
        urlYoutube = new KuorumFile(
                fileGroup: FileGroup.PROJECT_IMAGE,
                temporal: true,
                user: user,
                url: "http://kuorum.org",
                local: false
        )

    }

    @Unroll("test PROJECT constraints: Checking #field = #value expected #error and validation #objValidate")
    def "test PROJECT all constraints"() {
        when:

        def params = [hashtag: '#nombre',
                shortName: 'shortaname',
                realName: "realname",
                description: "desc",
                region: europe,
                institution: parliamentEurope,
                commissions: [CommissionType.JUSTICE],
                deadline: new Date() + 10,
                introduction: 'introduction',
                availableStats: true,
                shortUrl: 'http://short.url',
                pdfFile: pdfFile,
                urlYoutube: urlYoutube
        ]
        params[field] = value
        def obj = new Project(params)

        then:
        //Object validation is added to check if all the fields validates, not only the field that is being checked
        obj.validate() == objValidate
        Helper.validateConstraints(obj, field, error)

        where:
        error                        | field         | value            || objValidate
        'OK'                         | 'hashtag'     | '#nombre'        || true
        'matches'                    | 'hashtag'     | '#'              || false
        'nullable'                   | 'hashtag'     | ''               || false
        'nullable'                   | 'hashtag'     | null             || false
        'nullable'                   | 'shortName'   | ''               || false
        'nullable'                   | 'shortName'   | null             || false
        'nullable'                   | 'realName'    | ''               || false
        'nullable'                   | 'realName'    | null             || false
        'nullable'                   | 'description' | ''               || false
        'nullable'                   | 'description' | null             || false
        'nullable'                   | 'commissions' | null             || false
        'nullable'                   | 'region'      | null             || false
        'notSameRegionAsInstitution' | 'institution' | parliamentSpain  || false
        'imageOrUrlYoutubeRequired'  | 'urlYoutube'  | null             || false
        'deadlineLessThanToday'      | 'deadline'    | new Date() - 1   || false
        'deadlineGreaterThan120Days' | 'deadline'    | new Date() + 121 || false
    }

    @Unroll("test PROJECT constraints: Checking #field = #value expected #error and validation #objValidate")
    def "test PROJECT all constraints without urlYoutube and image"() {
        when:

        def params = [hashtag: '#nombre',
                shortName: 'shortaname',
                realName: "realname",
                description: "desc",
                region: europe,
                institution: parliamentEurope,
                commissions: [CommissionType.JUSTICE],
                deadline: new Date() + 10,
                introduction: 'introduction',
                availableStats: true,
                shortUrl: 'http://short.url',
                pdfFile: pdfFile
        ]
        params[field] = value
        def obj = new Project(params)

        then:
        //Object validation is added to check if all the fields validates, not only the field that is being checked
        obj.validate() == objValidate
        Helper.validateConstraints(obj, field, error)

        where:
        error                        | field         | value            || objValidate
        'OK'                         | 'hashtag'     | '#nombre'        || false
        'matches'                    | 'hashtag'     | '#'              || false
        'nullable'                   | 'hashtag'     | ''               || false
        'nullable'                   | 'hashtag'     | null             || false
        'nullable'                   | 'shortName'   | ''               || false
        'nullable'                   | 'shortName'   | null             || false
        'nullable'                   | 'realName'    | ''               || false
        'nullable'                   | 'realName'    | null             || false
        'nullable'                   | 'description' | ''               || false
        'nullable'                   | 'description' | null             || false
        'nullable'                   | 'commissions' | null             || false
        'nullable'                   | 'region'      | null             || false
        'notSameRegionAsInstitution' | 'institution' | parliamentSpain  || false
        'imageOrUrlYoutubeRequired'  | 'image'       | null             || false
        'imageOrUrlYoutubeRequired'  | 'urlYoutube'  | null             || false
        'deadlineLessThanToday'      | 'deadline'    | new Date() - 1   || false
        'deadlineGreaterThan120Days' | 'deadline'    | new Date() + 121 || false
    }
}
