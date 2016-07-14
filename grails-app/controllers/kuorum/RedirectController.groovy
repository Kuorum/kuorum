package kuorum

import kuorum.users.KuorumUser
import org.apache.http.HttpStatus
import org.bson.types.ObjectId
import org.springframework.web.servlet.LocaleResolver

class RedirectController {

    LocaleResolver localeResolver

    static defaultAction = 'redirect301'

    def redirect301 = {
        def link = g.createLink(mapping: params.newMapping)
        response.setHeader "Location", link
        response.status = 301
        render('')
        return false
    }

    def redirect301User = {
        KuorumUser user = KuorumUser.get(new ObjectId(params.id))
        if (user){
            def link = g.createLink(mapping: params.newMapping, params: user.encodeAsLinkProperties())
            response.setHeader "Location", link
            response.status = HttpStatus.SC_MOVED_PERMANENTLY
            render('')
            return false
        }else{
            def link = g.createLink(mapping: 'searcherLanding', params: [word:params.urlName])
            link = link + "#results"
            response.setHeader "Location", link
            response.status = HttpStatus.SC_MOVED_PERMANENTLY
            flash.message=g.message(code:'redirect.user.notFound')
            render('')
            return false
        }
    }

    def blogRedirect(String articlePath){
        def urlBlog = articlePath
        Locale locale = localeResolver.resolveLocale(request)
        String subDomain = locale.language=="es"?"es":"en";
        String redirectUrl = "https://${subDomain}.kuorum.org/blog/${urlBlog?:''}"
        redirect(url: redirectUrl, permanent: true)
        return;
    }
}
