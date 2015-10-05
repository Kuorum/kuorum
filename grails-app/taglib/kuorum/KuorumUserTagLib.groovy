package kuorum

import grails.plugin.springsecurity.ui.RegistrationCode
import kuorum.core.model.UserType
import kuorum.core.model.solr.SolrKuorumUser
import kuorum.core.model.solr.SolrPost
import kuorum.core.model.solr.SolrProject
import kuorum.post.Post
import kuorum.register.RegisterService
import kuorum.users.KuorumUser
import org.bson.types.ObjectId

class KuorumUserTagLib {
    static defaultEncodeAs = 'raw'
    static encodeAsForTags = [loggedUserName: 'html']

    static namespace = "userUtil"

    def springSecurityService
    RegisterService registerService
    private Integer NUM_MAX_ON_USER_LIST = 100

    def loggedUserName = {attrs ->
        if (springSecurityService.isLoggedIn()){
            out << KuorumUser.get(springSecurityService.principal.id).name
        }
    }

    def showLoggedUser={attrs ->
        attrs.showRole
        attrs.showName
        if (springSecurityService.isLoggedIn()){
            KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
            out << showUser(user:user, showRole: attrs.showRole, showName:attrs.showName)
        }
    }

    def showUser={attrs ->
        KuorumUser user
        //attrs.withPopover => String expected
        Boolean withPopover = !attrs.withPopover?true:Boolean.parseBoolean(attrs.withPopover)
        String name = ""
        if (attrs.user instanceof SolrKuorumUser){
            user = KuorumUser.get(new ObjectId(attrs.user.id))
            name = user.name
        }else if (attrs.user instanceof SolrPost || attrs.user instanceof SolrProject ){
            user = KuorumUser.get(new ObjectId(attrs.user.ownerId))
            name = attrs.user.highlighting.owner?:user.name
        }else{
            user = attrs.user
            name = user.name
        }
        Boolean showRole = attrs.showRole?Boolean.parseBoolean(attrs.showRole):false
        Boolean showName = attrs.showName?Boolean.parseBoolean(attrs.showName):true
        Boolean showActions = attrs.showActions?Boolean.parseBoolean(attrs.showActions):false
        Boolean showDeleteRecommendation = attrs.showDeleteRecommendation?Boolean.parseBoolean(attrs.showDeleteRecommendation):false

//        def link = g.createLink(mapping:'userShow', params:user.encodeAsLinkProperties())
        def imgSrc = image.userImgSrc(user:user)
        def userName = ""
        if (showName){
            userName = "<span itemprop='name'>${name}</span>"
        }
/*        def popOverSpanElements = """class="popover-trigger" rel="popover" role="button" data-toggle="popover" """
        if (!withPopover){
            popOverSpanElements = ""
        }
*/
        out << """
                <span>
                    <img src="${imgSrc}" alt="${user.name}" class="user-img" itemprop="image">${userName}
                </span>
        """
        if (withPopover){
            out << g.render(template: '/kuorumUser/popoverUser', model:[user:user])
        }
        if (showRole){
            out << """
                <span class="user-type">
                    <small>${userUtil.roleName(user:user)}</small>
                </span>
                """
        }
        if(showActions){
            out << "<div class='actions'>"
            out << userUtil.followButton(user: user, cssExtra: 'follow',cssSize:"btn-xs" )
            if (showDeleteRecommendation){
                out << userUtil.deleteRecommendedUserButton(user: user)
            }
            out << "</div>"
        }

    }

    def showDebateUsers={attrs->
        Post post = attrs.post
        Integer visibleUsers=Integer.parseInt(attrs.visibleUsers.toString())?:1
        List<KuorumUser> debateUsers = post.debates.kuorumUser.unique().minus(post.owner)
        if (debateUsers){
            out << showListUsers(users:debateUsers, visibleUsers:visibleUsers, messagesPrefix: "cluck.debateUsers")
        }
    }

