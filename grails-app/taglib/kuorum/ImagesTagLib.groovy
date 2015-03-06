package kuorum

import kuorum.core.model.UserType
import kuorum.core.model.solr.SolrKuorumUser
import kuorum.users.KuorumUser

class ImagesTagLib {
    static defaultEncodeAs = 'html'
    static encodeAsForTags = [showUserImage: 'raw']
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
        if (user && user.urlImage){
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

    def showUserImage={attrs ->
        out << "<img src='"
        KuorumUser user = null;
        String userName = "Tu nombre"
        if (springSecurityService.isLoggedIn()){
            user = KuorumUser.get(springSecurityService.principal.id)
            userName = user.name
            out << userImgSrc(user:user)
        }else{
            out << getDefaultAvatar(null)
        }
        out <<"' class='user-img' alt='Tu fotografía'><span>${userName}</span>"

    }


    private String getDefaultAvatar(def user){
        //User can be null
        g.resource(dir:'images', file: 'user-default.jpg')
    }

    private String getDefaultImgProfile(KuorumUser user){
        //User can be null
        def defaultImage = g.resource(dir:'images', file: 'img-userprofile.jpg')
        if (user && user.userType==UserType.POLITICIAN && !user.enabled){
            //Inactive politician
            defaultImage = g.resource(dir:'images', file: 'img-politicianprofile.jpg')
        }

        defaultImage
    }
}
