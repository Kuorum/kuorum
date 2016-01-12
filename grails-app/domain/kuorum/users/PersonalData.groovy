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
    String phonePrefix
    String telephone

    @Deprecated
    String postalCode
    //TODO: Refactor province to Region
    String provinceCode  // this code is Region.iso3166_2
    Region province
    @Deprecated
    Region country
    Date birthday
    static constraints = {
        userType nullable:false
        telephone nullable: true
        gender nullable: true
        provinceCode nullable: true
        postalCode nullable: true
        province nullable: true
        country nullable: true
        phonePrefix nullable:true
        birthday nullable:true
    }
}
