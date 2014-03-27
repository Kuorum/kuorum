package kuorum.web.commands.customRegister

import grails.validation.Validateable
import kuorum.core.model.CommissionType

/**
 * Created by iduetxe on 17/03/14.
 */
@Validateable
class Step4Command {

    List<String> recommendedUsers

    List<String> getRecommendedUsers(){
        recommendedUsers?:[]
    }

    static constraints = {
        recommendedUsers nullable:false, minSize: 3
    }
}
