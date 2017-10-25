<ul class="leader-post-stats">
    <li>
        <span class="fa fa-eye" aria-hidden="true"></span>
        <span class="info"><g:message code="debate.timesSeen" args="[debate.visits]" /></span>
    </li>
    <li>
        <span class="fa fa-users" aria-hidden="true"></span>
        <span class="info"><g:message code="debate.authorFollowers" args="[debateUser.numFollowers]" /></span>
    </li>
    <li>
        <span class="fa fa-lightbulb-o" aria-hidden="true"></span>
        <span class="info"><g:message code="debate.proposalsMade" args="[proposalPage.total]" /></span>
    </li>
    <li>
        <span class="fa fa-heart-o" aria-hidden="true"></span>
        <span class="info"><g:message code="debate.supportsCollected" args="[debate.likes]" /></span>
    </li>
    <li>
        <span class="fa fa-comment-o" aria-hidden="true"></span>
        <span class="info"><g:message code="debate.peopleCommentedProposals" args="[proposalPage?.data?.collect{it.comments.size()}.sum()?:0]" /></span>
    </li>
    <li>
        <span class="fa fa-flag-o" aria-hidden="true"></span>
        <span class="info"><g:message code="debate.proposalsPinned" args="[proposalPage?.data?.findAll{it.pinned}?.size()?:0]" /></span>
    </li>
    <li>
        <span class="fa fa-clock-o" aria-hidden="true"></span>
        <span class="info"><g:message code="debate.lastActivity"/> <kuorumDate:humanDate date="${lastActivity}"/> </span>
    </li>
</ul>
