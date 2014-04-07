package kuorum.web.commands.customRegister

import grails.validation.Validateable
import kuorum.core.model.Gender
import kuorum.core.model.Studies
import kuorum.core.model.WorkingSector

/**
 * Created by iduetxe on 17/03/14.
 */
@Validateable
class Step2Command {
    String photoId
    WorkingSector workingSector
    Studies studies
    String bio
    static constraints = {
        photoId nullable: false
        workingSector nullable: false, minSize: 5, maxSize: 5
        studies nullable: true
        bio nullable: true, maxSize: 10
    }
}
