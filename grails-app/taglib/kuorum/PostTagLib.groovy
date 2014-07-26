package kuorum

import kuorum.core.FileType
import kuorum.core.model.PostType
import kuorum.core.model.VoteType
import kuorum.core.model.solr.SolrSubType
import kuorum.core.model.solr.SolrType
import kuorum.post.Cluck
import kuorum.post.Post
import kuorum.post.PostComment
import kuorum.users.KuorumUser

class PostTagLib {
    static defaultEncodeAs = 'raw'
//    static encodeAsForTags = [removeCommentButton: 'html']

    def springSecurityService
    def postService
    def cluckService
    def postVoteService

    static namespace = "postUtil"

    def removeCommentButton={attrs ->
        Post post = attrs.post
        Integer commentPosition = attrs.commentPosition
        if (springSecurityService.isLoggedIn()){
            KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
            if (postService.isCommentDeletableByUser(user,post,commentPosition)){
                String link = createLink(mapping:"postDelComment",params: post.encodeAsLinkProperties()+[commentPosition:commentPosition])
                out << "<a href='$link'>BORRAR</a>"
            }
        }
    }

    def cssClassIfClucked = {attrs->
        Post post = attrs.post
        if (springSecurityService.isLoggedIn()){
            KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
            if (!cluckService.isAllowedToCluck(post, user)){
                out << "disabled"
            }
        }
    }

    def cssClassIfVoted = {attrs->
        Post post = attrs.post
        if (springSecurityService.isLoggedIn()){
            KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
            if (!postVoteService.isAllowedToVote(post, user)){
                out << "disabled"
            }
        }
    }
    def cssClassIfFavorite = {attrs->
        Post post = attrs.post
        if (springSecurityService.isLoggedIn()){
            KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
            if (user.favorites.contains(post.id)){
                out << "disabled"
            }else{
                out << "enabled"
            }
        }
    }

    def ifIsImportant={attrs, body ->
        Post post = attrs.post
        if (isPostImpotant(post)){
            out << body()
        }
    }

    private boolean isPostImpotant(Post post){
        !post.debates.isEmpty() || post.defender
    }

    def ifHasMultimedia={attrs, body ->
        Post post = attrs.post
        if (post.multimedia){
            out << body()
        }
    }

    def cssIconPostType={attrs, body ->
        Post post = attrs.post

        switch (post.postType){
            case PostType.HISTORY:  out << "fa-comment"; break;
            case PostType.QUESTION: out << "fa-question-circle"; break;
            case PostType.PURPOSE:  out << "fa-lightbulb-o"; break;
        }
    }
    def cssIconSolrSubType={attrs, body ->
        SolrSubType solrSubType = attrs.solrSubType

        switch (solrSubType){
            case SolrSubType.HISTORY:  out << "fa-comment"; break;
            case SolrSubType.QUESTION: out << "fa-question-circle"; break;
            case SolrSubType.PURPOSE:  out << "fa-lightbulb-o"; break;
        }
    }

    def cssIconSolrType={attrs, body ->
        SolrType solrType= attrs.solrType

        switch (solrType){
            case SolrType.LAW:  out << "fa-briefcase"; break;
            case SolrType.KUORUM_USER: out << "fa-user"; break;
            case SolrType.POST:  out << "fa-paperclip"; break;
        }
    }

    def postShowMultimedia ={attrs ->
        Post post = attrs.post
        if (post.multimedia && post.multimedia.fileType == FileType.IMAGE){
            out << render(template: '/post/multimedia/postImage', model:[image:post.multimedia])
        }else if (post.multimedia && post.multimedia.fileType == FileType.YOUTUBE){
            out << render(template: '/post/multimedia/postVideo', model:[video:post.multimedia])
        }
    }

    def politiciansHeadPost={attrs ->
        Post post = attrs.post

        Boolean showRecluckArrow = false
        if (attrs.cluck){
            Cluck cluck = attrs.cluck
            post = cluck.post
            showRecluckArrow = isPostImpotant(post) && cluck.owner !=post.owner
        }

        if (post.defender && post.victory){
            def onomatopoeia = message(code:'post.show.victory.onomatopoeia')
            def usersDesc = message(code:'post.show.victory.usersDescription')
            out << render(template:'/cluck/politicianPosts', model:[recluck:showRecluckArrow, defender:post.defender,onomatopoeia:onomatopoeia,usersDesc:usersDesc])
        }else if (post.defender && !post.victory){
            def onomatopoeia = message(code:'post.show.defend.onomatopoeia')
            def usersDesc = message(code:'post.show.defend.usersDescription')
            out << render(template:'/cluck/politicianPosts', model:[recluck:showRecluckArrow, defender:post.defender,onomatopoeia:onomatopoeia,usersDesc:usersDesc])
        }else if(!post.debates.isEmpty()){
            def onomatopoeia = message(code:'post.show.debate.onomatopoeia')
            out << render(template:'/cluck/politicianPosts', model:[recluck:showRecluckArrow, debateUsers:post.debates.kuorumUser,onomatopoeia:onomatopoeia])
        }
    }

