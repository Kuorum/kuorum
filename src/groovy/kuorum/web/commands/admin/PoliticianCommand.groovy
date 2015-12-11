package kuorum.web.commands.admin

import grails.validation.Validateable
import kuorum.Region

/**
 * Created by iduetxe on 10/12/15.
 */
@Validateable
class PoliticianCommand {

    Region politicianOnRegion
    String politicalParty

    static constraints = {
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
    }
}
