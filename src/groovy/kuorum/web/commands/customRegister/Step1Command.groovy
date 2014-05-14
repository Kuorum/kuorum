package kuorum.web.commands.customRegister

import grails.validation.Validateable
import kuorum.Region
import kuorum.core.model.Gender
import kuorum.web.commands.profile.BirthdayCommad
import kuorum.web.commands.profile.EditUserProfileCommand
import org.grails.databinding.BindUsing

/**
 * Created by iduetxe on 17/03/14.
 */
@Validateable
class Step1Command extends BirthdayCommad{

    @BindUsing({obj, source ->
        EditUserProfileCommand.bindingPostalCode(obj, source)
        //Returns gender because it assigns return value to gender. WHY??
        source['gender']
    })


    Gender gender
    String postalCode
    String name

    Region country
    Region province

    static constraints = {
        importFrom EditUserProfileCommand, include:["gender", "name", "postalCode", "country", "province", "day", "month", "year"]
    }
}
