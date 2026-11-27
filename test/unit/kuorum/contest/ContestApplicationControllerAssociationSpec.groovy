package kuorum.contest

import grails.test.mixin.TestFor
import kuorum.KuorumFile
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.register.KuorumUserSession
import kuorum.web.commands.payment.contest.ContestApplicationAssociationCommand
import kuorum.web.commands.payment.contest.ContestApplicationAuthorizationsCommand
import kuorum.web.commands.payment.CampaignContentCommand
import kuorum.web.commands.payment.contest.ContestApplicationScopeCommand
import kuorum.web.constants.WebConstants
import org.kuorum.rest.model.communication.contest.ContestApplicationRDTO
import org.kuorum.rest.model.communication.contest.ContestApplicationRSDTO
import org.kuorum.rest.model.communication.contest.ContestApplicationTypeDTO
import org.kuorum.rest.model.communication.contest.ContestRSDTO
import org.kuorum.rest.model.kuorumUser.BasicDataKuorumUserRSDTO
import org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO
import org.kuorum.rest.model.domain.DomainRSDTO
import payment.campaign.CampaignService
import payment.campaign.ContestApplicationService
import payment.campaign.ContestService
import payment.contact.ContactService
import org.kuorum.rest.model.contact.ContactRSDTO
import spock.lang.Specification
import spock.lang.Unroll

@TestFor(ContestApplicationController)
class ContestApplicationControllerAssociationSpec extends Specification {

    KuorumUserSession user = Stub(KuorumUserSession) { getId() >> new org.bson.types.ObjectId(); getName() >> "Farmacia Pepe"; getTimeZone() >> TimeZone.getDefault() }
    ContestApplicationRSDTO rsdto
    Map redirectArgs

    def setup() {
            DomainRSDTO domainRSDTO = new DomainRSDTO()
        domainRSDTO.contestApplicationWithCollaborator = true
        CustomDomainResolver.metaClass.static.getDomainRSDTO = { -> domainRSDTO }

        // KuorumFile is a Mongo GORM domain: not mockable here, the image lookups just find nothing
        KuorumFile.metaClass.static.get = { Serializable id -> null }
        KuorumFile.metaClass.static.findByUrl = { url -> null }

        // encodeAsLinkProperties comes from a codec extension and redirect(mapping:) needs the real url mappings: both are stubbed
        ContestApplicationRSDTO.metaClass.encodeAsLinkProperties = { -> [campaignId: 5L] }
        controller.metaClass.redirect = { Map args -> redirectArgs = args }

        controller.springSecurityService = Stub(grails.plugin.springsecurity.SpringSecurityService) { getPrincipal() >> user }
        controller.contestApplicationService = Mock(ContestApplicationService)
        controller.contestService = Mock(ContestService)
        controller.campaignService = Mock(CampaignService)
        controller.contactService = Mock(ContactService)

        rsdto = new ContestApplicationRSDTO(id: 5L, contestApplicationType: ContestApplicationTypeDTO.COLLABORATOR,
                contest: new org.kuorum.rest.model.communication.contest.EmbeddedContestRSDTO(id: 9L), campaignStatusRSDTO: CampaignStatusRSDTO.DRAFT,
                title: "Real title", body: "Body", photoUrl: "http://img/p.png")
        params.campaignId = "5"
        params.redirectLink = "contestApplicationEditContent"
    }

    def cleanup() {
        GroovySystem.metaClassRegistry.removeMetaClass(CustomDomainResolver)
        GroovySystem.metaClassRegistry.removeMetaClass(KuorumFile)
        GroovySystem.metaClassRegistry.removeMetaClass(ContestApplicationRSDTO)
    }

    void "association step with the files service down saves the texts but does not advance (error, not a success message)"() {
        when:
        controller.saveAssociation(new ContestApplicationAssociationCommand(associationName: "Assoc", associationImage: "1"))

        then:
        1 * controller.contestApplicationService.find(_, 5L) >> rsdto
        1 * controller.contestApplicationService.map(rsdto) >> new ContestApplicationRDTO()
        1 * controller.contestApplicationService.save(_, { ContestApplicationRDTO r -> r.associationName == "Assoc" }, 5L) >> rsdto
        1 * controller.contactService.getFiles(WebConstants.FAKE_LANDING_ALIAS_USER, _) >> { throw new RuntimeException("API down") }
        flash.error == "contestApplication.association.files.checkFailed"
        flash.message == null
        redirectArgs.mapping == "contestApplicationEditAssociation"
    }

    @Unroll
    void "association step with logo='#image' is rejected: nothing is saved and the step is shown again with the error"() {
        given:
        def command = new ContestApplicationAssociationCommand(associationName: "Assoc", associationImage: image)
        command.validate() // Grails does it when binding a real request

        when:
        controller.saveAssociation(command)

        then:
        1 * controller.contestApplicationService.find(_, 5L) >> rsdto
        1 * controller.contestService.find(_, _) >> new ContestRSDTO(id: 9L)
        0 * controller.contestApplicationService.save(*_)
        0 * controller.contactService.getFiles(WebConstants.FAKE_LANDING_ALIAS_USER, _)
        view == "/contestApplication/editAssociationStep" || view == "editAssociationStep"
        flash.error != null

        where:
        image << [null, ""]
    }

