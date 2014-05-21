package kuorum

import kuorum.core.model.gamification.GamificationAward
import kuorum.users.KuorumUser

class GamificationTagLib {
    static defaultEncodeAs = 'raw'
    //static encodeAsForTags = [tagName: 'html']
    static namespace = "gamification"

    def springSecurityService
    def gamificationService

    def roleButton={attrs ->
        GamificationAward role = attrs.role
        if (!role.toString().startsWith("ROLE_")) throw new Exception("No se puede tratar ${role} con este taglib");

        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        if (gamificationService.isAlreadyBought(user,role)){
            out << roleButtonAlreadyBought(user, role)
        }else{
            out << roleButtonBuyRole(user,role)
        }
    }

    def ifRoleIsBought={attrs, body->
        GamificationAward role = attrs.role
        if (!role.toString().startsWith("ROLE_")) throw new Exception("No se puede tratar ${role} con este taglib");
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        if (gamificationService.isAlreadyBought(user, role)){
            body()
        }
    }

    private String roleButtonAlreadyBought(KuorumUser user, GamificationAward role){
        if (user.gamification.activeRole == role){
            return "<a href='#' class='roleButton btn btn-green active'>${message(code:'profile.kuorumStore.roleButton.active')}</a>"
        }else{
            def link = createLink(mapping:"profileActivateAward",params:[award:role])
            return "<a href='${link}' class='roleButton btn btn-green'>${message(code:'profile.kuorumStore.roleButton.activate')}</a>"
        }
    }

    private String roleButtonBuyRole(KuorumUser user, GamificationAward role){
        if (gamificationService.canBuyAward(user, role)){
            def link = createLink(mapping:"profileBuyAward",params:[award:role])
            return "<a href='${link}' class='roleButton btn'>${message(code:'profile.kuorumStore.roleButton.buy')}</a>"
        }else{
            return "<a href='#' class='roleButton btn disabled'>${message(code:'profile.kuorumStore.roleButton.buy')}</a>"
        }
    }
}
