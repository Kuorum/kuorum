package kuorum

import kuorum.users.KuorumUser

class KuorumUserTagLib {
    static defaultEncodeAs = 'raw'
    static encodeAsForTags = [loggedUserName: 'html']
    static namespace = "user"

    def springSecurityService

    def loggedUserName = {attrs ->
        if (springSecurityService.isLoggedIn()){
            out << KuorumUser.get(springSecurityService.principal.id).name
        }
    }

    def showUser={attrs ->
        KuorumUser user = attrs.user

        def link = g.createLink(mapping:'userShow', params:user.encodeAsLinkProperties())
        def imgSrc = image.userImgSrc(user:user)
        out << """
            <a href='${link}' itemprop="url">
                <img src="${imgSrc}" alt="${user.name}" class="user-img" itemprop="image"><span itemprop="name">${user.name}</span>
            </a>
            <!-- POPOVER PARA IMÁGENES USUARIOS -->
            <div class="popover hide">
            <a href="#" class="hidden" rel="nofollow">Mostrar usuario</a>

            <div class="popover-user">

            <div class="user" itemscope itemtype="http://schema.org/Person">
            <a href="#" itemprop="url">
            <img src="images/user.jpg" alt="nombre" class="user-img"
            itemprop="image"><span itemprop="name">Nombre usuario</span>
            </a>
            <span class="user-type"><small>Activista digital</small></span>
            </div><!-- /user -->

            </div><!-- /popover-user -->

            </div>
            <!-- FIN POPOVER PARA IMÁGENES USUARIOS -->
            """
    }

    def roleName={attrs ->
        KuorumUser user = attrs.user
        out << g.message(code:"${kuorum.core.model.gamification.GamificationAward.name}.${user.gamification.activeRole}.${user.personalData.gender}")
    }
}
