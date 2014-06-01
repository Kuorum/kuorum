package kuorum.post

import com.mongodb.BasicDBObject
import com.mongodb.DBObject
import grails.transaction.Transactional
import kuorum.core.exception.KuorumException
import kuorum.core.exception.KuorumExceptionUtil
import kuorum.core.model.CommitmentType
import kuorum.core.model.PostType
import kuorum.core.model.UserType
import kuorum.core.model.search.Pagination
import kuorum.core.model.search.SearchUserPosts
import kuorum.law.Law
import kuorum.users.KuorumUser

@Transactional
class PostService {

    def cluckService
    def grailsApplication
    def indexSolrService
    def postVoteService
    def notificationService
    def gamificationService
    def fileService
    def shortUrlService

    /**
     * Save a post and creates the first firstCluck and first vote (owner vote)
     * @param post post data
     * @param law law's post
     * @param owner The persona who has created the post
     * @return
     */
    Post savePost(Post post, Law law, KuorumUser owner) {

        if (!law || !owner){
            KuorumException exception = new KuorumException("No se ha indicado el dieño ${owner} o la ley ${law}", "error.post.saving.data")
            log.error("No se ha indicado el dieño ${owner} o la ley ${law}")
            throw exception
        }
        post.numVotes = 0
        post.numClucks = 0
        post.owner = owner
        post.law =  law
        post.text = removeCustomCrossScripting(post.text)

        if (post.multimedia){
            fileService.convertTemporalToFinalFile(post.multimedia)
            fileService.deleteTemporalFiles(owner)
        }

        if (!post.save()){
            KuorumException exception = KuorumExceptionUtil.createExceptionFromValidatable(post, "Error salvando el post ${post}")
            log.warn("No se ha podido salvar un post debido a ${post.errors}")
            throw exception
        }
        post.shortUrl = shortUrlService.shortUrl(post) //Short URL needs the post id
        post.save()
        log.info("Se ha creado el post ${post.id}")
        post
    }

    Post publishPost(Post post){
        if (!post.published){
            Cluck cluck = cluckService.createCluck(post, post.owner)
            post.firstCluck = cluck  //Ref to first firstCluck
            post.published = Boolean.TRUE
            post.shortUrl = shortUrlService.shortUrl(post)
            post.save()
            updateUserActivity(post, post.owner)
            postVoteService.votePost(post, post.owner)
            indexSolrService.index(post)
            log.info("Se ha publicado el post ${post.id}")
        }else{
            log.warn("Se ha intentado publicar 2 veces el post ${post.id}")
        }
        post
    }

    KuorumUser updateUserActivity(Post post, KuorumUser user){
        switch (post.postType){
            case PostType.HISTORY:
                user.activity.histories << post.id
                user.activity.numHistories ++
                break;
            case PostType.PURPOSE:
                user.activity.purposes<< post.id
                user.activity.numPurposes ++
                break;
            case PostType.QUESTION:
                user.activity.questions<< post.id
                user.activity.numQuestions ++
                break;
        }
        user.save()
    }

    Boolean isEditableByUser(Post post, KuorumUser user){
        post &&(
                user.authorities.find{it.authority=="ROLE_ADMIN"} ||
                    (post.owner == user &&
                            !post.defender &&
                            post.debates.isEmpty() &&
                            post.numVotes < getPostConfiguration().limitVotesToBeEditable
                    )
        )
    }
    Boolean isDeletableByUser(Post post, KuorumUser user){
        post &&(
                user.authorities.find{it.authority=="ROLE_ADMIN"} ||
                        (post.owner == user &&
                                !post.defender &&
                                post.debates.isEmpty() &&
                                post.numVotes < getPostConfiguration().limitVotesToBeDeletable
                        )
        )
    }

    void deletePost(Post post, KuorumUser user){
        if (!isDeletableByUser(post,user)){
            throw new KuorumException("El usuario ${user} no puede borrar el post ${post.id}","")
        }
        indexSolrService.delete(post)
        PostVote.collection.remove([post:post.id])
        post.delete()
    }

    def updatePost(Post post){
        log.info("Updating post $post")
        post.text = removeCustomCrossScripting(post.text)
        if (post.multimedia){
            fileService.convertTemporalToFinalFile(post.multimedia)
            fileService.deleteTemporalFiles(post.owner)
        }
        post.mongoUpdate()
    }

