package kuorum.core.model.project

import grails.validation.Validateable
import kuorum.KuorumFile

@Validateable
class ProjectUpdate {

    String description
    KuorumFile image
    KuorumFile urlYoutube
    Date dateCreated

    static constraints = {
        description maxSize:500
        image nullable: true
        urlYoutube nullable: true
    }
}
