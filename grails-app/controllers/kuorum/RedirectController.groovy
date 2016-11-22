package kuorum

import kuorum.post.Post
import kuorum.project.Project
import kuorum.project.ProjectService
import kuorum.users.KuorumUser
import org.apache.http.HttpStatus
import org.bson.types.ObjectId
import org.springframework.web.servlet.LocaleResolver

class RedirectController {

    LocaleResolver localeResolver
    ProjectService projectService

    static defaultAction = 'redirect301'

    def redirect301 = {
        def newMapping = params.remove("newMapping")
        def link = g.createLink(mapping: newMapping, params: params)
        response.setHeader "Location", link
        response.status = 301
        render('')
        return false
    }

    def redirect301User = {
        KuorumUser user = KuorumUser.findByAlias(params.userAlias)
        if (user){
            def link = g.createLink(mapping: params.newMapping, params: user.encodeAsLinkProperties())
            response.setHeader "Location", link
            response.status = HttpStatus.SC_MOVED_PERMANENTLY
            render('')
            return false
        }else{
            def link = g.createLink(mapping: 'landingSearch', params: [word:params.urlName])
            link = link + "#results"
            response.setHeader "Location", link
            response.status = HttpStatus.SC_GONE
            flash.message=g.message(code:'redirect.user.notFound')
            render('')
            return false
        }
    }

    def redirect301Project = {
        Project project = projectService.findProjectByHashtag(params.hashtag.encodeAsHashtag())
        if (project){
            def link = g.createLink(mapping: "projectShow", params: project.encodeAsLinkProperties())
            response.setHeader "Location", link
            response.status = HttpStatus.SC_MOVED_PERMANENTLY
            render('')
            return false
        }else{
            def link = g.createLink(mapping: 'landingSearch', params: [word:params.urlName])
            link = link + "#results"
            response.setHeader "Location", link
            response.status = HttpStatus.SC_GONE
            flash.message=g.message(code:'redirect.project.notFound')
            render('')
            return false
        }
    }

    def redirect301Post = {
        Post post =  Post.get(new ObjectId(params.postId))
        if (post){
            def link = g.createLink(mapping: "postShow", params: post.encodeAsLinkProperties())
            response.setHeader "Location", link
            response.status = HttpStatus.SC_MOVED_PERMANENTLY
            render('')
            return false
        }else{
            def link = g.createLink(mapping: 'landingSearch', params: [word:params.urlName])
            link = link + "#results"
            response.setHeader "Location", link
            response.status = HttpStatus.SC_GONE
            flash.message=g.message(code:'redirect.project.notFound')
            render('')
            return false
        }
    }
    
    def blogRedirect(String articlePath){
        if (params.lang){
            render "CloudFront redirect"
            return;
        }
        def urlBlog = articlePath
        Locale locale = localeResolver.resolveLocale(request)
        String subDomain = locale.language=="es"?"es":"en";
        String redirectUrl = "https://kuorum.org/$subDomain/blog/${urlBlog?:''}"
        redirect(url: redirectUrl, permanent: true)
        return;
    }
}
