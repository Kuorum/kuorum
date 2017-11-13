
<div class="comment-box call-to-action hidden-sm">
    <div class="comment-header">
        <span class="call-title">${message(code: "event.callToAction.title")}</span>
        <span class="call-subTitle">${message(code: "event.callToAction.subTitle", args: [debateUser.name])}</span>
    </div>
    <div class="comment-proposal clearfix">
    </div>

    <div class="actions clearfix">

        <g:set var="extraButtonClass" value=""/>
        <g:set var="extraSuccessClass" value="hide"/>
        <g:if test="${eventRegistration}">
            <g:set var="extraButtonClass" value="hide"/>
            <g:set var="extraSuccessClass" value=""/>
        </g:if>
        <button
                type="button"
                class="btn btn-blue btn-lg event-confirm-button ${extraButtonClass}"
                data-userLoggedAlias="${userUtil.loggedUserAlias()}"
                data-postUrl="${g.createLink(mapping: 'eventConfirmAssistance')}"
                data-debateId="${debate.id}"
                data-debateAlias="${debateUser.alias}"
        >
            ${message(code: "event.callToAction.button")}
        </button>
        <div class="event-confirm-success ${extraSuccessClass}">
            <g:message code="event.callToAction.success"/> <span class="fa fa-check"></span>
        </div>
    </div>
</div>