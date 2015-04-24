package kuorum.web.commands.post

import grails.validation.Validateable

/**
 * Created by iduetxe on 24/04/15.
 */
@Validateable
class PostDefendCommand {
    String text;
    String postId;

    static constraints = {
        postId nullable: false
        text nullable:false
    }
}
