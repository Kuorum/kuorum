package kuorum.web.commands.payment.contact.promotionalCode

import grails.validation.Validateable
import payment.contact.PromotionalCodeService

/**
 * Created by iduetxe on 1/09/16.
 */
@Validateable
class PromotionalCodeCommand {

    PromotionalCodeService promotionalCodeService;

    String promotionalCode;

    static constraints = {
        promotionalCode nullable: true, validator: {val, obj ->
            if (!obj.promotionalCodeService.checkPromotionalCode(val)){
                return "invalid"
            }
        }
    }
}
