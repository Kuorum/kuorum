package kuorum

import kuorum.core.model.UserType
import kuorum.core.model.solr.SolrDebate
import kuorum.core.model.solr.SolrKuorumUser
import kuorum.core.model.solr.SolrPost
import kuorum.post.Post
import kuorum.register.RegisterService
import kuorum.users.KuorumUser
import org.bson.types.ObjectId
import org.kuorum.rest.model.geolocation.RegionRSDTO
import org.kuorum.rest.model.kuorumUser.BasicDataKuorumUserRSDTO
import org.kuorum.rest.model.kuorumUser.reputation.UserReputationRSDTO
import org.springframework.context.i18n.LocaleContextHolder

class KuorumUserTagLib {
    static defaultEncodeAs = 'raw'
    static encodeAsForTags = [loggedUserName: 'html']

    static namespace = "userUtil"

    def springSecurityService
    def userReputationService
    RegisterService registerService
    RegionService regionService;

    private Integer NUM_MAX_ON_USER_LIST = 100

    def loggedUserName = {attrs ->
        if (springSecurityService.isLoggedIn()){
            out << KuorumUser.get(springSecurityService.principal.id).name
        }
    }

    def loggedUserAlias = {attrs ->
        if (springSecurityService.isLoggedIn()){
            out << KuorumUser.get(springSecurityService.principal.id).alias
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

    @Deprecated
    def showUserByAlias= { attrs, body ->
        String alias = attrs.alias
        attrs.showRole
        attrs.showName
        attrs.extraCss
        KuorumUser user = KuorumUser.findByAlias(alias.toLowerCase());
        attrs.put("user", user)
        out << showUser(user:user, showRole: attrs.showRole, showName:attrs.showName, extraCss: attrs.extraCss)
    }
    def showUser={attrs, body ->
        KuorumUser user
        //attrs.withPopover => String expected
        Boolean withPopover = !attrs.withPopover?true:Boolean.parseBoolean(attrs.withPopover)
        String name = ""
        if (attrs.user instanceof SolrKuorumUser){
            user = KuorumUser.get(new ObjectId(attrs.user.id))
            name = user.name
        }else if (attrs.user instanceof SolrPost || attrs.user instanceof SolrDebate ){
            user = KuorumUser.get(new ObjectId(attrs.user.ownerId))
            name = attrs.user.highlighting.owner?:user.name
        }else if (attrs.user instanceof BasicDataKuorumUserRSDTO){
            user = KuorumUser.get(new ObjectId(attrs.user.id))
            name = user.name
        }else{
            user = attrs.user
            name = user.fullName
        }

        Boolean showRole = attrs.showRole?Boolean.parseBoolean(attrs.showRole):false
        Boolean showName = attrs.showName?Boolean.parseBoolean(attrs.showName):true
        Boolean showActions = attrs.showActions?Boolean.parseBoolean(attrs.showActions):false
        Boolean showDeleteRecommendation = attrs.showDeleteRecommendation?Boolean.parseBoolean(attrs.showDeleteRecommendation):false
        String htmlWrapper = attrs.htmlWrapper?:"div"
        String extraCss = attrs.extraCss?:''

//        def link = g.createLink(mapping:'userShow', params:user.encodeAsLinkProperties())
        out << "<${htmlWrapper} class='user ${extraCss} ${showDeleteRecommendation?'recommendation-deletable':''}' itemtype=\"http://schema.org/Person\" itemscope data-userId='${user.id}' data-userAlias='${user.alias}'>"
        def imgSrc = image.userImgSrc(user:user)
        def userName = ""
        if (showName){
            userName = "<span itemprop='name'>${name}</span>"
        }
        def popOverSpanElements = """class="popover-trigger" data-trigger="manual" rel="popover" role="button" data-toggle="popover" """
        if (!withPopover){
            popOverSpanElements = ""
        }
        String targetBlank = "_self";
        if (pageScope.widgetActive){
            targetBlank = "_blank"
        }
        String userLink = g.createLink(mapping: "userShow", params: user.encodeAsLinkProperties())
        out << """
                <a href="${userLink}" $popOverSpanElements target="${targetBlank}" itemprop="url">
                    <img src="${imgSrc}" alt="${user.name}" class="user-img" itemprop="image">
                    ${userName}
                </a>
        """
        if (withPopover) {
            UserReputationRSDTO userReputationRSDTO = userReputationService.getReputation(user)
            out << g.render(template: '/kuorumUser/popoverUser', model: [
                    user: user,
                    targetBlank:targetBlank,
                    userReputation: userReputationRSDTO
            ])
        }
        if(showActions){
            out << "<div class='actions'>"
            out << userUtil.followButton(user: user, cssExtra: 'follow',cssSize:"btn-xs" )
            if (showDeleteRecommendation){
                out << userUtil.deleteRecommendedUserButton(user: user)
            }
            out << "</div>"
        }
//        if (showRole){
//            out << """
//                <span class="user-type">
//                    <small>${userUtil.roleName(user:user)}</small>
//                </span>
//                """
//        }
        out << "</${htmlWrapper}>" //END DIV
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

    // NEGRITA: if !cargo -> "User" else "Cargo"
    // GRIS: if region -> "Region" else nothing

    def politicianPosition={attrs->
        KuorumUser user = attrs.user

        Region regionValue = user?.personalData?.province?:user?.professionalDetails?.region;
        def regionName = "";
        if (regionValue){
            Locale locale = LocaleContextHolder.getLocale();
            try {
                RegionRSDTO regionRSDTO = regionService.findRegionDataById(regionValue.iso3166_2, locale)
                regionName = regionRSDTO?.name ?: regionValue.name
            }catch (Exception e){
                regionName = "";
            }
        }
        out << "${regionName}"

    }

    def roleName={attrs ->
        KuorumUser user = attrs.user
        if (user.userType == UserType.POLITICIAN || user.userType == UserType.CANDIDATE || user.userType == UserType.PERSON){
            String rolePolitician = user.professionalDetails?.position?:message(code:"kuorum.core.model.UserType.${user.userType}")
            out << rolePolitician
        }else{
            out << g.message(code:"${kuorum.core.model.gamification.GamificationAward.name}.${user.gamification.activeRole}.${user.personalData.gender}")
            if (user.organization){
                def link = createLink(mapping: 'userShow', params: user.organization.encodeAsLinkProperties())
                out << "<span class='roleOrganization'>(<a href='$link'>${user.organization.name}</a>)</span>"
            }
        }
    }

    def isPolitician={attrs, body ->
        KuorumUser user = attrs.user
        if ([UserType.POLITICIAN, UserType.CANDIDATE].contains(user.userType)){
            out << body()
        }
    }

    def userTypeIcon={attrs ->
        KuorumUser user = attrs.user
        String faIcon = ""
        String tooltip = "";
        if (user.userType == UserType.PERSON){
            faIcon = "icon-megaphone"
            tooltip = message(code:'kuorum.core.model.UserType.PERSON');
        }else if (user.userType == UserType.ORGANIZATION){
            faIcon = "fa-university"
            tooltip = message(code:'kuorum.core.model.UserType.ORGANIZATION');
        }else if(user.userType == UserType.CANDIDATE){
            faIcon = "icon-megaphone"
            tooltip = message(code:'politician.image.icon.candidate.text');
        }else{
            if (user.enabled){
                faIcon = "fa-check"
                tooltip = message(code:'politician.image.icon.enabled.text');
            }else{
                faIcon = "fa-binoculars"
                tooltip = message(code:'politician.image.icon.notEnabled.text');
            }
        }
        out << """<abbr title="${tooltip}"><i class="fa ${faIcon}"></i></abbr>"""
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

    def followButton={attrs, body ->
        KuorumUser user = attrs.user
        String cssSize = attrs.cssSize?:''
        def linkAjaxFollow = g.createLink(mapping: 'ajaxFollow', params: [userAlias:user.alias])
        def linkAjaxUnFollow = g.createLink(mapping: 'ajaxUnFollow', params: [userAlias:user.alias])
        def prefixMessages = attrs.prefixMessages?: "kuorumUser.follow"
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
                    class="follow btn btn-blue inverted ${cssSize} allow ${cssClass} ${cssExtra}"
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

    def contactButton={attrs, body ->
        Boolean show = attrs.show!=null?attrs.show:true
        if (!show) {
            return
        }
        KuorumUser user = attrs.user
        String cssSize = attrs.cssSize?:''
        def prefixMessages = attrs.prefixMessages?:"kuorumUser.contact"
        def text = "${g.message(code:"${prefixMessages}", args:[user.name])} "
        def cssClass = "enabled"
        def cssExtra =  attrs.cssExtra?:''
        out << """
        <button
                type="button"
                class="contact btn btn-blue ${cssSize} allow ${cssClass} ${cssExtra}"
                data-userId='${user.id}'>
            ${text}
        ${body()}
        </button>
        """
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


    Set<String> kaunasAlias = ['dariusrazmislev', 'rimantasmikaiti','rasasnapstiene','onabalzekiene']
    Set<String> palermoAlias = ['antomonastra', 'toninorusso','totorlando','fabrizioferrara','ferrandelli']
    Set<String> manheimAlias = []
    Set<String> alcobendasAlias = ['marintegracion', 'luismi1980','mjortiz','agustin_martin', 'palomacanos']

    Set<String> weceAlias = kaunasAlias + manheimAlias + alcobendasAlias + palermoAlias
    def isWeceUser= { attrs, body ->
        if (springSecurityService.isLoggedIn()){
            KuorumUser user =  KuorumUser.get(springSecurityService.principal.id)
            if (weceAlias.contains(user.alias)){
                out << body()
            }
        }
    }

    def isNotWeceUser= { attrs, body ->
        if (springSecurityService.isLoggedIn()) {
            KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
            if (!weceAlias.contains(user.alias)) {
                out << body()
            }
        }
    }

    def ifUserIsTheLoggedOne={attrs, body->
        KuorumUser user
        if (attrs.user instanceof KuorumUser) {
            user = attrs.user
        }else if (attrs.user instanceof BasicDataKuorumUserRSDTO){
            user = KuorumUser.get(new ObjectId(attrs.user.id))
        }else{
            user = KuorumUser.findByAlias(attrs.user)
        }
        if (springSecurityService.isLoggedIn()){
            KuorumUser loggedUser = springSecurityService.currentUser;
            if (loggedUser.id == user.id){
                out << body()
            }
        }
    }
    def elseIfUserNotIsTheLoggedOne={attrs, body->
        if (!springSecurityService.isLoggedIn()){
            out << body()
        }else{
            // LOGGED
            KuorumUser user
            if (attrs.user instanceof KuorumUser){
                user = attrs.user
            }else if (attrs.user instanceof BasicDataKuorumUserRSDTO){
                user = KuorumUser.get(new ObjectId(attrs.user.id))
            }else{
                user = KuorumUser.findByAlias(attrs.user)
            }
            KuorumUser loggedUser = springSecurityService.currentUser;
            if (loggedUser.id != user.id){
                out << body()
            }
        }
    }

    def ifLoggedUserHasNotTimeZone = { attrs, body ->
        if (springSecurityService.isLoggedIn() && !((KuorumUser)springSecurityService.currentUser).timeZone) {
            out << body()
        }
    }
}
