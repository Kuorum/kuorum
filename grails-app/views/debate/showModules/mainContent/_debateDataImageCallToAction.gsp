<g:if test="${debate.event && !debate.event.registered}">
    <div class="actions call-to-action-mobile call-mobile-event-confirm event-unconfirmed"
         data-userLoggedAlias="${userUtil.loggedUserAlias()}"
         data-postUrl="${g.createLink(mapping: 'eventBookTicket',params:debate.encodeAsLinkProperties())}"
         data-debateId="${debate.id}">
        %{--EVENT DATA - CHAPU BORRAR --}%
        <button type="button" class="btn btn-orange btn-lg call-message">
            <g:message code="event.callToAction.button"/>
        </button>
        <span class="fa fa-caret-down arrow"></span>
        <button type="button" class="btn btn-orange btn-xl btn-circle call-button">
            <span class="fa fa-ticket fa-2x"></span>
        </button>
    </div>
</g:if>
<div class="actions call-to-action-mobile add-proposal">
    %{--EVENT DATA - CHAPU BORRAR --}%
    <button type="button" class="btn btn-blue btn-lg call-message">
        <g:if test="${debate?.event?.registered}">
            <g:message code="event.callToAction.success.mobile"/>
        </g:if>
        <g:else>
            <g:message code="debate.proposals.callToAction.mobile.message"/>
        </g:else>
    </button>
    <span class="fa fa-caret-down arrow"></span>
    <button type="button" class="btn btn-blue btn-xl btn-circle call-button">
        <span class="fa fa-lightbulb-o fa-2x"></span>
    </button>
</div>