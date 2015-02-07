package kuorum

import kuorum.core.model.gamification.GamificationAward
import kuorum.users.KuorumUser

class GamificationTagLib {
    static defaultEncodeAs = 'raw'
    //static encodeAsForTags = [tagName: 'html']
    static namespace = "gamification"

    def springSecurityService
    def gamificationService


    def ifAwardIsBought ={attrs, body->
        GamificationAward role = attrs.role
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        if (gamificationService.isAlreadyBought(user, role)){
            body()
        }
    }

    def roleButton={attrs ->
        GamificationAward role = attrs.role
        if (!role.toString().startsWith("ROLE_")) throw new Exception("No se puede tratar ${role} con este taglib. No es un rol. Es una habilidad");

        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        if (gamificationService.isAlreadyBought(user,role)){
            out << roleButtonAlreadyBought(user, role)
        }else{
            out << roleButtonBuyRole(user,role)
        }
    }

    private String roleButtonAlreadyBought(KuorumUser user, GamificationAward role){
        if (user.gamification.activeRole == role){
            return "<a href='#' class='roleButton btn btn-green active'>${message(code:'profile.kuorumStore.roleButton.active')}</a>"
        }else{
            def link = createLink(mapping:"toolsActivateAward",params:[award:role])
            return "<a href='${link}' class='roleButton btn btn-green'>${message(code:'profile.kuorumStore.roleButton.activate')}</a>"
        }
    }

    private String roleButtonBuyRole(KuorumUser user, GamificationAward role){
        if (gamificationService.canBuyAward(user, role)){
            def link = createLink(mapping:"toolsBuyAward",params:[award:role])
            return "<a href='${link}' class='roleButton btn'>${message(code:'profile.kuorumStore.roleButton.buy')}</a>"
        }else{
            return "<a href='#' class='roleButton btn disabled'>${message(code:'profile.kuorumStore.roleButton.buy')}</a>"
        }
    }
    private String skillButtonBuySkill(KuorumUser user, GamificationAward skill){
        if (gamificationService.canBuyAward(user, skill)){
            def link = createLink(mapping:"toolsBuyAward",params:[award:skill])
            return "<a href='${link}' class='skillButton btn'>${message(code:'profile.kuorumStore.skillButton.buy')}</a>"
        }else{
            return "<a href='#' class='skillButton btn disabled'>${message(code:'profile.kuorumStore.skillButton.buy')}</a>"
        }
    }

    def skillButton={attrs ->
        GamificationAward skill = attrs.skill
        if (skill.toString().startsWith("ROLE_")) throw new Exception("No se puede tratar ${role}. Es un rol, no una habilidad.");

        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        if (gamificationService.isAlreadyBought(user,skill)){
            out << "<a href='#' class='btn active'>${message(code:'profile.kuorumStore.skillButton.active')}</a>"
        }else{
            out << skillButtonBuySkill(user,skill)
        }
    }
}
