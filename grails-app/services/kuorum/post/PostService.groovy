package kuorum.post

import com.mongodb.BasicDBObject
import com.mongodb.DBObject
import grails.transaction.Transactional
import kuorum.core.exception.KuorumException
import kuorum.core.exception.KuorumExceptionUtil
import kuorum.core.model.CommitmentType
import kuorum.core.model.PostType
import kuorum.core.model.ProjectStatusType
import kuorum.core.model.UserType
import kuorum.core.model.VoteType
import kuorum.core.model.search.Pagination
import kuorum.core.model.search.SearchUserPosts
import kuorum.project.Project
import kuorum.mail.KuorumMailService
import kuorum.notifications.Notification
import kuorum.users.KuorumUser

import java.util.regex.Matcher
import java.util.regex.Pattern

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
    KuorumMailService kuorumMailService

    /**
     * Save a post and creates the first firstCluck and first vote (owner vote)
     * @param post post data
     * @param project project's post
     * @param owner The persona who has created the post
     * @return
     */
    Post savePost(Post post, Project project, KuorumUser owner) {

        if (!project || !owner){
            KuorumException exception = new KuorumException("No se ha indicado el dieño ${owner} o la ley ${project}", "error.post.saving.data")
            log.error("No se ha indicado el dieño ${owner} o la ley ${project}")
            throw exception
        }
        post.numVotes = 0
        post.numClucks = 0
        post.owner = owner
        post.project =  project
        post.text = removeCustomCrossScripting(post.text)
        post.ownerPersonalData = owner.personalData

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
            Cluck cluck = cluckService.createActionCluck(post, post.owner, CluckAction.CREATE)
            cluck.isFirstCluck = Boolean.TRUE
            cluck.save()
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
        Cluck.collection.remove([post:post.id])
        Notification.collection.remove([post:post.id])
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
        String text = raw
        if (text){
            text = text.replaceAll("\r\n","<br/>")
            text = text.replaceAll("\n","<br/>")
            text = text.replaceAll("\r","<br/>")
            text = text.replaceAll('<br/>','<br>')
            def brs = ~/<br>\s*<br>/
            while(text.find(brs)){
                text = text.replaceAll(brs,'<br>')
            }
            text = text.replaceAll('<br>','</p><p>')
            text = text.replaceAll("&nbsp;", " ")
            text = text.replaceAll(~/>\s*</, "> <")
            def openTags = ~/<[^\/ibaup]r{0,1} *[^>]*>/  // Only allow <a> <b> <i> <u> <p>
            def closeTags = ~/<\/[^ibaup] *[^>]*>/ // Only allow </a> </b> </i> </u> </p>
            text = text.replaceAll(openTags,'')
            text = text.replaceAll(closeTags,'')

            def notAllowedAttributes = ~/(<[abiup]r{0,1})([^h>]*)(href=[^ >]*){0,1}([^h>]*)(>)/ //Delete all atributes that are not href
            text = text.replaceAll(notAllowedAttributes, '$1 $3 $5')
            text = text.replaceAll(~/( *)(>)/,'$2')
            text = text.replaceAll(~/(<a href=[^ >]*)(>)/,'$1 rel=\'nofollow\' target=\'_blank\'>')

            //REMOVING NOT CLOSED TAGS
            "abiup".each {
                text = removeNotClosedTag(text, it)
            }

            def emtpyTags = ~/<[abiup]>\s*<\/[abiup]>/
            while(text.find(emtpyTags)){
                text = text.replaceAll(emtpyTags,'')
            }
            text = text.trim()
        }

        text
    }

    private String removeNotClosedTag(String text, String tag){
        Pattern openPattern = Pattern.compile("<${tag}[^>]*>");
        Matcher  openMatcher = openPattern.matcher(text);
        int openCount = 0;
        while (openMatcher.find())
            openCount++;

        Pattern closePattern = Pattern.compile("</${tag}[^>]*>");
        Matcher  closeMatcher = closePattern.matcher(text);
        int closeCount = 0;
        while (closeMatcher.find())
            closeCount ++;

        if (openCount > closeCount || openCount < closeCount){
            text = text.replaceAll(openPattern, "")
            text = text.replaceAll(closePattern, "")
        }
        return text;
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

    PostComment addComment(Post post, PostComment comment, Boolean sendNotification = true){
        log.info("Creating comment: ${comment.text}")
        if (!comment.validate()){
            throw KuorumExceptionUtil.createExceptionFromValidatable(comment)
        }
        comment.dateCreated = new Date()
        //Atomic operation
        def commentData = [
                kuorumUserId: comment.kuorumUser.id,
                text:removeCustomCrossScripting(comment.text.encodeAsHtmlLinks()),
                dateCreated: comment.dateCreated,
                moderated:comment.moderated,
                deleted :comment.deleted ]
        Post.collection.update ( [_id:post.id],['$push':['comments':commentData]])
        post.refresh()
        if (sendNotification){
            notificationService.sendCommentNotifications(post, comment)
        }
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

    Post voteComment(KuorumUser  votedBy, Post post, Integer commentPosition, VoteType voteType){
        if (commentPosition>post.comments.size()){
            throw new KuorumException("Se ha intentado votar un commentario que no existe","error.post.indexCommentOutOfBound")
        }
        if (isCommentVotableByUser(votedBy, post, commentPosition)){
            PostComment postComment = post.comments[commentPosition]
            String field = null;
            switch (voteType){
                case VoteType.POSITIVE:
                    field = "positiveVotes"
                    break;
                case VoteType.NEGATIVE:
                    field = "negativeVotes"
                    break;
                default:
                    field = null;
                    log.warn("Se ha intentado votar el comentario ${commentPosition} del post(${post.id}) con ${voteType}")
            }
            if (field){
                DBObject dbObject = new BasicDBObject()
                dbObject.append("comments.${commentPosition}.${field}",votedBy.id)
                Post.collection.update([_id:post.id],['$push':dbObject])
                post.refresh()
            }
            notificationService.sendCommentVoteNotification(post, postComment, voteType, votedBy)

        }else{
            log.warn("Un usuario ha intentado votar 2 veces")
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

    Boolean isCommentVotableByUser(KuorumUser user, Post post, Integer commentPosition){
        if (!post || !user || commentPosition==null || commentPosition>post.comments.size()){
            return false
        }
        PostComment postComment = post.comments[commentPosition]
        return  (!postComment.negativeVotes || !postComment.negativeVotes.contains(user.id)) &&
                (!postComment.positiveVotes || !postComment.positiveVotes.contains(user.id))
    }

    Post addDebate(Post post, PostComment comment,Boolean sendNotification = true){
        if (isAllowedToAddDebate(post, comment.kuorumUser)){
            KuorumUser user = comment.kuorumUser
            if (!post.debates.collect{it.kuorumUser.id}.contains(user.id) && user.userType == UserType.POLITICIAN){
                user.politicianActivity.numDebates ++
                user.save()
            }
            //Atomic operation
            def commentData = [
                    kuorumUserId: user.id,
                    text:removeCustomCrossScripting(comment.text.encodeAsHtmlLinks()),
                    dateCreated: new Date(),
                    moderated:comment.moderated,
                    deleted :comment.deleted ]
            Post.collection.update ( [_id:post.id],['$push':['debates':commentData]])
            post.refresh()
            if (sendNotification){
                cluckService.createActionCluck(post, comment.kuorumUser, CluckAction.DEBATE)
                notificationService.sendDebateNotification(post)
            }
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

    List<Post> userPosts(SearchUserPosts searchUserPosts){
        def criteria = Post.createCriteria()
        def result = criteria.list(max:searchUserPosts.max, offset:searchUserPosts.offset) {
            eq('owner', searchUserPosts.user)
            if (searchUserPosts.publishedPosts!=null) eq('published', searchUserPosts.publishedPosts)
            if (searchUserPosts.victory!=null) eq('victory', searchUserPosts.victory)
            order("dateCreated","desc")
        }
        result
    }

    Long countUserPost(SearchUserPosts searchUserPosts){
        def criteria = Post.createCriteria()
        def result = criteria.count() {
            eq('owner', searchUserPosts.user)
            if (searchUserPosts.publishedPosts!=null) eq('published', searchUserPosts.publishedPosts)
            if (searchUserPosts.victory!=null) eq('victory', searchUserPosts.victory)
        }
        result
    }

    List<Post> politicianDefendedPosts(SearchUserPosts searchUserPosts){
        def criteria = Post.createCriteria()
        def result = criteria.list(max:searchUserPosts.max, offset:searchUserPosts.offset) {
            eq('defender', searchUserPosts.user)
            if (searchUserPosts.publishedPosts!=null) eq('published', searchUserPosts.publishedPosts)
            if (searchUserPosts.victory!=null) eq('victory', searchUserPosts.victory)
            order("dateCreated","desc")
        }
        result
    }

    Long countPoliticianDefendedPosts(SearchUserPosts searchUserPosts){
        def criteria = Post.createCriteria()
        def result = criteria.count() {
            eq('defender', searchUserPosts.user)
            if (searchUserPosts.publishedPosts!=null) eq('published', searchUserPosts.publishedPosts)
            if (searchUserPosts.victory!=null) eq('victory', searchUserPosts.victory)
        }
        result
    }

    List<Post> favoritesPosts(KuorumUser user){
        if (user?.favorites){
            user.favorites.collect{Post.load(it)}
        }else{
            []
        }

    }

    Integer numUnpublishedUserPosts(KuorumUser user){
        Post.countByOwnerAndPublished(user, false)
    }

    List<Post> recommendedPosts(KuorumUser user = null, Project project = null, Pagination pagination = new Pagination()){
        //TODO: Improve algorithm
        Integer votesToBePublic = grailsApplication.config.kuorum.milestones.postVotes.publicVotes
        if (project){
            Post.findAllByProjectAndPublishedAndDateCreatedGreaterThan(project,true,new Date()-180,[max: pagination.max, sort: "numVotes", order: "desc", offset: pagination.offset])
        }else{
            List<Project> openProjects = Project.findAllByStatus(ProjectStatusType.OPEN);
            Post.findAllByPublishedAndDateCreatedGreaterThanAndProjectInList(true, new Date()-180, openProjects,[max: pagination.max, sort: "numVotes", order: "desc", offset: pagination.offset])
//        Post.findAllByNumVotesGreaterThan(votesToBePublic,[max: NUM_RECOMMENDED_POST, sort: "numVotes", order: "desc", offset: 0])
        }
    }


    List<Post> lastCreatedPosts(Pagination pagination = new Pagination(), Project project = null){
        if (project){
            Post.findAllByProjectAndPublished(project, true,[max: pagination.max, sort: "id", order: "desc", offset: pagination.offset])
        }else{
            Post.findAllByPublished(true, [max: pagination.max, sort: "id", order: "desc", offset: pagination.offset])
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

        //Post of the project
        List<Post> posts = Post.findAllByProjectAndIdNotEqual(post.project, post.id, [max: max, sort: "numVotes", order: "desc", offset: 0])

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

    Post victory(Post post, KuorumUser owner, Boolean victoryOk){

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
        Post.collection.update ( [_id:post.id],['$set':[victory:post.victory, victoryOk:victoryOk]])
        post.refresh()
        KuorumUser defender = post.defender
        defender.politicianActivity.numVictories +=1
        defender.save()
        cluckService.createActionCluck(post, owner, CluckAction.VICTORY)
        notificationService.sendVictoryNotification(post)
        try{
            kuorumMailService.sendVictoryToAdmins(owner, post, victoryOk)
        }catch (Exception e){
            log.error("No se ha podido enviar el email de notificacion de victoria a los administradores: "+e.getMessage())
        }
        post
    }

    List<Post> projectPosts(Project project, Pagination pagination = new Pagination()){
        List<Cluck> clucks = cluckService.projectClucks(project, pagination);
        clucks.collect{it.post}
    }


    Long countProjectPosts(Project project) {
        Post.countByProject(project)
    }


    List<Post> projectVictories(Project project, Pagination pagination = new Pagination()){
        Post.findAllByProjectAndVictory(project,Boolean.TRUE,[max: pagination.max, sort: "numVotes", order: "desc", offset: pagination.offset])
    }

    Long countProjectVictories(Project project){
        Post.countByProjectAndVictory(project,Boolean.TRUE)
    }

    List<Post> projectDefends(Project project, Pagination pagination = new Pagination()){
        Post.findAllByProjectAndDefenderIsNotNull(project,[max: pagination.max, sort: "defenderDate", order: "desc", offset: pagination.offset])
    }

    Long countProjectDefends(Project project){
        Post.countByProjectAndDefenderIsNotNull(project)
    }

    Post defendPost(Post post, KuorumUser politician, String text){
        if (politician.userType != UserType.POLITICIAN){
            throw new KuorumException("El usuario ${politician.name} (${politician.id}) no es un político para defender la publicacion ${post.id}", "error.security.post.defend.isNotPolitician")
        }

        if (post.defender){
            String message =  "Se ha intentado apadrinar una ${post.portType} qua ya había sido defendida."
            log.error(message)
            throw new KuorumException(message, 'error.security.post.defend.alreadyDefended')
        }
        Date defenderDate = new Date()
        Post.collection.update ( [_id:post.id],['$set':[defender:politician.id,defenderDate:defenderDate]])
        post.refresh()
        cluckService.createActionCluck(post, politician, CluckAction.DEFEND)
        politician.politicianActivity.numDefends +=1
        politician.save()
        addDebate(post, new PostComment(text:text, kuorumUser: politician), false)

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
        return (
                politician.userType == UserType.POLITICIAN &&
                politician.politicianOnRegion == post.project.region
        )
    }

    def getPostConfiguration(){
        grailsApplication.config.kuorum.post
    }
}
