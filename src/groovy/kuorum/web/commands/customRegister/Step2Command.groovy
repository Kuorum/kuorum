package kuorum.web.commands.customRegister

import grails.validation.Validateable
import kuorum.core.model.AvailableLanguage
import kuorum.core.model.UserType
import kuorum.users.KuorumUser
import kuorum.web.commands.profile.AccountDetailsCommand
import org.grails.databinding.BindUsing

/**
 * Commands for the steps after the first login
 * This is for second step
 */
@Validateable
class Step2Command {

    public Step2Command(){}
    public Step2Command(KuorumUser user, String recommendedAlias){
        this.user = user
        this.alias = user.alias?:recommendedAlias
        this.language = user.language
        this.phonePrefix = user.personalData?.phonePrefix
        this.phone = user.personalData?.telephone
        this.name = user.name
        this.surname = user.surname
//        this.userType=user.userType
    }

    KuorumUser user
    @BindUsing({obj, org.grails.databinding.DataBindingSource source->
        AccountDetailsCommand.normalizeAlias(source["alias"])
    })
    String alias
    String name
    String surname
    String password;
    AvailableLanguage language;
    String phonePrefix;
    String phone;
    UserType userType;
    static constraints = {
        importFrom AccountDetailsCommand, include:["alias", "name","surname","phonePrefix", "phone", "language", "user"]
        password nullable:false;
        userType nullable:false;
    }
}
