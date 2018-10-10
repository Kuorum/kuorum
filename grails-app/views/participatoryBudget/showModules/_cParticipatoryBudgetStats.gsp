<ul class="leader-post-stats">
    <li>
        <span class="fal fa-eye" aria-hidden="true"></span>
        <span class="info"><g:message code="debate.timesSeen" args="[participatoryBudget.visits]" /></span>
    </li>
    <li>
        <span class="fal fa-users" aria-hidden="true"></span>
        <span class="info"><g:message code="debate.authorFollowers" args="[campaignUser.followers.size()]" /></span>
    </li>
    <li>
        <span class="fal fa-money-bill-alt" aria-hidden="true"></span>
        <span class="info"><g:message code="participatoryBufget.columnC.basicStats.numProposals" args="[participatoryBudget.basicStats.numProposals]" /></span>
    </li>
    <li>
        <span class="fal fa-rocket" aria-hidden="true"></span>
        <span class="info"><g:message code="participatoryBufget.columnC.basicStats.numSupports" args="[participatoryBudget.basicStats.numSupports]" /></span>
    </li>
    <g:if test="${![org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.ADDING_PROPOSALS, org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.TECHNICAL_REVIEW].contains(participatoryBudget.status)}">
        <li>
            <span class="fal fa-shopping-cart" aria-hidden="true"></span>
            <span class="info"><g:message code="participatoryBufget.columnC.basicStats.numVotes" args="[participatoryBudget.basicStats.numVotes]" /></span>
        </li>
    </g:if>
    <li>
        <span class="fal fa-flag" aria-hidden="true"></span>
        <span class="info"><g:message code="participatoryBufget.columnC.basicStats.numProposalsApproved" args="[participatoryBudget.basicStats.numProposalsApproved]" /></span>
    </li>
    <li>
        <span class="fal fa-clock" aria-hidden="true"></span>
        <span class="info"><g:message code="debate.lastActivity"/> <kuorumDate:humanDate date="${participatoryBudget.lastActivity}"/> </span>
    </li>
</ul>
