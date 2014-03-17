package kuorum.users

import kuorum.Region
import kuorum.core.model.Gender
import kuorum.core.model.Studies
import kuorum.core.model.UserType
import kuorum.core.model.WorkingSector

/**
 * This object abtracts the personal data to store and handle it easily
 */
class PersonalData {
    UserType userType = UserType.PERSON
    Gender gender

    static constraints = {
        gender nullable: true
    }
}
