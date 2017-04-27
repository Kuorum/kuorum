package kuorum.web.commands.payment.massMailing

import grails.validation.Validateable
import org.grails.databinding.BindingFormat

/**
 * Created by toni on 25/4/17.
 */
@Validateable
class MassMailingContentTemplateCommand {
    String subject
    String text
    String headerPictureId

    @BindingFormat('dd/MM/yyyy HH:mm')
    Date scheduled
    String sendType

    static constraints = {
        subject nullable: true
        text nullable: true, validator: { val, obj ->
            if (obj.sendType!= "DRAFT" && !val){
                return "kuorum.web.commands.payment.massMailing.MassMailingCommand.text.nullable"
            }
        }
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
    }
}
