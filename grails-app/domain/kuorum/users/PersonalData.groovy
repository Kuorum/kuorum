package kuorum.users

import kuorum.Region
import kuorum.core.model.Gender
import kuorum.core.model.UserType

/**
 * This object abstract the personal data to store and handle it easily
 */
class PersonalData {
    UserType userType = UserType.PERSON
    Gender gender

    String postalCode
    //TODO: Refactor province to Region
    String provinceCode  // this code is Region.iso3166_2
    Region province
//    Date birthday
    Integer year

    static constraints = {
        gender nullable: true
        provinceCode nullable: true
        postalCode nullable: true
        year nullable:true
        province nullable: true
    }
}
