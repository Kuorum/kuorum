package kuorum

import grails.plugin.springsecurity.SpringSecurityService
import kuorum.core.model.search.Pagination
import kuorum.core.model.search.SearchProjects
import kuorum.core.model.solr.SolrResults
import kuorum.post.Post
import kuorum.post.PostService
import kuorum.project.Project
import kuorum.project.ProjectService
import kuorum.solr.SearchSolrService
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserService
import kuorum.web.constants.WebConstants

class DiscoverController {

    ProjectService projectService

    KuorumUserService kuorumUserService

    PostService postService

    SpringSecurityService springSecurityService

    RegionService regionService

    SearchSolrService searchSolrService

    def afterInterceptor = { model, modelAndView ->
        def dynamicDiscoverProjects = []
        List<Region> regions =[]
        if (springSecurityService.isLoggedIn()){
            KuorumUser loggedUser =  KuorumUser.get(springSecurityService.principal.id)
            regions = regionService.findUserRegions(loggedUser)
        }else{
            //TODO: PEnsar cuando no sea solo para espa�a
            regions << Region.findByIso3166_2("EU-ES")
        }
        regions.each {region ->
            int numProjects = Project.countByPublishedAndRegion(true, region)
            if (numProjects>0){
                dynamicDiscoverProjects << [numProjects:numProjects, region:region]
            }
        }
        model.dynamicDiscoverProjects = dynamicDiscoverProjects
    }

    def discoverProjects(SearchProjects searchParams) {
        KuorumUser user = null
        if (springSecurityService.isLoggedIn()){
            user = KuorumUser.get(springSecurityService.principal.id)
        }
//        Region region = Region.findByIso3166_2(params.iso3166_2)
//        List<Project> projects = projectService.relevantProjects(user,region, searchParams)
//        List<Project> projects = searchSolrService.searchProjects(searchParams);
        SolrResults result= searchSolrService.searchProjects(searchParams);
        if (request.isXhr()){
            response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${Project.count()-searchParams.offset<=searchParams.max}")
            render template: '/discover/discoverProjectList', model:[projects:result.getElements(), pagination:searchParams]
        }else{
            [ searchParams: searchParams, result:result]
        }

    }

    def discoverRecommendedPosts(Pagination pagination) {
        KuorumUser user = null
        if (springSecurityService.isLoggedIn()){
            user = KuorumUser.get(springSecurityService.principal.id)
        }
        List<Post> recommendedPost = postService.recommendedPosts(user, null, pagination)
        if (request.isXhr()){
            //TODO: Si es multiplo de 10, hara un true cuando es false
            response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${recommendedPost.size()-pagination.max!=0}")
            render template: '/discover/discoverPostsList', model:[posts:recommendedPost, pagination:pagination]
        }else{
            [posts:recommendedPost, pagination: pagination]
        }
    }

    def discoverRecentPosts(Pagination pagination) {
        List<Post> recommendedPost = postService.lastCreatedPosts(pagination)
        if (request.isXhr()){
            //TODO: Si es multiplo de 10, hara un true cuando es false
            response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${recommendedPost.size()-pagination.max!=0}")
            render template: '/discover/discoverPostsList', model:[posts:recommendedPost, pagination:pagination]
        }else{
            [posts:recommendedPost, pagination: pagination]
        }
    }


    def discoverPoliticians(Pagination pagination) {
        KuorumUser user = null
        if (springSecurityService.isLoggedIn()){
            user = KuorumUser.get(springSecurityService.principal.id)
        }
        List<KuorumUser> politicians = kuorumUserService.bestPoliticiansSince(user, pagination)
        if (request.isXhr()){
            //TODO: Si es multiplo de 10, hara un true cuando es false
            response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${politicians.size()-pagination.max!=0}")
            render template: '/discover/discoverUsersList', model:[users:politicians, pagination:pagination]
        }else{
            [politicians:politicians, pagination: pagination]
        }
    }
}
