package kuorum.post

import grails.transaction.Transactional
import kuorum.core.exception.KuorumException
import kuorum.core.exception.KuorumExceptionData
import kuorum.core.exception.UtilException
import kuorum.users.KuorumUser

@Transactional
class PostVoteService {

    def votePost(Post post, KuorumUser kuorumUser){
        if (post.id == null || kuorumUser.id == null){
            throw new KuorumException("El post o el usuario no se han salvado previamente en BBDD","error.postVoteService.paramsError")
        }
        PostVote postVote = new PostVote()
        postVote.personalData = kuorumUser.personalData
        postVote.post = post
        postVote.user = kuorumUser
        if (!postVote)
            throw UtilException.createExceptionFromValidatable(postVote,"Error salvando el post")
        postVote

    }
}
