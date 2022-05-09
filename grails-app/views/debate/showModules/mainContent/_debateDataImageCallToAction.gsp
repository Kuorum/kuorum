<g:render template="/campaigns/showModules/eventCallToActionMovile" model="[campaign:debate]"/>
<div class="actions call-to-action-mobile add-proposal">

    <g:set var="buttonText"><g:message code="debate.proposals.callToAction.mobile.message"/></g:set>
    <g:if test="${debate?.event?.registered}">
        <g:set var="buttonText"><g:message code="event.callToAction.success.mobile"/></g:set>
    </g:if>
    %{--EVENT DATA - CHAPU BORRAR --}%
    <button type="button" class="btn btn-blue btn-lg call-message" name="${buttonText}">
        ${raw(buttonText)}
    </button>
    <span class="fas fa-caret-down arrow"></span>
    <button type="button" class="btn btn-blue btn-xl btn-circle call-button" name="${buttonText}">
        <span class="fal fa-lightbulb fa-2x"></span>
    </button>
</div>