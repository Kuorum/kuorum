package kuorum.web.commands.profile

import grails.validation.Validateable
import kuorum.Region
import kuorum.core.model.Gender
import kuorum.core.model.VoteType
import kuorum.users.PersonalData
import org.bson.types.ObjectId
import org.codehaus.groovy.grails.web.context.ServletContextHolder
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.grails.databinding.BindUsing

/**
 * Created by iduetxe on 28/02/15.
 */
@Validateable
class BasicPersonalDataCommand {
//TODO: Copiadas muchas cosas de EditUserProfileCommand - Mirar como compartir este codigo entre ambos command
    @BindUsing({obj, source ->
        EditUserProfileCommand.bindingPostalCode(obj, source)
        //Returns gender because it assigns return value to gender. WHY??
        source['gender']
    })
    Gender gender
    String postalCode
    Region country
    Region province
    Integer year

    VoteType voteType //voteType null means that the user has been clicked on "Create post" instead of voting


    static constraints = {
        gender nullable: false
        country nullable: false
        province nullable:true
        postalCode nullable: false, maxSize: 5, matches:"[0-9]+", validator: {val, command ->
            if (command.postalCode && !command.province){
                return "notExists"
            }
        }
        year nullable: false, min:1900, max:(Calendar.getInstance().get(Calendar.YEAR) - 18)
        voteType nullable:true
    }
}
