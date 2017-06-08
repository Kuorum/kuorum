package kuorum.post

import com.fasterxml.jackson.core.type.TypeReference
import grails.transaction.Transactional
import kuorum.core.exception.KuorumException
import kuorum.core.model.ProjectStatusType
import kuorum.core.model.search.Pagination
import kuorum.core.model.search.SearchUserPosts
import kuorum.mail.KuorumMailService
import kuorum.project.Project
import kuorum.users.KuorumUser
import kuorum.util.TimeZoneUtil
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.communication.post.PagePostRSDTO
import org.kuorum.rest.model.communication.post.PostRDTO
import org.kuorum.rest.model.communication.post.PostRSDTO

@Transactional
class PostService {

    def cluckService
    def grailsApplication
    def indexSolrService
    def postVoteService
    def notificationService
    def fileService
    KuorumMailService kuorumMailService
    RestKuorumApiService restKuorumApiService

    PagePostRSDTO findAllPosts(Integer page = 0, Integer size = 10){
        Map<String, String> params = [:]
        Map<String, String> query = [page:page.toString(), size:size.toString()]
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.ACCOUNT_POSTS_ALL,
                params,
                query,
                new TypeReference<PagePostRSDTO>(){}
        )

        response.data

    }
    List<PostRSDTO> findAllPosts(KuorumUser user, String viewerUid = null){
        Map<String, String> params = [userAlias: user.id.toString()]
        Map<String, String> query = [:]
        if (viewerUid){
            query.put("viewerUid",viewerUid)
        }
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.ACCOUNT_POSTS,
                params,
                query,
                new TypeReference<List<PostRSDTO>>(){}
        )

        response.data
    }

    PostRSDTO savePost(KuorumUser user, PostRDTO postRDTO, Long postId){

        PostRSDTO post = null;
        if (postId) {
            post= updatePost(user, postRDTO, postId)
        } else {
            post= createPost(user, postRDTO)
        }
        indexSolrService.deltaIndex();
        post
    }

    PostRSDTO createPost(KuorumUser user, PostRDTO postRDTO){
        if (postRDTO.publishOn != null) {
            postRDTO.publishOn = TimeZoneUtil.convertToUserTimeZone(postRDTO.publishOn, user.timeZone)
        }

        Map<String, String> params = [userAlias: user.id.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.post(
                RestKuorumApiService.ApiMethod.ACCOUNT_POSTS,
                params,
                query,
                postRDTO,
                new TypeReference<PostRSDTO>(){}
        )

        PostRSDTO postSaved = null
        if (response.data) {
            postSaved = response.data
        }

        postSaved
    }

    PostRSDTO findPost(KuorumUser user, Long postId, String viewerUid = null){
        Map<String, String> params = [userAlias: user.id.toString(), postId: postId.toString()]
        Map<String, String> query = [:]
        if (viewerUid){
            query.put("viewerUid",viewerUid)
        }
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.ACCOUNT_POST,
                params,
                query,
                new TypeReference<PostRSDTO>(){}
        )

        response.data
    }

    PostRSDTO updatePost(KuorumUser user, PostRDTO postRDTO, Long postId) {

        if (postRDTO.publishOn != null) {
            postRDTO.publishOn = TimeZoneUtil.convertToUserTimeZone(postRDTO.publishOn, user.timeZone)
        }
        Map<String, String> params = [userAlias: user.id.toString(), postId: postId.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.put(
                RestKuorumApiService.ApiMethod.ACCOUNT_POST,
                params,
                query,
                postRDTO,
                new TypeReference<PostRSDTO>(){}
        )

        PostRSDTO postSaved = null
        if (response.data) {
            postSaved = response.data
        }

        postSaved
    }

    PostRSDTO likePost (Long postId, KuorumUser currentUser, Boolean like, String userAlias){
        Map<String, String> params = [userAlias: userAlias, postId: postId.toString()]
        Map<String, String> query = [viewerUid: currentUser.id.toString()]
        PostRSDTO postRSDTO;
        if(like){
            def response = restKuorumApiService.put(
                    RestKuorumApiService.ApiMethod.ACCOUNT_POST_LIKES,
                    params,
                    query,
                    null,
                    new TypeReference<PostRSDTO>(){}
            );
            postRSDTO = response.data
        }
        else {
            def response = restKuorumApiService.delete(
                    RestKuorumApiService.ApiMethod.ACCOUNT_POST_LIKES,
                    params,
                    query,
                    new TypeReference<PostRSDTO>(){}
            );
            postRSDTO = response.data
        }
        return  postRSDTO;
    }

    Long removePost(KuorumUser user, Long postId) {
        Map<String, String> params = [userAlias: user.id.toString(), postId: postId.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.delete(
                RestKuorumApiService.ApiMethod.ACCOUNT_POST,
                params,
                query
        )

        postId
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
        if (project){
            Post.findAllByProjectAndPublishedAndDateCreatedGreaterThan(project,true,new Date()-180,[max: pagination.max, sort: "numVotes", order: "desc", offset: pagination.offset])
        }else{
            List<Project> openProjects = Project.findAllByStatus(ProjectStatusType.OPEN);
            Post.findAllByPublishedAndDateCreatedGreaterThanAndProjectInList(true, new Date()-180, openProjects,[max: pagination.max, sort: "numVotes", order: "desc", offset: pagination.offset])
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
}
