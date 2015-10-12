package kuorum.web.commands.admin

import grails.validation.Validateable
import kuorum.PoliticalParty
import kuorum.Region
import kuorum.core.model.CommissionType
import kuorum.core.model.Gender
import kuorum.core.model.UserType
import kuorum.web.commands.profile.EditUserProfileCommand

/**
 * Created by iduetxe on 29/05/14.
 */
@Validateable
class AdminUserCommand extends EditUserProfileCommand{
    String email
    String password
    Region politicianOnRegion
    String politicalParty
    Boolean verified
    Boolean enabled
    UserType userType
    List<CommissionType> commissions = []

    static constraints = {
        email nullable:false, email:true
        politicalParty nullable: true, validator:{val, obj ->
            if (obj.userType && obj.userType == UserType.POLITICIAN && !val){
                return "politicianWithoutPoliticalParty"
            }else if(obj.userType && obj.userType != UserType.POLITICIAN && val){
                return "normalUserWithPoliticalParty"
            }
        }
        politicianOnRegion nullable:true, validator:{val, obj ->
            if (obj.userType && obj.userType == UserType.POLITICIAN && !val){
                return "politicianWithoutPoliticianRegion"
            }else if(obj.userType && obj.userType != UserType.POLITICIAN && val){
                return "normalUserWithPoliticianRegion"
            }
        }
        userType nullable: false
        gender nullable:false, validator:{val, obj ->
            if (val && obj.userType != UserType.ORGANIZATION && val == Gender.ORGANIZATION){
                return "incompatibleWithUserType"
            }
            if (val && obj.userType == UserType.ORGANIZATION && val != Gender.ORGANIZATION){
                return "incompatibleWithUserType"
            }
        }
        enabled nullable:true, validator:{val, obj ->
            if (!val && obj.userType != UserType.POLITICIAN){
                return "onlyPoliticiansCanBeInactive"
            }
        }
        password nullable:false

    }
}
