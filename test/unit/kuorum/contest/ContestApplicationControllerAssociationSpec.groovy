package kuorum.contest

import grails.test.mixin.TestFor
import kuorum.KuorumFile
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.register.KuorumUserSession
import kuorum.web.commands.payment.contest.ContestApplicationAssociationCommand
import kuorum.web.commands.payment.contest.ContestApplicationAuthorizationsCommand
import kuorum.web.commands.payment.CampaignContentCommand
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
import spock.lang.Specification
import spock.lang.Unroll

@TestFor(ContestApplicationController)
class ContestApplicationControllerAssociationSpec extends Specification {

    KuorumUserSession user = Stub(KuorumUserSession) { getId() >> new org.bson.types.ObjectId() }
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

        rsdto = new ContestApplicationRSDTO(id: 5L, contestApplicationType: ContestApplicationTypeDTO.COLLABORATOR,
                contest: new org.kuorum.rest.model.communication.contest.EmbeddedContestRSDTO(id: 9L), campaignStatusRSDTO: CampaignStatusRSDTO.DRAFT)
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
        1 * controller.campaignService.getFiles(_) >> { throw new RuntimeException("API down") }
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
        0 * controller.campaignService.getFiles(_)
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
        1 * controller.campaignService.getFiles(_) >> []
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
        1 * controller.campaignService.getFiles(_) >> ["http://s3/file.pdf"]
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
        0 * controller.campaignService.getFiles(_)
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
        1 * controller.campaignService.getFiles(_) >> { throw new RuntimeException("API down") }
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
        1 * controller.campaignService.getFiles(_) >> []
        0 * controller.contestApplicationService.save(*_)
        flash.error == "contestApplication.association.files.sendMissing"
        redirectArgs.mapping == "contestApplicationEditAssociation"
    }
}
