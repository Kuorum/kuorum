package kuorum.web.commands.admin

import grails.validation.Validateable
import kuorum.Institution
import kuorum.ParliamentaryGroup
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
    Institution institution
    ParliamentaryGroup parliamentaryGroup
    Boolean verified
    Boolean enabled
    UserType userType

    static constraints = {
        email nullable:false, email:true
        institution nullable: true, validator:{val, obj ->
            if (obj.userType && obj.userType == UserType.POLITICIAN && !val){
                return "politicianWithoutInstitution"
            }else if(obj.userType && obj.userType != UserType.POLITICIAN && val){
                return "normalUserWithInstitution"
            }
        }
        parliamentaryGroup nullable: true, validator:{val, obj ->
            if (obj.userType && obj.userType == UserType.POLITICIAN && !val){
                return "politicianWithoutParliamentaryGroup"
            }else if(obj.userType && obj.userType != UserType.POLITICIAN && val){
                return "normalUserWithParliamentaryGroup"
            }

            if (val && obj.institution && val.institution != obj.institution){
                return "notSameInstitution"
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
