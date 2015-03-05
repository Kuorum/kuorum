package kuorum.web.commands.profile

import grails.validation.Validateable
import kuorum.Region
import kuorum.core.model.*
import kuorum.users.PersonalData
import org.bson.types.ObjectId
import org.codehaus.groovy.grails.web.context.ServletContextHolder
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.grails.databinding.BindUsing

@Validateable
class PersonalDataCommand {
    Gender gender
    String phonePrefix
    String telephone
    String postalCode
    Region country
    Integer year


    static constraints = {
        importFrom PersonalData
    }
}
