package kuorum.web.commands.admin

import grails.validation.Validateable
import kuorum.core.model.UserType
import kuorum.users.KuorumUser
import kuorum.web.commands.profile.AccountDetailsCommand

@Validateable
class KuorumAccountCommand extends AccountDetailsCommand{
    public KuorumAccountCommand(){};
    public KuorumAccountCommand(KuorumUser user, Boolean emailAccountActive){
        super(user)
        this.active = user.enabled
        this.userType = user.userType
        this.emailAccountActive = emailAccountActive
    }
    Boolean emailAccountActive;
    Boolean active
    UserType userType
    static constraints = {
        userType nullable: false
        password nullable: true //Chapu para usar herencia y luego poder ignorar el campo
    }
}
