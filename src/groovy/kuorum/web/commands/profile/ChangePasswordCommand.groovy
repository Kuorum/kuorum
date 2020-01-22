package kuorum.web.commands.profile

import grails.validation.Validateable
import springSecurity.RegisterController

/**
 * Created by iduetxe on 13/02/14.
 */
@Validateable
class ChangePasswordCommand{

    String originalPassword
    String password
    String password2

    public String getUsername(){ "123456" }// RegisterController.passwordValidator uses username
    static constraints = {
        originalPassword nullable: false, blank: false
        password blank: false, validator: RegisterController.passwordValidator
        password2 validator: RegisterController.password2Validator
    }
}
