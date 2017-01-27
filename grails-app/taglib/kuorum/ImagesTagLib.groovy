package kuorum

import kuorum.core.model.UserType
import kuorum.core.model.solr.SolrKuorumUser
import kuorum.users.KuorumUser
import org.kuorum.rest.model.contact.ContactRSDTO

class ImagesTagLib {
    static defaultEncodeAs = 'html'
    static encodeAsForTags = [showUserImage: 'raw', showYoutube:'raw']
    def springSecurityService

    static namespace = "image"

    def userImgSrc={attrs ->
        KuorumUser user = attrs.user
        if (user?.avatar){
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
    def contactImgSrc={attrs ->
        ContactRSDTO contact = attrs.contact
        if (contact.urlImage){
            out << contact.urlImage
        }else{
            out << getDefaultAvatar(contact)
        }
    }


    def showYoutube ={attrs ->
        String youtubeFileName = ""
        if (attrs.youtube instanceof String){
            youtubeFileName = attrs.youtube.decodeYoutubeName()
        }else{
            KuorumFile youtube = attrs.youtube
            youtubeFileName = youtube.fileName
        }
        String screenShot = "mqdefault.jpg" // Si es de alta resolucion se podría poner maxresdefault.jpg
out << """
    <div class="video">
        <a href="#" class="front">
            <span class="fa fa-play-circle fa-4x"></span>
            <img src="https://img.youtube.com/vi/${youtubeFileName}/${screenShot}">
        </a>
        <iframe class="youtube" itemprop="video" src="https://www.youtube.com/embed/${youtubeFileName}?fs=1&rel=0&showinfo=0&autoplay=0&enablejsapi=1&showsearch=0" frameborder="0" allowfullscreen></iframe>
    </div>
"""
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
        out <<"' class='user-img' alt='Tu foto'><span>${userName}</span>"

    }


    private String getDefaultAvatar(def user){
        //User can be null
        g.resource(dir:'images', file: 'user-default.jpg')
    }

    private String getDefaultImgProfile(KuorumUser user){
        //User can be null
        def defaultImage = g.resource(dir:'images', file: 'img-userprofile.jpg')
        if (user && (user.userType==UserType.POLITICIAN || user.userType==UserType.CANDIDATE)){
            //Inactive politician
            defaultImage = g.resource(dir:'images', file: 'img-politicianprofile.jpg')
        }

        defaultImage
    }
}
