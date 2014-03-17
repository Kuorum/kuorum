package kuorum.users

import kuorum.Region
import kuorum.core.model.Studies
import kuorum.core.model.WorkingSector

class PersonData extends PersonalData{
    String postalCode
    String provinceCode  // this code is Region.iso3166_2
    Region province
    Date birthday
    Studies studies
    WorkingSector workingSector
    static constraints = {
        provinceCode nullable: true
        postalCode nullable: true
        birthday nullable:true
        province nullable: true
        studies nullable:true
        workingSector nullable: true
    }
}
