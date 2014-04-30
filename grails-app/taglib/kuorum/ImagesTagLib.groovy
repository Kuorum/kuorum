package kuorum

import kuorum.core.model.solr.SolrKuorumUser
import kuorum.users.KuorumUser

class ImagesTagLib {
    static defaultEncodeAs = 'html'
    //static encodeAsForTags = [tagName: 'raw']
    def springSecurityService

    static namespace = "image"

    def userImgSrc={attrs ->
        KuorumUser user = attrs.user
        if (user.avatar){
            out << user.avatar.url
        }else{
            out << getDefaultAvatar(user)
        }
    }

    def solrUserImgSrc={attrs ->
        SolrKuorumUser user = attrs.user
        if (user.urlImage){
            out << user.urlImage
        }else{
            out << getDefaultAvatar(null)
        }
    }


    def loggedUserImgSrc={attrs ->
        if (springSecurityService.isLoggedIn()){
            KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
            out << userImgSrc(user:user)
        }else{
            out << getDefaultAvatar(null)
        }
    }


    private String getDefaultAvatar(KuorumUser user){
        //User can be null
        g.resource(dir:'images', file: 'pre-user.jpg')
    }
}
