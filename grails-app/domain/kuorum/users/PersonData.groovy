package kuorum.users

import kuorum.core.model.Studies
import kuorum.core.model.WorkingSector

class PersonData extends PersonalData{

    Studies studies
    WorkingSector workingSector
    Integer year

    static constraints = {
        studies nullable:true
        workingSector nullable: true
        year nullable: true
    }
}
