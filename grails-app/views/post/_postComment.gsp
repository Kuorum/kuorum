<%@ page import="kuorum.core.model.VoteType" %>
<li id="commentPos_${pos}" style="display:${display?:''} ">
    <script>
        function removeComment(liId){
            $("#"+liId).hide('slow')
        }
    </script>
    <kuorumDate:humanDate date="${comment.dateCreated}"/>
    <div class="user author" itemprop="author" itemscope itemtype="http://schema.org/Person">
        <userUtil:showUser user="${comment.kuorumUser}" showRole="true"/>
    </div>

    <p>${raw(comment.text)}</p>
    <div class="actions clearfix">
        <postUtil:ifCommentIsDeletable post="${post}" commentPosition="${pos}">
            <g:remoteLink url="[mapping:'postDelComment', params:post.encodeAsLinkProperties() + [commentPosition:pos]]" onSuccess="removeComment('commentPos_${pos}')" class="pull-left">
                <g:message code="post.show.comments.delete"/>
            </g:remoteLink>
        </postUtil:ifCommentIsDeletable>
        <sec:ifLoggedIn>
            <ul class="pull-right">
                <li><postUtil:voteCommentLi posComment="${pos}" post="${post}" voteType="${kuorum.core.model.VoteType.POSITIVE}"/></li>
                <li><postUtil:voteCommentLi posComment="${pos}" post="${post}" voteType="${kuorum.core.model.VoteType.NEGATIVE}"/></li>
            </ul>
        </sec:ifLoggedIn>
    </div>
</li>