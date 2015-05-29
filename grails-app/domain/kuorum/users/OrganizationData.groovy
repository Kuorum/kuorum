package kuorum.users

import kuorum.Region
import kuorum.core.model.EnterpriseSector

class OrganizationData extends PersonalData{

    EnterpriseSector enterpriseSector

    static constraints = {
        enterpriseSector nullable:true
    }
}