    /**
     * Removes all not allowed html tags
     * @param raw
     * @return
     */
    private String removeCustomCrossScripting(String raw){
        String text
        if (raw){
            def openTags = ~/<[^\/ibau]r{0,1} *[^>]*>/  // Only allow <a> <b> <i> <u> <br>
            def closeTags = ~/<\/[^ibau] *[^>]*>/ // Only allow </a> </b> </i> </u>
            text = raw.replaceAll(openTags,'')
            text = text.replaceAll(closeTags,'')

            def notAllowedAttributes = ~/(<[abiu]r{0,1})([^h>]*)(href=[^ >]*){0,1}([^h>]*)(>)/ //Delete all atributes that are not href
            text = text.replaceAll(notAllowedAttributes, '$1 $3 $5')
            text = text.replaceAll(~/( *)(>)/,'$2')
            text = text.replaceAll(~/(<a href=[^ >]*)(>)/,'$1 rel=\'nofollow\' target=\'_blank\'>')
            def brs = ~/<br><br>/
            while(text.find(brs)){
                text = text.replaceAll(brs,'<br>')
            }
        }else{
            text = raw
        }

        text
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

        Integer numMails = calculateNumEmails(sponsor.amount)
        notificationService.sendSponsoredPostNotification(post, sponsor.kuorumUser, numMails)
        gamificationService.sponsorAPostAward(sponsor.kuorumUser, numMails)
    }

    Integer calculateNumEmails(Double price){
        Integer numMails
        Double mailPrice = grailsApplication.config.kuorum.promotion.mailPrice
        switch (price as int){
            case 0: numMails = 0; break;
            case 1..<5: numMails = 5; break;
            case 5..<15: numMails = 30; break;
            default:
                Double remainingAmount = price - 15
                Integer extraNumMails = (remainingAmount / mailPrice) as Integer  //Truncate
                numMails = 100 + extraNumMails
                break;
        }
        numMails
    }

    PostComment addComment(Post post, PostComment comment){

        if (!comment.validate()){
            throw KuorumExceptionUtil.createExceptionFromValidatable(comment)
        }
        comment.dateCreated = new Date()
        //Atomic operation
        def commentData = [
                kuorumUserId: comment.kuorumUser.id,
                text:comment.text,
                dateCreated: comment.dateCreated,
                moderated:comment.moderated,
                deleted :comment.deleted ]
        Post.collection.update ( [_id:post.id],['$push':['comments':commentData]])
        post.refresh()
        post.comments.last()
    }

    Post deleteComment(KuorumUser  deletedBy, Post post, Integer commentPosition){
        if (commentPosition>post.comments.size()){
            throw new KuorumException("Se ha intentado borrar un commentario que no existe","error.post.indexCommentOutOfBound")
        }
        if (isCommentDeletableByUser(deletedBy, post, commentPosition)){
            PostComment postComment = post.comments[commentPosition]
            String field = "deleted"
            if (deletedBy == postComment.kuorumUser){
                field = "deleted"
            }else{
                field = "moderated"
            }
            DBObject dbObject = new BasicDBObject()
            dbObject.append("comments.${commentPosition}.${field}",Boolean.TRUE)
            Post.collection.update([_id:post.id],['$set':dbObject])
            post.refresh()

        }else{
            throw new KuorumException("El usuario no tiene permisos para borrar el comentario","error.post.notAllowDeleteComment")
        }
    }

    /**
     * Checks if the users can delete/moderate a comment
     *
     * CommentPosition starts on 0 and ends on post.comments.size()-1
     * @param deleteBy
     * @param post
     * @param commentPosition
     * @return
     */
    Boolean isCommentDeletableByUser(KuorumUser deleteBy, Post post, Integer commentPosition){
        if (!post || !deleteBy || commentPosition==null || commentPosition>post.comments.size()){
            return false
        }

        Boolean isAdmin = deleteBy.authorities.collect{it.authority}.contains("ROLE_ADMIN")
        PostComment postComment = post.comments[commentPosition]
        isAdmin || postComment.kuorumUser == deleteBy || post.owner == deleteBy
    }

    Post addDebate(Post post, PostComment comment){
        if (isAllowedToAddDebate(post, comment.kuorumUser)){
            //Atomic operation
            def commentData = [
                    kuorumUserId: comment.kuorumUser.id,
                    text:comment.text,
                    dateCreated: new Date(),
                    moderated:comment.moderated,
                    deleted :comment.deleted ]
            Post.collection.update ( [_id:post.id],['$push':['debates':commentData]])
            post.refresh()
            if (comment.kuorumUser != post.owner){
                //Is politician, so is stored on cluck
                Cluck.collection.update([_id:post.firstCluck.id], ['$addToSet':['debateMembers':comment.kuorumUser.id]])
                post.firstCluck.refresh()
            }
            notificationService.sendDebateNotification(post)
            post
        }else{
            throw new KuorumException("El usuario no es el dueño o un político", "error.security.post.notDebateAllowed")
        }
    }

    Boolean isAllowedToAddDebate(Post post, KuorumUser user){
        user &&
        (
                UserType.POLITICIAN.equals(user.userType) && isAllowedToDefendAPost(post, user)
                        ||
                post.owner == user && !post.debates.isEmpty()
        )
    }

    KuorumUser favoriteAddPost(Post post, KuorumUser user){
        KuorumUser.collection.update([_id:user.id],['$addToSet':[favorites:post.id]])
        user.refresh()
    }
    KuorumUser favoriteRemovePost(Post post, KuorumUser user){
        KuorumUser.collection.update([_id:user.id],['$pull':[favorites:post.id]])
        user.refresh()
    }

