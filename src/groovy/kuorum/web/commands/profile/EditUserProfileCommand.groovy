package kuorum.web.commands.profile

import grails.validation.Validateable
import kuorum.core.model.*
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
        if (user.userType == UserType.ORGANIZATION){
            this.enterpriseSector = user.personalData?.enterpriseSector
        }else{
            this.position = user.professionalDetails?.position
            this.birthday= user.personalData?.birthday
            this.workingSector = user.personalData?.workingSector
            this.studies = user.personalData?.studies
        }
    }


    String bio
    Gender gender

    //Citizen
    @BindingFormat("dd/MM/yyyy")
    Date birthday
    Studies studies
    EnterpriseSector enterpriseSector
    WorkingSector workingSector

    //Politician
    String position
    //String politicalParty

    static constraints = {
        gender nullable: true
        birthday nullable:true
        workingSector nullable: true
        studies nullable: true
        enterpriseSector nullable:true
        bio nullable: true, maxSize: 1000
        //politicalParty nullable:true
        position nullable:true
    }
}
