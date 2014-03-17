package kuorum.users

import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import kuorum.Region
import kuorum.core.model.AvailableLanguage
import kuorum.core.model.Gender
import kuorum.helper.Helper
import kuorum.web.commands.customRegister.Step1Command
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
class Step1CommandSpec extends Specification {


    def setup() {
        mockForConstraintsTests(Step1Command, [new Step1Command()])
    }

    def cleanup() {
    }

    @Unroll
    def "test Step1 constraints [month=#month]: Checking #field = #value expected #error"() {
        when:

        def params = [
                gender:Gender.MALE,
                postalCode:"28001",
                year:year,
                month:month,
                day:1
        ]
        params[field] = value
        def obj = new Step1Command( params)
        then:
        Helper.validateConstraints(obj, field, error)

        where:
        error                   | field         | value      | month |year
        'OK'                    | 'postalCode'  | '28021'    | 1     |2001
        'minSize'               | 'postalCode'  | '2802'     | 1     |2001
        'maxSize'               | 'postalCode'  | '280211'   | 1     |2001
        'min'                   | 'year'        | -1         | 1     |2001
        'OK'                    | 'month'       | 1          | 1     |2001
        'OK'                    | 'month'       | 8          | 1     |2001
        'OK'                    | 'month'       | 12         | 1     |2001
        'max'                   | 'month'       | 13         | 1     |2001
        'min'                   | 'month'       | 0          | 1     |2001
        'min'                   | 'day'         | 0          | 1     |2001
        'OK'                    | 'day'         | 31         | 1     |2001
        'max'                   | 'day'         | 32         | 1     |2001
        'notCorrectBirthday'    | 'day'         | 31         | 4     |2001
        'notCorrectBirthday'    | 'day'         | 31         | 6     |2001
        'notCorrectBirthday'    | 'day'         | 29         | 2     |2001
        'OK'                    | 'day'         | 28         | 2     |2001
        'OK'                    | 'day'         | 29         | 2     |2000
    }
}
