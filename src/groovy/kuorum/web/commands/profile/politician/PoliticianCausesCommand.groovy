package kuorum.web.commands.profile.politician

import grails.validation.Validateable
import org.grails.databinding.BindUsing

/**
 * Created by iduetxe on 28/12/15.
 */
@Validateable
class PoliticianCausesCommand {
    @BindUsing({obj,  org.grails.databinding.DataBindingSource source ->
        String normalizedCauses = source['causes'].replaceAll(';', ',')
        normalizedCauses.split(',').findAll({it}).collect{it.decodeHashtag().trim()}.unique { a, b -> a <=> b }
    })
    List<String> causes
    static constraints = {
        causes nullable: true
    }

    PoliticianCausesCommand(){}

    PoliticianCausesCommand(List<String> causes){
        this.causes = causes?:[]
    }
}
