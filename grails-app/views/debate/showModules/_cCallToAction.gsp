<g:set var="callTitleMsg" value="${g.message(code:'debate.proposals.callToAction.title')}"/>
<g:set var="callSubtitleMsg" value="${g.message(code:'debate.proposals.callToAction.subTitle', args: [debateUser.name])}"/>
<g:set var="callButtonMsg" value="${g.message(code:'debate.proposals.callToAction.button')}"/>
<g:if test="${debate.closed}">
    <g:set var="callTitleMsg" value="${g.message(code:'debate.callToAction.closed.title')}"/>
    <g:if test="${debate.endDate?.before(new Date())}">
        <g:set var="callClosedTimeAgo"><kuorumDate:humanDate date="${debate.endDate}"/></g:set>
        <g:set var="callSubtitleMsg" value="${g.message(code:"debate.callToAction.closed.subtitle.after", args: [callClosedTimeAgo], encodeAs: "raw")}"/>
    </g:if>
    <g:else>
        <g:set var="callClosedTimeAgo"><kuorumDate:humanDate date="${debate.startDate}"/></g:set>
        <g:set var="callSubtitleMsg" value="${g.message(code:"debate.callToAction.closed.subtitle.before", args: [callClosedTimeAgo], encodeAs: "raw")}"/>
    </g:else>
    <g:set var="callButtonMsg" value="${g.message(code:"debate.callToAction.closed.button")}"/>
</g:if>
<div class="comment-box call-to-action call-to-action-add-proposal hidden-sm hidden-xs">
    <div class="comment-header">
        <span class="call-title">${callTitleMsg}</span>
        <span class="call-subTitle">${callSubtitleMsg}</span>
    </div>

    <g:if test="${debate.closed}">
        <div class="actions clearfix">
            <button type="button" class="btn btn-blue btn-lg" data-goto="#proposal-list" id="debate-call-to-action">
                ${callButtonMsg}
            </button>
        </div>
    </g:if>
    <g:else>
        <div class="comment-proposal clearfix">
            <div class="comment editable col-md-11 col-xs-12" data-placeholder="${message(code: "debate.proposal.placeholder")}" style="min-height: 100px; padding-top: 20px"></div>
        </div>

        <span for="editable" class="error" style="display: none;"><span class="tooltip-arrow"></span>${message(code: "debate.proposals.error")}</span>

        <div class="actions clearfix">
            <button
                    type="button"
                    class="btn btn-blue btn-lg publish publish-proposal"
                    data-userLoggedAlias="${userUtil.loggedUserId()}"
                    data-postUrl="${g.createLink(mapping: 'debateProposalNew')}"
                    data-debateId="${debate.id}"
                    data-debateAlias="${debateUser.alias}"
            >
                ${callButtonMsg}
            </button>
        </div>
    </g:else>
</div>