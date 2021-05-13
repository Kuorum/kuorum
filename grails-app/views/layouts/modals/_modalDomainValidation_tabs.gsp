
<g:set var="numberValidationsActive" value="${(_domainValidations.phone?2:0)+(_domainValidations.census?1:0)+(_domainValidations.customCode?1:0)}"/>

<g:if test="${numberValidationsActive>1}">
%{-- Only shows steps if the phone is active because if phone is not active the census doesn't need the step bullets--}%
    <ol class="modal-domain-validation-step-tabs stepsSign">
        <g:if test="${_domainValidations?.census}"><li class="modal-domain-validation-step-tabs-census"><div class="step-label"><g:message code="kuorum.web.commands.profile.DomainValidationCommand.title"/></div></li></g:if>
        <g:if test="${_domainValidations?.customCode}"><li class="modal-domain-validation-step-tabs-customCode"><div class="step-label"><g:message code="kuorum.web.commands.profile.DomainUserCustomCodeValidationCommand.title"/></div></li></g:if>
        <g:if test="${_domainValidations?.phone}"><li class="modal-domain-validation-step-tabs-phoneNumber"><div class="step-label"><g:message code="kuorum.web.commands.profile.DomainUserPhoneValidationCommand.title"/></div></li></g:if>
        <g:if test="${_domainValidations?.phone}"><li class="modal-domain-validation-step-tabs-phoneCode"><div class="step-label"><g:message code="kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand.title"/></div></li></g:if>
    </ol>
</g:if>