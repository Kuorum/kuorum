package kuorum.web.commands.post

import grails.validation.Validateable

/**
 * Created by iduetxe on 30/04/14.
 */
@Validateable
class CommentPostCommand {

    String comment
    static constraints = {
        comment nullable: false
    }
}
