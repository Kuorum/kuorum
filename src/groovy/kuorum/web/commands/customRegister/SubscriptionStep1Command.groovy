package kuorum.web.commands.customRegister

import grails.validation.Validateable
import kuorum.core.model.AvailableLanguage
import kuorum.core.model.OfferType
import kuorum.core.model.UserType
import kuorum.users.KuorumUser
import kuorum.web.commands.profile.AccountDetailsCommand
import springSecurity.KuorumRegisterCommand

/**
 * Created by iduetxe on 5/02/16.
 */
@Validateable
class SubscriptionStep1Command extends KuorumRegisterCommand{

    String alias
    AvailableLanguage language;
    String phonePrefix;
    String phone;
    UserType userType;

    OfferType offerType;
    Long kpeople

    static constraints = {
        importFrom AccountDetailsCommand, include:["name", "phonePrefix", "phone", "language", "user"]
        password nullable:false;

        offerType nullable:true;
        kpeople nullable:true
        email nullable: false, email:true, validator: {val, obj ->
            if (val && KuorumUser.findByEmail(val)){
                return "unique"
            }
        }
        alias nullable: false, validator: {val, obj ->
            if (val && KuorumUser.findByAlias(val)){
                return "unique"
            }
        }
    }


}
