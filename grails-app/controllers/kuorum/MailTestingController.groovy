package kuorum

import grails.plugin.springsecurity.annotation.Secured
import kuorum.core.model.AvailableLanguage
import kuorum.core.model.PostType
import kuorum.core.model.UserType
import kuorum.mail.MailUserData
import kuorum.post.Cluck
import kuorum.post.Post
import kuorum.post.PostComment
import kuorum.users.KuorumUser

@Secured(['ROLE_ADMIN'])
class MailTestingController {
    
    def kuorumMailService
    def springSecurityService

    def index(String email, String lang) {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        if (!email)
            email = user.email
        if (!lang)
            lang = user.language.locale.language

        [email:email, lang:lang]
    }

    def testDebateNotificationPolitician(String email, String lang){
        def data = prepareMailTestEnviroment(email,lang)
        kuorumMailService.sendDebateNotificationMailPolitician(data.post, data.peopleAlerted)

        flash.message ="Se ha enviado el mail al email $email"
        redirect action: "index", params:[email:email]
    }
    def testDebateNotificationInterestedUsers(String email, String lang){

        def data = prepareMailTestEnviroment(email,lang)
        kuorumMailService.sendDebateNotificationMailInterestedUsers(data.post, data.peopleNotified)

        flash.message ="Se ha enviado el mail al email $email"
        redirect action: "index", params:[email:email]
    }
    def testDebateNotificationAuthor(String email, String lang){

        def data = prepareMailTestEnviroment(email,lang)
        kuorumMailService.sendDebateNotificationMailAuthor(data.post)

        flash.message ="Se ha enviado el mail al email $email"
        redirect action: "index", params:[email:email]
    }

    def testPostDefendNotificationAuthor(String email, String lang){

        def data = prepareMailTestEnviroment(email,lang)
        kuorumMailService.sendPostDefendedNotificationMailAuthor(data.post)

        flash.message ="Se ha enviado el mail al email $email"
        redirect action: "index", params:[email:email]
    }

    def testPostDefendedNotificationPeopleInterested(String email, String lang){

        def data = prepareMailTestEnviroment(email,lang)
        kuorumMailService.sendPostDefendedNotificationMailInterestedUsers(data.post, data.peopleNotified)

        flash.message ="Se ha enviado el mail al email $email"
        redirect action: "index", params:[email:email]
    }


    def testPostDefendedNotificationDefender(String email, String lang){

        def data = prepareMailTestEnviroment(email,lang)
        kuorumMailService.sendPostDefendedNotificationMailDefender(data.post)

        flash.message ="Se ha enviado el mail al email $email"
        redirect action: "index", params:[email:email]
    }

    def testPostDefendedNotificationPoliticians(String email, String lang){
        def data = prepareMailTestEnviroment(email,lang)
        kuorumMailService.sendPostDefendedNotificationMailPoliticians(data.post, data.peopleAlerted)

        flash.message ="Se ha enviado el mail al email $email"
        redirect action: "index", params:[email:email]
    }

    def testPublicMilestone(String email, String lang){
        def data = prepareMailTestEnviroment(email,lang)
        kuorumMailService.sendPublicMilestoneNotificationMail(data.post)

        flash.message ="Se ha enviado el mail al email $email"
        redirect action: "index", params:[email:email]
    }

    def testCluck(String email, String lang){
        def data = prepareMailTestEnviroment(email,lang)
        Cluck cluck = new Cluck(owner:data.politician, postOwner: data.user, post:data.post)
        kuorumMailService.sendCluckNotificationMail(cluck)

        flash.message ="Se ha enviado el mail al email $email"
        redirect action: "index", params:[email:email]
    }

    def testNewFollower(String email, String lang){
        def data = prepareMailTestEnviroment(email,lang)
        kuorumMailService.sendFollowerNotificationMail(data.user, data.politician)

        flash.message ="Se ha enviado el mail al email $email"
        redirect action: "index", params:[email:email]
    }

    def testVictoryUsers(String email, String lang){
        def data = prepareMailTestEnviroment(email,lang)
        kuorumMailService.sendVictoryNotificationUsers(data.post, data.peopleAlerted)

        flash.message ="Se ha enviado el mail al email $email"
        redirect action: "index", params:[email:email]
    }

    def testVictoryDefender(String email, String lang){
        def data = prepareMailTestEnviroment(email,lang)
        kuorumMailService.sendVictoryNotificationDefender(data.post)

        flash.message ="Se ha enviado el mail al email $email"
        redirect action: "index", params:[email:email]
    }

    def testRegister(String email, String lang){
        def data = prepareMailTestEnviroment(email,lang)
        kuorumMailService.sendRegisterUser(data.user, "https://kuorum.org")

        flash.message ="Se ha enviado el mail al email $email"
        redirect action: "index", params:[email:email]
    }

    def testRegisterRRSS(String email, String lang){
        def data = prepareMailTestEnviroment(email,lang)
        kuorumMailService.sendRegisterUserViaRRSS(data.user, "testProvider")

        flash.message ="Se ha enviado el mail al email $email"
        redirect action: "index", params:[email:email]
    }

    def testAccountConfirmed(String email, String lang){
        def data = prepareMailTestEnviroment(email,lang)
        kuorumMailService.sendUserAccountConfirmed(data.user)

        flash.message ="Se ha enviado el mail al email $email"
        redirect action: "index", params:[email:email]
    }


    private String prepareEmail(String email, String userName){
        def parts = email.split("@")
        "${parts[0]}+${userName}@${parts[1]}"
    }

    private def prepareMailTestEnviroment(String email, String lang){

        /* NO SAVES PLEASE */

        Post post = Post.findByNumClucksGreaterThan(1)
        KuorumUser user = transformUserToTest(post.owner, lang, email)
        KuorumUser politician = transformUserToTest(KuorumUser.findByUserType(UserType.POLITICIAN), lang, email)
        post.debates = [new PostComment(text:"Debate molon", kuorumUser: politician)]
        post.defender = politician
//        post.save(); //NO SAVES
        Set<MailUserData> peopleNotified = [
                transformUserToTest(KuorumUser.findAllByUserType(UserType.PERSON, [max:10])[0], lang, email),
                transformUserToTest(KuorumUser.findAllByUserType(UserType.PERSON, [max:10])[1], lang, email)
        ].collect{KuorumUser userVoted -> new MailUserData(user: userVoted, bindings: [postType:"PROPUESTA A PELO POR TEST"])} as Set<MailUserData>


        Set<MailUserData> peopleAlerted = [
                politician,
        ].collect{KuorumUser userVoted -> new MailUserData(user: userVoted, bindings: [postType:"POST TYPE SIN IDIOMA POR TEST"])} as Set<MailUserData>

        [post: post, politician:politician, user:user, peopleNotified:peopleNotified, peopleAlerted:peopleAlerted]
    }

    private KuorumUser transformUserToTest(KuorumUser user, String lang, String email="info@kuorum.org"){
        user.email = prepareEmail(email, user.userType.toString())
        AvailableLanguage language = AvailableLanguage.fromLocaleParam(lang)
        if (!language) language = AvailableLanguage.es_ES
        user.language = language
        user
    }
}
