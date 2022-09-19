<g:set var="contestApplicationVoted" value="${contestApplication.voted}"/>
<g:link
        mapping="contestApplicationVote" params="${contestApplication.encodeAsLinkProperties()}"
        type="button"
        data-loggedUser="${sec.username()}"
        data-userLoggedAlias="${userUtil.loggedUserId()}"
        data-campaignValidationActive="${contestApplication.checkValidationActive}"
        data-allowAnonymousAction="${contestApplication.allowAnonymousAction}"
        data-ajaxAnonymousValidationChecker="${g.createLink(mapping: 'campaignCheckValidation', params: contestApplication.encodeAsLinkProperties())}"
        data-campaignGroupValidationActive="${contestApplication.groupValidation ? g.createLink(mapping: "campaignCheckGroupValidation", params: contestApplication.encodeAsLinkProperties()) : ''}"
        data-campaignId="${contestApplication.id}"
        data-deadLineVotesErrorMsg="${g.message(code:'contestApplication.callToAction.VOTING.error.api.SERVICE_CAMPAIGN_CONTEST_OUT_OF_TIME',args: [contestApplication.contest.title, g.formatDate([format: g.message(code: 'default.date.format'), date: contestApplication.contest.deadLineVotes, timeZone: contestApplication.contest.timeZone])])}"
        data-deadLineVotesTimeStamp="${contestApplication.contest.deadLineVotes.time}"
        data-disabledText="${g.message(code: 'contestApplication.callToAction.VOTING.SENT.button.disabled')}"
        class="contestApplication-vote contestApplication-vote-${contestApplication.id} ${contestApplicationVoted ? 'active disabled' : ''}"
        btn-disabled="${contestApplicationVoted}">
    <span class="${contestApplicationVoted ? 'fas' : 'fal'} fa-scroll" aria-hidden="true"></span>
    <span class="number">${contestApplication.votes}</span>
</g:link>
