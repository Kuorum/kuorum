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
        String normalizedCauses = source['causes'].replaceAll(';', ',')
        normalizedCauses.split(',').findAll({it}).collect{it.decodeHashtag().trim()}.unique { a, b -> a <=> b }
    })
    List<String> causes
    static constraints = {
        politician nullable: false;
    }

    public PoliticianCausesCommand(){}
    public PoliticianCausesCommand(KuorumUser politician, List<String> causes){
        this.politician = politician
        this.causes = causes?:[]
    }
}
