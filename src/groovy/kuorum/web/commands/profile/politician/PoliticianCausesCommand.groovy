package kuorum.web.commands.profile.politician

import grails.validation.Validateable
import kuorum.users.KuorumUser
import org.grails.databinding.BindUsing

/**
 * Created by iduetxe on 28/12/15.
 */
@Validateable
class PoliticianCausesCommand {
    KuorumUser politician
    @BindUsing({obj,  org.grails.databinding.DataBindingSource source ->
        String normalizedCauses = source['causes'].replaceAll(';', ' ').replaceAll(',', ' ')
        normalizedCauses.split(' ').findAll({it}).collect{it.encodeAsHashtag()}.unique { a, b -> a <=> b }
    })
    List<String> causes
    String prueba
    static constraints = {
        politician nullable: false;
        prueba nullable: true
    }

    public PoliticianCausesCommand(){}
    public PoliticianCausesCommand(KuorumUser politician){
        this.politician = politician
        this.causes = politician.tags?:[]
    }
}
