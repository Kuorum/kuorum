package kuorum

import kuorum.core.model.solr.SolrKuorumUser
import kuorum.core.model.solr.SolrPost
import kuorum.users.KuorumUser
import org.bson.types.ObjectId

class KuorumUserTagLib {
    static defaultEncodeAs = 'raw'
    static encodeAsForTags = [loggedUserName: 'html']

    static namespace = "userUtil"

    def springSecurityService

    def loggedUserName = {attrs ->
        if (springSecurityService.isLoggedIn()){
            out << KuorumUser.get(springSecurityService.principal.id).name
        }
    }

    def showUser={attrs ->
        KuorumUser user
        String name = ""
        if (attrs.user instanceof SolrKuorumUser){
            user = KuorumUser.get(new ObjectId(attrs.user.id))
            name = user.name
        }else if (attrs.user instanceof SolrPost){
            user = KuorumUser.get(new ObjectId(attrs.user.ownerId))
            name = attrs.user.highlighting.owner?:user.name
        }else{
            user = attrs.user
            name = user.name
        }
        Boolean showRole = attrs.showRole?Boolean.parseBoolean(attrs.showRole):false
        Boolean showName = attrs.showName?Boolean.parseBoolean(attrs.showName):true

        def link = g.createLink(mapping:'userShow', params:user.encodeAsLinkProperties())
        def imgSrc = image.userImgSrc(user:user)
        def userName = ""
        if (showName){
            userName = "<span itemprop='name'>${name}</span>"
        }
        out << """
                <span class="popover-trigger" rel="popover" role="button" data-toggle="popover">
                    <img src="${imgSrc}" alt="${user.name}" class="user-img" itemprop="image">${userName}
                </span>
        """
        out << g.render(template: '/kuorumUser/popoverUser', model:[user:user])
        if (showRole){
            out << """
                <span class="user-type">
                    <small>${userUtil.roleName(user:user)}</small>
                </span>
                """
        }
    }

    def showListUsers={attrs->
        List<KuorumUser> users = attrs.users
        if (users){
            Integer visibleUsers=Integer.parseInt(attrs.visibleUsers)?:1
            List<KuorumUser> visibleUsersList = users.take(visibleUsers)
            List<KuorumUser> hiddenUsersList = users.drop(visibleUsers)
            Integer total = (attrs.total?:users.size() ) - visibleUsers
            out << render (template:'/kuorumUser/usersList', model:[
                    users:users,
                    visibleUsers:visibleUsers,
                    visibleUsersList:visibleUsersList,
                    hiddenUsersList:hiddenUsersList,
                    total:total
            ])
        }
    }


    def roleName={attrs ->
        KuorumUser user = attrs.user
        out << g.message(code:"${kuorum.core.model.gamification.GamificationAward.name}.${user.gamification.activeRole}.${user.personalData.gender}")
    }
    def roleNameSolrUser={attrs ->
        SolrKuorumUser user = attrs.user
        out << g.message(code:"${kuorum.core.model.gamification.GamificationAward.name}.${user.role}.${user.gender}")
    }

    def ifIsFollower={attrs, body ->
        KuorumUser user = attrs.user
        if (springSecurityService.isLoggedIn()){
            if (user.following.contains(springSecurityService.principal.id)){
                out << body()
            }
        }
    }
    def isFollower={attrs, body ->
        KuorumUser user
        if (attrs.user instanceof SolrKuorumUser){
            user = KuorumUser.get(new ObjectId(attrs.user.id))
        }else{
            user = attrs.user
        }
        if (springSecurityService.isLoggedIn()){
            if (user.following.contains(springSecurityService.principal.id)){
                out << """
                <div class="pull-right">
                    <span class="fa fa-check-circle-o"></span>
                    <small>${message(code:'kuorumUser.popover.follower')}"</small>
                </div>
                """
            }
        }
    }

    def followButton={attrs ->
        KuorumUser user = attrs.user
        if (springSecurityService.isLoggedIn()){
            def linkAjaxFollow = g.createLink(mapping:'ajaxFollow', params: [id:user.id])
            def linkAjaxUnFollow = g.createLink(mapping:'ajaxUnFollow', params: [id:user.id])
            def isFollowing = user.followers.contains(springSecurityService.principal.id)
            def cssClass = ""
            def text = ""
            if (isFollowing){
                cssClass = "disabled"
                text = "${g.message(code:"kuorumUser.popover.unfollow", codec:"raw")} "
            }else{
                text = g.message(code:"kuorumUser.popover.follow")
            }
            out << """
        <button type="button" class="btn btn-blue btn-xs allow ${cssClass}" id="follow" data-ajaxFollowUrl="${linkAjaxFollow}" data-ajaxUnFollowUrl="${linkAjaxUnFollow}">
            ${text}
        </button> <!-- ESTADO NORMAL permite cambiar de estado al clickar  -->
        """
      }

    }
}
