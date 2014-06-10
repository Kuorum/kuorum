package kuorum.web.commands

import grails.validation.Validateable
import kuorum.core.FileType
import kuorum.core.model.PostType

/**
 * Created by iduetxe on 13/02/14.
 */
@Validateable
class PostCommand {
    private static final YOUTUBE_REGEX = ~/http[s]{0,1}:\/\/(w{3}.){0,1}youtube\.com\/watch\?v=[a-zA-Z0-9_]*/
    String postId
    String title
    String textPost
    FileType fileType
    String imageId
    String videoPost
    PostType postType
    Integer numberPage
    static constraints = {
        postId nullable: true, blank: true //Para reusar este command en la edición
        title nullable: false, blank: false
        textPost nullable: false, blank: false
        postType nullable: false
        videoPost nullable: true,
                url:true,
//                matches: 'http[s]{0,1}://(w{3}.){0,1}youtube\\.com/watch\\?v=[a-zA-Z0-9_]*',
                validator: { val, obj ->
                    if (obj.fileType == FileType.YOUTUBE && val && !YOUTUBE_REGEX.matcher(val).matches()) {
                        return ['notYoutubeFormat']
                    }
                }
        numberPage nullable:true
    }
}
