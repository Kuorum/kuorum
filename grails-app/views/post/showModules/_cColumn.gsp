<ul class="leader-post-stats">
    <li>
        <span class="fal fa-eye" aria-hidden="true"></span>
        <span class="info"><g:message code="debate.timesSeen" args="[post.visits]" /></span>
    </li>
    <g:if test="${!post.event}">
        <li>
            <span class="fal fa-heart" aria-hidden="true"></span>
            <span class="info"><g:message code="debate.supportsCollected" args="[post.likes]" /></span>
        </li>
    </g:if>
</ul>