    def showListUsers={attrs->
        List<KuorumUser> users = attrs.users.unique()
        String cssClass = attrs.cssClass?:'user-list-images'
        if (users){
            Integer visibleUsers=Integer.parseInt(attrs.visibleUsers.toString())?:1
            List<KuorumUser> visibleUsersList = users.take(visibleUsers)
            List<KuorumUser> hiddenUsersList = users.drop(visibleUsers)
            Integer total = (attrs.total?:users.size() ) - visibleUsers
            String messagePrefix = attrs.messagesPrefix
            def messages = [
                    intro:message(code:"${messagePrefix}.intro", default: ''),
                    seeMore:message(code:"${messagePrefix}.seeMore",default: ''),
                    showUserList:message(code:"${messagePrefix}.showUserList",default: ''),
                    userListTitle:message(code:"${messagePrefix}.userListTitle",default: '')
            ]
            out << render (template:'/kuorumUser/usersList', model:[
                    users:users,
                    visibleUsers:visibleUsers,
                    visibleUsersList:visibleUsersList,
                    hiddenUsersList:hiddenUsersList,
                    total:total,
                    cssClass:cssClass,
                    messages:messages
            ])
        }
    }

    def listFollowers={attrs ->
        KuorumUser user = attrs.user
        if (!user && springSecurityService.isLoggedIn()){
            user = KuorumUser.get(springSecurityService.principal.id)
        }
        if (!user){
            throw Exception("Si no est� logado el usuario es necesario indicar el usuario")
        }
        List<ObjectId> userIdsLimited = user.followers.size()>NUM_MAX_ON_USER_LIST? user.followers.take(NUM_MAX_ON_USER_LIST):user.followers
        List<KuorumUser> users = userIdsLimited.collect{id -> KuorumUser.load(id)}
        out << showListUsers(users:users, visibleUsers:"13", messagesPrefix: 'kuorumUser.show.follower.userList', total:user.followers.size())
    }

    def listFollowing={attrs ->
        KuorumUser user = attrs.user
        if (!user && springSecurityService.isLoggedIn()){
            user = KuorumUser.get(springSecurityService.principal.id)
        }
        if (!user){
            throw new Exception("Si no esta logado el usuario es necesario indicar el usuario")
        }
        List<ObjectId> userIdsLimited = user.following.size()>NUM_MAX_ON_USER_LIST? user.following.take(NUM_MAX_ON_USER_LIST):user.following
        List<KuorumUser> users = userIdsLimited.collect{id -> KuorumUser.load(id)}
        out << showListUsers(users:users, visibleUsers:"13", messagesPrefix: 'kuorumUser.show.following.userList', total:user.following.size())
    }

    def counterUserLikes={attrs->
        Post post = attrs.post

        Integer total=post.numVotes
        String messagesPrefix="cluck.footer.likes.counter"
        def ajaxUrl = createLink(mapping:'postVotesList', params: post.encodeAsLinkProperties())
        out << counterUsers(total:total, messagesPrefix:messagesPrefix, ajaxUrl:ajaxUrl)
    }

    def counterUserClucks={attrs->
        Post post = attrs.post

        Integer total=post.numClucks
        String messagesPrefix="cluck.footer.clukers.counter"
        def ajaxUrl = createLink(mapping:'postClucksList', params: post.encodeAsLinkProperties())
        out << counterUsers(total:total, messagesPrefix:messagesPrefix, ajaxUrl:ajaxUrl)
    }

    def counterFollowers={attrs->
        KuorumUser user = attrs.user

        Integer numFollowers = user.followers.size()
        String messagesPrefix="dashboard.userProfile.followers"
        def ajaxUrl = createLink(mapping:'userFollowers', params: user.encodeAsLinkProperties())
        out << counterUsers(total:numFollowers, messagesPrefix:messagesPrefix, ajaxUrl:ajaxUrl)
    }
    def counterFollowing={attrs->
        KuorumUser user = attrs.user

        Integer numFollowing = user.following.size()
        String messagesPrefix="dashboard.userProfile.following"
        def ajaxUrl = createLink(mapping:'userFollowing', params: user.encodeAsLinkProperties())
        out << counterUsers(total:numFollowing, messagesPrefix:messagesPrefix, ajaxUrl:ajaxUrl)
    }

    def counterUsers={attrs ->
        def total = attrs.total
        String messagesPrefix = attrs.messagesPrefix
        String ajaxUrl = attrs.ajaxUrl

        String title = message(code:"${messagesPrefix}.title")
        String linkText = message(code:"${messagesPrefix}.link")
        String label = message(code:"${messagesPrefix}.label", default: '')

        out << render (
                template:'/kuorumUser/counterUsers',
                model:[total:total,title:title,linkText:linkText, ajaxUrl:ajaxUrl,label:label]
        )
    }

