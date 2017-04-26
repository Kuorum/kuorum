package kuorum.web.commands.payment.massMailing

import grails.validation.Validateable
import kuorum.web.constants.WebConstants
import org.grails.databinding.BindUsing
import org.grails.databinding.BindingFormat
import org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO

@Validateable
class DebateCommand {

    // Filter
    Long filterId
    Boolean filterEdited

    String title
    String body

    String campaignName

    String fileType
    String headerPictureId
    String videoPost


    @BindUsing({ obj, source ->return MassMailingCommand.bindTags(source)})
    Map<TrackingMailStatusRSDTO, List<String>> tags =[:]

    //ÑAPA FOR FAST MAPPING TAGS DEPENDING ON MAIL EVENT
//    def propertyMissing(String name) {
////        def tagValue = TrackingMailStatusRSDTO.valueOf((name =~ /\[(.*)\]/)[0][1])
//        def tagValue = TrackingMailStatusRSDTO.valueOf(name.split("\\.")[1])
//        tags?.get(tagValue)?:null
//    }


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
            /*if (val && val < new Date()) {
                return "kuorum.web.commands.payment.massMailing.DebateCommand.scheduled.min.error"
            }*/
        }
        tags nullable: true

        campaignName nullable: false
    }

}
