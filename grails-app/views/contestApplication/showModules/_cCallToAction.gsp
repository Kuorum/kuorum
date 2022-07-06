<g:set var="callTitleMsg"
       value="${g.message(code: 'contestApplication.callToAction.' + contestApplication.campaignStatusRSDTO + '.title')}"/>
<g:set var="callSubtitleMsg"
       value="${g.message(code: 'contestApplication.callToAction.' + contestApplication.campaignStatusRSDTO + '.subtitle')}"/>
<g:set var="callButtonMsg"
       value="${g.message(code: 'contestApplication.callToAction.' + contestApplication.campaignStatusRSDTO + '.subtitle')}"/>
<g:if test="${contestApplication.published && [org.kuorum.rest.model.communication.contest.ContestStatusDTO.VOTING, org.kuorum.rest.model.communication.contest.ContestStatusDTO.RESULTS].contains(contestApplication.contest.status)}">
    <g:set var="callTitleMsg"
           value="${g.message(code: 'contestApplication.callToAction.' + contestApplication.contest.status + '.' + contestApplication.campaignStatusRSDTO + '.title')}"/>
    <g:set var="callSubtitleMsg"
           value="${g.message(code: 'contestApplication.callToAction.' + contestApplication.contest.status + '.' + contestApplication.campaignStatusRSDTO + '.subtitle')}"/>
    <g:set var="callButtonMsg"
           value="${g.message(code: 'contestApplication.callToAction.' + contestApplication.contest.status + '.' + contestApplication.campaignStatusRSDTO + '.button')}"/>
</g:if>
<div class="comment-box call-to-action call-to-action-add-proposal ${hideXs ? 'hidden-sm hidden-xs' : ''}">
    <div class="comment-header">
        <span class="call-title">${callTitleMsg}</span>
        <span class="call-subTitle">${callSubtitleMsg}</span>
    </div>
    <g:if test="${contestApplication.published && contestApplication.contest.status == org.kuorum.rest.model.communication.contest.ContestStatusDTO.VOTING}">
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
    <g:render template="/contestApplication/showModules/cCallToActionInfoContest"
              model="[contestApplication: contestApplication]"/>
</div>