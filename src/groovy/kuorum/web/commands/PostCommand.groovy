package kuorum.web.commands

import grails.validation.Validateable
import kuorum.core.model.PostType

/**
 * Created by iduetxe on 13/02/14.
 */
@Validateable
class PostCommand {
    String postId
    String title
    String textPost
    String imageId
    String videoPost
    PostType postType
    Integer numberPage
    static constraints = {
        postId nullable: true, blank: true //Para reusar este command en la edición
        title nullable: false, blank: false
        textPost nullable: false, blank: false
        postType nullable: false
        videoPost nullable: true, url:true
        numberPage nullable:true
    }
}
