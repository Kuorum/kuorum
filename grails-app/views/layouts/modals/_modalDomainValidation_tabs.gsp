
<g:if test="${_domainValidations.phone}">
%{-- Only shows steps if the phone is active because if phone is not active the census doesn't need the step bullets--}%
    <ol class="modal-domain-validation-step-tabs stepsSign">
        <g:if test="${_domainValidations.census}">
            <li class="modal-domain-validation-step-tabs-census"><div class="step-label"><g:message code="kuorum.web.commands.profile.DomainValidationCommand.title"/></div></li>
        </g:if>
            <li class="modal-domain-validation-step-tabs-phoneNumber"><div class="step-label"><g:message code="kuorum.web.commands.profile.DomainUserPhoneValidationCommand.title"/></div></li>
            <li class="modal-domain-validation-step-tabs-phoneCode"><div class="step-label"><g:message code="kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand.title"/></div></li>
    </ol>
</g:if>