package kuorum.users

import grails.plugin.springsecurity.annotation.Secured
import kuorum.campaign.Campaign
import kuorum.campaign.CampaignService
import kuorum.causes.CausesService
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
import org.kuorum.rest.model.kuorumUser.news.UserNewRSDTO
import org.kuorum.rest.model.kuorumUser.reputation.UserReputationRSDTO
import org.kuorum.rest.model.tag.CauseRSDTO

import javax.servlet.http.HttpServletResponse

class KuorumUserController {

    def springSecurityService
    def kuorumUserService
    def kuorumUserStatsService
    def cluckService
    def searchSolrService
    def postService
    def projectService

    CausesService causesService
    UserNewsService userNewsService

    CampaignService campaignService

    UserReputationService userReputationService;

//    def beforeInterceptor = [action: this.&checkUser, except: 'login']
    def beforeInterceptor = [action: this.&checkUser, except: ['index', 'politicians']]

    private checkUser(){
        KuorumUser user = kuorumUserService.findByAlias(params.userAlias)
        if (!user) {
            //Search by old alias
            user = kuorumUserService.findByOldAlias(params.userAlias)
            if (user){
                def userLink = g.createLink(mapping: 'userShow', params: user.encodeAsLinkProperties())
                response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY)
                response.setHeader("Location", userLink);
                return false
            }else{
                response.sendError(HttpServletResponse.SC_NOT_FOUND)
                return false
            }
        }
    }
    def index(){
        redirect(mapping:'searcherSearch',params: [type:SolrType.KUORUM_USER], permanent: true)
    }

    def politicians(){
        redirect(mapping:'searcherSearch', params:[type:SolrType.POLITICIAN],permanent: true)
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def secShow(String userAlias){
        KuorumUser user = KuorumUser.findByAlias(userAlias)
        log.info("Redirecting to normal user show")
        redirect (mapping:'userShow', params:user.encodeAsLinkProperties())
    }

    @Deprecated
    def show(String id){
        KuorumUser user = KuorumUser.get(new ObjectId(id))
        if (!user){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return false
        }else {
            log.warn("Executing show user")
            switch (user.userType){
                case UserType.ORGANIZATION: return showCitizen(user); break;
                case UserType.PERSON: return showCitizen(user); break;
                case UserType.POLITICIAN: return showPolitician(user); break;
                case UserType.CANDIDATE: return showPolitician(user); break;
            }
        }
    }

    def showWithAlias(String userAlias){
        KuorumUser user = kuorumUserService.findByAlias(userAlias)
        if (!user) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return false
        }
        switch (user.userType){
            case UserType.ORGANIZATION: return showCitizen(user); break;
            case UserType.PERSON: return showCitizen(user); break;
            case UserType.POLITICIAN: return showPolitician(user); break;
            case UserType.CANDIDATE: return showPolitician(user); break;
        }
        log.error("User type (userType=${user.userType}) not found for user ${user.id} (${user})")
        response.sendError(HttpServletResponse.SC_NOT_FOUND)
        return false
    }

    def showCitizen(KuorumUser user){
        if (!user) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return false
        }
        if (user.userType == UserType.POLITICIAN || user.userType == UserType.CANDIDATE){
            redirect(mapping: "userShow", params: user.encodeAsLinkProperties())
            return
        }
        Pagination pagination = new Pagination()
        List<Cluck> clucks = cluckService.userClucks(user)
        Long numClucks = cluckService.countUserClucks(user)
        SearchUserPosts searchUserPosts = new SearchUserPosts(user:user, publishedPosts: true);
        List<Post> userPosts = postService.userPosts(searchUserPosts)
        Long numUserPosts = postService.countUserPost(searchUserPosts)
        List<CauseRSDTO> causes = causesService.findSupportedCauses(user)
        Integer numCauses = causes.size()
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
                        numCauses:numCauses,
                        numUserVictoryPosts:numUserVictoryPosts
                ])
    }

    def showPolitician(KuorumUser politician){
        if (!politician) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return false
        }
        if (politician.userType != UserType.POLITICIAN && politician.userType != UserType.CANDIDATE){
            redirect(mapping: "userShow", params: politician.encodeAsLinkProperties())
            return
        }

        def model = showExtendedPolitician(politician)
        render (view:"showExtendedPolitician", model:model)
        return;
    }

    def showExtendedPolitician(KuorumUser politician){
        List<KuorumUser> recommendPoliticians = kuorumUserService.recommendPoliticians(politician, new Pagination(max:12))
        List<Project> userProjects = projectService.politicianProjects(politician)
        List<CauseRSDTO> causes = causesService.findDefendedCauses(politician)
        Campaign campaign = campaignService.findActiveCampaign(politician)
        UserReputationRSDTO userReputationRSDTO = userReputationService.getReputation(politician)
        List<UserNewRSDTO> userNews = userNewsService.findUserNews(politician)
        [
                politician:politician,
                userProjects:userProjects,
                recommendPoliticians:recommendPoliticians,
                campaign:campaign,
                causes:causes,
                userReputation: userReputationRSDTO,
                userNews:userNews
        ]
    }

    def userClucks(Pagination pagination){
        KuorumUser user = kuorumUserService.findByAlias(params.userAlias)
        if (!user){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        List<Cluck> clucks = cluckService.userClucks(user, pagination)
        response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${clucks.size()<pagination.max}")
        render template: "/cluck/liClucks", model:[clucks:clucks]
    }
    def userPosts(SearchUserPosts searchUserPosts){
        KuorumUser user = kuorumUserService.findByAlias(params.userAlias)
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
        KuorumUser user = kuorumUserService.findByAlias(params.userAlias)
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
        KuorumUser politician = kuorumUserService.findByAlias(params.userAlias)
        if (!politician){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        List<Project> projects = projectService.politicianProjects(politician, pagination)
        response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${projects.size()<pagination.max}")
        render template: "/project/liProjects2Columns", model:[projects:projects]
    }
    def politicianDefendedPosts(SearchUserPosts searchUserPosts){
        KuorumUser user = KuorumUser.findByAlias(params.userAlias)
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
        KuorumUser user = kuorumUserService.findByAlias(params.userAlias)
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


    def userFollowers(String userAlias){
        KuorumUser user = kuorumUserService.findByAlias(userAlias)
        List<KuorumUser> followers = kuorumUserService.findFollowers(user, new Pagination())
        if (request.xhr){
            render (template:'/kuorumUser/embebedUsersList', model:[users:followers])
        }else{
            [users:followers]
        }
    }

    def userFollowing(String userAlias){
        KuorumUser user = kuorumUserService.findByAlias(userAlias)
        List<KuorumUser> following = kuorumUserService.findFollowing(user, new Pagination())
        if (request.xhr){
            render (template:'/kuorumUser/embebedUsersList', model:[users:following])
        }else{
            [users:following]
        }
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def follow(String userAlias){
        KuorumUser following = kuorumUserService.findByAlias(userAlias)
        if (!following){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        KuorumUser follower = KuorumUser.get(springSecurityService.principal.id)
        kuorumUserService.createFollower(follower, following)
        render follower.following.size()
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def unFollow(String userAlias){
        KuorumUser following = kuorumUserService.findByAlias(userAlias)
        if (!following){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        KuorumUser follower = KuorumUser.get(springSecurityService.principal.id)
        kuorumUserService.deleteFollower(follower, following)
        render follower.following.size()
    }
}