    void "association step without any file saves the texts but does not advance"() {
        when:
        controller.saveAssociation(new ContestApplicationAssociationCommand(associationName: "Assoc", associationImage: "1"))

        then:
        1 * controller.contestApplicationService.find(_, 5L) >> rsdto
        1 * controller.contestApplicationService.map(rsdto) >> new ContestApplicationRDTO()
        1 * controller.contestApplicationService.save(*_) >> rsdto
        1 * controller.contactService.getFiles(WebConstants.FAKE_LANDING_ALIAS_USER, _) >> []
        flash.error == "contestApplication.association.files.missing"
        flash.message == null
        redirectArgs.mapping == "contestApplicationEditAssociation"
    }

    void "association step with at least one file advances to the next step"() {
        when:
        controller.saveAssociation(new ContestApplicationAssociationCommand(associationName: "Assoc", associationImage: "1"))

        then:
        1 * controller.contestApplicationService.find(_, 5L) >> rsdto
        1 * controller.contestApplicationService.map(rsdto) >> new ContestApplicationRDTO()
        1 * controller.contestApplicationService.save(*_) >> rsdto
        1 * controller.contactService.getFiles(WebConstants.FAKE_LANDING_ALIAS_USER, _) >> ["http://s3/file.pdf"]
        flash.error == null
        redirectArgs.mapping == "contestApplicationEditContent"
    }

    void "sending a collaborator application whose association step was never completed (no association image) is blocked"() {
        given:
        rsdto.associationImage = null // provisional data only
        def command = new ContestApplicationAuthorizationsCommand(authorizedAgent: true, acceptedLegalBases: true, imageRights: true,
                sendType: CampaignContentCommand.CAMPAIGN_SEND_TYPE_SEND)

        when:
        controller.saveAuthorizations(command)

        then:
        1 * controller.contestApplicationService.find(_, 5L) >> rsdto
        0 * controller.contestApplicationService.save(*_)
        0 * controller.contactService.getFiles(WebConstants.FAKE_LANDING_ALIAS_USER, _)
        flash.error == "contestApplication.association.sendMissingLogo"
        redirectArgs.mapping == "contestApplicationEditAssociation"
    }

    void "sending a collaborator application with the files service down is blocked with a retry message"() {
        given:
        rsdto.associationImage = "http://img/a.png"
        def command = new ContestApplicationAuthorizationsCommand(authorizedAgent: true, acceptedLegalBases: true, imageRights: true,
                sendType: CampaignContentCommand.CAMPAIGN_SEND_TYPE_SEND)

        when:
        controller.saveAuthorizations(command)

        then:
        1 * controller.contestApplicationService.find(_, 5L) >> rsdto
        1 * controller.contactService.getFiles(WebConstants.FAKE_LANDING_ALIAS_USER, _) >> { throw new RuntimeException("API down") }
        0 * controller.contestApplicationService.save(*_)
        flash.error == "contestApplication.association.files.sendCheckFailed"
        redirectArgs.mapping == "contestApplicationEditAuthorizations"
    }

    void "sending a collaborator application without any file is blocked"() {
        given:
        rsdto.associationImage = "http://img/a.png"
        def command = new ContestApplicationAuthorizationsCommand(authorizedAgent: true, acceptedLegalBases: true, imageRights: true,
                sendType: CampaignContentCommand.CAMPAIGN_SEND_TYPE_SEND)

        when:
        controller.saveAuthorizations(command)

        then:
        1 * controller.contestApplicationService.find(_, 5L) >> rsdto
        1 * controller.contactService.getFiles(WebConstants.FAKE_LANDING_ALIAS_USER, _) >> []
        0 * controller.contestApplicationService.save(*_)
        flash.error == "contestApplication.association.files.sendMissing"
        redirectArgs.mapping == "contestApplicationEditAssociation"
    }

    // ---- The title is only asked in the Content step ----

    void "creating a draft without title uses 'Candidatura sin publicar en <contest>' as provisional name (and association name) and leaves the title empty"() {
        given:
        params.campaignId = "9"
        messageSource.addMessage("contestApplication.provisionalName", request.locale, "Candidatura sin publicar en {0}")

        when:
        ContestApplicationRDTO rdto = controller.convertCommandContentToRDTO(
                new ContestApplicationScopeCommand(cause: "c", numBeneficiaries: 3, hasCollaborator: true), user, null, controller.contestApplicationService)

        then:
        1 * controller.contestApplicationService.find(_, null) >> null
        1 * controller.contestApplicationService.map(null) >> new ContestApplicationRDTO()
        1 * controller.contestService.find(_, 9L) >> new ContestRSDTO(id: 9L, title: "Concurso Solidario")
        rdto.name == "Candidatura sin publicar en Concurso Solidario"
        !rdto.title
        rdto.associationName == rdto.name
        rdto.contestId == 9L
    }

