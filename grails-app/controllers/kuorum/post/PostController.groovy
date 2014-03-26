package kuorum.post

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import kuorum.law.Law
import kuorum.users.KuorumUser
import kuorum.web.commands.PostCommand
import org.bson.types.ObjectId

import javax.servlet.http.HttpServletResponse

class PostController {

    def postService
    def springSecurityService
    def lawService

    def index() {
        [postInstanceList:Post.list()]
    }

    def show(String postId){
        [postInstance:params.post]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def edit(String postId){
        Post post = Post.get(new ObjectId(postId))
        if (post.owner.id != springSecurityService.principal.id){
            response.sendError(HttpServletResponse.SC_FORBIDDEN)
            return;
        }
        PostCommand command = new PostCommand()
        command.properties.each {k,v -> command."$k" = post."$k"}
        [command:command,post:post ]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def update(PostCommand command){
        Post post = Post.get(new ObjectId(command.postId))
        if (!post){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        if (post.owner.id != springSecurityService.principal.id){
            response.sendError(HttpServletResponse.SC_FORBIDDEN)
            return;
        }
        if (command.hasErrors()){
            render view: "edit", model:[command:command,post:post ]
            return
        }

        command.properties.each {k,v -> if (k!="class") {post."$k" = command."$k"}}

        postService.updatePost(post)
        redirect mapping:"postShow", params:post.encodeAsLinkProperties()
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def create(String hashtag){
        Law law = lawService.findLawByHashtag(hashtag.encodeAsHashtag())
        if (!law){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }

        [command:new PostCommand(), law:law]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def save(PostCommand command){
        Law law = lawService.findLawByHashtag(params.hashtag.encodeAsHashtag())
        if (!law){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        if (command.hasErrors()){
            render view: "create", model:[command:command,law:law]
            return;
        }
        Post post = new Post(command.properties)
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        postService.savePost(post, law, user)
        redirect mapping:"postReview", params:post.encodeAsLinkProperties()
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def review(){
        Post post = params.post
        if (post.owner.id != springSecurityService.principal.id){
            response.sendError(HttpServletResponse.SC_FORBIDDEN)
            return;
        }
        [post:post]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def publish(String postId){
        Post post = params.post
        if (post.owner.id != springSecurityService.principal.id){
            response.sendError(HttpServletResponse.SC_FORBIDDEN)
            return;
        }
        postService.publishPost(post)
        redirect mapping:"postPromoteYourPost", params:post.encodeAsLinkProperties()
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def promoteYourPost(String postId){
        Post post = params.post
        if (post.owner.id != springSecurityService.principal.id){
            response.sendError(HttpServletResponse.SC_FORBIDDEN)
            return;
        }
        [post:post]
    }


    @Secured('IS_AUTHENTICATED_REMEMBERED')
    def favorite(String postId) {
        Post post = params.post
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        postService.favoriteAddPost(post,user)
        render "Ajax CALL OK"
    }

    @Secured('IS_AUTHENTICATED_REMEMBERED')
    def unfavorite(String postId) {
        Post post = params.post
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        postService.favoriteRemovePost(post,user)
        render "Ajax CALL OK"
    }

    @Secured('IS_AUTHENTICATED_REMEMBERED')
    def deleteComment(String postId, Integer commentPosition){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        Post post = params.post
        if (postService.isCommentDeletableByUser(user, post, commentPosition)){
            postService.deleteComment(user,post,commentPosition)
        }else{
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED)
            return;
        }

        render "Comment deleted"
    }

    @Secured('IS_AUTHENTICATED_REMEMBERED')
    def addComment(String comment){
        if (!comment.trim().isEmpty()){
            KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
            PostComment postComent = new PostComment(kuorumUser: user, text:comment)
            postService.addComment(params.post, postComent)
            render "Comment '$comment' added"
        }else{
            render "EMpty comment"
        }
    }
}
