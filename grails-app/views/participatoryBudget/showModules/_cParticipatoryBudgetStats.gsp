<ul class="leader-post-stats">
    <li>
        <span class="fa fa-eye" aria-hidden="true"></span>
        <span class="info"><g:message code="debate.timesSeen" args="[participatoryBudget.visits]" /></span>
    </li>
    <li>
        <span class="fa fa-users" aria-hidden="true"></span>
        <span class="info"><g:message code="debate.authorFollowers" args="[campaignUser.followers.size()]" /></span>
    </li>
    <li>
        <span class="fa fa-money" aria-hidden="true"></span>
        <span class="info"><g:message code="participatoryBufget.columnC.basicStats.numProposals" args="[participatoryBudget.basicStats.numProposals]" /></span>
    </li>
    <li>
        <span class="fa fa-rocket" aria-hidden="true"></span>
        <span class="info"><g:message code="participatoryBufget.columnC.basicStats.numSupports" args="[participatoryBudget.basicStats.numSupports]" /></span>
    </li>
    <li>
        <span class="fa fa-shopping-cart" aria-hidden="true"></span>
        <span class="info"><g:message code="participatoryBufget.columnC.basicStats.numVotes" args="[participatoryBudget.basicStats.numVotes]" /></span>
    </li>
    <li>
        <span class="fa fa-flag-o" aria-hidden="true"></span>
        <span class="info"><g:message code="participatoryBufget.columnC.basicStats.numProposalsApproved" args="[participatoryBudget.basicStats.numProposalsApproved]" /></span>
    </li>
    <li>
        <span class="fa fa-clock-o" aria-hidden="true"></span>
        <span class="info"><g:message code="debate.lastActivity"/> <kuorumDate:humanDate date="${participatoryBudget.lastActivity}"/> </span>
    </li>
</ul>
