package kuorum

import kuorum.users.KuorumUser
import org.bson.types.ObjectId

class RedirectController {

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
        def link = g.createLink(mapping: params.newMapping, params: user.encodeAsLinkProperties())
        response.setHeader "Location", link
        response.status = 301
        render('')
        return false
    }
}