    List<Post> findUserPosts(SearchUserPosts searchUserPosts){
        def criteria = Post.createCriteria()
        def result = criteria.list(max:searchUserPosts.max, offset:searchUserPosts.offset) {
            eq('owner', searchUserPosts.user)
            if (searchUserPosts.publishedPosts==Boolean.TRUE) eq('published', true)
            if (searchUserPosts.publishedPosts==Boolean.FALSE) eq('published', false)
            order("dateCreated","desc")
        }
        result
    }

    List<Post> favoritesPosts(KuorumUser user){
        user.favorites.collect{Post.load(it)}
    }

    Integer numUserPosts(KuorumUser user){
        Post.countByOwner(user)
    }

    List<Post> recommendedPosts(KuorumUser user = null, Law law = null, Pagination pagination = new Pagination()){
        //TODO: Improve algorithm
        Integer votesToBePublic = grailsApplication.config.kuorum.milestones.postVotes.publicVotes
        if (law){
            Post.findAllByLaw(law,[max: pagination.max, sort: "numVotes", order: "desc", offset: 0])
        }else{
            Post.list([max: pagination.max, sort: "numVotes", order: "desc", offset: 0])
//        Post.findAllByNumVotesGreaterThan(votesToBePublic,[max: NUM_RECOMMENDED_POST, sort: "numVotes", order: "desc", offset: 0])
        }
    }
    /**
     * Related posts to post and user. User can be null
     * @param post
     * @param user
     * @param pagination
     * @return
     */
    List<Post> relatedPosts(Post post,KuorumUser user, Integer max){
        //TODO: Improve algorithm

        //Post of the law
        List<Post> posts = Post.findAllByLawAndIdNotEqual(post.law, post.id, [max: max, sort: "numVotes", order: "desc", offset: 0])

        //If not enough post then => Post with the same owner
        if (posts.size() < max){
            posts += Post.findAllByOwnerAndIdNotEqual(post.owner, post.id, [max: max - posts.size(), sort: "numVotes", order: "desc", offset: 0])
        }

        //If not enough post then => List post ordere by numVotes
        if (posts.size() < max){
            posts += Post.findAllByIdNotEqual(post.id, [max: max - posts.size(), sort: "numVotes", order: "desc", offset: 0])
        }
        posts
    }

    Post victory(Post post, KuorumUser owner){

        if (post.owner != owner){
            String message =  "Esta dando victoria el usuario ${owner} cuando el dueño del post es ${post.owner}"
            log.error(message)
            throw new KuorumException(message, 'error.security.post.victory.notOwnerGivenVictory')
        }
        if (post.victory){
            String message =  "Al post ${post.id} ya se había otrogado la victoria"
            log.error(message)
            throw new KuorumException(message, 'error.security.post.victory.alreadyVictoryGiven')
        }

        post.victory = Boolean.TRUE
        Post.collection.update ( [_id:post.id],['$set':[victory:post.victory]])
        post.refresh()
        notificationService.sendVictoryNotification(post)
        post
    }
    List<Post> lawVictories(Law law, Pagination pagination = new Pagination()){
        Post.findAllByLawAndVictory(law,Boolean.TRUE,[max: pagination.max, sort: "numVotes", order: "desc", offset: pagination.offset])
    }

    Post defendPost(Post post, CommitmentType commitmentType, KuorumUser politician){
        if (politician.userType != UserType.POLITICIAN){
            throw new KuorumException("El usuario ${politician.name} (${politician.id}) no es un político para defender la publicacion ${post.id}", "error.security.post.defend.isNotPolitician")
        }
        if (post.postType != commitmentType.associatedPostType){
            String message =  "Se ha intentado defender con (${commitmentType}) que no se ajusta al tipo de post ${post.postType}"
            log.error(message)
            throw new KuorumException(message, 'error.security.post.defend.wrongCommitmentType')
        }
        if (post.defender){
            String message =  "Se ha intentado apadrinar una ${post.portType} qua ya había sido defendida."
            log.error(message)
            throw new KuorumException(message, 'error.security.post.defend.alreadyDefended')
        }
        Date defenderDate = new Date()
        Post.collection.update ( [_id:post.id],['$set':[defender:politician.id,defenderDate:defenderDate, commitmentType:commitmentType.toString()]])
        Cluck.collection.update([_id:post.firstCluck.id],['$set':[defendedBy:politician.id, lastUpdated:defenderDate]])
        post.refresh()
        post.firstCluck.refresh()
        notificationService.sendPostDefendedNotification(post)
        post
    }

    /**
     * This function simulates ACLs for post, and which politicians can work as politician on this post.
     *
     * If user is not politician => FALSE
     * If politician is not of the same institucion => False
     * Otherwise => True
     *
     * @param post
     * @param user
     * @return
     */
    Boolean isAllowedToDefendAPost(Post post, KuorumUser politician){
        politician.userType == UserType.POLITICIAN &&
        politician.institution == post.law.institution
    }

    def getPostConfiguration(){
        grailsApplication.config.kuorum.post
    }
}
