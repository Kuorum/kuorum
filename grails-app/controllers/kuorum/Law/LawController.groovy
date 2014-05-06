package kuorum.Law

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import kuorum.Institution
import kuorum.Region
import kuorum.core.model.VoteType
import kuorum.core.model.gamification.GamificationElement
import kuorum.law.Law
import kuorum.law.LawVote
import kuorum.post.Post
import kuorum.users.KuorumUser
import kuorum.web.commands.LawCommand

import javax.servlet.http.HttpServletResponse

class LawController {

    def lawService
    def postService
    def cluckService
    def springSecurityService
    def gamificationService

    def index(){
        [lawInstanceList:Law.findAll()]
    }

    def show(String hashtag){
        Law law = lawService.findLawByHashtag(hashtag.encodeAsHashtag())
        if (!law){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        def clucks = cluckService.lawClucks(law)
        List<Post> victories = postService.lawVictories(law)

        LawVote userVote
        if (springSecurityService.isLoggedIn()){
            KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
            userVote = lawService.findLawVote(law,user)
        }
        Integer necessaryVotesForKuorum = lawService.necessaryVotesForKuorum(law)


        [law:law, clucks: clucks,victories:victories, userVote:userVote,necessaryVotesForKuorum:necessaryVotesForKuorum]

    }

    @Secured(['ROLE_ADMIN'])
    def create(){
        [   lawInstance:new LawCommand(),
            regions:Region.findAll(),
            institutions:Institution.findAll()
        ]
    }

    @Secured(['ROLE_ADMIN'])
    def save(LawCommand command){
        if (command.hasErrors()){
            def regions = Region.findAll()
            def institutions = Institution.findAll()
            render view: "create", model:[
                    lawInstance:command,
                    regions:Region.findAll(),
                    institutions:Institution.findAll()
            ]
            return
        }

        Law law = new Law(command.properties)
        lawService.saveLaw(law)
        redirect mapping:"lawShow", params:law.encodeAsLinkProperties()
    }

    @Secured(['ROLE_ADMIN'])
    def edit(String hashtag){
        Law law = lawService.findLawByHashtag(hashtag.encodeAsHashtag())
        if (!law){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        LawCommand lawCommand = new LawCommand()
        lawCommand.properties.each {k,v ->
            if (k!="class")
                lawCommand."$k" = law."$k"
        }
        [
            lawInstance:lawCommand,
            regions:Region.findAll(),
            institutions:Institution.findAll()
        ]
    }


    @Secured(['ROLE_ADMIN'])
    def update(LawCommand command){
        if (command.hasErrors()){
            render view: "edit", model:[
                    lawInstance:command,
                    regions:Region.findAll(),
                    institutions:Institution.findAll()
                ]
        }
        Law law = lawService.findLawByHashtag(command.hashtag.encodeAsHashtag())
        if (!law){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        command.properties.each {k,v -> if (k!="class") {law."$k" = command."$k"}}
        lawService.updateLaw(law)
        flash.message=message(code: "law.update.success", args: [law.hashtag])
        redirect mapping:"lawShow", params:law.encodeAsLinkProperties()

    }

    @Secured(['ROLE_ADMIN'])
    def publish(String hashtag){
        Law law = lawService.findLawByHashtag(hashtag.encodeAsHashtag())
        if (!law){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        law = lawService.publish(law)
        render "publish"
    }

    @Secured(['ROLE_ADMIN'])
    def unpublish(String hashtag){
        Law law = lawService.findLawByHashtag(hashtag.encodeAsHashtag())
        if (!law){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        law = lawService.unpublish(law)
        render "unpublish"
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def voteLaw(String hashtag){
        VoteType voteType = VoteType.valueOf(params.voteType)
        Law law = lawService.findLawByHashtag(hashtag.encodeAsHashtag())
        if (!law || !voteType){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        LawVote lawVote = lawService.voteLaw(law, user, voteType)
        Integer necessaryVotesForKuorum = lawService.necessaryVotesForKuorum(law)
        def gamification = [
                title: "${message(code:'law.vote.gamification.title', args:[law.hashtag])}",
                text:"${message(code:'law.vote.gamification.motivationText', args:[law.hashtag])}",
                eggs:gamificationService.gamificationConfigVoteLaw()[GamificationElement.EGG]?:0,
                plumes:gamificationService.gamificationConfigVoteLaw()[GamificationElement.PLUME]?:0,
                corns:gamificationService.gamificationConfigVoteLaw()[GamificationElement.CORN]?:0
        ]

        render ([
                necessaryVotesForKuorum:necessaryVotesForKuorum,
                voteType:lawVote.voteType.toString(),
                votes:law.peopleVotes,
                gamification:gamification
        ] as JSON)
    }
}
