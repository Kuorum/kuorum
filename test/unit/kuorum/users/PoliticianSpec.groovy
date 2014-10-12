package kuorum.users

import grails.test.mixin.TestFor
import kuorum.Institution
import kuorum.PoliticalParty
import kuorum.Region
import kuorum.core.model.AvailableLanguage
import kuorum.helper.Helper
import org.bson.types.ObjectId
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@TestFor(KuorumUser)
class PoliticianSpec extends Specification {

    @Shared
    def params = [
            name:'nombre',
            password:'XX',
            email:'email@email.com',
            username:'email@email.com',
            languaje:AvailableLanguage.es_ES,
            institution:parliament
    ]
    @Shared
    Region europe = new Region(name:"Europe", iso3166_2: "EU")

    @Shared
    Institution parliament = new Institution( name:"parliament", region: europe, id:new ObjectId("52efd75c44ae2878c8f6e158"))
    @Shared
    Institution senate = new Institution(name:"senate", region: europe, id:new ObjectId("52efd75c55ae2878c8f6e159"))

    @Shared
    PoliticalParty psoe = new PoliticalParty(name:'psoe',region:europe,institution: parliament)
    @Shared
    PoliticalParty xxxx = new PoliticalParty(name:'xxxx',region:europe,institution: senate)

    def setup() {
        mockForConstraintsTests(KuorumUser, [new KuorumUser(institution:parliament)])
    }

    def cleanup() {
    }

    @Unroll("test POLITICIAN specific politicalParty constraints: Checking #field = #value expected #error")
    def "test POLITICIAN specific politicalParty constraints"() {
        when:
        def obj = new KuorumUser( params)
        obj.institution = parliament
        obj.politicalParty = value
        then:
        Helper.validateConstraints(obj, field, error)

        where:
        error                   | field                 | value
        'OK'                    | 'politicalParty'  | psoe
        'OK'                    | 'politicalParty'  | psoe
        'notCorrectInstitution' | 'politicalParty'  | xxxx
        'notParliamentaryGroup' | 'politicalParty'  | null
    }

}
