package kuorum.web.commands.payment.massMailing

import grails.validation.Validateable
import org.grails.databinding.BindUsing
import org.grails.databinding.BindingFormat
import org.grails.databinding.DataBindingSource
import org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO

@Validateable
class DebateCommand {

    // Filter
    Long filterId
    Boolean filterEdited

    String title
    String body

    String headerPictureId
    String videoPost

    @BindUsing({ obj, DataBindingSource source ->
        source.map.tags?.split(',')?.collect{it.trim()}?.findAll{it}?:[]
    })
    List<String> tags
    List<TrackingMailStatusRSDTO> eventsWithTag

    @BindingFormat('dd/MM/yyyy HH:mm')
    Date publishOn

    static constraints = {
        title nullable: false
        body nullable: false
        filterId nullable: true
        filterEdited nullable: true
        headerPictureId nullable: true
        videoPost nullable: true
        publishOn nullable: true, validator: { val, obj ->
            /*if (val && val < new Date()) {
                return "kuorum.web.commands.payment.massMailing.DebateCommand.scheduled.min.error"
            }*/
        }
        eventsWithTag nullable: true
    }

}
