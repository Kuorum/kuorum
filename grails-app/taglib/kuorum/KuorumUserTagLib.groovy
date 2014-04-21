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
                <span class="popover-trigger" rel="popover" role="button" data-toggle="popover">
                    <img src="${imgSrc}" alt="${user.name}" class="user-img" itemprop="image"><span itemprop="name">${user.name}</span>
                </span>
            <!-- POPOVER PARA IMÁGENES USUARIOS -->
            <div class="popover">

                <a href="#" class="hidden" rel="nofollow">Mostrar usuario</a>
                <div class="popover-user">
                    <div class="user" itemscope itemtype="http://schema.org/Person">
                        <a href="#" itemprop="url">
                            <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"><span itemprop="name">Nombre usuario</span>
                        </a>
                        <span class="user-type">
                            <small>Activista digital</small>
                        </span>
                    </div><!-- /user -->
                    <button type="button" class="btn btn-blue btn-xs allow" id="follow">Seguir</button> <!-- ESTADO NORMAL permite cambiar de estado al clickar  -->
                    <div class="pull-right"><span class="fa fa-check-circle-o"></span> <small>te sigue</small></div>
                    <ul class="infoActivity clearfix">
                        <li><span class="fa fa-question-circle"></span> <span class="counter">31</span> <span class="sr-only">preguntas</span></li>
                        <li><span class="fa fa-book"></span> 25 <span class="sr-only">historias</span></li>
                        <li><span class="fa fa-lightbulb-o"></span> 58 <span class="sr-only">propuestas</span></li>
                        <li><span class="fa fa-user"></span> <small><span class="fa fa-forward"></span></small> 458 <span class="sr-only">siguiendo</span></li>
                        <li><span class="fa fa-user"></span> <small><span class="fa fa-backward"></span></small> 328 <span class="sr-only">seguidores</span></li>
                        <li class="pull-right"><span class="sr-only">Verificada</span> <span class="fa fa-check"></span></li>
                    </ul>

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
