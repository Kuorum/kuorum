package kuorum

import grails.plugin.springsecurity.annotation.Secured
import kuorum.mail.MailUserData
import kuorum.post.Cluck
import kuorum.post.Post
import kuorum.post.PostComment
import kuorum.users.KuorumUser

@Secured(['ROLE_ADMIN'])
class MailTestingController {

    def kuorumMailService
    def springSecurityService

    def index(String email) {
        if (!email)
            email = KuorumUser.get(springSecurityService.principal.id).email
        [email:email]
    }

    def testDebateNotificationPolitician(String email){

       def data = prepareMailTestEnviroment(email)
        kuorumMailService.sendDebateNotificationMailPolitician(data.post, data.peopleAlerted)

        flash.message ="Se ha enviado el mail al email $email"
        redirect action: "index", params:[email:email]
    }
    def testDebateNotificationInterestedUsers(String email){

        def data = prepareMailTestEnviroment(email)
        kuorumMailService.sendDebateNotificationMailInterestedUsers(data.post, data.peopleNotified)

        flash.message ="Se ha enviado el mail al email $email"
        redirect action: "index", params:[email:email]
    }
    def testDebateNotificationAuthor(String email){

        def data = prepareMailTestEnviroment(email)
        kuorumMailService.sendDebateNotificationMailAuthor(data.post)

        flash.message ="Se ha enviado el mail al email $email"
        redirect action: "index", params:[email:email]
    }

    def testPostDefendNotificationAuthor(String email){

        def data = prepareMailTestEnviroment(email)
        kuorumMailService.sendPostDefendedNotificationMailAuthor(data.post)

        flash.message ="Se ha enviado el mail al email $email"
        redirect action: "index", params:[email:email]
    }

    def testPostDefendedNotificationPeopleInterested(String email){

        def data = prepareMailTestEnviroment(email)
        kuorumMailService.sendPostDefendedNotificationMailInterestedUsers(data.post, data.peopleNotified)

        flash.message ="Se ha enviado el mail al email $email"
        redirect action: "index", params:[email:email]
    }


    def testPostDefendedNotificationDefender(String email){

        def data = prepareMailTestEnviroment(email)
        kuorumMailService.sendPostDefendedNotificationMailDefender(data.post)

        flash.message ="Se ha enviado el mail al email $email"
        redirect action: "index", params:[email:email]
    }

    def testPostDefendedNotificationPoliticians(String email){
        def data = prepareMailTestEnviroment(email)
        kuorumMailService.sendPostDefendedNotificationMailPoliticians(data.post, data.peopleAlerted)

        flash.message ="Se ha enviado el mail al email $email"
        redirect action: "index", params:[email:email]
    }

    def testPublicMilestone(String email){
        def data = prepareMailTestEnviroment(email)
        kuorumMailService.sendPublicMilestoneNotificationMail(data.post)

        flash.message ="Se ha enviado el mail al email $email"
        redirect action: "index", params:[email:email]
    }

    def testCluck(String email){
        def data = prepareMailTestEnviroment(email)
        Cluck cluck = new Cluck(owner:data.politician, postOwner: data.user, post:data.post)
        kuorumMailService.sendCluckNotificationMail(cluck)

        flash.message ="Se ha enviado el mail al email $email"
        redirect action: "index", params:[email:email]
    }

    def testNewFollower(String email){
        def data = prepareMailTestEnviroment(email)
        kuorumMailService.sendFollowerNotificationMail(data.user, data.politician)

        flash.message ="Se ha enviado el mail al email $email"
        redirect action: "index", params:[email:email]
    }

    def testVictoryUsers(String email){
        def data = prepareMailTestEnviroment(email)
        kuorumMailService.sendVictoryNotificationUsers(data.post, data.peopleAlerted)

        flash.message ="Se ha enviado el mail al email $email"
        redirect action: "index", params:[email:email]
    }

    def testVictoryDefender(String email){
        def data = prepareMailTestEnviroment(email)
        kuorumMailService.sendVictoryNotificationDefender(data.post)

        flash.message ="Se ha enviado el mail al email $email"
        redirect action: "index", params:[email:email]
    }

    def testRegister(String email){
        def data = prepareMailTestEnviroment(email)
        kuorumMailService.sendRegisterUser(data.user, "http://kuorum.org")

        flash.message ="Se ha enviado el mail al email $email"
        redirect action: "index", params:[email:email]
    }

    def testRegisterRRSS(String email){
        def data = prepareMailTestEnviroment(email)
        kuorumMailService.sendRegisterUserViaRRSS(data.user, "testProvider")

        flash.message ="Se ha enviado el mail al email $email"
        redirect action: "index", params:[email:email]
    }

    def testAccountConfirmed(String email){
        def data = prepareMailTestEnviroment(email)
        kuorumMailService.sendUserAccountConfirmed(data.user)

        flash.message ="Se ha enviado el mail al email $email"
        redirect action: "index", params:[email:email]
    }

    def testPromotedOwner(String email){
        def data = prepareMailTestEnviroment(email)
        kuorumMailService.sendPromotedPostMailOwner(data.post, data.politician)

        flash.message ="Se ha enviado el mail al email $email"
        redirect action: "index", params:[email:email]
    }

    def testPromotedSponsor(String email){
        def data = prepareMailTestEnviroment(email)
        kuorumMailService.sendPromotedPostMailSponsor(data.post, data.politician)

        flash.message ="Se ha enviado el mail al email $email"
        redirect action: "index", params:[email:email]
    }

    def testPromotedUsers(String email){
        def data = prepareMailTestEnviroment(email)
        kuorumMailService.sendPromotedPostMailUsers(data.post, data.politician,data.peopleNotified)

        flash.message ="Se ha enviado el mail al email $email"
        redirect action: "index", params:[email:email]
    }


    private String prepareEmail(String email, String userName){
        def parts = email.split("@")
        "${parts[0]}+${userName}@${parts[1]}"
    }

    private def prepareMailTestEnviroment(String email){

        /* NO SAVES PLEASE */

        KuorumUser user = KuorumUser.findByEmail("peter@example.com")
        user.email = prepareEmail(email, "user")
        KuorumUser politician = KuorumUser.findByEmail("politician@example.com")
        politician.email = prepareEmail(email, "politician")
        Post post = Post.findByOwner(user)
        post.debates = [new PostComment(text:"Debate molon", kuorumUser: politician)]
        post.defender = politician
//        post.save(); //NO SAVES
        Set<MailUserData> peopleNotified = [
                KuorumUser.findByEmail("carmen@example.com"),
                KuorumUser.findByEmail("noe@example.com")
        ].collect{KuorumUser userVoted -> new MailUserData(user: userVoted, bindings: [postType:post.postType.toString()])} as Set<MailUserData>

        peopleNotified.each {
            it.user.email = prepareEmail(email, it.user.email.split("@")[0])
        }

        Set<MailUserData> peopleAlerted = [
                politician,
        ].collect{KuorumUser userVoted -> new MailUserData(user: userVoted, bindings: [postType:post.postType.toString()])} as Set<MailUserData>

        [post: post, politician:politician, user:user, peopleNotified:peopleNotified, peopleAlerted:peopleAlerted]
    }
}
