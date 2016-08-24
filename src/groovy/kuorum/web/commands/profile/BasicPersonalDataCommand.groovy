package kuorum.web.commands.profile

import grails.validation.Validateable
import kuorum.Region
import kuorum.core.model.Gender
import kuorum.core.model.VoteType
import kuorum.users.PersonalData
import kuorum.web.binder.RegionBinder
import org.bson.types.ObjectId
import org.codehaus.groovy.grails.web.context.ServletContextHolder
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.grails.databinding.BindUsing

/**
 * Created by iduetxe on 28/02/15.
 */
@Validateable
class BasicPersonalDataCommand {
    @BindUsing({obj,  org.grails.databinding.DataBindingSource source ->
        RegionBinder.bindRegion(obj, "homeRegion", source)
    })
    Region homeRegion

    Gender gender
    Integer year

    VoteType voteType //voteType null means that the user has been clicked on "Create post" instead of voting


    static constraints = {
        homeRegion nullable:false
        gender nullable: false
        year nullable: true, min:1900, max:(Calendar.getInstance().get(Calendar.YEAR) - 18), validator: {val, command ->
            if (command.gender != Gender.ORGANIZATION && !val){
                return "nullable"
            }
        }
        voteType nullable:true
    }
}
