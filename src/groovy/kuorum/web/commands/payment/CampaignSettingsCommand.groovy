package kuorum.web.commands.payment

import grails.validation.Validateable
import kuorum.web.commands.payment.massMailing.MassMailingCommand
import kuorum.web.constants.WebConstants
import org.grails.databinding.BindUsing
import org.grails.databinding.BindingFormat
import org.kuorum.rest.model.communication.CampaignValidationTypeRDTO
import org.kuorum.rest.model.communication.survey.SurveyVoteTypeDTO
import org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO

@Validateable
class CampaignSettingsCommand {

    // Filter
    Long filterId
    Boolean filterEdited
    Boolean debatable
    Boolean eventAttached

    String campaignName

    @BindUsing({ obj, source ->return MassMailingCommand.bindTags(source)})
    Map<TrackingMailStatusRSDTO, List<String>> tags =[:]

    @BindUsing({obj,  org.grails.databinding.DataBindingSource source ->
        String normalizedCauses = source['causes'].replaceAll(';', ',')
        normalizedCauses.split(',').findAll({it}).collect{it.decodeHashtag().trim()}.unique { a, b -> a <=> b }
    })
    Set<String> causes;

    CampaignValidationTypeRDTO validationType;
    Boolean hideResultsFlag;
    SurveyVoteTypeDTO voteType = SurveyVoteTypeDTO.ANONYMOUS;
    Boolean signVotes;
    Boolean groupValidation = false;
    Boolean newsletterCommunication = false;

    @BindingFormat(WebConstants.WEB_FORMAT_DATE)
    Date endDate

    @BindingFormat(WebConstants.WEB_FORMAT_DATE)
    Date startDate

    static constraints = {
        filterId nullable: true
        filterEdited nullable: true
        campaignName nullable: false
        tags nullable: true
        causes nullable: true
        debatable nullable: true
        eventAttached nullable: true
        validationType nullable: true
        hideResultsFlag nullable: true;
        groupValidation nullable: true;
        voteType nullable: false;
        signVotes nullable: true;
        newsletterCommunication nullable: true;
        endDate nullable: true
        startDate nullable: true
    }
}
