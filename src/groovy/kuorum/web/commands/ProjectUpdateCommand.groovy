package kuorum.web.commands

import grails.validation.Validateable

@Validateable
class ProjectUpdateCommand {

    private static final YOUTUBE_REGEX = ~/http[s]{0,1}:\/\/(w{3}.){0,1}youtube\.com\/watch\?v=[a-zA-Z0-9_-]*/

    String description
    String photoId
    String videoPost

    static constraints = {
        description maxSize:500
        photoId nullable: true
        videoPost nullable: true, url:true, validator: { val, obj ->
            if (val && !YOUTUBE_REGEX.matcher(val).matches()) {
                return ['notYoutubeFormat']
            }
        }
    }
}
