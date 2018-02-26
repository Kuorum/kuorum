package kuorum

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserService
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(DashboardController)
@Mock(KuorumUser)
class DashboardControllerSpec extends Specification {

    def setup() {
//        def kuorumUserService = mockFor(KuorumUserService)
//        kuorumUserService.demand.mostActiveUsersSince(_,_) {def user, def pagination -> []}
//
//        def projectService = mockFor(ProjectService)
//        projectService.demand.findRelevantProjectEvents(_,_){def user, def pagination -> []}
//
//        controller.kuorumUserService = kuorumUserService.createMock()
//        controller.projectService = projectService.createMock()

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
            "/dashboard/landingPoliticians"    | false
            "/dashboard/dashboard"      | true
    }
}
