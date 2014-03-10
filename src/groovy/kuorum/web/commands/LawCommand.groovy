package kuorum.web.commands

import grails.validation.Validateable
import kuorum.Institution
import kuorum.Region
import kuorum.core.model.CommissionType
import kuorum.law.Law

/**
 * Created by iduetxe on 3/03/14.
 */

@Validateable
class LawCommand {
    String hashtag
    String shortName
    String realName
    String description
    String introduction
    List<CommissionType> commissions  = new ArrayList<CommissionType>()
    Region region
    Institution institution

    static constraints = {
        importFrom Law

        //Validator is not imported
        region  nullable:false, validator: { val, obj ->
            if (val != obj.institution.region) {
                return ['notSameRegionAsInstitution']
            }
        }
    }
}
