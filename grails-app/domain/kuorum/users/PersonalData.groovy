package kuorum.users

import kuorum.core.model.Gender

/**
 * This object abtracts the personal data to store and handle it easily
 */
class PersonalData {
    Gender gender
    String postalCode
    String regionCode  // this code is Region.iso3166_2
    Date birthday

    static constraints = {
        regionCode nullable: true
        gender nullable: true
        postalCode nullable: true
        birthday nullable:true
    }
}
