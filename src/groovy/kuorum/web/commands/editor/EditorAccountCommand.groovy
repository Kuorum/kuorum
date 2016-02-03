package kuorum.web.commands.editor

import grails.validation.Validateable
import kuorum.core.model.UserType
import kuorum.users.KuorumUser
import kuorum.web.commands.profile.AccountDetailsCommand

@Validateable
class EditorAccountCommand extends AccountDetailsCommand{
    public EditorAccountCommand(){};
    public EditorAccountCommand(KuorumUser user){
        super(user)
    }
    static constraints = {
        password nullable: true //Chapu para usar herencia y luego poder ignorar el campo
    }
}
