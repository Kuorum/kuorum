package kuorum.post

import grails.plugin.springsecurity.annotation.Secured
import kuorum.law.Law
import kuorum.users.KuorumUser
import kuorum.web.commands.PostCommand
import kuorum.web.constants.WebConstants
import org.bson.types.ObjectId

import javax.servlet.http.HttpServletResponse

class PostController {

    def postService
    def postVoteService
    def springSecurityService
    def lawService
    def cluckService

    def index() {
        [postInstanceList:Post.list()]
    }

    def show(){
        [postInstance:params.post]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def edit(){
        Post post = params.post
        if (post.owner.id != springSecurityService.principal.id){
            response.sendError(HttpServletResponse.SC_FORBIDDEN)
            return;
        }
        PostCommand command = new PostCommand()
        command.properties.each {k,v ->
            if (k != "class"){
                if (k!="imageId"){
                    command."$k" = post."$k"
                }else{
                    command.imageId = post.multimedia?.id
                }
            }
        }
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
    def publish(){
        Post post = params.post
        if (post.owner.id != springSecurityService.principal.id){
            response.sendError(HttpServletResponse.SC_FORBIDDEN)
            return;
        }
        postService.publishPost(post)
        redirect mapping:"postPromoteYourPost", params:post.encodeAsLinkProperties()
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def promoteYourPost(){
        Post post = params.post
        if (post.owner.id != springSecurityService.principal.id){
            response.sendError(HttpServletResponse.SC_FORBIDDEN)
            return;
        }
        [post:post]
    }


    @Secured('IS_AUTHENTICATED_REMEMBERED')
    def favorite() {
        Post post = params.post
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        if (user.favorites.contains(post.id)){
            postService.favoriteRemovePost(post,user)
            response.setHeader(WebConstants.AJAX_IS_FAVORITE, "false")
            response.setHeader(WebConstants.AJAX_NUM_LIST, "${user.favorites.size()}")
            render "Deleted from favorites"
        }else{
            postService.favoriteAddPost(post,user)
            response.setHeader(WebConstants.AJAX_IS_FAVORITE, "true")
            response.setHeader(WebConstants.AJAX_NUM_LIST, "${user.favorites.size()}")
            render template: "/modules/columnCPost/columnCPost", model: [post:post]
        }

    }

    @Secured('IS_AUTHENTICATED_REMEMBERED')
    def deleteComment(Integer commentPosition){
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

    @Secured('isAuthenticated()')
    def cluckPost() {
        KuorumUser kuorumUser = KuorumUser.get(springSecurityService.principal.id)
        Post post = params.post
        Cluck cluck = cluckService.createCluck(post, kuorumUser)
        //TODO: que se renderiza
        render "OK"
    }

    @Secured('isAuthenticated()')
    def votePost() {
        KuorumUser kuorumUser = KuorumUser.get(springSecurityService.principal.id)
        Post post = params.post
        //Cluck cluck = cluckService.createCluck(post, kuorumUser)
        postVoteService.votePost(post, kuorumUser)
        //TODO: que se renderiza
        render "OK"
    }
}
