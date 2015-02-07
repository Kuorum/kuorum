package kuorum.users

import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import kuorum.core.model.gamification.GamificationAward
import kuorum.core.model.search.SearchNotifications
import kuorum.core.model.search.SearchUserPosts
import kuorum.notifications.Notification
import kuorum.notifications.NotificationService
import kuorum.post.Post
import kuorum.post.PostService
import kuorum.web.constants.WebConstants

class ToolsController {

    SpringSecurityService springSecurityService
    PostService postService
    GamificationService gamificationService
    NotificationService notificationService

    def beforeInterceptor ={
        if (springSecurityService.isLoggedIn()){
            //Este if es para la confirmacion del email
            KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
            params.user = user
        }
    }
    def afterInterceptor = [action: this.&prepareMenuData]

    private prepareMenuData(model) {
        KuorumUser user = params.user
        model.menu = [
                activeNotifications:Notification.countByKuorumUserAndIsAlertAndIsActive(user, true, true),
                unpublishedPosts:postService.numUnpublishedUserPosts(user),
                favorites: postService.favoritesPosts(user).size(),
                unreadMessages:3
        ]
    }

    def index() {}


    def userNotifications(SearchNotifications searchNotificationsCommand) {
        KuorumUser user = params.user
        searchNotificationsCommand.user = user
        List<Notification> notifications = notificationService.findUserNotifications(searchNotificationsCommand)
        if (request.xhr){
            response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${notifications.size()<searchNotificationsCommand.max}")
            render template: "/tools/usrNotificationsList", model:[notifications:notifications]
        }else{
            [user:user, notifications:notifications, searchNotificationsCommand:searchNotificationsCommand]
        }
    }

    def showFavoritesPosts() {
        KuorumUser user = params.user
        List<Post> favorites = postService.favoritesPosts(user)
        [user:user, favorites: favorites]
    }


    def showUserPosts(SearchUserPosts searchUserPosts) {
        KuorumUser user = params.user
        searchUserPosts.user =  user
        List<Post> posts = postService.findUserPosts(searchUserPosts)
        if (request.xhr){
            response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${posts.size()<searchUserPosts.max}")
            render template: "/tools/userPostsList", model:[posts:posts]
        }else{
            [user:user,posts:posts, searchUserPosts:searchUserPosts]
        }
    }

    def kuorumStore() {
        KuorumUser user = params.user
        [user:user]
    }
    def kuorumStoreActivateAward() {
        GamificationAward award = GamificationAward.valueOf(params.award)
        KuorumUser user = params.user
        if (gamificationService.canActivateAward(user,award)){
            gamificationService.activateAward(user,award)
            render ([
                    numEggs : user.gamification.numEggs,
                    numCorns : user.gamification.numCorns,
                    numPlumes : user.gamification.numPlumes
            ] as JSON)
        }else{
            render "AJAX NOT ALLOWED"
        }
    }

    def kuorumStoreBuyAward() {
        GamificationAward award = GamificationAward.valueOf(params.award)
        KuorumUser user = params.user
        if (gamificationService.canBuyAward(user,award)){
            gamificationService.buyAward(user,award)
            render ([
                    numEggs : user.gamification.numEggs,
                    numCorns : user.gamification.numCorns,
                    numPlumes : user.gamification.numPlumes
            ] as JSON)
        }else{
            render "AJAX  no hay dinerito"
        }
    }
}
