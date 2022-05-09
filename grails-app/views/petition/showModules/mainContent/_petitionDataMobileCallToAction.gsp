<div class="actions call-to-action-mobile petition-sign-call-to-action ${petition.signed?'hidden':''}">
    %{--EVENT DATA - CHAPU BORRAR --}%
    <g:set var="buttonText"><g:message code="petition.callToAction.SENT.title"/></g:set>
    <button type="button" class="btn btn-blue btn-lg call-message" name="${buttonText}">
        ${buttonText}
    </button>
    <span class="fas fa-caret-down arrow"></span>
    <button type="button" class="btn btn-blue btn-xl btn-circle call-button" name="${buttonText}">
        <span class="fal fa-microphone fa-2x"></span>
    </button>
</div>