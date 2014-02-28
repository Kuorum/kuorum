package kuorum.mail

import kuorum.core.exception.KuorumException
import kuorum.post.Post
import kuorum.post.PostComment
import kuorum.users.KuorumUser
import spock.lang.Specification

/**
 * Created by iduetxe on 28/02/14.
 */
class KuorumMailServiceIntegrationSpec extends Specification{

    def kuorumMailService

    def setup(){
    }

    void "test send mail of debate notification"() {
        given: "A post"
        KuorumUser user = KuorumUser.findByEmail("peter@example.com")
        KuorumUser politician = KuorumUser.findByEmail("politician@example.com")
        Post post = Post.findByOwner(user)
        post.debates = [new PostComment(text:"kk", kuorumUser: politician)]
        post.save();
        List<MailUserData> peopleVoted = [
                KuorumUser.findByEmail("carmen@example.com"),
                KuorumUser.findByEmail("noe@example.com")
            ].collect{KuorumUser userVoted -> new MailUserData(user: userVoted, bindings: [])}

        when: "Saving a post"
        kuorumMailService.sendDebateNotificationMail(post, peopleVoted)

        then: "Expected an exception"
        true //An exception is not thrown (Is the best test that I can imagine)
    }

}
