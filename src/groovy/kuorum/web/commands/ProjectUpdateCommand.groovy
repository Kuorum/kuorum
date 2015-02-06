package kuorum.web.commands

import grails.validation.Validateable

@Validateable
class ProjectUpdateCommand {
    String description
    String photoId
    String urlYoutubeId

    static constraints = {
        description maxSize:500
    }
}
