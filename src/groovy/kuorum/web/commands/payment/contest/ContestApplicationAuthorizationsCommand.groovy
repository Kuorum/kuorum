package kuorum.web.commands.payment.contest

import grails.validation.Validateable
import kuorum.web.commands.payment.CampaignContentCommand
import kuorum.web.constants.WebConstants
import org.grails.databinding.BindingFormat
import org.kuorum.rest.model.communication.contest.ContestApplicationActivityTypeDTO
import org.kuorum.rest.model.communication.contest.ContestApplicationFocusTypeDTO

@Validateable
class ContestApplicationAuthorizationsCommand {
    Boolean authorizedAgent
    Boolean acceptedLegalBases
    Boolean imageRights

    @BindingFormat(WebConstants.WEB_FORMAT_DATE)
    Date publishOn
    String sendType

    static constraints = {
        importFrom CampaignContentCommand, include: ["publishOn", "sendType"]
        authorizedAgent nullable: false
        acceptedLegalBases nullable: false
        imageRights nullable: false
    }

}
