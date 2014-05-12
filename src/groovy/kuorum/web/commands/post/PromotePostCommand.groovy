package kuorum.web.commands.post

import grails.validation.Validateable

/**
 * Created by iduetxe on 12/05/14.
 */
@Validateable
class PromotePostCommand {

    Double amount
    String postId
    static constraints = {
        amount nullable: false, min: 1D
        postId nullable: false
    }

}
