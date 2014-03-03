package kuorum.mail

import kuorum.core.exception.KuorumException
import kuorum.post.Post
import kuorum.post.PostComment
import kuorum.users.KuorumUser
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by iduetxe on 28/02/14.
 */
class KuorumMailServiceIntegrationSpec extends Specification{

    def kuorumMailService

    def setup(){
    }

    @Unroll
    void "test send mail of debate notification firstDebate = #firstDebate"() {
        given: "A post"
        KuorumUser user = KuorumUser.findByEmail("peter@example.com")
        KuorumUser politician = KuorumUser.findByEmail("politician@example.com")
        Post post = Post.findByOwner(user)
        post.debates = [new PostComment(text:"kk", kuorumUser: politician)]
        post.save();
        Set<MailUserData> peopleNotified = [
                KuorumUser.findByEmail("carmen@example.com"),
                KuorumUser.findByEmail("noe@example.com")
            ].collect{KuorumUser userVoted -> new MailUserData(user: userVoted, bindings: [])} as Set<MailUserData>

        Set<MailUserData> peopleAlerted = [
                politician,
        ].collect{KuorumUser userVoted -> new MailUserData(user: userVoted, bindings: [])} as Set<MailUserData>

        when: "Saving a post"
        kuorumMailService.sendDebateNotificationMail(post, peopleNotified, peopleAlerted, isFirstDebate)

        then: "Expected an exception"
        !exception //An exception is not thrown (Is the best test that I can imagine)
        where:
        isFirstDebate | exception
        true          | false
        false         | false
    }

}
