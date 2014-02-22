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
//    def springSecurityService
    def indexSolrService
    def postVoteService

    /**
     * Save a post and creates the first cluck and first vote of the owner
     * @param post
     * @return
     */
    Post savePost(Post post) {

        //post.owner = KuorumUser.get(springSecurityService.principal?.id)
        post.numVotes = 1
        post.numClucks = 1

        if (!post.save()){
            KuorumException exception = UtilException.createExceptionFromValidatable(post, "Error salvando el post ${post}")
            log.warn("No se ha podido salvar un post debido a ${post.errors}")
            throw exception
        }
        log.info("Se ha creado el post ${post.id}")

        Cluck cluck = cluckService.createCluck(post, post.owner)
        post.cluck = cluck  //Ref to first cluck
        post.save()

        postVoteService.votePost(post, post.owner)
        indexSolrService.index(post)
        post
    }

    def updatePost(Post post){
        log.info("Updating post $post")
    }

    void sponsorAPost(Post post, Sponsor sponsor){

        Sponsor alreadySponsor = post.sponsors.find{it == sponsor}
        if (alreadySponsor){
            double amount = alreadySponsor.amount + sponsor.amount
            Post.collection.update ( [_id:post.id,        'sponsors.kuorumUserId':sponsor.kuorumUser.id],['$set':['sponsors.$.amount':amount]])
            Cluck.collection.update ( [_id:post.cluck.id, 'sponsors.kuorumUserId':sponsor.kuorumUser.id],['$set':['sponsors.$.amount':amount]])
        }else{
            //NEW SPONSOR
            def sponsorData = [kuorumUserId:sponsor.kuorumUser.id, amount:sponsor.amount]
            //ATOMIC OPERATION
            Post.collection.update ( [_id:post.id],['$push':['sponsors':sponsorData]])
            //ATOMIC OPERATION
            Cluck.collection.update ( [_id:post.cluck.id],['$push':['sponsors':sponsorData]])

        }
        Post.collection.update ( [_id:post.id],['$push':['sponsors':[$each: [],$sort:[amount:1]]]])

        //Reloading data from DDBB
        post.refresh()
    }
}
