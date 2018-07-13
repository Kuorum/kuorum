<g:render template="/campaigns/showModules/eventCallToActionMovile" model="[campaign:debate]"/>
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