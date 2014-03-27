package kuorum.web.commands.customRegister

import grails.validation.Validateable
import kuorum.core.model.CommissionType
import kuorum.core.model.Studies
import kuorum.core.model.WorkingSector

/**
 * Created by iduetxe on 17/03/14.
 */
@Validateable
class Step3Command {

    List<CommissionType> relevantCommissions

    List<CommissionType> getRelevantCommissions(){
        relevantCommissions?:[]
    }

    static constraints = {
        relevantCommissions nullable: false, minSize: 3
    }
}
