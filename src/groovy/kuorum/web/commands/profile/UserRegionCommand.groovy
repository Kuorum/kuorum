package kuorum.web.commands.profile

import kuorum.Region
import kuorum.core.model.Gender
import kuorum.web.binder.RegionBinder
import org.codehaus.groovy.grails.validation.Validateable
import org.grails.databinding.BindUsing

/**
 * Created by iduetxe on 3/05/15.
 */
@Validateable
class UserRegionCommand {
    @BindUsing({obj,  org.grails.databinding.DataBindingSource source ->
        RegionBinder.bindRegion(obj, "province", source)
    })
    Region province
    static constraints = {
        province nullable: false
    }
}
