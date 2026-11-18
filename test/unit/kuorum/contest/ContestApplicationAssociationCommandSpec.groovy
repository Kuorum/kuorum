package kuorum.contest

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import kuorum.web.commands.payment.contest.ContestApplicationAssociationCommand
import spock.lang.Specification
import spock.lang.Unroll

@TestMixin(GrailsUnitTestMixin)
class ContestApplicationAssociationCommandSpec extends Specification {

    void setup() {
        mockForConstraintsTests(ContestApplicationAssociationCommand)
    }

    @Unroll
    void "association command validity: name=#name image=#image -> #valid"() {
        given:
        def command = new ContestApplicationAssociationCommand(associationName: name, associationImage: image)

        expect:
        command.validate() == valid

        where:
        name    | image || valid
        "Assoc" | "1"   || true
        null    | "1"   || false
        "Assoc" | null  || false
        null    | null  || false
        ""      | "1"   || false
        "Assoc" | ""    || false
        " "     | "1"   || false
        "Assoc" | "  "  || false
    }
}
