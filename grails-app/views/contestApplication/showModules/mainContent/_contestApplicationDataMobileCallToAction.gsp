<g:set var="callTitleMsg"
       value="${g.message(code: 'contestApplication.callToAction.' + contestApplication.campaignStatusRSDTO + '.title')}"/>
<g:set var="callSubtitleMsg"
       value="${g.message(code: 'contestApplication.callToAction.' + contestApplication.campaignStatusRSDTO + '.subtitle')}"/>
<g:set var="callButtonMsg"
       value="${g.message(code: 'contestApplication.callToAction.' + contestApplication.campaignStatusRSDTO + '.button')}"/>
<g:if test="${contestApplication.published && [org.kuorum.rest.model.communication.contest.ContestStatusDTO.VOTING, org.kuorum.rest.model.communication.contest.ContestStatusDTO.RESULTS].contains(contestApplication.contest.status)}">
    <g:set var="contestApplicationVoted" value="${contestApplication.voted}"/>
    <g:set var="callTitleMsg"
           value="${g.message(code: 'contestApplication.callToAction.' + contestApplication.contest.status + '.' + contestApplication.campaignStatusRSDTO + '.title')}"/>
    <g:set var="callSubtitleMsg"
           value="${g.message(code: 'contestApplication.callToAction.' + contestApplication.contest.status + '.' + contestApplication.campaignStatusRSDTO + '.subtitle')}"/>
    <g:set var="callButtonMsg"
           value="${g.message(code: 'contestApplication.callToAction.' + contestApplication.contest.status + '.' + contestApplication.campaignStatusRSDTO + '.button')}"/>
    <g:set var="callButtonMsgDisabled"
           value="${g.message(code: 'contestApplication.callToAction.' + contestApplication.contest.status + '.' + contestApplication.campaignStatusRSDTO + '.button.disabled')}"/>
</g:if>

<div class="actions call-to-action-mobile go-to-vote">
    <button type="button" class="comment-box btn btn-blue btn-lg call-message" name="${callButtonMsg}">
        ${callButtonMsg}
    </button>
    <span class="fas fa-caret-down arrow"></span>
    <button type="button" class="comment-box btn btn-blue btn-xl btn-circle call-button" name="${callButtonMsg}">
        <span class="fal fa-rocket fa-2x"></span>
    </button>
</div>