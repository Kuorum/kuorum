package kuorum

import kuorum.core.model.PostType
import kuorum.core.model.solr.SolrSubType
import kuorum.core.model.solr.SolrType
import kuorum.post.Post
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
        if (!post.debates.isEmpty() || post.defender){
            out << body()
        }
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
            case PostType.HISTORY:  out << "fa-book"; break;
            case PostType.QUESTION: out << "fa-question"; break;
            case PostType.PURPOSE:  out << "fa-lightbulb-o"; break;
        }
    }
    def cssIconSolrSubType={attrs, body ->
        SolrSubType solrSubType = attrs.solrSubType

        switch (solrSubType){
            case SolrSubType.HISTORY:  out << "fa-book"; break;
            case SolrSubType.QUESTION: out << "fa-question"; break;
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

    def postShowImage={attrs ->
        Post post = attrs.post
        if (post.multimedia){
            out <<"""
        <div class="photo">
            <img src="${post.multimedia.url}" alt="${post.multimedia.alt}" itemprop="image">
        </div>
"""
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
        out << range.to
    }

    def ifPostIsEditable={attrs, body->
        Post post = attrs.post
        if (springSecurityService.isLoggedIn()){
            KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
            if (postService.isPostUpdatableByUser(post, user)){
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
}
