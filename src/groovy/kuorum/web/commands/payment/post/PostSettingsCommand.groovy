package kuorum.web.commands.payment.post

import grails.validation.Validateable
import kuorum.web.commands.payment.massMailing.MassMailingCommand
import kuorum.web.commands.payment.massMailing.MassMailingSettingsCommand
import org.grails.databinding.BindUsing
import org.grails.databinding.DataBindingSource
import org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO


/**
 * Created by toni on 26/4/17.
 */

@Validateable
class PostSettingsCommand {

    Long filterId;
    Boolean filterEdited;
    String campaignName;

    @BindUsing({ obj, source ->return MassMailingCommand.bindTags(source)})
    Map<TrackingMailStatusRSDTO, List<String>> tags =[:]

    @BindUsing({obj,  org.grails.databinding.DataBindingSource source ->
        String normalizedCauses = source['causes'].replaceAll(';', ',')
        normalizedCauses.split(',').findAll({it}).collect{it.decodeHashtag().trim()}.unique { a, b -> a <=> b }
    })
    Set<String> causes;

    static constraints = {
        campaignName nullable: false
        filterId nullable: false
        filterEdited nullable: true
        causes nullable:true
    }
}
