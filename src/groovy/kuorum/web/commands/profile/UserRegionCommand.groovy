package kuorum.web.commands.profile

import kuorum.Region
import kuorum.core.model.Gender
import org.codehaus.groovy.grails.validation.Validateable
import org.grails.databinding.BindUsing

/**
 * Created by iduetxe on 3/05/15.
 */
@Validateable
class UserRegionCommand {
    @BindUsing({obj, source ->
        EditUserProfileCommand.bindingPostalCode(obj, source)
    })
    String postalCode
    Region country
    Region province
    static constraints = {
        importFrom EditUserProfileCommand, include: ["postalCode","country", "province"]
    }
}
