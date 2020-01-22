package kuorum.web.commands.profile

import grails.validation.Validateable
import kuorum.core.model.Gender
import kuorum.core.model.UserType
import kuorum.users.KuorumUser
import org.grails.databinding.BindingFormat

/**
 * Created by iduetxe on 13/02/14.
 */
@Validateable
class EditUserProfileCommand{

    public EditUserProfileCommand(){}
    public EditUserProfileCommand(KuorumUser user){
        this.gender = user.personalData?.gender
        this.bio = user.bio
        if (user.userType == UserType.PERSON){
            this.birthday= user.personalData?.birthday
        }
    }


    String bio
    Gender gender

    //Citizen
    @BindingFormat("dd/MM/yyyy")
    Date birthday

    static constraints = {
        gender nullable: true
        birthday nullable:true
        bio nullable: true, maxSize: 1000
    }
}
