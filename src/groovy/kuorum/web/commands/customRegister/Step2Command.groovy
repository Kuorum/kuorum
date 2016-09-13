package kuorum.web.commands.customRegister

import grails.validation.Validateable
import kuorum.core.model.AvailableLanguage
import kuorum.core.model.UserType
import kuorum.users.KuorumUser
import kuorum.web.commands.profile.AccountDetailsCommand

/**
 * Commands for the steps after the first login
 * This is for second step
 */
@Validateable
class Step2Command {

    public Step2Command(){}
    public Step2Command(KuorumUser user){
        this.user = user
        this.alias = user.alias
        this.language = user.language
        this.phonePrefix = user.personalData?.phonePrefix
        this.phone = user.personalData?.telephone
//        this.userType=user.userType
    }

    KuorumUser user
    String alias
    String password;
    AvailableLanguage language;
    String phonePrefix;
    String phone;
    UserType userType;
    static constraints = {
        importFrom AccountDetailsCommand, include:["alias", "phonePrefix", "phone", "language", "user"]
        password nullable:false;
        userType nullable:false;
    }
}
