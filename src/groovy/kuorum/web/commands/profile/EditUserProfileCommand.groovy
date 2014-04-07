package kuorum.web.commands.profile

import grails.validation.Validateable
import kuorum.core.model.Gender
import kuorum.core.model.PostType
import kuorum.core.model.Studies
import kuorum.core.model.UserType
import kuorum.core.model.WorkingSector

/**
 * Created by iduetxe on 13/02/14.
 */
@Validateable
class EditUserProfileCommand extends BirthdayCommad{

    String name
    Gender gender
    String postalCode
    String photoId
    WorkingSector workingSector
    Studies studies
    String bio

    static constraints = {
        importFrom BirthdayCommad
        name nullable: false, blank: false
        gender nullable: false
        postalCode nullable: false, blank: false
        photoId nullable: true
        workingSector nullable: true
        studies nullable: true
        bio nullable: true
    }
}
