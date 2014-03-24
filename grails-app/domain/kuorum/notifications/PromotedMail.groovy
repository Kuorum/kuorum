package kuorum.notifications

import kuorum.post.Post
import kuorum.users.KuorumUser

/**
 * Solo sirve para comprobar si se ha enviado un mail sponsorizado a un usuario o no.
 */
class PromotedMail {
    KuorumUser user
    Post post
    KuorumUser sponsor
    Date dateCreated

    static constraints = {

    }
    static mapping = {
        compoundIndex user:1, post:1
    }
}
