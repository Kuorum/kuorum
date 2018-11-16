package kuorum

import kuorum.core.customDomain.CustomDomainResolver
import kuorum.register.KuorumUserSession
import kuorum.users.KuorumUser
import org.kuorum.rest.model.contact.ContactRSDTO
import org.kuorum.rest.model.kuorumUser.BasicDataKuorumUserRSDTO
import org.kuorum.rest.model.search.kuorumElement.SearchKuorumUserRSDTO

class ImagesTagLib {
    static defaultEncodeAs = 'html'
    static encodeAsForTags = [showUserImage: 'raw', showYoutube:'raw']
    def springSecurityService

    static namespace = "image"

    def userImgSrc={attrs ->
        String imageUrl = ""
        if (!attrs.user && springSecurityService.isLoggedIn()){
            // USING LOGGED USER
            imageUrl = springSecurityService.principal.avatarUrl
        }else if (attrs.user instanceof KuorumUserSession) {
            imageUrl = attrs.user.avatarUrl
        }else if (attrs.user instanceof BasicDataKuorumUserRSDTO) {
            imageUrl = attrs.user.avatarUrl
        }else if (attrs.user instanceof KuorumUser) {
            imageUrl = attrs.user.avatar?.url
        }else if(attrs.user instanceof SearchKuorumUserRSDTO){
            imageUrl = attrs.user.urlImage
        }else{
            // IS A STRING => Alias
            KuorumUser user = KuorumUser.findByAliasAndDomain(attrs.user, CustomDomainResolver.domain)
            imageUrl = user?.avatar?.url
        }
        if (imageUrl){
            out << imageUrl
        }else{
            out << getDefaultAvatar()
        }
    }
    def userImgProfile={attrs ->
        String imageURL =""
        if (attrs.user instanceof SearchKuorumUserRSDTO){
            imageURL = attrs.user.urlImageProfile
        }else{
            // KUORUM USER
            imageURL = attrs.user.imageProfile?.url
        }
        if (imageURL){
            out << imageURL
        }else{
            out << getDefaultImgProfile()
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
    <div class="video click-handler-no-processed" itemscope itemtype="http://schema.org/VideoObject">
        <meta itemprop="name" content="YouTube" />
        <meta itemprop="thumbnailUrl" content="${imageYoutubeSrc(youtube:attrs.youtube)}" />
        <a href="#" class="front">
            <span class="fas fa-play-circle fa-4x"></span>
            <img itemprop='image' src="${imageYoutubeSrc(youtube:attrs.youtube)}" data-youtubeId="${youtubeFileName}" data-urlYoutubeNotFound="${imageYoutubeNotFound}">
        </a>
        <iframe class="youtube" src="https://www.youtube.com/embed/${youtubeFileName}?fs=1&rel=0&showinfo=0&autoplay=0&enablejsapi=1&showsearch=0" frameborder="0" allowfullscreen></iframe>
    </div>
"""
    }

    def imageYoutubeSrc = {attrs ->
        Boolean maxResolution = attrs.maxResolution?Boolean.parseBoolean(attrs.maxResolution):false
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
            out << userImgSrc()
        }else{
            out << getDefaultAvatar()
        }
    }

    def showUserImage={attrs ->
        out << "<img src='"
        KuorumUser user = null
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
