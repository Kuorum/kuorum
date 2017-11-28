package kuorum.web.commands.payment.debate

import grails.validation.Validateable
import kuorum.web.commands.payment.massMailing.MassMailingCommand
import org.grails.databinding.BindUsing
import org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO

/**
 * Created by toni on 27/4/17.
 */

@Validateable
class DebateSettingsCommand {

    // Filter
    Long filterId
    Boolean filterEdited

    String campaignName

    @BindUsing({ obj, source ->return MassMailingCommand.bindTags(source)})
    Map<TrackingMailStatusRSDTO, List<String>> tags =[:]

    @BindUsing({obj,  org.grails.databinding.DataBindingSource source ->
        String normalizedCauses = source['causes'].replaceAll(';', ',')
        normalizedCauses.split(',').findAll({it}).collect{it.decodeHashtag().trim()}.unique { a, b -> a <=> b }
    })
    Set<String> causes;

    static constraints = {
        filterId nullable: true
        filterEdited nullable: true
        campaignName nullable: false
        tags nullable: true
        causes nullable: true
    }
}
