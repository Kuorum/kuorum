package kuorum.web.commands.editor

import grails.validation.Validateable
import kuorum.users.KuorumUser
import kuorum.web.commands.profile.AccountDetailsCommand

@Validateable
class EditorAccountCommand extends AccountDetailsCommand{
    EditorAccountCommand(){}

    EditorAccountCommand(KuorumUser user){
        super(user)
    }
    static constraints = {
        password nullable: true, validator: {val, obj -> true} //Chapu para usar herencia y luego poder ignorar el campo
    }
}
