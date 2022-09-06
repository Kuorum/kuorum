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
                    mapping="contestApplicationVote"
                    params="${contestApplication.encodeAsLinkProperties()}"
                    type="button"
                    data-loggedUser="${sec.username()}"
                    data-allowAnonymousAction="${contestApplication.allowAnonymousAction}"
                    data-ajaxAnonymousValidationChecker="${g.createLink(mapping: 'campaignCheckValidation', params: contestApplication.encodeAsLinkProperties())}"
                    data-userLoggedAlias="${userUtil.loggedUserId()}"
                    data-campaignValidationActive="${contestApplication.checkValidationActive}"
                    data-campaignGroupValidationActive="${contestApplication.groupValidation ? g.createLink(mapping: "campaignCheckGroupValidation", params: contestApplication.encodeAsLinkProperties()) : ''}"
                    data-campaignId="${contestApplication.id}"
                    data-disabledText="${g.message(code: 'contestApplication.callToAction.VOTING.SENT.button.disabled')}"
                    class="btn btn-blue btn-lg contestApplication-vote">
                ${callButtonMsg}
            </g:link>
        </div>
    </g:if>
    <g:render template="/contestApplication/showModules/cCallToActionInfoContest"
              model="[contestApplication: contestApplication]"/>
</div>