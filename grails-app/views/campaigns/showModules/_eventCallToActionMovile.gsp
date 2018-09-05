<g:if test="${campaign.event && !campaign.event.registered}">
    <div class="actions call-to-action-mobile call-mobile-event-confirm event-unconfirmed"
         data-userLoggedAlias="${userUtil.loggedUserId()}"
         data-eventUserId="${campaign.user.id}"
         data-postUrl="${g.createLink(mapping: 'eventBookTicket',params:campaign.encodeAsLinkProperties())}">
        %{--EVENT DATA - CHAPU BORRAR --}%
        <button type="button" class="btn btn-orange btn-lg call-message">
            <g:message code="event.callToAction.button"/>
        </button>
        <span class="fas fa-caret-down arrow"></span>
        <button type="button" class="btn btn-orange btn-xl btn-circle call-button">
            <span class="fal fa-ticket-alt fa-2x"></span>
        </button>
    </div>
</g:if>