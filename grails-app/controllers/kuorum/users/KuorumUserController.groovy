package kuorum.users

import grails.plugin.springsecurity.annotation.Secured
import kuorum.Institution
import kuorum.core.model.UserType
import kuorum.core.model.kuorumUser.UserParticipating
import kuorum.core.model.search.Pagination
import kuorum.core.model.search.SearchPolitician
import kuorum.core.model.solr.SolrPoliticiansGrouped
import kuorum.post.Cluck
import kuorum.web.constants.WebConstants
import org.bson.types.ObjectId

import javax.servlet.http.HttpServletResponse

class KuorumUserController {

    static scaffold = true
    def springSecurityService
    def kuorumUserService
    def cluckService
    def searchSolrService

//    def beforeInterceptor = [action: this.&checkUser, except: 'login']
    def beforeInterceptor = [action: this.&checkUser, except: ['index', 'politicians']]

    private checkUser(){
        KuorumUser user = KuorumUser.get(new ObjectId(params.id))
        if (!user) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return false
        }
    }
    def index(){
        def maxElemens = grailsApplication.config.kuorum.seo.maxElements
        [userType: params.userTypeUrl, users:KuorumUser.findAllByUserType(params.userTypeUrl,[max:1000])]
    }

    def politicians(){
        SearchPolitician searchParams = new SearchPolitician(max: 1000, regionIso3166_2: params.regionIso3166_2)
        def groupPoliticians =[:]
        if (params.institutionName){
            searchParams.institutionName = params.institutionName
            List<SolrPoliticiansGrouped> politiciansPerInstitution = searchSolrService.listPoliticians(searchParams)
            if (politiciansPerInstitution){
                searchParams.institutionName = politiciansPerInstitution.politicians[0][0].institutionName
            }
            groupPoliticians.put(searchParams.institutionName  , politiciansPerInstitution)
        }else{
            Institution.list().each {
                searchParams.institutionName = it.name
                List<SolrPoliticiansGrouped> politiciansPerInstitution = searchSolrService.listPoliticians(searchParams)
                if (politiciansPerInstitution)
                    groupPoliticians.put("${it.name}" , politiciansPerInstitution)
            }
        }
        [groupPoliticians:groupPoliticians]
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
            case UserType.ORGANIZATION: return showOrganization(id); break;
            case UserType.PERSON: return showCitizen(id); break;
            case UserType.POLITICIAN: return showPolitician(id); break;
        }
    }

    def showCitizen(String id){
        KuorumUser user = KuorumUser.get(new ObjectId(id))
        if (user.userType != UserType.PERSON){
            redirect(mapping: "userShow", params: user.encodeAsLinkProperties())
            return
        }
        Pagination pagination = new Pagination()
        List<Cluck> clucks = cluckService.userClucks(user, pagination)
        List<UserParticipating> activeLaws = kuorumUserService.listUserActivityPerLaw(user)
        String provinceName = user.personalData.province?.name
        render (view:"show", model:[user:user, clucks:clucks, activeLaws:activeLaws, provinceName:provinceName, seeMore: clucks.size()==pagination.max])
    }

    def showOrganization(String id){
        KuorumUser user = KuorumUser.get(new ObjectId(id))
        if (user.userType != UserType.ORGANIZATION){
            redirect(mapping: "userShow", params: user.encodeAsLinkProperties())
            return
        }
        Pagination pagination = new Pagination()
        List<Cluck> clucks = cluckService.userClucks(user, pagination)
        List<UserParticipating> activeLaws = kuorumUserService.listUserActivityPerLaw(user)
        String provinceName = user.personalData.country.name
        render (view:"show", model:[user:user, clucks:clucks, activeLaws:activeLaws, provinceName:provinceName, seeMore: clucks.size()==pagination.max])
    }

    def showPolitician(String id){
        KuorumUser politician = KuorumUser.get(new ObjectId(id))
        if (politician.userType != UserType.POLITICIAN){
            redirect(mapping: "userShow", params: politician.encodeAsLinkProperties())
            return
        }
        String provinceName = politician.personalData.province.name
        if (politician.enabled){
            Pagination pagination = new Pagination()
            List<Cluck> clucks = cluckService.userClucks(politician, pagination)
            List<UserParticipating> activeLaws = kuorumUserService.listUserActivityPerLaw(politician)
            def politicianStats =[postDefended:0, victories:0, debates:0]
            render (view:"show", model:[user:politician, clucks:clucks, activeLaws:activeLaws, provinceName:provinceName,politicianStats:politicianStats, seeMore: clucks.size()==pagination.max])
        }else{
            render (view:"showInactivePolitician", model:[user:politician, provinceName:provinceName])
        }
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
