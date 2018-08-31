<g:if test="${districtProposal.published}">
    <g:set var="iconClass" value="fa-rocket"/>
    <g:set var="iconNumber" value="${districtProposal.numSupports}"/>
    <g:set var="isActive" value="${districtProposal.isSupported}"/>
    <g:if test="${[
            org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.BALLOT,
            org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.CLOSED,
            org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.RESULTS]
            .contains(districtProposal.participatoryBudget.status)}">
        <g:set var="iconClass" value="fa-shopping-cart"/>
        <g:set var="iconNumber" value="${districtProposal.numVotes}"/>
        <g:set var="isActive" value="${districtProposal.isVoted}"/>
    </g:if>
    <div class="comment-counter pull-right">
        <button type="button ${isActive?'active':''}" data-goto="#survey-progress">
            <span class="fa ${iconClass}" aria-hidden="true"></span>
            <span class="number">${iconNumber}</span>
        </button>
    </div>
</g:if>
