package kuorum

import kuorum.post.Post
import kuorum.users.KuorumUser

class PostTagLib {
    static defaultEncodeAs = 'html'
    static encodeAsForTags = [removeCommentButton: 'raw']

    def springSecurityService
    def postService

    static namespace = "post"

    def removeCommentButton={attrs ->
        Post post = attrs.post
        Integer commentPosition = attrs.commentPosition
        if (springSecurityService.isLoggedIn()){
            KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
            if (postService.isCommentDeletableByUser(user,post,commentPosition)){
                String link = createLink(mapping:"postDelComment",params: post.encodeAsLinkProperties()+[commentPosition:commentPosition])
                out << "<a href='$link'>BORRAR</a>"
            }
        }
    }
}
