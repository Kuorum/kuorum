package kuorum.web.commands.customRegister

import grails.validation.Validateable
import kuorum.core.model.EnterpriseSector
import kuorum.core.model.Studies
import kuorum.core.model.WorkingSector

/**
 * Created by iduetxe on 17/03/14.
 */
@Validateable
@Deprecated
class Step2Command {
    String photoId
    WorkingSector workingSector
    Studies studies
    EnterpriseSector enterpriseSector
    String bio
    static constraints = {

    }
}
