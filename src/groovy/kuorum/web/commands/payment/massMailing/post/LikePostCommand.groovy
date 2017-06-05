package kuorum.web.commands.payment.massMailing.post

import grails.validation.Validateable

@Validateable
class LikePostCommand {

    Long postId;
    String userAlias;
    Boolean like;

    static constraints = {
        postId nullable: false
        userAlias nullable: false
        like nullable: false
    }
}
