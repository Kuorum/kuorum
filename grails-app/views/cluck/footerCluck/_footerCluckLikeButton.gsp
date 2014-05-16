<li class="like-number">
    <span class="counter">${post.numVotes}</span>
    <meta itemprop="interactionCount" content="UserLikes:${post.numVotes}"><!-- pasarle el valor que corresponda -->
    <g:link mapping="postVoteIt" class="${postUtil.cssClassIfVoted(post:post)} action drive" params="${post.encodeAsLinkProperties()}" rel="nofollow">
        <span class="fa fa-rocket fa-lg"></span>
        <span class="${displayingColumnC?'sr-only':'hidden-xs'}">
            <g:message code="cluck.footer.vote"/>
        </span>
    </g:link>
</li>