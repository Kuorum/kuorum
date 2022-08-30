<g:set var="callTitleMsg" value="${g.message(code:'petition.callToAction.draft.title')}"/>
<g:set var="callSubtitleMsg" value="${g.message(code:'petition.callToAction.draft.subtitle')}"/>
<g:if test="${org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SCHEDULED.equals(petition.newsletter?.status?:null)}">
    <g:set var="callTitleMsg" value="${g.message(code:"petition.callToAction.SCHEDULED.title", args: [campaignUser.name])}"/>
    <g:set var="callSubtitleMsg" value="${g.message(code:"petition.callToAction.SCHEDULED.subtitle", args: [campaignUser.name])}"/>
</g:if>
<g:elseif test="${org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.PROCESSING.equals(petition.newsletter?.status?:null)}">
    <g:set var="callTitleMsg" value="${g.message(code:"petition.callToAction.PROCESSING.title", args: [campaignUser.name])}"/>
    <g:set var="callSubtitleMsg" value="${g.message(code:"petition.callToAction.PROCESSING.subtitle", args: [campaignUser.name])}"/>
</g:elseif>
<g:elseif test="${petition.closed}">
    <g:set var="callTitleMsg" value="${g.message(code:'petition.callToAction.closed.title')}"/>
    <g:if test="${petition.endDate?.before(new Date())}">
        <g:set var="callClosedTimeAgo"><kuorumDate:humanDate date="${petition.endDate}"/></g:set>
        <g:set var="callSubtitleMsg" value="${g.message(code:"petition.callToAction.closed.subtitle.after", args: [callClosedTimeAgo], encodeAs: "raw")}"/>
    </g:if>
    <g:else>
        <g:set var="callClosedTimeAgo"><kuorumDate:humanDate date="${petition.startDate}"/></g:set>
        <g:set var="callSubtitleMsg" value="${g.message(code:"petition.callToAction.closed.subtitle.before", args: [callClosedTimeAgo], encodeAs: "raw")}"/>
    </g:else>
</g:elseif>
<g:elseif test="${petition.published}">
    <g:set var="callTitleMsg" value="${g.message(code:"petition.callToAction.SENT.title", args: [campaignUser.name])}"/>
    <g:set var="callSubtitleMsg" value="${g.message(code:"petition.callToAction.SENT.subtitle", args: [campaignUser.name])}"/>
</g:elseif>

<div class="comment-box call-to-action call-to-action-add-proposal ${hideXs?'hidden-sm hidden-xs':''}">
    <div class="comment-header">
        <span class="call-title">${callTitleMsg}</span>
        <span class="call-subTitle">${callSubtitleMsg}</span>
    </div>
    <g:if test="${petition.published && !petition.closed}">
        <div class="actions clearfix">
            <g:link
                    mapping="petitionSign"
                    params="${petition.encodeAsLinkProperties()}"
                    class="petition-sign petition-sign-${petition.id} btn btn-blue-status btn-lg ${petition.signed ? 'on' : ''}"
                    type="button"
                    data-petitionId="${petition.id}"
                    data-petitionUserId="${petition.user.id}"
                    data-campaignValidationActive="${petition.checkValidationActive}"
                    data-campaignGroupValidationActive="${petition.groupValidation ? g.createLink(mapping: "campaignCheckGroupValidation", params: petition.encodeAsLinkProperties()) : ''}"
                    data-campaignId="${petition.id}"
                    data-loggedUser="${sec.username()}"
                    data-requestPdfUrl="${g.createLink(mapping: "petitionPdfRequest", params: petition.encodeAsLinkProperties())}"
                    data-viewPdfUrl="${g.createLink(mapping: "petitionPdfView", params: petition.encodeAsLinkProperties())}"
                    data-txt-on="${g.message(code: "petition.callToAction.SENT.button.on", args: [petition.title])}"
                    data-txt-on-hover="${g.message(code: "petition.callToAction.SENT.button.on.hover", args: [petition.title])}"
                    data-txt-off="${g.message(code: "petition.callToAction.SENT.button.off", args: [petition.title])}"
                    data-txt-off-hover="${g.message(code: "petition.callToAction.SENT.button.off.hover", args: [petition.title])}">
                <span class="${petition.signed ? 'fas' : 'fal'} fa-microphone"></span>
            </g:link>
        </div>
    </g:if>
</div>