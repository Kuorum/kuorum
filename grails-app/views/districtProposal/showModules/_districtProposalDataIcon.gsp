<g:if test="${districtProposal.published}">
    <g:set var="iconClass" value="fa-rocket"/>
    <g:set var="iconNumber" value="${districtProposal.numSupports}"/>
    <g:set var="isActive" value="${districtProposal.isSupported}"/>
    <g:set var="callButtonActionClass" value="districtProposal-support"/>
    <g:set var="callButtonAction" value="${g.createLink(mapping:'participatoryBudgetDistrictProposalSupport', params:districtProposal.encodeAsLinkProperties())}"/>
    <g:if test="${[
            org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.BALLOT,
            org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.CLOSED,
            org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.RESULTS]
            .contains(districtProposal.participatoryBudget.status)}">
        <g:set var="iconClass" value="fa-shopping-cart"/>
        <g:set var="iconNumber" value="${districtProposal.numVotes}"/>
        <g:set var="isActive" value="${districtProposal.isVoted}"/>
        <g:set var="callButtonActionClass" value="districtProposal-vote"/>
        <g:set var="callButtonAction" value="${g.createLink(mapping:'participatoryBudgetDistrictProposalVote', params:districtProposal.encodeAsLinkProperties())}"/>
    </g:if>
    <g:if test="${[
            org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.TECHNICAL_REVIEW,
            org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.CLOSED,
            org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.RESULTS]
            .contains(districtProposal.participatoryBudget.status)}">
        <g:set var="callButtonActionClass" value="disabled"/>
    </g:if>
    <div class="comment-counter pull-right" id="comment-counter-${districtProposal.id}">
        <a type="button" class="${isActive?'active':''} ${callButtonActionClass}"
                href="${callButtonAction}"
                data-campaignValidationActive="${districtProposal.checkValidationActive}"
                data-districtId="${districtProposal.district.id}"
                data-participatoryBudgetId="${districtProposal.participatoryBudget.id}"
                data-proposalId="${districtProposal.id}"
                data-loggedUser="${sec.username()}"
                data-txt-on="${btnStatusTextOn}"
                data-txt-on-hover="${btnStatusTextOnHover}"
                data-txt-off="${btnStatusTextOff}"
                data-txt-off-hover="${btnStatusTextOffHover}">
            <span class="${isActive?'fas':'fal'} ${iconClass}" aria-hidden="true"></span>
            <span class="number">${iconNumber}</span>
        </a>
    </div>
</g:if>
