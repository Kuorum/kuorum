<g:set var="extraUnconfirmedClass" value=""/>
<g:set var="extraConfirmedClass" value="hide"/>
<g:if test="${eventRegistration}">
    <g:set var="extraUnconfirmedClass" value="hide"/>
    <g:set var="extraConfirmedClass" value=""/>
    <g:set var="extraCommentBoxConfirmedClass" value="event-confirmed"/>
</g:if>
<div class="comment-box call-to-action call-to-action-confirm-event hidden-sm ${extraCommentBoxConfirmedClass}">
    <div class="comment-header unconfirmed ${extraUnconfirmedClass}">
        <span class="call-title">${message(code: "event.callToAction.title")}</span>
        <span class="call-subTitle">${message(code: "event.callToAction.subTitle", args: [debateUser.name])}</span>
    </div>
    <div class="actions clearfix">
        <button
                type="button"
                class="btn btn-blue btn-lg event-confirm-button unconfirmed ${extraUnconfirmedClass}"
                data-userLoggedAlias="${userUtil.loggedUserAlias()}"
                data-postUrl="${g.createLink(mapping: 'eventConfirmAssistance')}"
                data-debateId="${debate.id}"
                data-debateAlias="${debateUser.alias}"
        >
            ${message(code: "event.callToAction.button")}
        </button>
        <div class="event-confirm-success confirmed ${extraConfirmedClass}">
            <g:message code="event.callToAction.success.text"/> <span class="fa fa-check"></span>
        </div>
    </div>
</div>