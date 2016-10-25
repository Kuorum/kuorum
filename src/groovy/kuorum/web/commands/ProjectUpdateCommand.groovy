package kuorum.web.commands

import grails.validation.Validateable

@Validateable
class ProjectUpdateCommand {

    String description
    String photoId
    String videoPost

    static constraints = {
        description nullable: false, maxSize:500
        photoId nullable: true
        videoPost nullable: true, url:true, validator: { val, obj ->
            if (val && !val.decodeYoutubeName()) {
                return ['notYoutubeFormat']
            }
        }
    }
}
