package kuorum.users

import kuorum.Region
import kuorum.core.model.Gender

/**
 * This object abtracts the personal data to store and handle it easily
 */
class PersonalData {
    Gender gender
    String postalCode
    String provinceCode  // this code is Region.iso3166_2
    Region province
    Date birthday

    static constraints = {
        provinceCode nullable: true
        gender nullable: true
        postalCode nullable: true
        birthday nullable:true
        province nullable: true
    }
}
