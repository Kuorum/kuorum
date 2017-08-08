package kuorum.web.commands.payment.massMailing

import grails.validation.Validateable
import org.grails.databinding.BindUsing
import org.grails.databinding.DataBindingSource
import org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO

/**
 * Created by toni on 21/4/17.
 */

@Validateable
class MassMailingSettingsCommand {

    Long filterId;
    Boolean filterEdited;
    String campaignName;

    @BindUsing({ obj, source ->return MassMailingCommand.bindTags(source)})
    Map<TrackingMailStatusRSDTO, List<String>> tags =[:]

    static constraints = {
        campaignName nullable: false
        filterId nullable: false
        filterEdited nullable: true
    }
}
