package kuorum.web.commands.customRegister

import grails.validation.Validateable
import kuorum.core.model.AvailableLanguage

/**
 * Commands for the steps after the first login
 * This is for second step
 */
@Validateable
class Step2Command {

    String alias
    String password;
    AvailableLanguage language;
    String phonePrefix;
    String phone;
    static constraints = {
        alias nullable:false, unique:true, maxSize: 15
        password nullable:false;
        language nullable:false;
        phonePrefix nullable:true
        phone nullable:true
    }
}
