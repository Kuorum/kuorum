package kuorum.web.commands.payment.massMailing.post

import grails.validation.Validateable

@Validateable
class LikePostCommand {

    Long postId;
    String postUserId;
    Boolean like;

    static constraints = {
        postId nullable: false
        postUserId nullable: false
        like nullable: false
    }
}
