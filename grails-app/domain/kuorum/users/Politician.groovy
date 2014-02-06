package kuorum.users

import kuorum.Institution
import kuorum.ParliamentaryGroup

class Politician extends Person{

    ParliamentaryGroup parliamentaryGroup
    Institution institution

    static constraints = {
        parliamentaryGroup nullable: true, validator: { val, obj ->
            if (val  && val.institution != obj.institution) {
                return ['notCorrectInstitution']
            }
            if (!val && obj.institution){
                return ['notParliamentaryGroup']
            }
        }

        institution nullable:true

    }
}
