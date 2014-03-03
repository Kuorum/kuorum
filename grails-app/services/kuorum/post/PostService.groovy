package kuorum.post

import com.mongodb.util.JSON
import grails.transaction.Transactional
import kuorum.core.exception.KuorumException
import kuorum.core.exception.KuorumExceptionUtil
import kuorum.users.Politician

@Transactional
class PostService {

    def cluckService
//    def springSecurityService
    def indexSolrService
    def postVoteService
    def notificationService

    /**
     * Save a post and creates the first firstCluck and first vote (owner vote)
     * @param post
     * @return
     */
    Post savePost(Post post) {

        //post.owner = KuorumUser.get(springSecurityService.principal?.id)
        post.numVotes = 0
        post.numClucks = 0

        if (!post.save()){
            KuorumException exception = KuorumExceptionUtil.createExceptionFromValidatable(post, "Error salvando el post ${post}")
            log.warn("No se ha podido salvar un post debido a ${post.errors}")
            throw exception
        }
        log.info("Se ha creado el post ${post.id}")

        Cluck cluck = cluckService.createCluck(post, post.owner)
        post.firstCluck = cluck  //Ref to first firstCluck
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
            Cluck.collection.update ( [_id:post.firstCluck.id, 'sponsors.kuorumUserId':sponsor.kuorumUser.id],['$set':['sponsors.$.amount':amount]])
        }else{
            //NEW SPONSOR
            def sponsorData = [kuorumUserId:sponsor.kuorumUser.id, amount:sponsor.amount]
            //ATOMIC OPERATION
            Post.collection.update ( [_id:post.id],['$push':['sponsors':sponsorData]])
            //ATOMIC OPERATION
            Cluck.collection.update ( [_id:post.firstCluck.id],['$push':['sponsors':sponsorData]])

        }
        Post.collection.update ( [_id:post.id],['$push':['sponsors':[$each: [],$sort:[amount:1]]]])

        //Reloading data from DDBB
        post.refresh()
        post.firstCluck.refresh()
    }

    Post addComment(Post post, PostComment comment){
        //Atomic operation
        def commentData = [kuorumUserId: comment.kuorumUser.id, text:comment.text, dateCreated: new Date()]
        Post.collection.update ( [_id:post.id],['$push':['comments':commentData]])
        post.refresh()
        post
    }

    Post addDebate(Post post, PostComment comment){
        if (comment.kuorumUser.instanceOf(Politician) || post.owner == comment.kuorumUser){
            //Atomic operation
            def commentData = [kuorumUserId: comment.kuorumUser.id, text:comment.text, dateCreated: new Date()]
            Post.collection.update ( [_id:post.id],['$push':['debates':commentData]])
            post.refresh()
            notificationService.sendDebateNotification(post)
            post
        }else{
            throw new KuorumException("El usuario no es el dueño o un político", "error.security.post.notDebateAllowed")
        }
    }
}
