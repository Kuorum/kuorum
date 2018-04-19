<g:set var="event" value="${campaign?.event}"/>
<g:if test="${event}">
    <g:set var="extraUnconfirmedClass" value=""/>
    <g:set var="extraConfirmedClass" value="hide"/>
    <g:set var="fullCapacity" value="${event && event.capacity != null && event.capacity <= event.amountAssistants}"/>
    <g:if test="${event?.registered}">
        <g:set var="extraUnconfirmedClass" value="hide"/>
        <g:set var="extraConfirmedClass" value=""/>
        <g:set var="extraCommentBoxConfirmedClass" value="box-event-confirmed"/>
    </g:if>
    <div class="hidden-sm comment-box call-to-action call-to-action-confirm-event ${extraCommentBoxConfirmedClass}">
        <div class="comment-header event-unconfirmed ${extraUnconfirmedClass}">
            <span class="call-title">${message(code: "event.callToAction.title")}</span>
            <g:if test="${fullCapacity}">
                <span class="call-subTitle warn">${message(code: "event.callToAction.subTitle.full", args: [eventUser.name])}</span>
            </g:if>
            <g:else>
                <span class="call-subTitle">${message(code: "event.callToAction.subTitle", args: [eventUser.name])}</span>
            </g:else>
        </div>
        <g:if test="${!fullCapacity}">
            <div class="actions clearfix event-unconfirmed ${extraUnconfirmedClass}">
                    <button
                            type="button"
                            class="btn btn-blue btn-lg event-confirm-button event-unconfirmed ${extraUnconfirmedClass}"
                            data-userLoggedAlias="${userUtil.loggedUserId()}"
                            data-eventUserId="${campaign.user.id}"
                            data-postUrl="${g.createLink(mapping: 'eventBookTicket', params:campaign.encodeAsLinkProperties())}"
                    >
                        ${message(code: "event.callToAction.button")}
                    </button>
            </div>
        </g:if>
        <div class="event-confirm-success event-confirmed ${extraConfirmedClass}">
            <g:message code="event.callToAction.success.text"/> <span class="fa fa-check"></span>
        </div>
    </div>
</g:if>