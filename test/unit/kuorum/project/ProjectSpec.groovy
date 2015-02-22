package kuorum.project

import grails.test.mixin.TestFor
import kuorum.Institution
import kuorum.KuorumFile
import kuorum.Region
import kuorum.core.FileGroup
import kuorum.core.model.CommissionType
import kuorum.core.model.RegionType
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


    @Shared Region europe = new Region(name: "Europe", iso3166_2: "EU", regionType: RegionType.STATE)
    @Shared Region spain = new Region(name: "Spain", iso3166_2: "EU-ES", superRegion: europe, regionType: RegionType.NATION)

    @Shared Institution parliamentEurope = new Institution(name: "Parlamenteo Europeo", region: europe)
    @Shared Institution parliamentSpain = new Institution(name: "Parlamenteo Europeo", region: spain)

    @Shared KuorumFile pdfFile
    @Shared KuorumFile urlYoutube
    @Shared KuorumUser user

    def setup() {
        mockForConstraintsTests(Project, [new Project()])
        mockForConstraintsTests(KuorumFile, [new KuorumFile()])
        user = Helper.createDefaultUser("email@email.com")
        pdfFile = new KuorumFile(
                fileGroup: FileGroup.PDF,
                temporal: true,
                user: user,
                url: "http://kuorum.org",
                local: true,
                storagePath: "/tmp",
                fileName: "test.pdf",
                originalName: 'test'
        )
        urlYoutube = new KuorumFile(
                fileGroup: FileGroup.PROJECT_IMAGE,
                temporal: true,
                user: user,
                url: "http://kuorum.org",
                local: false,
                originalName: 'test'
        )

    }

    @Unroll
    void "Validate a project update. Check constraints: Checking desciption: #description, #field expected #error and validation #objValidate"() {
        when: "Create a project"
        ProjectUpdate projectUpdate = new ProjectUpdate(description: description)
        projectUpdate.dateCreated = new Date()

        then:
        projectUpdate.validate() == objValidate

        where:
        description | field     | error || objValidate
        'a'         | 'updates' | 'OK'  || true
        'a' * 501   | 'updates' | 'OK'  || false

    }

    @Unroll("test PROJECT constraints: Checking #field = #value expected #error and validation #objValidate")
    void "test PROJECT all constraints"() {
        when:

        def params = [hashtag: '#nombre',
                shortName: 'shortaname',
                realName: "realname",
                description: "desc",
                region: europe,
                institution: parliamentEurope,
                commissions: [CommissionType.JUSTICE],
                deadline: new Date() + 10,
                availableStats: true,
                shortUrl: 'http://short.url',
                pdfFile: pdfFile,
                urlYoutube: urlYoutube,
                owner: user
        ]
        params[field] = value
        Project obj = new Project(params)

        then:
        //Object validation is added to check if all the fields validates, not only the field that is being checked
        obj.validate() == objValidate
        Helper.validateConstraints(obj, field, error)

        where:
        error                        | field         | value                                   || objValidate
        'OK'                         | 'hashtag'     | '#nombre'                               || true
        'maxSize'                    | 'hashtag'     | "#${'n' * 17}"                          || false
        'matches'                    | 'hashtag'     | '#'                                     || false
        'nullable'                   | 'hashtag'     | ''                                      || false
        'nullable'                   | 'hashtag'     | null                                    || false
        'nullable'                   | 'shortName'   | ''                                      || false
        'nullable'                   | 'shortName'   | null                                    || false
        'nullable'                   | 'description' | ''                                      || false
        'nullable'                   | 'description' | null                                    || false
        'nullable'                   | 'commissions' | null                                    || false
        'maxSize'                    | 'commissions' | [CommissionType.AGRICULTURE, CommissionType.BUDGETS,
                CommissionType.CONSTITUTIONAL, CommissionType.DEFENSE, CommissionType.CULTURE] || false
        'nullable'                   | 'region'      | null                                    || false
        'notSameRegionAsInstitution' | 'institution' | parliamentSpain                         || false
        'imageOrUrlYoutubeRequired'  | 'urlYoutube'  | null                                    || false
        'deadlineLessThanToday'      | 'deadline'    | new Date() - 1                          || false
        'deadlineGreaterThan120Days' | 'deadline'    | new Date() + 121                        || false
        'maxSize'                    | 'shortName'   | 'a' * 108                               || false
//        'maxSize'                    | 'description' | 'a' * 5001                              || false // Se ha eliminado la restriccion de máximo numero de caracteress
    }

    @Unroll("test PROJECT constraints: Checking #field = #value expected #error and validation #objValidate")
    void "test PROJECT all constraints without urlYoutube and image"() {
        when:

        def params = [hashtag: '#nombre',
                shortName: 'shortaname',
                realName: "realname",
                description: "desc",
                region: europe,
                institution: parliamentEurope,
                commissions: [CommissionType.JUSTICE],
                deadline: new Date() + 10,
                availableStats: true,
                shortUrl: 'http://short.url',
                pdfFile: pdfFile
        ]
        params[field] = value
        Project obj = new Project(params)

        then:
        //Object validation is added to check if all the fields validates, not only the field that is being checked
        obj.validate() == objValidate
        Helper.validateConstraints(obj, field, error)

        where:
        error                        | field         | value                                   || objValidate
        'OK'                         | 'hashtag'     | '#nombre'                               || false
        'maxSize'                    | 'hashtag'     | "#${'n' * 17}"                          || false
        'matches'                    | 'hashtag'     | '#'                                     || false
        'nullable'                   | 'hashtag'     | ''                                      || false
        'nullable'                   | 'hashtag'     | null                                    || false
        'nullable'                   | 'shortName'   | ''                                      || false
        'nullable'                   | 'shortName'   | null                                    || false
        'nullable'                   | 'description' | ''                                      || false
        'nullable'                   | 'description' | null                                    || false
        'nullable'                   | 'commissions' | null                                    || false
        'maxSize'                    | 'commissions' | [CommissionType.AGRICULTURE, CommissionType.BUDGETS,
                CommissionType.CONSTITUTIONAL, CommissionType.DEFENSE, CommissionType.CULTURE] || false
        'nullable'                   | 'region'      | null                                    || false
        'notSameRegionAsInstitution' | 'institution' | parliamentSpain                         || false
        'imageOrUrlYoutubeRequired'  | 'image'       | null                                    || false
        'imageOrUrlYoutubeRequired'  | 'urlYoutube'  | null                                    || false
        'deadlineLessThanToday'      | 'deadline'    | new Date() - 1                          || false
        'deadlineGreaterThan120Days' | 'deadline'    | new Date() + 121                        || false
        'maxSize'                    | 'shortName'   | 'a' * 108                               || false
        'maxSize'                    | 'description' | 'a' * 5001                              || false
    }
}
