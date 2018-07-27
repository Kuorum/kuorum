<g:set var="callTitleMsg" value="${g.message(code:'districtProposal.callToAction.draft.title')}"/>
<g:set var="callSubtitleMsg" value="${g.message(code:'districtProposal.callToAction.draft.subtitle')}"/>
<g:set var="callButtonMsg" value=""/>
<g:if test="${districtProposal.published}">
    <g:if test="${districtProposal.participatoryBudget.status == org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.ADDING_PROPOSALS}">
        <g:set var="callTitleMsg" value="${g.message(code:"districtProposal.callToAction.${districtProposal.participatoryBudget.status}.title", args: [campaignUser.name])}"/>
        <g:set var="callSubtitleMsg" value="${g.message(code:"districtProposal.callToAction.${districtProposal.participatoryBudget.status}.subtitle", args: [campaignUser.name])}"/>
        <g:set var="callButtonMsg" value="${g.message(code:"districtProposal.callToAction.${districtProposal.participatoryBudget.status}.button",args: [campaignUser.name])}"/>
    </g:if>

</g:if>

<div class="comment-box call-to-action call-to-action-add-proposal hidden-sm hidden-xs">
    <div class="comment-header">
        <span class="call-title">${callTitleMsg}</span>
        <span class="call-subTitle">${callSubtitleMsg}</span>
    </div>
    <div class="actions clearfix">
        <g:if test="${districtProposal.published}">
            <a href="" type="button" class="btn btn-blue btn-lg">
                ${callButtonMsg}
            </a>
        </g:if>
    </div>
    <div class="comment-header">
        <span class="call-subTitle">This proposal corresponds to the budget for <g:link mapping="participatoryBudgetShow" params="${districtProposal.participatoryBudget.encodeAsLinkProperties()}" fragment="${districtProposal.district.name}">${districtProposal.district.name}</g:link></span>
        <div class="budget">
            <div class="campaign-progress-bar-wrapper">
                <h4>Budget for this area: <span class="budget-price">${districtProposal.district.budget.encodeAsReducedPrice()}€</span></h4>
                <div class="campaign-progress-bar" data-width="10">
                    <div class="pop-up">
                        300 gastado de 20
                        <div class="arrow"></div>
                    </div>
                    <div class="progress-bar-custom">
                        <div class="progress-bar-custom-done"></div>
                    </div>
                </div>
                <div class="campaign-progress-bar-footer">
                    <g:link mapping="participatoryBudgetShow" params="${districtProposal.participatoryBudget.encodeAsLinkProperties()}" fragment="${districtProposal.district.name}">
                        see to the full budget
                    </g:link>
                </div>
            </div>
        </div>
    </div>
</div>