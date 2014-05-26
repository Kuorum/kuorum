package kuorum.users

import kuorum.Region
import kuorum.core.model.EnterpriseSector

class OrganizationData extends PersonalData{

    Boolean isPoliticalParty
    EnterpriseSector enterpriseSector
    Region country

    static constraints = {
        enterpriseSector nullable:true
    }
}
