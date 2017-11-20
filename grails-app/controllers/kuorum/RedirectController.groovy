package kuorum

import kuorum.project.Project
import kuorum.project.ProjectService
import kuorum.users.KuorumUser
import org.apache.http.HttpStatus
import org.kuorum.rest.model.communication.debate.DebateRSDTO
import org.springframework.web.servlet.LocaleResolver
import payment.campaign.DebateService

class RedirectController {

    LocaleResolver localeResolver
    ProjectService projectService
    DebateService debateService;

    static defaultAction = 'redirect301'

    def redirect301 = {
        def newMapping = params.remove("newMapping")
        log.info("OLD URL: Redirecting to ${newMapping} with params: ${params}")
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
            def link = g.createLink(mapping: 'searcherSearch', params: [word:params.urlName])
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
        DebateRSDTO debateMoved = null;
        if (project){
            List<DebateRSDTO> debates = debateService.findAllDebates(project.owner)
            debateMoved = debates.find{it.title == project.shortName}
        }
        if (debateMoved){
            def link = g.createLink(mapping: "debateShow", params: debateMoved.encodeAsLinkProperties())

            response.setHeader "Location", link
            response.status = HttpStatus.SC_MOVED_PERMANENTLY
            render('')
            return false
        }else{
            def link = g.createLink(mapping: 'searcherSearch', params: [word:params.hashtag])
            response.setHeader "Location", link
            response.status = HttpStatus.SC_GONE
//            flash.message=g.message(code:'redirect.project.notFound')
            render("<script>window.location = '${link}' ;</script>")
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
