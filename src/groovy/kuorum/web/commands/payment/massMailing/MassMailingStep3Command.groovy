package kuorum.web.commands.payment.massMailing

import grails.validation.Validateable
import org.grails.databinding.BindingFormat

/**
 * Created by toni on 21/4/17.
 */
@Validateable
class MassMailingStep3Command {
    String subject
    String text

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
        scheduled nullable: true, validator: { val, obj ->
            if (val && obj.sendType== "SCHEDULED" && val < new Date()){
                return "kuorum.web.commands.payment.massMailing.MassMailingCommand.scheduled.min.error"
            }
        }
        sendType nullable: false, inList:["DRAFT", "SCHEDULED", "SEND", "SEND_TEST"]

    }
}
