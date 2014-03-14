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
    Law law
    String title
    String text
    String photo
    PostType postType
    static constraints = {
        importFrom Post
        postId nullable: false, blank: false
        title nullable: false, blank: false
        text nullable: false, blank: false
        postType nullable: false
        law nullable: false

    }
}