    def progressBarMinValue={attrs ->
        Post post = attrs.post
//        Range<Long> range = postVoteService.findPostRange(post)
        out << 0
    }
    def progressBarMaxValue={attrs ->
        Post post = attrs.post
        Range<Long> range = postVoteService.findPostRange(post)
        out << range.to +1
    }

    def ifPostIsEditable={attrs, body->
        Post post = attrs.post
        if (springSecurityService.isLoggedIn()){
            KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
            if (postService.isEditableByUser(post, user)){
                out << body()
            }

        }
    }

    def ifPostIsDeletable={attrs, body ->
        Post post = attrs.post
        if (springSecurityService.isLoggedIn()){
            KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
            if (postService.isDeletableByUser(post, user)){
                out << body()
            }

        }
    }

    def ifUserCanAddDebates={attrs, body ->
        Post post = attrs.post
        if (springSecurityService.isLoggedIn()){
            KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
            if (postService.isAllowedToAddDebate(post, user)){
                out << body()
            }
        }
    }

    def ifCommentIsDeletable={attrs, body ->
        Post post = attrs.post
        Integer commentPosition = attrs.commentPosition
        if (springSecurityService.isLoggedIn()){
            KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
            if (postService.isCommentDeletableByUser(user, post, commentPosition)){
                out << body()
            }

        }
    }

    def voteCommentLi={attrs ->
        VoteType voteType = attrs.voteType
        Post post = attrs.post
        Integer pos = attrs.posComment

        //PRE: USER IS LOGGED
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id);
        PostComment comment = post.comments.get(pos);
        def link = createLink(mapping:"postVoteComment", params:post.encodeAsLinkProperties()+[commentPosition:pos, voteType:voteType])
        def numVotes = 0
        def symbol = ""
        def cssClass=""
        switch (voteType){
            case VoteType.NEGATIVE:
                numVotes = !comment.negativeVotes?0:comment.negativeVotes.size()
                cssClass = "minus"
                symbol = "-"
                break;
            case VoteType.POSITIVE:
                numVotes = !comment.positiveVotes?0:comment.positiveVotes.size()
                cssClass = "plus"
                symbol = "+"
                break;
            default:
                log.warn("Displaying a not supported VoteType: $voteType")
                break;
        }
        if (!postService.isCommentVotableByUser(user, post, pos )){
            cssClass += " disabled"
        }
        out << "<span>${numVotes}</span> <a href='$link' class='votePostCommentLink ${cssClass}'>${symbol}</a>"

    }


    def loggedAsPolitician
    def loggedAsUser
    def noLogged
    def insideUserOption
    def userOption = {attrs, body->

        Post post = attrs.post
        insideUserOption = true
        if (springSecurityService.isLoggedIn()){
            noLogged = false
            KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
            loggedAsPolitician = postService.isAllowedToDefendAPost(post, user)
            loggedAsUser = !loggedAsPolitician
        }else{
            noLogged = true
            loggedAsPolitician = false
            loggedAsUser = false
        }
        out << body()
        insideUserOption = false
    }
    def asUser={attrs, body ->
        if (!insideUserOption){
            log.warn("El tag lib PostTagLib.asUser se ha ejecutado incorrectamente. Params: [insideUserOption:${insideUserOption}, attrs:$attrs ]")
            throw new Exception("Este taglib se debe ejecutar dentro de userOption")
        }
        if (loggedAsUser){
            out << body()
        }
    }
    def asPolitician={attrs, body ->
        if (!insideUserOption){
            log.warn("El tag lib PostTagLib.asPolitician se ha ejecutado incorrectamente. Params: [insideUserOption:${insideUserOption}, attrs:$attrs ]")
            throw new Exception("Este taglib se debe ejecutar dentro de userOption")
        }
        if (loggedAsPolitician){
            out << body()
        }
    }
    def asNoLogged={attrs, body ->
        if (!insideUserOption){
            log.warn("El tag lib PostTagLib.asNoLogged se ha ejecutado incorrectamente. Params: [insideUserOption:${insideUserOption}, attrs:$attrs ]")
            throw new Exception("Este taglib se debe ejecutar dentro de userOption")
        }
        if (noLogged){
            out << body()
        }
    }
}
