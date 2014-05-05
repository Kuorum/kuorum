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
    def userImgProfile={attrs ->
        KuorumUser user = attrs.user
        if (user.imageProfile){
            out << user.imageProfile.url
        }else{
            out << getDefaultImgProfile(user)
        }
    }

    def solrUserImgSrc={attrs ->
        SolrKuorumUser user = attrs.user
        if (user.urlImage){
            out << user.urlImage
        }else{
            out << getDefaultAvatar(user)
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


    private String getDefaultAvatar(def user){
        //User can be null
        g.resource(dir:'images', file: 'user-default.jpg')
    }

    private String getDefaultImgProfile(def user){
        //User can be null
        g.resource(dir:'images', file: 'img-userprofile.jpg')
    }
}
