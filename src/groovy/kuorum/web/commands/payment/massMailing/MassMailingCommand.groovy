package kuorum.web.commands.payment.massMailing

import grails.validation.Validateable
import org.grails.databinding.BindUsing
import org.grails.databinding.BindingFormat
import org.grails.databinding.DataBindingSource
import org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO

/**
 * Created by iduetxe on 16/08/16.
 */
@Validateable
class MassMailingCommand {

    Long filterId;
    Boolean filterEdited;
    String subject;
    String text;
    String headerPictureId;

    String campaignName

    String simpleTemplate
    String uploadHTML
    String plainText

    @BindUsing({ obj, source ->return MassMailingCommand.bindTags(source)})
    Map<TrackingMailStatusRSDTO, List<String>> tags =[:]

    @BindingFormat('dd/MM/yyyy HH:mm')
    Date scheduled
    String sendType

//    //ÑAPA FOR FAST MAPPING TAGS DEPENDING ON MAIL EVENT
//    def propertyMissing(String name) {
////        def tagValue = TrackingMailStatusRSDTO.valueOf((name =~ /\[(.*)\]/)[0][1])
//        def tagValue = TrackingMailStatusRSDTO.valueOf(name.split("\\.")[1])
//        tags?.get(tagValue)?:null
//    }


    static constraints = {
        subject nullable: false
        text nullable: true, validator: { val, obj ->
            if (obj.sendType!= "DRAFT" && !val){
                return "kuorum.web.commands.payment.massMailing.MassMailingCommand.text.nullable"
            }
        }
        filterId nullable: false
        filterEdited nullable: true
        headerPictureId nullable: true, validator: {val, obj ->
            if (obj.sendType!= "DRAFT" && !val){
                return "kuorum.web.commands.payment.massMailing.MassMailingCommand.headerPictureId.nullable"
            }
        }
        scheduled nullable: true, validator: { val, obj ->
            if (val && obj.sendType== "SCHEDULED" && val < new Date()){
                return "kuorum.web.commands.payment.massMailing.MassMailingCommand.scheduled.min.error"
            }
        }
        sendType nullable: false, inList:["DRAFT", "SCHEDULED", "SEND", "SEND_TEST"]
        tags nullable: true

        campaignName nullable: false

        simpleTemplate nullable: true
        uploadHTML nullable: true
        plainText nullable: true
    }

    public static Map<TrackingMailStatusRSDTO, List<String>> bindTags(DataBindingSource source){
        return source["tags"]?.collectEntries{
            it -> [TrackingMailStatusRSDTO.valueOf(it.key),it.value.split(",")]
        }
    }
}