    void "the scope command does not ask for a title any more"() {
        expect:
        !ContestApplicationScopeCommand.declaredFields.any { it.name == "name" }
    }

    void "re-editing the scope step keeps the title, the name and the association name already saved"() {
        given:
        params.contestId = "9"
        rsdto.name = "Real name"
        rsdto.title = "Real title"
        rsdto.associationName = "Real association"
        rsdto.associationImage = "http://img/a.png"
        ContestApplicationRDTO sentToBack = null
        controller.kuorumUserService = Stub(kuorum.users.KuorumUserService) { findBasicUserRSDTO(_) >> new BasicDataKuorumUserRSDTO(id: new org.bson.types.ObjectId()) }

        when:
        controller.saveScope(new ContestApplicationScopeCommand(cause: "c", numBeneficiaries: 7,
                activityType: org.kuorum.rest.model.communication.contest.ContestApplicationActivityTypeDTO.values().first(),
                focusType: org.kuorum.rest.model.communication.contest.ContestApplicationFocusTypeDTO.values().first(),
                numBenefitedCaregivers: 1, numBenefitedPacients: 1))

        then:
        1 * controller.contestApplicationService.find(_, 5L) >> rsdto
        1 * controller.contestService.find(_, 9L) >> new ContestRSDTO(id: 9L, title: "Contest")
        1 * controller.contestApplicationService.map(rsdto) >> new ContestApplicationRDTO(name: "Real name", title: "Real title", associationName: "Real association")
        1 * controller.contestApplicationService.save(_, { ContestApplicationRDTO r ->
            r.name == "Real name" && r.title == "Real title" && r.associationName == "Real association" && r.numBeneficiaries == 7
        }, 5L) >> rsdto
    }

    void "sending an application whose Content step was never saved (no body or image) is blocked and sent back to Content"() {
        given:
        rsdto.body = null
        def command = new ContestApplicationAuthorizationsCommand(authorizedAgent: true, acceptedLegalBases: true, imageRights: true,
                sendType: CampaignContentCommand.CAMPAIGN_SEND_TYPE_SEND)

        when:
        controller.saveAuthorizations(command)

        then:
        1 * controller.contestApplicationService.find(_, 5L) >> rsdto
        0 * controller.contestApplicationService.save(*_)
        0 * controller.contactService.getFiles(*_)
        flash.error == "contestApplication.content.sendMissing"
        redirectArgs.mapping == "contestApplicationEditContent"
    }

    void "sending an application whose Content step has no title is blocked and sent back to Content"() {
        given:
        rsdto.title = null
        def command = new ContestApplicationAuthorizationsCommand(authorizedAgent: true, acceptedLegalBases: true, imageRights: true,
                sendType: CampaignContentCommand.CAMPAIGN_SEND_TYPE_SEND)

        when:
        controller.saveAuthorizations(command)

        then:
        1 * controller.contestApplicationService.find(_, 5L) >> rsdto
        0 * controller.contestApplicationService.save(*_)
        flash.error == "contestApplication.content.sendMissing"
        redirectArgs.mapping == "contestApplicationEditContent"
    }

    // ---- The title replaces the provisional name when the application is published ----

    void "publishing copies the title into the name"() {
        given:
        rsdto.associationImage = "http://img/a.png"
        def command = new ContestApplicationAuthorizationsCommand(authorizedAgent: true, acceptedLegalBases: true, imageRights: true,
                sendType: CampaignContentCommand.CAMPAIGN_SEND_TYPE_SEND)

        when:
        controller.saveAuthorizations(command)

        then:
        2 * controller.contestApplicationService.find(_, 5L) >> rsdto
        1 * controller.contactService.getFiles(WebConstants.FAKE_LANDING_ALIAS_USER, _) >> ["http://s3/file.pdf"]
        1 * controller.contestApplicationService.map(rsdto) >> new ContestApplicationRDTO(name: "Candidatura sin publicar en X", title: "Real title")
        1 * controller.contestApplicationService.save(_, { ContestApplicationRDTO r -> r.name == "Real title" && r.title == "Real title" }, 5L) >> rsdto
    }

    void "saving the authorizations as draft keeps the provisional name"() {
        given:
        def command = new ContestApplicationAuthorizationsCommand(authorizedAgent: true, acceptedLegalBases: true, imageRights: true,
                sendType: CampaignContentCommand.CAMPAIGN_SEND_TYPE_DRAFT)

        when:
        controller.saveAuthorizations(command)

        then:
        1 * controller.contestApplicationService.find(_, 5L) >> rsdto
        1 * controller.contestApplicationService.map(rsdto) >> new ContestApplicationRDTO(name: "Candidatura sin publicar en X", title: "Real title")
        0 * controller.contactService.getFiles(*_)
        1 * controller.contestApplicationService.save(_, { ContestApplicationRDTO r -> r.name == "Candidatura sin publicar en X" && r.title == "Real title" }, 5L) >> rsdto
    }
}
