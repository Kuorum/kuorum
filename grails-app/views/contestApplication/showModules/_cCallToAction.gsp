<g:set var="callTitleMsg" value="${g.message(code: 'contestApplication.callToAction.draft.title')}"/>
<g:set var="callSubtitleMsg" value="${g.message(code: 'contestApplication.callToAction.draft.subtitle')}"/>
<g:set var="callButtonMsg" value="XXXXXXXXXX"/>

<div class="comment-box call-to-action call-to-action-add-proposal ${hideXs ? 'hidden-sm hidden-xs' : ''}">
    <div class="comment-header">
        <span class="call-title">${callTitleMsg}</span>
        <span class="call-subTitle">${callSubtitleMsg}</span>
    </div>
    <g:if test="${contestApplication.published}">
        <div class="actions clearfix">
            <g:link
                    mapping="districtProposalCreate"
                    params="${contestApplication.encodeAsLinkProperties()}"
                    type="button"
                    data-loggedUser="${sec.username()}"
                    class="btn btn-blue btn-lg">
                ${callButtonMsg}
            </g:link>
        </div>
    </g:if>
</div>