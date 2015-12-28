package kuorum.web.commands.profile.politician

import grails.validation.Validateable
import kuorum.users.KuorumUser

/**
 * Created by iduetxe on 28/12/15.
 */
@Validateable
class PoliticianCausesCommand {
    KuorumUser politician
    List<String> causes
    static constraints = {
        politician nullable: false;
    }

    public PoliticianCausesCommand(){}
    public PoliticianCausesCommand(KuorumUser politician){
        this.politician = politician
        this.causes = politician.tags?:[]
    }
}
