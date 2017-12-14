package kuorum.web.commands.payment.event

import grails.validation.Validateable
import kuorum.web.constants.WebConstants
import org.grails.databinding.BindingFormat

@Validateable
class EventCommand {

    @BindingFormat(WebConstants.WEB_FORMAT_DATE)
    Date eventDate;
    @BindingFormat(".")
    Double latitude;
    @BindingFormat(".")
    Double longitude;
    Integer zoom;
    String localName;
    String address;
    Integer capacity

    static constraints = {
        eventDate nullable: false
        latitude nullable: false
        longitude nullable: false
        zoom nullable: false
        localName nullable: false
        address nullable: false
        capacity nullable: true
    }

}
