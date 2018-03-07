package kuorum

import kuorum.core.model.UserType
import kuorum.core.model.solr.SolrKuorumUser
import kuorum.users.KuorumUser
import org.kuorum.rest.model.contact.ContactRSDTO
import org.kuorum.rest.model.search.kuorumElement.SearchKuorumUserRSDTO

class ImagesTagLib {
    static defaultEncodeAs = 'html'
    static encodeAsForTags = [showUserImage: 'raw', showYoutube:'raw']
    def springSecurityService

    static namespace = "image"

    def userImgSrc={attrs ->
        KuorumUser user = null;
        if (attrs.user instanceof KuorumUser){
            user = attrs.user
        }else{
            // IS A STRING => Alias
            user = KuorumUser.findByAlias(attrs.user)
        }
        if (user?.avatar){
            out << user.avatar.url
        }else{
            out << getDefaultAvatar()
        }
    }
    def userImgProfile={attrs ->
        KuorumUser user = attrs.user
        if (user.imageProfile){
            out << user.imageProfile.url
        }else{
            out << getDefaultImgProfile()
        }
    }

    def solrUserImgSrc={attrs ->
        SolrKuorumUser user = attrs.user
        if (user && user.urlImage){
            out << user.urlImage
        }else{
            out << getDefaultAvatar()
        }
    }
    def contactImgSrc={attrs ->
        ContactRSDTO contact = attrs.contact
        if (contact.urlImage){
            out << contact.urlImage
        }else{
            out << getDefaultAvatar()
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
        String imageYoutubeNotFound = g.resource(dir:"/images", file: "youtube-broken-link.png", absolute: true)
out << """
    <div class="video" itemscope itemtype="http://schema.org/VideoObject">
        <meta itemprop="name" content="YouTube" />
        <meta itemprop="thumbnailUrl" content="${imageYoutubeSrc(youtube:attrs.youtube)}" />
        <a href="#" class="front">
            <span class="fa fa-play-circle fa-4x"></span>
            <img itemprop='image' src="${imageYoutubeSrc(youtube:attrs.youtube)}" data-youtubeId="${youtubeFileName}" data-urlYoutubeNotFound="${imageYoutubeNotFound}">
        </a>
        <iframe class="youtube" src="https://www.youtube.com/embed/${youtubeFileName}?fs=1&rel=0&showinfo=0&autoplay=0&enablejsapi=1&showsearch=0" frameborder="0" allowfullscreen></iframe>
    </div>
"""
    }

    def imageYoutubeSrc = {attrs ->
        Boolean maxResolution = attrs.maxResolution?Boolean.parseBoolean(attrs.maxResolution):false;
        String youtubeFileName = ""
        if (attrs.youtube instanceof String){
            youtubeFileName = attrs.youtube.decodeYoutubeName()
        }else{
            KuorumFile youtube = attrs.youtube
            youtubeFileName = youtube.fileName
        }
        String screenShot = "mqdefault.jpg" // Si es de alta resolucion se podría poner maxresdefault.jpg
        if (maxResolution){
            screenShot = "maxresdefault.jpg"
        }
        out << "https://img.youtube.com/vi/${youtubeFileName}/${screenShot}"
    }

    def loggedUserImgSrc={attrs ->
        if (springSecurityService.isLoggedIn()){
            KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
            out << userImgSrc(user:user)
        }else{
            out << getDefaultAvatar()
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
            out << getDefaultAvatar()
        }
        out <<"' class='user-img' alt='Tu foto'><span>${userName}</span>"

    }


    private String getDefaultAvatar(){
        //User can be null
        g.resource(dir:'images', file: 'user-default.jpg')
    }

    private String getDefaultImgProfile(){
        //User can be null
        def defaultImage = g.resource(dir:'images', file: 'img-userprofile.jpg')
        defaultImage
    }
}
