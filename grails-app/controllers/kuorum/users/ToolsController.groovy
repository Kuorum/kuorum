package kuorum.users

import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import kuorum.core.model.gamification.GamificationAward
import kuorum.core.model.search.Pagination
import kuorum.core.model.search.SearchNotifications
import kuorum.core.model.search.SearchUserPosts
import kuorum.notifications.Notification
import kuorum.notifications.NotificationService
import kuorum.post.Post
import kuorum.post.PostService
import kuorum.project.Project
import kuorum.project.ProjectService
import kuorum.util.Order
import kuorum.web.constants.WebConstants

class ToolsController {

    SpringSecurityService springSecurityService
    PostService postService
    GamificationService gamificationService
    NotificationService notificationService
    ProjectService projectService

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


    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
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

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def showFavoritesPosts() {
        KuorumUser user = params.user
        List<Post> favorites = postService.favoritesPosts(user)
        [user:user, favorites: favorites]
    }


    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def showUserPosts(SearchUserPosts searchUserPosts) {
        KuorumUser user = params.user
        searchUserPosts.user =  user
        List<Post> posts = postService.userPosts(searchUserPosts)
        if (request.xhr){
            response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${posts.size()<searchUserPosts.max}")
            render template: "/tools/userPostsList", model:[posts:posts]
        }else{
            [user:user,posts:posts, searchUserPosts:searchUserPosts]
        }
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def kuorumStore() {
        KuorumUser user = params.user
        [user:user]
    }
    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
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

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
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

    @Secured(['ROLE_POLITICIAN'])
    def ajaxShowProjectListOfUsers(Pagination pagination){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        Map projectsOrderListOfUser = projectService.search(user, params.sort, Order.findByValue(params.order),
                params.published?Boolean.parseBoolean(params.published):null, pagination.offset, pagination.max)
        Integer totalProjects = Project.countByOwner(user)
        Integer publishedProjects = Project.countByOwnerAndPublished(user, true)
        Integer draftProjects = Project.countByOwnerAndPublished(user, false)
        Boolean seeMore

        if(params.published){
            if(Boolean.parseBoolean(params.published)){
                seeMore = projectsOrderListOfUser.projects?projectsOrderListOfUser.projects.size() < publishedProjects:false
            } else {
                seeMore = projectsOrderListOfUser.projects?projectsOrderListOfUser.projects.size() < draftProjects:false
            }
        } else {
            seeMore = projectsOrderListOfUser.projects?projectsOrderListOfUser.projects.size() < totalProjects:false
        }
        response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${projectsOrderListOfUser.projects?projectsOrderListOfUser.projects.size() < pagination.max:true}")
        render template: params.template?:"projects", model: [projects: projectsOrderListOfUser.projects, order: params.order,
                sort: params.sort, published: params.published, max: pagination.max, offset: pagination.offset,
                totalProjects: totalProjects, publishedProjects: publishedProjects, draftProjects: draftProjects,
                seeMore: seeMore, urlLoadMore: params.urlLoadMore]
    }

    @Secured(['ROLE_POLITICIAN'])
    def listProjects(){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        String sort = 'dateCreated'
        Order order = Order.ASC
        Pagination pagination = new Pagination()
        Map projectsOrderListOfUser = projectService.search(user, sort, order, null, pagination.offset, pagination.max)
        Integer totalProjects = Project.countByOwner(user)
        Integer publishedProjects = Project.countByOwnerAndPublished(user, true)
        Integer draftProjects = Project.countByOwnerAndPublished(user, false)
        Boolean seeMore = projectsOrderListOfUser.projects?projectsOrderListOfUser.projects.size() < totalProjects:false

        [projects: projectsOrderListOfUser.projects, order: order.value, sort: sort, published: '', max: pagination.max, offset: pagination.offset,
                totalProjects: totalProjects, publishedProjects: publishedProjects, draftProjects: draftProjects,
                seeMore: seeMore]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def publishProject(String hashtag){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        Project project = projectService.findProjectByHashtag(hashtag.encodeAsHashtag())
        projectService.publish(project)


        Map projectsOrderListOfUser = projectService.search(user, params.sort, Order.findByValue(params.order),
                params.published?Boolean.parseBoolean(params.published):null, 0, params.max.toInteger())
        Integer totalProjects = Project.countByOwner(user)
        Integer publishedProjects = Project.countByOwnerAndPublished(user, true)
        Integer draftProjects = Project.countByOwnerAndPublished(user, false)
        Boolean seeMore

        if(params.published){
            if(Boolean.parseBoolean(params.published)){
                seeMore = projectsOrderListOfUser.projects?projectsOrderListOfUser.projects.size() < publishedProjects:false
            } else {
                seeMore = projectsOrderListOfUser.projects?projectsOrderListOfUser.projects.size() < draftProjects:false
            }
        } else {
            seeMore = projectsOrderListOfUser.projects?projectsOrderListOfUser.projects.size() < totalProjects:false
        }
        response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${projectsOrderListOfUser.projects?projectsOrderListOfUser.projects.size() < params.max.toInteger():true}")
        String publishMessage = message(code:'admin.publishProject.success', args: [project.hashtag])
        render template: "projects", model: [projects: projectsOrderListOfUser.projects, order: params.order,
                sort: params.sort, published: params.published, max: params.max, offset: params.offset,
                totalProjects: totalProjects, publishedProjects: publishedProjects, draftProjects: draftProjects,
                seeMore: seeMore, urlLoadMore: params.urlLoadMore, publishMessage: publishMessage]
    }


}
