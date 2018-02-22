<ul class="leader-post-stats">
    <li>
        <span class="fa fa-eye" aria-hidden="true"></span>
        <span class="info"><g:message code="debate.timesSeen" args="[post.visits]" /></span>
    </li>
    <li>
        <span class="fa fa-users" aria-hidden="true"></span>
        <span class="info"><g:message code="debate.authorFollowers" args="[postUser.followers.size()]" /></span>
    </li>
    <g:if test="${!post.event}">
        <li>
            <span class="fa fa-heart-o" aria-hidden="true"></span>
            <span class="info"><g:message code="debate.supportsCollected" args="[post.likes]" /></span>
        </li>
    </g:if>
</ul>
