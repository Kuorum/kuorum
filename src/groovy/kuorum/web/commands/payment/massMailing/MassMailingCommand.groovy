package kuorum.web.commands.payment.massMailing

import grails.validation.Validateable
import org.grails.databinding.BindUsing
import org.grails.databinding.BindingFormat
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

    @BindUsing({obj,  org.grails.databinding.DataBindingSource source ->
        source.map.tags?.split(",")?.collect{it.trim()}?.findAll{it}?:[]
    })
    List<String> tags;
    List<TrackingMailStatusRSDTO> eventsWithTag;

    @BindingFormat('dd/MM/yyyy HH:mm')
    Date scheduled
    String sendType

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
        eventsWithTag nullable: true
    }
}
