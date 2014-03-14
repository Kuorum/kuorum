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
        Post post = Post.get(new ObjectId(postId))
        [postInstance:post]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def edit(String postId){
        Post post = Post.get(new ObjectId(postId))
        if (post.owner.id != springSecurityService.principal.id){
            response.sendError(HttpServletResponse.SC_FORBIDDEN)
            return;
        }
        [postInstance:post]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def update(PostCommand command){
        if (command.hasErrors()){
            render view: "edit", model:[
                    postInstance:command,
            ]
        }
        Post post = Post.get(new ObjectId(command.postId))
        if (!post){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        if (post.owner.id != springSecurityService.principal.id){
            response.sendError(HttpServletResponse.SC_FORBIDDEN)
            return;
        }

        command.properties.each {k,v -> if (k!="class") {post."$k" = command."$k"}}

        postService.updatePost(post)
        redirect mapping:"postShow", params:[postId:post.id, postTypeUrl:post.postType.urlText, hashtag:post.law.hashtag.decodeHashtag()]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def create(String hashtag){
        Law law = lawService.findLawByHashtag(hashtag.encodeAsHashtag())
        if (!law){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }

        [postInstance:new PostCommand(), law:law]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def save(PostCommand command){
        if (command.hasErrors()){
            render view: "create", model:[
                    postInstance:command,
            ]
        }
        Post post = new Post(command.properties)
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        postService.savePost(post, command.law, user)
        redirect mapping:"postShow", params:[postId:post.id, postTypeUrl:post.postType.urlText, hashtag:post.law.hashtag.decodeHashtag()]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def review(String postId){
        Post post = Post.get(new ObjectId(postId))
        if (!post){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        if (post.owner != springSecurityService.currentUser){
            response.sendError(HttpServletResponse.SC_FORBIDDEN)
            return;
        }
        [post:post]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def publish(String postId){
        Post post = Post.get(new ObjectId(postId))
        if (!post){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        if (post.owner != springSecurityService.currentUser){
            response.sendError(HttpServletResponse.SC_FORBIDDEN)
            return;
        }

    }

    def promoteYourPost(String postId){
        Post post = Post.get(new ObjectId(postId))
        if (!post){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        if (post.owner != springSecurityService.currentUser){
            response.sendError(HttpServletResponse.SC_FORBIDDEN)
            return;
        }
        [post:post]
    }
}
