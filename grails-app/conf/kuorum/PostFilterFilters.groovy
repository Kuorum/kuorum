package kuorum

import kuorum.post.Post
import org.bson.types.ObjectId

import javax.servlet.http.HttpServletResponse

class PostFilterFilters {

    def filters = {
        all(controller: 'post', action: '*',actionExclude:'save|create') {
            before = {
                if (params.postId){
                    Post post = Post.get(new ObjectId(params.postId))
                    if (!post){
                        response.sendError(HttpServletResponse.SC_NOT_FOUND)
                        return;
                    }
                    params.post = post
                }

            }
            after = { Map model ->

            }
            afterView = { Exception e ->

            }
        }
    }
}
