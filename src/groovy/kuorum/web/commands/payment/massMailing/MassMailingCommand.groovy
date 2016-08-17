package kuorum.web.commands.payment.massMailing

import grails.validation.Validateable

/**
 * Created by iduetxe on 16/08/16.
 */
@Validateable
class MassMailingCommand {

    Long filterId;
    String subject;
    String text;
    String headerPictureId;

    Date scheduled
    String sendType

    static constraints = {
        subject nullable: false
        text nullable: false
        filterId nullable: false
        headerPictureId nullable: false
        scheduled nullable: true
        sendType nullable: false, inList:["DRAFT", "SCHEDULED", "SEND"]
    }
}
