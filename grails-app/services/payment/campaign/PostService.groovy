package payment.campaign

import com.fasterxml.jackson.core.type.TypeReference
import grails.transaction.Transactional
import kuorum.core.exception.KuorumException
import kuorum.mail.KuorumMailService
import kuorum.register.KuorumUserSession
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.communication.post.PagePostRSDTO
import org.kuorum.rest.model.communication.post.PostRDTO
import org.kuorum.rest.model.communication.post.PostRSDTO
import org.kuorum.rest.model.kuorumUser.BasicDataKuorumUserRSDTO

@Transactional
class PostService implements CampaignCreatorService<PostRSDTO, PostRDTO>{

    def grailsApplication
    def indexSolrService
    def notificationService
    def fileService
    KuorumMailService kuorumMailService
    RestKuorumApiService restKuorumApiService
    CampaignService campaignService

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

    @Deprecated
    List<PostRSDTO> findAllPosts(String userId, String viewerUid = null){
        Map<String, String> params = [userId: userId]
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

    PostRSDTO save(KuorumUserSession user, PostRDTO postRDTO, Long postId){

        PostRSDTO post = null
        if (postId) {
            post= update(user, postRDTO, postId)
        } else {
            post= createPost(user, postRDTO)
        }
        indexSolrService.deltaIndex()
        post
    }

    private PostRSDTO createPost(KuorumUserSession user, PostRDTO postRDTO){
        Map<String, String> params = [userId: user.id.toString()]
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

    List<PostRSDTO> findAll(KuorumUserSession user,String viewerUid = null) {
        Map<String, String> params = [userId: user.getId().toString()]
        Map<String, String> query = [:]
        if (viewerUid){
            query.put("viewerUid",viewerUid)
        }
        try {
            def response = restKuorumApiService.get(
                    RestKuorumApiService.ApiMethod.ACCOUNT_POSTS,
                    params,
                    query,
                    new TypeReference<List<PostRSDTO>>() {}
            )

            return response.data ?: null
        }catch (KuorumException e){
            log.info("Posts of user not found [Excpt: ${e.message}")
            return null
        }
    }

    PostRSDTO find(KuorumUserSession user, Long postId, String viewerUid = null){
        find(user.id.toString(), postId, viewerUid)
    }
    PostRSDTO find(String userId, Long postId, String viewerUid = null){
        if (!postId){
            return null
        }
        Map<String, String> params = [userId: userId, postId: postId.toString()]
        Map<String, String> query = [:]
        if (viewerUid){
            query.put("viewerUid",viewerUid)
        }
        try {
            def response = restKuorumApiService.get(
                    RestKuorumApiService.ApiMethod.ACCOUNT_POST,
                    params,
                    query,
                    new TypeReference<PostRSDTO>() {}
            )

            return response.data ?: null
        }catch (KuorumException e){
            log.info("Post not found [Excpt: ${e.message}")
            return null
        }
    }

    private PostRSDTO update(KuorumUserSession user, PostRDTO postRDTO, Long postId) {
        Map<String, String> params = [userId: user.id.toString(), postId: postId.toString()]
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
//
//    @PreAuthorize("hasPermission(#postId, 'like')")
    PostRSDTO likePost (Long postId, KuorumUserSession currentUser, Boolean like, String postUserId){
        Map<String, String> params = [userId: postUserId, postId: postId.toString()]
        Map<String, String> query = [viewerUid: currentUser.id.toString()]
        PostRSDTO postRSDTO
        if(like){
            def response = restKuorumApiService.put(
                    RestKuorumApiService.ApiMethod.ACCOUNT_POST_LIKES,
                    params,
                    query,
                    null,
                    new TypeReference<PostRSDTO>(){}
            )
            postRSDTO = response.data
        }
        else {
            def response = restKuorumApiService.delete(
                    RestKuorumApiService.ApiMethod.ACCOUNT_POST_LIKES,
                    params,
                    query,
                    new TypeReference<PostRSDTO>(){}
            )
            postRSDTO = response.data
        }
        return  postRSDTO
    }

    void remove(KuorumUserSession user, Long postId) {
        Map<String, String> params = [userId: user.id.toString(), postId: postId.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.delete(
                RestKuorumApiService.ApiMethod.ACCOUNT_POST,
                params,
                query
        )

        postId
    }

    @Override
    PostRDTO map(PostRSDTO postRSDTO) {
        return campaignService.basicMapping(postRSDTO, new PostRDTO())
    }

    @Override
    def buildView(PostRSDTO campaignRSDTO, BasicDataKuorumUserRSDTO campaignOwner, String viewerUid, def params) {
        def model = [post: campaignRSDTO, postUser: campaignOwner]
        [view: "/post/show", model:model]
    }

    @Override
    PostRSDTO copy(KuorumUserSession user, Long campaignId) {
        //TODO: KPV-1606
        return null
    }

    @Override
    PostRSDTO copy(String userId, Long campaignId) {
        //TODO: KPV-1606
        return null
    }
}
