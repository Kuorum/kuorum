package kuorum

import grails.plugin.springsecurity.SpringSecurityService
import kuorum.core.model.search.Pagination
import kuorum.law.Law
import kuorum.law.LawService
import kuorum.post.Post
import kuorum.post.PostService
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserService
import kuorum.web.constants.WebConstants

class DiscoverController {

    LawService lawService

    KuorumUserService kuorumUserService

    PostService postService

    SpringSecurityService springSecurityService

    RegionService regionService

    def afterInterceptor = { model, modelAndView ->
        def dynamicDiscoverLaws = []
        List<Region> regions =[]
        if (springSecurityService.isLoggedIn()){
            regions = regionService.findUserRegions(springSecurityService.currentUser)
        }else{
            //TODO: PEnsar cuando no sea solo para españa
            regions << Region.findByIso3166_2("EU-ES")
        }
        regions.each {region ->
            int numLaws = Law.countByPublishedAndRegion(true, region)
            if (numLaws>0){
                dynamicDiscoverLaws << [numLaws:numLaws, region:region]
            }
        }
        model.dynamicDiscoverLaws = dynamicDiscoverLaws
    }

    def discoverLaws(Pagination pagination) {
        KuorumUser user = null
        if (springSecurityService.isLoggedIn()){
            user = KuorumUser.get(springSecurityService.principal.id)
        }
        Region region = Region.findByIso3166_2(params.iso3166_2)
        List<Law> laws = lawService.relevantLaws(user,region, pagination)
        if (request.isXhr()){
            response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${Law.count()-pagination.offset<=pagination.max}")
            render template: '/discover/discoverLawList', model:[laws:laws, pagination:pagination]
        }else{
            [laws:laws, pagination: pagination]
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
        List<KuorumUser> politicians = kuorumUserService.bestPoliticiansSince(user, new Date() -7, pagination)
        if (request.isXhr()){
            //TODO: Si es multiplo de 10, hara un true cuando es false
            response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${politicians.size()-pagination.max!=0}")
            render template: '/discover/discoverUsersList', model:[users:politicians, pagination:pagination]
        }else{
            [politicians:politicians, pagination: pagination]
        }
    }
}
