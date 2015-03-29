package kuorum.web.commands.profile

import grails.validation.Validateable
import kuorum.Region
import kuorum.core.model.*
import org.bson.types.ObjectId
import org.codehaus.groovy.grails.web.context.ServletContextHolder
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.grails.databinding.BindUsing

/**
 * Created by iduetxe on 13/02/14.
 */
@Validateable
class EditCommissionsProfileCommand {

    List<CommissionType> commissions = []

    static constraints = {}

}
