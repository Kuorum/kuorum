package kuorum.contest

import grails.test.mixin.TestFor
import kuorum.users.CookieUUIDService
import kuorum.web.commands.payment.contest.ContestApplicationVoteCommand
import org.kuorum.rest.model.communication.contest.ContestRSDTO
import payment.campaign.ContestApplicationService
import payment.campaign.ContestService
import spock.lang.Specification

@TestFor(ContestController)
class ContestControllerVoteSpec extends Specification {

    def setup() {
        controller.contestService = Mock(ContestService)
        controller.contestApplicationService = Mock(ContestApplicationService)
        controller.cookieUUIDService = Mock(CookieUUIDService)
        controller.springSecurityService = Mock(grails.plugin.springsecurity.SpringSecurityService)
    }

    void "vote with an invalid command renders the error and stops (no REST call, no cookie handling)"() {
        given:
        def command = new ContestApplicationVoteCommand(userAlias: alias, contestId: contestId, campaignId: campaignId)
        command.validate() // Grails does it when binding a real request

        when:
        controller.vote(command)

        then:
        response.json.success == false
        response.json.message == "There are errors on the data"
        0 * controller.contestService._
        0 * controller.contestApplicationService._
        0 * controller.cookieUUIDService._

        where:
        alias   | contestId | campaignId
        null    | 1L        | 2L
        "alias" | null      | 2L
        "alias" | 1L        | null
        null    | null      | null
    }

    void "vote of a non logged user in a contest without anonymous action is rejected"() {
        given:
        def command = new ContestApplicationVoteCommand(userAlias: "alias", contestId: 1L, campaignId: 2L)
        command.validate()

        when:
        controller.vote(command)

        then:
        1 * controller.contestService.find("alias", 1L) >> new ContestRSDTO(allowAnonymousAction: false)
        1 * controller.springSecurityService.isLoggedIn() >> false
        0 * controller.contestApplicationService._
        0 * controller.cookieUUIDService._
        response.json.success == false
        response.json.message == "You are unauthorized"
    }
}
