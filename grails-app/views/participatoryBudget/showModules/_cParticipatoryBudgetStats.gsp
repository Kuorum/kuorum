<ul class="leader-post-stats">
    <li>
        <span class="fal fa-eye" aria-hidden="true"></span>
        <span class="info"><g:message code="debate.timesSeen" args="[participatoryBudget.visits]" /></span>
    </li>
    <li>
        <span class="fal fa-users" aria-hidden="true"></span>
        <span class="info"><g:message code="debate.authorFollowers" args="[campaignUser.numFollowers]" /></span>
    </li>
    <li>
        <span class="fal fa-money-bill-alt" aria-hidden="true"></span>
        <span class="info"><g:message code="participatoryBufget.columnC.basicStats.numProposals" args="[participatoryBudget.basicStats.numProposals]" /></span>
    </li>
    <g:if test="${![org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.ADDING_PROPOSALS, org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.TECHNICAL_REVIEW].contains(participatoryBudget.status)}">
        <li>
            <span class="fal fa-clipboard-check" aria-hidden="true"></span>
            <span class="info"><g:message code="participatoryBufget.columnC.basicStats.numProposalsApproved" args="[participatoryBudget.basicStats.numProposalsApproved]" /></span>
        </li>
    </g:if>
    <g:if test="${[org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.RESULTS].contains(participatoryBudget.status)}">
        <li>
            <span class="fal fa-flag" aria-hidden="true"></span>
            <span class="info"><g:message code="participatoryBufget.columnC.basicStats.numProposalsImplemented" args="[participatoryBudget.basicStats.numProposalsImplemented]" /></span>
        </li>
    </g:if>
    <g:if test="${participatoryBudget.activeSupport}">
        <li>
            <span class="fal fa-rocket" aria-hidden="true"></span>
            <span class="info"><g:message code="participatoryBufget.columnC.basicStats.numSupports" args="[participatoryBudget.basicStats.numSupports]" /></span>
        </li>
    </g:if>
    <g:if test="${![org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.ADDING_PROPOSALS, org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.TECHNICAL_REVIEW].contains(participatoryBudget.status)}">
        <li>
            <span class="fal fa-shopping-cart" aria-hidden="true"></span>
            <span class="info"><g:message code="participatoryBufget.columnC.basicStats.numVotes" args="[participatoryBudget.basicStats.numVotes]" /></span>
        </li>
    </g:if>

    <li>
        <span class="fal fa-clock" aria-hidden="true"></span>
        <span class="info"><g:message code="debate.lastActivity"/> <kuorumDate:humanDate date="${participatoryBudget.lastActivity}"/> </span>
    </li>
</ul>
