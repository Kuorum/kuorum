package kuorum.users

import grails.plugin.springsecurity.annotation.Secured
import kuorum.core.model.UserType
import kuorum.core.model.kuorumUser.UserParticipating
import kuorum.core.model.search.Pagination
import kuorum.core.model.search.SearchUserPosts
import kuorum.core.model.solr.SolrType
import kuorum.post.Cluck
import kuorum.post.Post
import kuorum.project.Project
import kuorum.web.constants.WebConstants
import org.bson.types.ObjectId

import javax.servlet.http.HttpServletResponse

class KuorumUserController {

    def springSecurityService
    def kuorumUserService
    def kuorumUserStatsService
    def cluckService
    def searchSolrService
    def postService
    def projectService

//    def beforeInterceptor = [action: this.&checkUser, except: 'login']
    def beforeInterceptor = [action: this.&checkUser, except: ['index', 'politicians', 'showWithAlias']]

    private checkUser(){
        KuorumUser user = KuorumUser.get(new ObjectId(params.id))
        if (!user) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return false
        }
    }
    def index(){
        redirect(mapping:'searcherSearch',params: [type:SolrType.KUORUM_USER], permanent: true)
    }

    def politicians(){
        redirect(mapping:'searcherSearch', params:[type:SolrType.POLITICIAN],permanent: true)
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def secShow(String id){
        KuorumUser user = KuorumUser.get(new ObjectId(id))
        log.info("Redirecting to normal user show")
        redirect (mapping:'userShow', params:user.encodeAsLinkProperties())
    }

    def show(String id){
        KuorumUser user = KuorumUser.get(new ObjectId(id))
        log.warn("Executing show user")
        switch (user.userType){
            case UserType.ORGANIZATION: return showCitizen(id); break;
            case UserType.PERSON: return showCitizen(id); break;
            case UserType.POLITICIAN: return showPolitician(id); break;
        }
    }

    def showWithAlias(String userAlias){
        KuorumUser user = KuorumUser.findByAlias(userAlias.toLowerCase())
        if (!user) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return false
        }
        switch (user.userType){
            case UserType.ORGANIZATION: return showCitizen(user.getId().toString()); break;
            case UserType.PERSON: return showCitizen(user.getId().toString()); break;
            case UserType.POLITICIAN: return showPolitician(user.getId().toString()); break;
        }
        log.error("User type (userType=${user.userType}) not found for user ${user.id} (${user})")
        response.sendError(HttpServletResponse.SC_NOT_FOUND)
        return false
    }

    def showCitizen(String id){
        KuorumUser user = KuorumUser.get(new ObjectId(id))
        if (user.userType == UserType.POLITICIAN){
            redirect(mapping: "userShow", params: user.encodeAsLinkProperties())
            return
        }
        Pagination pagination = new Pagination()
        List<Cluck> clucks = cluckService.userClucks(user)
        Long numClucks = cluckService.countUserClucks(user)
        SearchUserPosts searchUserPosts = new SearchUserPosts(user:user, publishedPosts: true);
        List<Post> userPosts = postService.userPosts(searchUserPosts)
        Long numUserPosts = postService.countUserPost(searchUserPosts)
        SearchUserPosts searchVictoryUserPosts = new SearchUserPosts(user:user, publishedPosts: true,  victory: true);
        List<Post> userVictoryPosts = postService.userPosts(searchVictoryUserPosts)
        Long numUserVictoryPosts = postService.countUserPost(searchVictoryUserPosts)
        List<UserParticipating> activeProjects = kuorumUserService.listUserActivityPerProject(user)
        String provinceName = user.personalData.province?.name
        render (
                view:"show",
                model:[
                        user:user,
                        activeProjects:activeProjects,
                        provinceName:provinceName,
                        clucks:clucks,
                        numClucks:numClucks,
                        userPosts:userPosts,
                        numUserPosts:numUserPosts,
                        userVictoryPosts:userVictoryPosts,
                        numUserVictoryPosts:numUserVictoryPosts
                ])
    }

    def showPolitician(String id){
        KuorumUser politician = KuorumUser.get(new ObjectId(id))
        if (politician.userType != UserType.POLITICIAN){
            redirect(mapping: "userShow", params: politician.encodeAsLinkProperties())
            return
        }

        def model = showExtendedPolitician(politician)
        render (view:"showExtendedPolitician", model:model)
        return;
        String provinceName = politician.personalData.province.name
        if (politician.enabled){
            Pagination pagination = new Pagination()
            List<Project> userProjects = projectService.politicianProjects(politician)
            Long numUserProjects = projectService.countPoliticianProjects(politician)
            SearchUserPosts searchUserPosts = new SearchUserPosts(user:politician, publishedPosts: true);
            List<Post> defendedPosts = postService.politicianDefendedPosts(searchUserPosts)
            Long numDefendedPosts = postService.countPoliticianDefendedPosts(searchUserPosts)
            SearchUserPosts searchVictoryUserPosts = new SearchUserPosts(user:politician, publishedPosts: true,  victory: true);
            List<Post> userVictoryPost = postService.politicianDefendedPosts(searchVictoryUserPosts)
            Long numUserVictoryPost = postService.countPoliticianDefendedPosts(searchVictoryUserPosts)
            PoliticianActivity politicianStats =kuorumUserStatsService.calculatePoliticianActivity(politician)
            render (view:"show",
                    model:[
                            user:politician,
                            userProjects:userProjects,
                            numUserProjects:numUserProjects,
                            defendedPosts:defendedPosts,
                            numDefendedPosts:numDefendedPosts,
                            userVictoryPost:userVictoryPost,
                            numUserVictoryPost:numUserVictoryPost,
                            politicianStats:politicianStats])
        }else{
            render (view:"showInactivePolitician", model:[user:politician, provinceName:provinceName])
        }
    }

    def showExtendedPolitician(KuorumUser politician){
        KuorumUser user = null;
        if (springSecurityService.isLoggedIn()){
            user = springSecurityService.currentUser
        }
//        List<KuorumUser> recommendPoliticians = kuorumUserService.recommendPoliticians(user, new Pagination(max:3))
        List<KuorumUser> recommendPoliticians = []
        List<Project> userProjects = projectService.politicianProjects(politician)
        [
                politician:politician,
                userProjects:userProjects,
                recommendPoliticians:recommendPoliticians
        ]
    }

    def userClucks(Pagination pagination){
        KuorumUser user = KuorumUser.get(new ObjectId(params.id))
        if (!user){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        List<Cluck> clucks = cluckService.userClucks(user, pagination)
        response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${clucks.size()<pagination.max}")
        render template: "/cluck/liClucks", model:[clucks:clucks]
    }
    def userPosts(SearchUserPosts searchUserPosts){
        KuorumUser user = KuorumUser.get(new ObjectId(params.id))
        if (!user){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        searchUserPosts.user = user
        searchUserPosts.publishedPosts =  true
        List<Post> userPost = postService.userPosts(searchUserPosts)
        response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${userPost.size()<searchUserPosts.max}")
        render template: "/cluck/liPosts", model:[posts:userPost]
    }

    def userVictories(SearchUserPosts searchUserPosts){
        KuorumUser user = KuorumUser.get(new ObjectId(params.id))
        if (!user){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        searchUserPosts.user = user
        searchUserPosts.publishedPosts =  true
        searchUserPosts.victory = true
        List<Post> userPost = postService.userPosts(searchUserPosts)
        response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${userPost.size()<searchUserPosts.max}")
        render template: "/cluck/liPosts", model:[posts:userPost]
    }

    def politicianProjects(Pagination pagination){
        KuorumUser politician = KuorumUser.get(new ObjectId(params.id))
        if (!politician){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        List<Project> projects = projectService.politicianProjects(politician, pagination)
        response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${projects.size()<pagination.max}")
        render template: "/project/liProjects2Columns", model:[projects:projects]
    }
    def politicianDefendedPosts(SearchUserPosts searchUserPosts){
        KuorumUser user = KuorumUser.get(new ObjectId(params.id))
        if (!user){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        searchUserPosts.user = user
        searchUserPosts.publishedPosts =  true
        List<Post> userPost = postService.politicianDefendedPosts(searchUserPosts)
        response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${userPost.size()<searchUserPosts.max}")
        render template: "/cluck/liPosts", model:[posts:userPost]
    }

    def politicianDefendedVictories(SearchUserPosts searchUserPosts){
        KuorumUser user = KuorumUser.get(new ObjectId(params.id))
        if (!user){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        searchUserPosts.user = user
        searchUserPosts.publishedPosts =  true
        searchUserPosts.victory = true
        List<Post> userPost = postService.politicianDefendedPosts(searchUserPosts)
        response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${userPost.size()<searchUserPosts.max}")
        render template: "/cluck/liPosts", model:[posts:userPost]
    }


    def userFollowers(String id){
        KuorumUser user = KuorumUser.get(new ObjectId(id))
        List<KuorumUser> followers = kuorumUserService.findFollowers(user, new Pagination())
        if (request.xhr){
            render (template:'/kuorumUser/embebedUsersList', model:[users:followers])
        }else{
            [users:followers]
        }
    }

    def userFollowing(String id){
        KuorumUser user = KuorumUser.get(new ObjectId(id))
        List<KuorumUser> following = kuorumUserService.findFollowing(user, new Pagination())
        if (request.xhr){
            render (template:'/kuorumUser/embebedUsersList', model:[users:following])
        }else{
            [users:following]
        }
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def follow(String id){
        KuorumUser following = KuorumUser.get(new ObjectId(id))
        if (!following){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        KuorumUser follower = KuorumUser.get(springSecurityService.principal.id)
        kuorumUserService.createFollower(follower, following)
        render follower.following.size()

    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def unFollow(String id){
        KuorumUser following = KuorumUser.get(new ObjectId(id))
        if (!following){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        KuorumUser follower = KuorumUser.get(springSecurityService.principal.id)
        kuorumUserService.deleteFollower(follower, following)
        render follower.following.size()
    }

}
