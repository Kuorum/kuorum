package kuorum.contest

import grails.test.mixin.TestFor
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.users.CookieUUIDService
import kuorum.web.commands.payment.contest.ContestApplicationVoteCommand
import org.kuorum.rest.model.communication.CampaignTypeRSDTO
import org.kuorum.rest.model.communication.contest.ContestRSDTO
import org.kuorum.rest.model.kuorumUser.BasicDataKuorumUserRSDTO
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

    // --- confirmation message: the domain sponsor (if any) is the one thanking for the vote ---

    def owner = Stub(BasicDataKuorumUserRSDTO) { getFullName() >> "Owner name" }

    private void messages() {
        // Unit tests use a static message source: register the texts with their {n} arguments
        messageSource.addMessage("contestApplication.callToAction.VOTING.success", request.locale, "Saved. Thank you")
        messageSource.addMessage("contestApplication.callToAction.VOTING.success.customDomain", request.locale, "Registered. On behalf of {0}, thanks for {1}.")
        messageSource.addMessage("contestApplication.callToAction.VOTING.success.sponsor", request.locale, "Registered. On behalf of {0}, thanks for this initiative.")
    }

    private ContestRSDTO contest(CampaignTypeRSDTO type) {
        return new ContestRSDTO(campaignType: type, title: "Contest title", user: owner)
    }

    private void sponsor(String sponsor) {
        CustomDomainResolver.metaClass.static.getSponsor = { -> sponsor }
    }

    def cleanup() {
        GroovySystem.metaClassRegistry.removeMetaClass(CustomDomainResolver)
    }

    void "vote confirmation of a contest application in a domain with sponsor thanks on behalf of the sponsor"() {
        given:
        messages()
        sponsor("Sponsor name")

        expect:
        controller.buildVoteSuccessMessage(contest(CampaignTypeRSDTO.CONTEST_APPLICATION)) == "Registered. On behalf of Sponsor name, thanks for this initiative."
        controller.buildVoteSuccessMessage(contest(CampaignTypeRSDTO.CONTEST)) == "Registered. On behalf of Sponsor name, thanks for this initiative."
    }

    void "the sponsor is HTML encoded in the confirmation (the noty prints HTML)"() {
        given:
        messages()
        sponsor("<b>Acme</b> & \"Co\"")

        when:
        String message = controller.buildVoteSuccessMessage(contest(CampaignTypeRSDTO.CONTEST_APPLICATION))

        then:
        message.contains("&lt;b&gt;Acme&lt;/b&gt;")
        !message.contains("<b>")
    }

    void "without sponsor the confirmation is the previous one (contest owner and contest title)"() {
        given:
        messages()
        sponsor(null)

        expect:
        controller.buildVoteSuccessMessage(contest(CampaignTypeRSDTO.CONTEST_APPLICATION)) == "Registered. On behalf of Owner name, thanks for Contest title."
    }

    void "campaigns that are not contests keep the generic confirmation even if the domain has a sponsor"() {
        given:
        messages()
        sponsor("Sponsor name")

        expect:
        controller.buildVoteSuccessMessage(contest(CampaignTypeRSDTO.DEBATE)) == "Saved. Thank you"
    }
}
