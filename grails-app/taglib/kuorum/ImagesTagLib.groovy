package kuorum

import kuorum.users.KuorumUser

class ImagesTagLib {
    static defaultEncodeAs = 'html'
    //static encodeAsForTags = [tagName: 'raw']

    static namespace = "image"

    def userImgSrc={attrs ->
        KuorumUser user = attrs.user
        if (user.avatar){
            out << user.avatar.url
        }else{
            out << getDefaultAvatar(user)
        }
    }


    private String getDefaultAvatar(KuorumUser user){
        g.resource(dir:'images', file: 'user.jpg')
    }
}
