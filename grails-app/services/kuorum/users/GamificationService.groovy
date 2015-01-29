package kuorum.users

import grails.transaction.Transactional
import kuorum.core.exception.KuorumException
import kuorum.core.model.gamification.GamificationAward
import kuorum.core.model.gamification.GamificationElement
import kuorum.project.Project
import kuorum.post.Post

@Transactional
class GamificationService {

    def grailsApplication
    def kuorumUserService

    private gamificationConfig(){
        grailsApplication.config.kuorum.gamification
    }

    def gamificationConfigVotePost(){ gamificationConfig().votePost }
    def gamificationConfigVoteProject(){ gamificationConfig().voteProject }
    def gamificationConfigCreatePost(){ gamificationConfig().newPost }
    def postCreatedAward(KuorumUser user, Post post) {
        def config = gamificationConfig().newPost
        updateGamificationUser(user,config)
    }

    def projectVotedAward(KuorumUser user, Project project){
        def config = gamificationConfig().voteProject
        updateGamificationUser(user,config)
    }

    def postVotedAward(KuorumUser user, Post post){
        def config = gamificationConfigVotePost()
        updateGamificationUser(user,config)
    }

    def sponsorAPostAward(KuorumUser sponsor, Integer numMailsSent){
        def config = gamificationConfigVotePost().collectEntries{ k, v -> [k, v * numMailsSent] }
        updateGamificationUser(sponsor,config)
    }

    Boolean isAlreadyBought(KuorumUser user, GamificationAward award){
        user.gamification.boughtAwards.contains(award)
    }

    Boolean  canBuyAward(KuorumUser user, GamificationAward award){
        user.gamification.numCorns >= award.numCorns &&
                user.gamification.numEggs >= award.numEggs &&
                user.gamification.numPlumes >= award.numPlumes

    }

    KuorumUser buyAward(KuorumUser user, GamificationAward award){
        if (!canBuyAward(user,award)){
            throw new KuorumException("El usuario ${user.email} no puede comprar ${award}", "error.gamification.notAllowBuyAward")
        }
        user.gamification.numCorns -= award.numCorns
        user.gamification.numPlumes -= award.numPlumes
        user.gamification.numEggs -= award.numEggs
        user.gamification.boughtAwards.add(award)
        user.save()
        activateAward(user, award)
    }

    Boolean canActivateAward(KuorumUser user, GamificationAward award){
        user.gamification.boughtAwards.contains(award)
    }

    KuorumUser activateAward(KuorumUser user, GamificationAward award){
        if (!canActivateAward(user,award)){
            throw new KuorumException("El usuario ${user.email} no puede activar ${award}", "error.gamification.notAllowedToActivateAward")
        }
        switch (award){
            case GamificationAward.ROLE_DEFAULT:
            case GamificationAward.ROLE_ACTIVISTA:
            case GamificationAward.ROLE_LIDER_OPINION:
            case GamificationAward.ROLE_MAESTRO_JEDI:
            case GamificationAward.ROLE_MILITANTE:
                user.gamification.activeRole = award
                break
            case GamificationAward.STATS_OWN_HISTORY:
                RoleUser role = RoleUser.findByAuthority("ROLE_HISTORY_STATS")
                kuorumUserService.addRole(user, role)
                break
            case GamificationAward.STATS_OWN_PURPOSE:
                RoleUser role = RoleUser.findByAuthority("ROLE_PURPOSE_STATS")
                kuorumUserService.addRole(user, role)
                break
            case GamificationAward.STATS_OWN_QUESTION:
                RoleUser role = RoleUser.findByAuthority("ROLE_QUESTION_STATS")
                kuorumUserService.addRole(user, role)
                break
        }
        user.save()
    }

    private KuorumUser updateGamificationUser(KuorumUser user, def config){
        user.gamification.numEggs += config[GamificationElement.EGG]?:0
        user.gamification.numPlumes += config[GamificationElement.PLUME]?:0
        user.gamification.numCorns += config[GamificationElement.CORN]?:0
        user.save()
    }
}
