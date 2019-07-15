<ul class="leader-post-stats">
    <li>
        <span class="fal fa-eye" aria-hidden="true"></span>
        <span class="info"><g:message code="debate.timesSeen" args="[districtProposal.visits]" /></span>
    </li>
    <li>
        <span class="fal fa-users" aria-hidden="true"></span>
        <span class="info"><g:message code="debate.authorFollowers" args="[campaignUser.numFollowers]" /></span>
    </li>
    <g:if test="${districtProposal.activeSupport}">
        <li>
            <span class="fal fa-rocket" aria-hidden="true"></span>
            <span class="info"><g:message code="districtProposal.stats.numberSupports" args="[districtProposal.numSupports]" /></span>
        </li>
    </g:if>
    <g:if test="${![org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.ADDING_PROPOSALS, org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.TECHNICAL_REVIEW].contains(districtProposal.participatoryBudget.status) && org.kuorum.rest.model.communication.participatoryBudget.TechnicalReviewStatusRDTO.VALID.equals(districtProposal.technicalReviewStatus)}">
        <li>
            <span class="fal fa-shopping-cart" aria-hidden="true"></span>
            <span class="info"><g:message code="districtProposal.stats.numberVotes" args="[districtProposal.numVotes]" /></span>
        </li>
    </g:if>
    <li>
        <span class="fal fa-clock" aria-hidden="true"></span>
        <span class="info"><g:message code="debate.lastActivity"/> <kuorumDate:humanDate date="${districtProposal.lastActivity}"/> </span>
    </li>
</ul>