    def roleName={attrs ->
        KuorumUser user = attrs.user
        if (user.userType == UserType.POLITICIAN){
            String rolePolitician = user.politicalParty.name
            if (!user.enabled)
                rolePolitician = "${g.message(code:"kuorumUser.role.politicianInactive")} (${rolePolitician})"
            out << rolePolitician
        }else{
            out << g.message(code:"${kuorum.core.model.gamification.GamificationAward.name}.${user.gamification.activeRole}.${user.personalData.gender}")
            if (user.organization){
                def link = createLink(mapping: 'userShow', params: user.organization.encodeAsLinkProperties())
                out << "<span class='roleOrganization'>(<a href='$link'>${user.organization.name}</a>)</span>"
            }
        }
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
                    <small>${message(code:'kuorumUser.popover.follower')}</small>
                </div>
                """
            }
        }
    }

    def followButton={attrs ->
        KuorumUser user = attrs.user
        String cssSize = attrs.cssSize?:''
        def linkAjaxFollow = g.createLink(mapping:'ajaxFollow', params: [id:user.id])
        def linkAjaxUnFollow = g.createLink(mapping:'ajaxUnFollow', params: [id:user.id])
        def prefixMessages = attrs.prefixMessages?:"kuorumUser.follow"
        def text = "${g.message(code:"${prefixMessages}.follow", args:[user.name], codec:"raw")} "
        def cssClass = "enabled"
        def cssExtra =  attrs.cssExtra?:''
        if (springSecurityService.isLoggedIn() && springSecurityService.principal.id != user.id){
            def isFollowing = user.followers.contains(springSecurityService.principal.id)
            if (isFollowing){
                cssClass = "disabled"
                text = "${g.message(code:"${prefixMessages}.unfollow", args:[user.name], codec:"raw")} "
            }
        }else if (!springSecurityService.isLoggedIn()){
            cssClass += " noLogged"
        }

        if (springSecurityService.isLoggedIn() && springSecurityService.principal.id != user.id || !springSecurityService.isLoggedIn()){
            out << """
            <button
                    type="button"
                    class="follow btn btn-blue ${cssSize} allow ${cssClass} ${cssExtra}"
                    data-ajaxFollowUrl="${linkAjaxFollow}"
                    data-ajaxUnFollowUrl="${linkAjaxUnFollow}"
                    data-message-follow_hover='${g.message(code:"${prefixMessages}.follow_hover", args:[user.name], codec:"raw")}'
                    data-message-follow='${g.message(code:"${prefixMessages}.follow", args:[user.name], codec:"raw")}'
                    data-message-unfollow_hover='${g.message(code:"${prefixMessages}.unfollow_hover", args:[user.name], codec:"raw")}'
                    data-message-unfollow='${g.message(code:"${prefixMessages}.unfollow", args:[user.name], codec:"raw")}'
                    data-userId='${user.id}'>
                ${text}
            </button> <!-- ESTADO NORMAL permite cambiar de estado al clickar  -->
            """
        }

    }

    def showMailConfirm = {attrs ->
        String token = registerService.findOrRegisterUserCode(KuorumUser.get(springSecurityService.principal.id)).token
        String url = createLink(absolute: true, controller: 'register', action: 'sendConfirmationEmail',params: [t: token])
        out <<  message(code: "register.confirmEmail.label", args:[url])
    }


    def deleteRecommendedUserButton={attrs ->

        KuorumUser user = attrs.user
        //TODO: Este código lo ha copiado y pegado Salenda. Ahora tengo que mirar para que sirve el showNoLoggedButton, que no se usa en ningun lado
        Boolean showNoLoggedButton = attrs.showNoLoggedButton?:Boolean.FALSE
        def linkAjaxDeleteRecommendedUser = g.createLink(mapping:'ajaxDeleteRecommendedUser', params: [deletedUserId:user.id])
        def linkNoLoggedFollow = g.createLink(mapping:'secUserShow', params: user.encodeAsLinkProperties())

        def prefixMessages = attrs.prefixMessages?:"kuorumUser.follow"
        def text = "${g.message(code:"${prefixMessages}.follow", args:[user.name], codec:"raw")} "


        if (springSecurityService.isLoggedIn() || !springSecurityService.isLoggedIn() && showNoLoggedButton){
            out << """
            <button
                    type="button"
                    class="close"
                    data-noLoggedUrl="${linkNoLoggedFollow}"
                    data-ajaxDeleteRecommendedUserUrl="${linkAjaxDeleteRecommendedUser}"
                    data-userId='${user.id}'>
                <span class="fa fa-times-circle-o fa"></span><span class="sr-only">eliminar de la lista</span>
            </button>
            """
        }
    }
}
