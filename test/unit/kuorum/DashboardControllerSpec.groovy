package kuorum

import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.userdetails.GrailsUser
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import kuorum.post.CluckService
import kuorum.users.KuorumUser
import org.bson.types.ObjectId
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(DashboardController)
@Mock(KuorumUser)
class DashboardControllerSpec extends Specification {

    def setup() {
        def mockCluckService = mockFor(CluckService)
        mockCluckService.demand.dashboardClucks(1) {def user -> []}
        controller.cluckService = mockCluckService.createMock()

    }

    def cleanup() {
    }

    @Unroll
    void "test index when is logged( #logged ) displaying #expectedView"() {
        given:
            KuorumUser user = new KuorumUser(name:"test",email:"email@test.com", password: "test" )
            user.save()
            controller.springSecurityService = [isLoggedIn:{->logged}, principal: [id: user.id]]
        when:
            controller.index()
        then:
            view == expectedView
        where:
            expectedView                | logged
            "/dashboard/landingPage"    | false
            "/dashboard/dashboard"      | true
    }
}
