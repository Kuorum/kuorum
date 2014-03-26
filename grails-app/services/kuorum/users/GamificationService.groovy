package kuorum.users

import grails.transaction.Transactional
import kuorum.core.model.gamification.GamificationElement
import kuorum.law.Law
import kuorum.post.Post
import kuorum.post.Sponsor
import org.codehaus.groovy.grails.web.converters.configuration.configtest

@Transactional
class GamificationService {

    def grailsApplication
    private gamificationConfig(){
        grailsApplication.config.kuorum.gamification
    }

    def postCreatedAward(KuorumUser user, Post post) {
        def config = gamificationConfig().newPost
        updateGamigicationUser(user,config)
    }

    def lawVotedAward(KuorumUser user, Law law){
        def config = gamificationConfig().voteLaw
        updateGamigicationUser(user,config)
    }

    def postVotedAward(KuorumUser user, Post post){
        def config = gamificationConfig().votePost
        updateGamigicationUser(user,config)
    }

    def sponsorAPostAward(KuorumUser sponsor, Integer numMailsSent){
        def config = gamificationConfig().votePost.collectEntries{ k, v -> [k, v * numMailsSent] }
        updateGamigicationUser(sponsor,config)
    }

    private updateGamigicationUser(KuorumUser user, def config){
        user.gamification.numEggs += config[GamificationElement.EGG]?:0
        user.gamification.numPlumes += config[GamificationElement.PLUME]?:0
        user.gamification.numCorns += config[GamificationElement.CORN]?:0
        user.save()
    }
}
