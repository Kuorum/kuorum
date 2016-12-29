package kuorum.web.commands.payment.contact

import grails.validation.Validateable
import org.grails.databinding.BindUsing

@Validateable
class BulkAddTagsContactsCommand extends BulkActionContactsCommand {

    @BindUsing({obj, source ->
        source.map.tags?.split(",")?.collect{it.trim()}?.findAll{it}?:[]
    })
    List<String> tags

    static constraints = {
        tags nullable: false, minSize: 1
    }

}
