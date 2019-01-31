package kuorum

import kuorum.register.KuorumUserSession
import kuorum.register.RegisterService
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserService
import org.bson.types.ObjectId
import org.kuorum.rest.model.geolocation.RegionRSDTO
import org.kuorum.rest.model.kuorumUser.BasicDataKuorumUserRSDTO
import org.kuorum.rest.model.kuorumUser.UserTypeRSDTO
import org.kuorum.rest.model.search.SearchKuorumElementRSDTO
import org.kuorum.rest.model.search.kuorumElement.SearchKuorumUserRSDTO
import org.springframework.context.i18n.LocaleContextHolder

class KuorumUserTagLib {
    static defaultEncodeAs = 'raw'
    static encodeAsForTags = [loggedUserName: 'html']

    static namespace = "userUtil"

    def springSecurityService
    def userReputationService
    RegisterService registerService
    RegionService regionService
    KuorumUserService kuorumUserService

    private Integer NUM_MAX_ON_USER_LIST = 100

    def loggedUserName = {attrs ->
        if (springSecurityService.isLoggedIn()){
            out << springSecurityService.principal.name
        }
    }

    def loggedUserId = { attrs ->
        if (springSecurityService.isLoggedIn()){
            out << springSecurityService.principal.id.toString()
        }
    }

