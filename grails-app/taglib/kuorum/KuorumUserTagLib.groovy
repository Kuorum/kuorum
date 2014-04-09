package kuorum

import kuorum.users.KuorumUser

class KuorumUserTagLib {
    static defaultEncodeAs = 'raw'
    //static encodeAsForTags = [tagName: 'html']
    static namespace = "user"

    def showUser={attrs ->
        KuorumUser user = attrs.user

        def link = g.createLink(mapping:'userShow', params:user.encodeAsLinkProperties())
        def imgSrc = image.userImgSrc(user:user)
        out << """
            <a href='${link}' itemprop="url">
                <img src="${imgSrc}" alt="${user.name}" class="user-img" itemprop="image"><span itemprop="name">${user.name}</span>
            </a>
            """
    }

    def roleName={attrs ->
        KuorumUser user = attrs.user
        out << g.message(code:"${kuorum.core.model.gamification.GamificationAward.name}.${user.gamification.activeRole}.${user.personalData.gender}")
    }
}
