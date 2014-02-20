package kuorum.post

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional
import kuorum.core.exception.KuorumException
import kuorum.core.exception.UtilException
import kuorum.users.KuorumUser
import org.springframework.security.access.prepost.PreAuthorize

@Transactional
class PostService {

    def cluckService
    def springSecurityService
    def indexSolrService
    def postVoteService

    Post savePost(Post post) {

        post.owner = KuorumUser.get(springSecurityService.principal?.id)
        post.numVotes = 1
        post.numClucks = 1

        if (!post.save()){
            KuorumException exception = UtilException.createExceptionFromValidatable(post, "Error salvando el post ${post}")
            log.warn("No se ha podido salvar un post debido a ${post.errors}")
            throw exception
        }
        log.info("Se ha creado el post ${post.id}")

        cluckService.createCluck(post, post.owner)
        postVoteService.votePost(post, post.owner)
        indexSolrService.index(post)
        post
    }

    def updatePost(Post post){
        log.info("Updating post $post")
    }
}
