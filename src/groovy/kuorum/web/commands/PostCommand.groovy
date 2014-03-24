package kuorum.web.commands

import grails.validation.Validateable
import kuorum.core.model.PostType
import kuorum.law.Law
import kuorum.post.Post

/**
 * Created by iduetxe on 13/02/14.
 */
@Validateable
class PostCommand {
    String postId
    String hashtag
    String title
    String text
    String photo
    PostType postType
    static constraints = {
        postId nullable: true, blank: true //Para reusar este command en la edición
        title nullable: false, blank: false
        text nullable: false, blank: false
        postType nullable: false
        hashtag nullable: false, blank: false
    }
}
