package kuorum.post

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import kuorum.KuorumFile
import kuorum.core.FileType
import kuorum.core.model.PostType
import kuorum.core.model.search.Pagination
import kuorum.law.Law
import kuorum.users.KuorumUser
import kuorum.web.commands.PostCommand
import kuorum.web.commands.post.CommentPostCommand
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
        Post post = params.post //Intellij Detects type for autocomplete
        KuorumUser user= null
        if (springSecurityService.isLoggedIn()){
            user = KuorumUser.get(springSecurityService.principal.id)
        }
        List<Post> relatedPost = postService.relatedPosts(post,  user,  3 )
        List<KuorumUser> usersVotes = postVoteService.findVotedUsers(post, new Pagination(max:20))
        [post:post,relatedPost:relatedPost, usersVotes:usersVotes, orange:post.victory || post.debates.size()>0]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def edit(){
        Post post = params.post
        if (post.owner.id != springSecurityService.principal.id){
            response.sendError(HttpServletResponse.SC_FORBIDDEN)
            return;
        }
        PostCommand command = new PostCommand()
        command.postId = post.id.toString()
        command.imageId = post.multimedia?.id
        command.videoPost = post.multimedia?.fileType == FileType.VIDEO?post.multimedia.url:''
        command.postType = post.postType
        command.textPost = post.text
        command.title = post.title
        command.numberPage = post.pdfPage
        [command:command,post:post ]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def update(PostCommand command){
        Post post = params.post
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

        KuorumFile multimedia = null
        if (command.imageId){
            multimedia = KuorumFile.get(new ObjectId(command.imageId))
        }
        post.multimedia = multimedia
//        command.videoPost = post.multimedia?.fileType == FileType.VIDEO?post.multimedia.url:''
        post.postType = command.postType
        post.text = command.textPost
        post.title = command.title
        post.pdfPage = command.numberPage

        postService.updatePost(post)
        if (post.published)
            redirect mapping:"postShow", params:post.encodeAsLinkProperties()
        else
            redirect mapping:"postReview", params:post.encodeAsLinkProperties()
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def create(String hashtag){
        Law law = lawService.findLawByHashtag(hashtag.encodeAsHashtag())
        if (!law){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        PostCommand command = new PostCommand(postType: PostType.valueOf(params.postType))
        [command:command, law:law]
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
        Post post = new Post()
        KuorumFile multimedia = null
        if (command.imageId){
            multimedia = KuorumFile.get(new ObjectId(command.imageId))
        }
        post.multimedia = multimedia
//        command.videoPost = post.multimedia?.fileType == FileType.VIDEO?post.multimedia.url:''
        post.postType = command.postType
        post.text = command.textPost
        post.title = command.title
        post.pdfPage = command.numberPage

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

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def payForMailing(){
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
    def addComment(CommentPostCommand command){
        if (command.hasErrors()){
            render "EMpty comment"
        }else{
            Post post = params.post
            KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
            PostComment postComent = new PostComment(kuorumUser: user, text:command.comment)
            postComent = postService.addComment(params.post, postComent)
            render template: '/post/postComment', model:[post:post, comment:postComent, pos:post.comments.size()-1, display:'none']
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
        Boolean anonymous = params.privateVote?Boolean.valueOf(params.privateVote):Boolean.FALSE
        Post post = params.post
//        postVoteService.votePost(post, kuorumUser, anonymous)
        post.numVotes ++
        Range<Long> range = postVoteService.findPostRange(post)
        render ([numLikes:post.numVotes, limitTo:range.to +1] as JSON)
    }
}
