package kuorum

import grails.plugin.springsecurity.SpringSecurityService
import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(DashboardController)
class DashboardControllerSpec extends Specification {

    def setup() {

    }

    def cleanup() {
    }

    @Unroll
    void "test index when is logged( #logged ) displaying #expectedView"() {
        given:
            controller.springSecurityService = [
                    isLoggedIn: { -> logged }
            ]
        when:
            controller.index()
        then:
            assert view == expectedView
        where:
            expectedView                | logged
            "/dashboard/landingPage"    | false
            "/dashboard/dashboard"      | true
    }
}
