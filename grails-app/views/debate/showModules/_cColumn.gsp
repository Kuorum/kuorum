<ul class="leader-post-stats">
    <li>
        <span class="fal fa-eye" aria-hidden="true"></span>
        <span class="info"><g:message code="debate.timesSeen" args="[debate.visits]" /></span>
    </li>
    <li>
        <span class="fal fa-users" aria-hidden="true"></span>
        <span class="info"><g:message code="debate.authorFollowers" args="[debateUser.followers.size()]" /></span>
    </li>
    <li>
        <span class="fal fa-lightbulb" aria-hidden="true"></span>
        <span class="info"><g:message code="debate.proposalsMade" args="[proposalPage.total]" /></span>
    </li>
    <li>
        <span class="fas fa-heart" aria-hidden="true"></span>
        <span class="info"><g:message code="debate.supportsCollected" args="[debate.likes]" /></span>
    </li>
    <li>
        <span class="fal fa-comment" aria-hidden="true"></span>
        <span class="info"><g:message code="debate.peopleCommentedProposals" args="[proposalPage?.data?.collect{it.comments.size()}.sum()?:0]" /></span>
    </li>
    <li>
        <span class="fal fa-flag" aria-hidden="true"></span>
        <span class="info"><g:message code="debate.proposalsPinned" args="[proposalPage?.data?.findAll{it.pinned}?.size()?:0]" /></span>
    </li>
    <li>
        <span class="fal fa-clock" aria-hidden="true"></span>
        <span class="info"><g:message code="debate.lastActivity"/> <kuorumDate:humanDate date="${debate.lastActivity}"/> </span>
    </li>
</ul>
