<ul class="leader-post-stats">
    <li>
        <span class="fal fa-eye" aria-hidden="true"></span>
        <span class="info"><g:message code="debate.timesSeen" args="[post.visits]" /></span>
    </li>
    <li>
        <span class="fal fa-users" aria-hidden="true"></span>
        <span class="info"><g:message code="debate.authorFollowers" args="[postUser.numFollowers]" /></span>
    </li>
    <g:if test="${!post.event}">
        <li>
            <span class="fas fa-heart" aria-hidden="true"></span>
            <span class="info"><g:message code="debate.supportsCollected" args="[post.likes]" /></span>
        </li>
    </g:if>
</ul>
