package kuorum.web.commands.payment.post

import grails.validation.Validateable
import kuorum.web.commands.payment.massMailing.MassMailingCommand
import kuorum.web.constants.WebConstants
import org.grails.databinding.BindUsing
import org.grails.databinding.BindingFormat
import org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO

/**
 * Created by iduetxe on 13/02/14.
 */
@Validateable
class PostCommand {
    // Filter
    Long filterId
    Boolean filterEdited

    String title
    String body

    String fileType
    String headerPictureId
    String videoPost

    @BindUsing({ obj, source ->return MassMailingCommand.bindTags(source)})
    Map<TrackingMailStatusRSDTO, List<String>> tags =[:]

    @BindingFormat(WebConstants.WEB_FORMAT_DATE)
    Date publishOn

    static constraints = {
        title nullable: false
        body nullable: false
        filterId nullable: true
        filterEdited nullable: true
        fileType nullable: true
        headerPictureId nullable: true
        videoPost nullable: true
        publishOn nullable: true, validator: { val, obj ->
        }
        tags nullable: true
    }
}
