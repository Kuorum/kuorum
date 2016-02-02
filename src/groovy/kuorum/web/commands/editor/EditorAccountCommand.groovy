package kuorum.web.commands.editor

import grails.validation.Validateable
import kuorum.core.model.UserType
import kuorum.users.KuorumUser
import kuorum.web.commands.profile.AccountDetailsCommand

@Validateable
class EditorAccountCommand extends AccountDetailsCommand{
    public EditorAccountCommand(){};
    public EditorAccountCommand(KuorumUser user, Boolean emailAccountActive){
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
        alias nullable: true, validator: {val, obj ->
            if (val && obj.user && val != obj.user.alias && KuorumUser.findByAlias(val)){
                return "unique"
            }
            if (!val && obj.active){ // On admin section the active is the future value of enabled, not of the user one
                return "nullable"
            }
        }
    }
}
