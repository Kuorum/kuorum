<g:set var="callButtonMsg"
       value="${g.message(code: 'contestApplication.callToAction.' + contestApplication.campaignStatusRSDTO + '.button')}"/>
<g:if test="${contestApplication.published && [org.kuorum.rest.model.communication.contest.ContestStatusDTO.VOTING,
                                               org.kuorum.rest.model.communication.contest.ContestStatusDTO.RESULTS]
                                               .contains(contestApplication.contest.status)}" >
<g:set var="callButtonMsg"
           value="${g.message(code: 'contestApplication.callToAction.' + contestApplication.contest.status + '.' + contestApplication.campaignStatusRSDTO + '.button')}"/>

<div class="actions call-to-action-mobile go-to-vote">
    <button type="button" class="comment-box btn btn-blue btn-lg call-message" name="${callButtonMsg}">
        ${callButtonMsg}
    </button>
    <span class="fas fa-caret-down arrow"></span>
    <button type="button" class="comment-box btn btn-blue btn-xl btn-circle call-button" name="${callButtonMsg}">
        <span class="fal fa-rocket fa-2x"></span>
    </button>
</div>
</g:if>