package payment.campaign

import com.fasterxml.jackson.core.type.TypeReference
import grails.transaction.Transactional
import kuorum.core.exception.KuorumException
import kuorum.mail.KuorumMailService
import kuorum.users.KuorumUser
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.communication.post.PagePostRSDTO
import org.kuorum.rest.model.communication.post.PostRDTO
import org.kuorum.rest.model.communication.post.PostRSDTO

@Transactional
class PostService implements CampaignService<PostRSDTO, PostRDTO>{

    def grailsApplication
    def indexSolrService
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

    PostRSDTO save(KuorumUser user, PostRDTO postRDTO, Long postId){

        PostRSDTO post = null;
        if (postId) {
            post= update(user, postRDTO, postId)
        } else {
            post= createPost(user, postRDTO)
        }
        indexSolrService.deltaIndex();
        post
    }

    PostRSDTO createPost(KuorumUser user, PostRDTO postRDTO){
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

    PostRSDTO find(KuorumUser user, Long postId, String viewerUid = null){
        if (!postId){
            return null;
        }
        Map<String, String> params = [userAlias: user.id.toString(), postId: postId.toString()]
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

            return response.data ?: null;
        }catch (KuorumException e){
            log.info("Post not found [Excpt: ${e.message}")
            return null;
        }
    }

    PostRSDTO update(KuorumUser user, PostRDTO postRDTO, Long postId) {
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

    @Override
    PostRDTO map(PostRSDTO postRSDTO) {
        PostRDTO postRDTO = new PostRDTO()
        if(postRSDTO){
            postRDTO.title = postRSDTO.title
            postRDTO.body = postRSDTO.body
            postRDTO.photoUrl = postRSDTO.photoUrl
            postRDTO.videoUrl = postRSDTO.videoUrl
            postRDTO.publishOn = postRSDTO.datePublished
            postRDTO.name = postRSDTO.name
            postRDTO.triggeredTags = postRSDTO.triggeredTags
            postRDTO.anonymousFilter = postRSDTO.anonymousFilter
            postRDTO.filterId = postRSDTO.newsletter?.filter?.id
            postRDTO.causes = postRSDTO.causes
        }
        return postRDTO;
    }
}