    def showUser={attrs, body ->
        BasicDataKuorumUserRSDTO user // KuorumUser or BasicDataKuorumUserRSDTO
        //attrs.withPopover => String expected
        Boolean withPopover = !attrs.withPopover?true:Boolean.parseBoolean(attrs.withPopover)
        String name = ""
        String role
        def imgSrc
        if (attrs.user instanceof SearchKuorumUserRSDTO){
            role = getRoleUser(attrs.user)
            imgSrc = image.userImgSrc(user:attrs.user)
            user = kuorumUserService.findBasicUserRSDTO(attrs.user.id)
            name = highlightedField(attrs.user, "owner")
            name = name?:user.fullName
        }else if (attrs.user instanceof SearchKuorumElementRSDTO){
            user = kuorumUserService.findBasicUserRSDTO(attrs.user.ownerId)
            name = highlightedField(attrs.user, "owner")
            name = name?:user.fullName
            role = getRoleUser(user)
            imgSrc = image.userImgSrc(user:user)
        }else if (attrs.user instanceof String){
            // ALIAS
            user = kuorumUserService.findBasicUserRSDTO(attrs.user)
            name = user.fullName
            role = getRoleUser(user)
            imgSrc = image.userImgSrc(user:user)
        }else{
            // BasicDataKuorumUserRSDTO
            user = attrs.user
            name = user.fullName
            role = getRoleUser(user)
            imgSrc = image.userImgSrc(user:user)
        }

        Boolean showRole = role && (attrs.showRole?Boolean.parseBoolean(attrs.showRole):false)
        Boolean showName = attrs.showName?Boolean.parseBoolean(attrs.showName):true
        Boolean showActions = attrs.showActions?Boolean.parseBoolean(attrs.showActions):false
        Boolean showDeleteRecommendation = attrs.showDeleteRecommendation?Boolean.parseBoolean(attrs.showDeleteRecommendation):false
        String itemprop=attrs.itemprop?"itemprop='${attrs.itemprop}'":''

        String htmlWrapper = attrs.htmlWrapper?:"div"
        String extraCss = attrs.extraCss?:''
        if (showRole){
            extraCss = extraCss + " show-user-role"
        }

//        def link = g.createLink(mapping:'userShow', params:user.encodeAsLinkProperties())
        out << "<${htmlWrapper} class='user ${extraCss} ${showDeleteRecommendation?'recommendation-deletable':''}' ${itemprop} itemtype=\"http://schema.org/Person\" itemscope data-userId='${user.id}' data-userAlias='${user.alias}'>"
        def userName = ""
        if (showName){
            userName = "<span itemprop='name'>${name}</span>"
        }
        def popOverSpanElements = """class="popover-trigger" data-trigger="manual" rel="popover" role="button" data-toggle="popover" """
        if (!withPopover){
            popOverSpanElements = ""
        }
        String targetBlank = "_self"
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
        if (showRole){
            out << """
                <span class="user-type">
                    ${role}
                </span>
                """
        }

        if (withPopover) {
//            UserReputationRSDTO userReputationRSDTO = userReputationService.getReputationWithCache(user)
            out << g.render(template: '/kuorumUser/popoverUser', model: [
                    user: user,
                    targetBlank:targetBlank
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

        out << "</${htmlWrapper}>" //END DIV
    }

    def showListUsers={attrs->
        List<BasicDataKuorumUserRSDTO> users = attrs.users.unique()
        String cssClass = attrs.cssClass?:'user-list-images'
        if (users){
            Integer visibleUsers=Integer.parseInt(attrs.visibleUsers.toString())?:1
            List<BasicDataKuorumUserRSDTO> visibleUsersList = users.take(visibleUsers)
            List<BasicDataKuorumUserRSDTO> hiddenUsersList = users.drop(visibleUsers)
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

    def counterFollowers={attrs->
        BasicDataKuorumUserRSDTO user = attrs.user

        Integer numFollowers = user.numFollowers
        String messagesPrefix="dashboard.userProfile.followers"
        def ajaxUrl = createLink(mapping:'userFollowers', params: user.encodeAsLinkProperties())
        out << counterUsers(total:numFollowers, messagesPrefix:messagesPrefix, ajaxUrl:ajaxUrl)
    }
    def counterFollowing={attrs->
        BasicDataKuorumUserRSDTO user = attrs.user

        Integer numFollowing = user.numFollowing
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

    def userRegionName ={ attrs->
        String regionIso = ""
        if (attrs.user instanceof KuorumUser){
            KuorumUser user = attrs.user
            Region regionValue = user?.personalData?.province
            regionIso = regionValue?.iso3166_2
        }else if (attrs.user instanceof SearchKuorumUserRSDTO){
            regionIso = attrs.user.regionIso
        }

        def regionName = ""
        if (regionIso){
            Locale locale = LocaleContextHolder.getLocale()
            try {
                RegionRSDTO regionRSDTO = regionService.findRegionDataById(regionIso, locale)
                regionName = regionRSDTO?.name
            }catch (Exception e){
                regionName = ""
            }
        }
        out << "${regionName?:''}"

    }

    def roleName={attrs ->
        String role = getRoleUser(attrs.user)
        if (role){
            out << role
        }
    }

    private String getRoleUser(SearchKuorumUserRSDTO user){
        return user?.roleName?:""
    }

    private String getRoleUser(BasicDataKuorumUserRSDTO user){
        return user?.roleName?:""
    }

    def userTypeIcon={attrs ->
        BasicDataKuorumUserRSDTO user = attrs.user
        String faIcon = ""
        String tooltip = ""
//        if (user.isValid){ // Logic showing users with check when is validated against census
//            faIcon = "fa-check"
//            tooltip = message(code:'politician.image.icon.enabled.text')
//        }else if (user.userType == UserTypeRSDTO.PERSON){
        if (user.userType == UserTypeRSDTO.PERSON){
            faIcon = "fa-megaphone"
            tooltip = message(code:'kuorum.core.model.UserType.PERSON')
        }else if (user.userType == UserTypeRSDTO.ORGANIZATION){
            faIcon = "fa-university"
            tooltip = message(code:'kuorum.core.model.UserType.ORGANIZATION')
//        }else if(user.userType == UserType.CANDIDATE){
//            faIcon = "icon-megaphone"
//            tooltip = message(code:'politician.image.icon.candidate.text');
        }else {
            faIcon = "fa-binoculars"
            tooltip = message(code:'politician.image.icon.notEnabled.text')
        }
        out << """<abbr title="${tooltip}"><i class="fas ${faIcon}"></i></abbr>"""
    }

    def ifIsFollower={attrs, body ->
        if (attrs.user instanceof KuorumUser){
            if (springSecurityService.isLoggedIn()){
                KuorumUser user = attrs.user
                if (user.following.contains(springSecurityService.principal.id)){
                    out << body()
                }
            }
        }else if (attrs.user instanceof SearchKuorumUserRSDTO){
            if (attrs.user.isFollower){
                out << body()
            }
        }
    }


    def followButton={attrs, body ->
        String cssSize = attrs.cssSize?:''
        String alias = ""
        String name = ""
        Boolean isFollowing = false
        String userId = ""
        if (attrs.user instanceof KuorumUser){
            KuorumUser user = attrs.user
            alias = user.alias
            name = user.fullName
            isFollowing = springSecurityService.isLoggedIn() && springSecurityService.principal.id != user.id && user.followers.contains(springSecurityService.principal.id)
            userId = attrs.user.id.toString()
        }else if (attrs.user instanceof SearchKuorumUserRSDTO) {
            alias = attrs.user.alias
            name = attrs.user.name
            isFollowing = attrs.user.isFollowing
//            isFollowing = false
            userId = attrs.user.id
        }else if (attrs.user instanceof BasicDataKuorumUserRSDTO){
            alias = attrs.user.alias
            name = attrs.user.fullName
            isFollowing = attrs.user.following
            userId = attrs.user.id
        }else{
            throw UnsupportedOperationException("Unkonwn user type")
        }
        def linkAjaxFollow = g.createLink(mapping: 'ajaxFollow', params: [userAlias:alias])
        def linkAjaxUnFollow = g.createLink(mapping: 'ajaxUnFollow', params: [userAlias:alias])
        def prefixMessages = attrs.prefixMessages?: "kuorumUser.follow"
        def text = "${g.message(code:"${prefixMessages}.follow", args:[name], codec:"raw")} "
        def cssClass = "enabled"
        def cssExtra =  attrs.cssExtra?:''

        if (isFollowing){
            cssClass = "disabled"
            text = "${g.message(code:"${prefixMessages}.unfollow", args:[name], codec:"raw")} "
        }else if (!springSecurityService.isLoggedIn()){
            cssClass += " noLogged"
        }

        if (springSecurityService.isLoggedIn() && springSecurityService.principal.id.toString() != userId || !springSecurityService.isLoggedIn()){
            out << """
            <button
                    type="button"
                    class="follow btn btn-blue inverted ${cssSize} allow ${cssClass} ${cssExtra}"
                    data-ajaxFollowUrl="${linkAjaxFollow}"
                    data-ajaxUnFollowUrl="${linkAjaxUnFollow}"
                    data-message-follow_hover='${g.message(code:"${prefixMessages}.follow_hover", args:[name], codec:"raw")}'
                    data-message-follow='${g.message(code:"${prefixMessages}.follow", args:[name], codec:"raw")}'
                    data-message-unfollow_hover='${g.message(code:"${prefixMessages}.unfollow_hover", args:[name], codec:"raw")}'
                    data-message-unfollow='${g.message(code:"${prefixMessages}.unfollow", args:[name], codec:"raw")}'
                    data-userId='${userId}'>
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
        BasicDataKuorumUserRSDTO user = attrs.user
        String cssSize = attrs.cssSize?:''
        def prefixMessages = attrs.prefixMessages?:"kuorumUser.contact"
        def text = "${g.message(code:"${prefixMessages}", args:[user.name])} "
        def cssClass = "enabled"
        def cssExtra =  attrs.cssExtra?:''
        String loggedUserId = ""
        if (springSecurityService.isLoggedIn()){
            loggedUserId = springSecurityService.principal.id
        }
        out << """
        <button
                type="button"
                class="contact btn btn-blue ${cssSize} allow ${cssClass} ${cssExtra}"
                data-userId='${user.id}'
                data-loggedUser='${loggedUserId}'>
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

        def user = attrs.user // KuorumUser or BasicDataKuorumUser
        def linkAjaxDeleteRecommendedUser = g.createLink(mapping:'ajaxDeleteRecommendedUser', params: [deletedUserId:user.id])
        def linkNoLoggedFollow = g.createLink(mapping:'secUserShow', params: user.encodeAsLinkProperties())

        def prefixMessages = attrs.prefixMessages?:"kuorumUser.follow"
        def text = "${g.message(code:"${prefixMessages}.follow", args:[user.name], codec:"raw")} "


        if (springSecurityService.isLoggedIn()){
            out << """
            <button
                    type="button"
                    class="close"
                    data-noLoggedUrl="${linkNoLoggedFollow}"
                    data-ajaxDeleteRecommendedUserUrl="${linkAjaxDeleteRecommendedUser}"
                    data-userId='${user.id}'>
                <span class="fal fa-times-circle"></span><span class="sr-only">eliminar de la lista</span>
            </button>
            """
        }
    }

    def ifUserIsTheLoggedOne={attrs, body->
        ObjectId userId
        if (attrs.user instanceof KuorumUser) {
            throw new Exception("Using old KuorumUser")
            userId = attrs.user.id
        }else if (attrs.user instanceof BasicDataKuorumUserRSDTO){
            userId = new ObjectId(attrs.user.id)
        }else{
            // ALIAS -- DEPRECATED BRANCH
            throw new Exception("Using alias instead BasicDataKuorumUser")
            BasicDataKuorumUserRSDTO user = kuorumUserService.findBasicUserRSDTO(attrs.user)
            userId = new ObjectId(user.id)
        }
        if (springSecurityService.isLoggedIn()){
            KuorumUserSession loggedUser = springSecurityService.principal
            if (loggedUser.id.equals(userId)){
                out << body()
            }
        }
    }
    def elseIfUserNotIsTheLoggedOne={attrs, body->
        if (!springSecurityService.isLoggedIn()){
            out << body()
        }else{
            // LOGGED
            ObjectId userId
            if (attrs.user instanceof KuorumUser){
                throw new Exception("Using old KuorumUser")
                userId = attrs.user.id
            }else if (attrs.user instanceof BasicDataKuorumUserRSDTO){
                userId = new ObjectId(attrs.user.id)
            }else{
                throw new Exception("Using alias instead BasicDataKuorumUser")
                // BY ALIAS -- DEPRECATED BRANCH
                BasicDataKuorumUserRSDTO user = kuorumUserService.findBasicUserRSDTO(attrs.user)
                userId = user.id
            }
            KuorumUserSession loggedUser = springSecurityService.principal
            if (loggedUser.id != userId){
                out << body()
            }
        }
    }

    def ifLoggedUserHasNotTimeZone = { attrs, body ->
        if (springSecurityService.isLoggedIn() && !((KuorumUser)springSecurityService.currentUser).timeZone) {
            out << body()
        }
    }

    private static final Integer MAX_LENGTH_TEXT = 300
    private String highlightedField(SearchKuorumElementRSDTO element, String field, Integer maxLength = MAX_LENGTH_TEXT){

        String res = ""
        if (element.highlighting?."$field"){
            res = element.highlighting."$field"
        }else if (element.hasProperty(field) && element."${field}"){
            res = element."${field}"
            if (res){
                res = res.substring(0, Math.min(res.length(), maxLength))
            }
        }
        if (res && res.length() < element."${field}".length()){
            res += " ..."
        }
        return res
    }
}
