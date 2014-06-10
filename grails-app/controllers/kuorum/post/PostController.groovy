package kuorum.post

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import kuorum.KuorumFile
import kuorum.core.FileGroup
import kuorum.core.FileType
import kuorum.core.model.CommitmentType
import kuorum.core.model.PostType
import kuorum.core.model.UserType
import kuorum.core.model.gamification.GamificationElement
import kuorum.core.model.search.Pagination
import kuorum.law.Law
import kuorum.users.KuorumUser
import kuorum.web.commands.PostCommand
import kuorum.web.commands.post.CommentPostCommand
import kuorum.web.commands.post.PromotePostCommand
import kuorum.web.constants.WebConstants
import org.bson.types.ObjectId

import javax.servlet.http.HttpServletResponse

class PostController {

    def postService
    def postVoteService
    def springSecurityService
    def lawService
    def cluckService
    def gamificationService
    def grailsApplication
    def fileService

    def index() {
        [postInstanceList:Post.list()]
    }

    def show(){
        Post post = params.post //Intellij Detects type for autocomplete
        KuorumUser user= null
        PostVote userVote=null
        if (springSecurityService.isLoggedIn()){
            user = KuorumUser.get(springSecurityService.principal.id)
            userVote = PostVote.findByPostAndUser(post,user)
        }
        List<Post> relatedPost = postService.relatedPosts(post,  user,  3 )
        List<KuorumUser> usersVotes = postVoteService.findVotedUsers(post, new Pagination(max:20))

        [post:post,relatedPost:relatedPost, usersVotes:usersVotes, userVote:userVote]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def edit(){
        Post post = params.post
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        if (!postService.isEditableByUser(post, user)){
            response.sendError(HttpServletResponse.SC_FORBIDDEN)
            return;
        }
        PostCommand command = new PostCommand()
        command.postId = post.id.toString()
        command.imageId = post.multimedia?.fileType == FileType.IMAGE?post.multimedia.id:null
        command.videoPost = post.multimedia?.fileType == FileType.YOUTUBE?post.multimedia.url:''
        command.fileType = post.multimedia?.fileType
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
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        if (!postService.isEditableByUser(post, user)){
            response.sendError(HttpServletResponse.SC_FORBIDDEN)
            return;
        }
        if (command.hasErrors()){
            render view: "edit", model:[command:command,post:post ]
            return
        }
        post.multimedia = preparePostFile(post, command,user)
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

    private KuorumFile preparePostFile(Post post, PostCommand command, KuorumUser user){
        KuorumFile multimedia = null
        if (command.fileType == FileType.IMAGE && command.imageId){
            multimedia = KuorumFile.get(new ObjectId(command.imageId))
        }else if(command.fileType == FileType.YOUTUBE && command.videoPost && !post.multimedia){
            multimedia = createKuorumFileFromYoutubeUrl(command.videoPost, user)
        }else if (command.fileType == FileType.YOUTUBE && command.videoPost && post.multimedia){
            if (command.videoPost == post.multimedia.url){
                multimedia = post.multimedia
            }else{
                fileService.deleteFile(post.multimedia)
                multimedia = createKuorumFileFromYoutubeUrl(command.videoPost, user)
            }
        }
        multimedia
    }

    private KuorumFile createKuorumFileFromYoutubeUrl(String url, KuorumUser user){
        def fileName = url.replaceAll(~/http[s]{0,1}:\/\/(w{3}.){0,1}youtube.com\/watch\?v=([a-zA-Z0-9]*)/, '$2')
        KuorumFile multimedia = new KuorumFile(
                user:user,
                local:Boolean.FALSE,
                temporal:Boolean.FALSE,
                storagePath:null,
                alt:null,
                fileName:fileName,
                url:url,
                fileGroup:FileGroup.POST_IMAGE,
                fileType:FileType.YOUTUBE
        )
        multimedia.save()
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def create(String hashtag){
        Law law = lawService.findLawByHashtag(hashtag.encodeAsHashtag())
        if (!law){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        PostType postType = PostType.HISTORY
        if (params.postType){
            postType = PostType.valueOf(params.postType)
        }
        PostCommand command = new PostCommand(postType: postType)
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

        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        Post post = new Post()

        post.multimedia = preparePostFile(post, command, user)
        post.postType = command.postType
        post.text = command.textPost
        post.title = command.title
        post.pdfPage = command.numberPage

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
        flash.args = [justPublished:true]
        redirect mapping:"postPublished", params:post.encodeAsLinkProperties()
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def deletePost(){
        Post post = params.post
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        if (!postService.isDeletableByUser(post, user)){
            response.sendError(HttpServletResponse.SC_FORBIDDEN)
            return;
        }
        postService.deletePost(post, user)
        flash.message=message(code:'profile.profileMyPosts.post.success')
        redirect( mapping:'profileMyPosts')
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def postPublished(){
        Post post = params.post
        if (post.owner.id != springSecurityService.principal.id){
            response.sendError(HttpServletResponse.SC_FORBIDDEN)
            return;
        }
        def model = [post:post]
        if (flash.args?.justPublished){
            def numVotesToBePublic = grailsApplication.config.kuorum.milestones.postVotes.publicVotes
            def gamification = [
                    title: "${message(code:'post.edit.step3.gamification.title')}",
                    text:"${message(code:'post.edit.step3.gamification.motivationText', args:[numVotesToBePublic])}",
                    eggs:gamificationService.gamificationConfigCreatePost()[GamificationElement.EGG]?:0,
                    plumes:gamificationService.gamificationConfigCreatePost()[GamificationElement.PLUME]?:0,
                    corns:gamificationService.gamificationConfigCreatePost()[GamificationElement.CORN]?:0
            ]
            model += [gamificationData:gamification]
        }
        model
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def promotePost(){
        Post post = params.post
        def promotionPrizes = grailsApplication.config.kuorum.post.promotionPrizes
        def prices = promotionPrizes.collect{[price:it, numMails: postService.calculateNumEmails(it)]}
        [post:post, prices:prices, command: new PromotePostCommand()]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def paimentPost(PromotePostCommand command){
        Post post = params.post
        if (command.hasErrors()){
            def promotionPrizes = grailsApplication.config.kuorum.post.promotionPrizes
            def prices = promotionPrizes.collect{[price:it, numMails: postService.calculateNumEmails(it)]}
            [post:post, prices:prices, command: command]
        }

        [post:post, amount:command.amount, numMails:postService.calculateNumEmails(command.amount)]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def successPromotePost(PromotePostCommand command){
        Post post = params.post
        [post:post, numMails: 1000]
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
        Boolean anonymous = params.anonymous?Boolean.valueOf(params.anonymous):Boolean.FALSE
        Post post = params.post
        postVoteService.votePost(post, kuorumUser, anonymous)
        Range<Long> range = postVoteService.findPostRange(post)
        def gamification = [
                title: "${message(code:'post.show.boxes.like.vote.gamification.title', args:[post.owner.name])}",
                text:"${message(code:'post.show.boxes.like.vote.gamification.motivationText', args:[post.owner.name])}",
                eggs:gamificationService.gamificationConfigVotePost()[GamificationElement.EGG]?:0,
                plumes:gamificationService.gamificationConfigVotePost()[GamificationElement.PLUME]?:0,
                corns:gamificationService.gamificationConfigVotePost()[GamificationElement.CORN]?:0
        ]
        render ([numLikes:post.numVotes, limitTo:range.to +1, gamification:gamification] as JSON)
    }

    def listVotes(){
        Post post = params.post
        List<KuorumUser> users = postVoteService.findVotedUsers(post, new Pagination())
        render (template:'/kuorumUser/embebedUsersList', model:[users:users])
    }

    def listClucks(){
        Post post = params.post
        List<KuorumUser> users = cluckService.findPostCluckers(post, new Pagination())
        render (template:'/kuorumUser/embebedUsersList', model:[users:users])
    }

    @Secured('isAuthenticated()')
    def addDebate(CommentPostCommand command) {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        Post post = params.post
        if (command.hasErrors() || !postService.isAllowedToAddDebate(post, user) ){
            render "Something wrong. Not possible to arrive here using web normally"
        }else{
            PostComment postComment = new PostComment(
                    text: command.comment,
                    dateCreated:new Date(),
                    moderated: Boolean.FALSE,
                    deleted :Boolean.FALSE,
                    kuorumUser: user
            )
            postService.addDebate(post, postComment)
            postComment = post.debates.last()
            if (user.userType == UserType.POLITICIAN){
                render (template: '/post/debates/postDebatePolitician', model:[debate:postComment])
            }else{
                render (template: '/post/debates/postDebateActivist', model:[debate:postComment])
            }
        }
    }
    @Secured("ROLE_POLITICIAN")
    def addDefender() {
        Post post = params.post
        KuorumUser politician = KuorumUser.get(springSecurityService.principal.id)
        CommitmentType commitmentType = CommitmentType.valueOf(params.commitmentType)
        postService.defendPost(post, commitmentType, politician)
        String postTypeText = message(code:"${PostType.class.name}.${post.postType}")
        flash.message = message(code:'modalDefend.success', args:[postTypeText])
        redirect (mapping:"postShow", params:post.encodeAsLinkProperties())
    }

    @Secured('isAuthenticated()')
    def addVictory() {

        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        Post post = params.post
        postService.victory(post, user)
        render ([postId:post.id]) as JSON
    }
}
