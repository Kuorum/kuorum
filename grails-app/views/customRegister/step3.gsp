<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.login" args="[kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.name]"/> </title>
    <meta name="layout" content="centerColumn1Layout">
    <parameter name="showHeadSearch" value="false"/>
    <parameter name="showNavBar" value="false"/>
    <parameter name="extraHeadCss" value="landing"/>
</head>

<content tag="title">
    <g:message code="customRegister.step2.title"/>
</content>

<content tag="mainContent">
    <ol class="stepsSign">
        <li class=""><div class="step-label"><g:message code="customRegister.step2.title"/></div></li>
        <li class=""><div class="step-label"><g:message code="customRegister.step3.title"/></div></li>
%{--        <g:if test="${_domainValidations.census}"><li class=""><div class="step-label"><g:message code="kuorum.web.commands.profile.DomainValidationCommand.title"/></div></li></g:if>--}%
%{--        <g:if test="${_domainValidations.customCode}"><li class=""><div class="step-label"><g:message code="kuorum.web.commands.profile.DomainUserCustomCodeValidationCommand.title"/></div></li></g:if>--}%
%{--        <g:if test="${_domainValidations.phone}"><li class=""><div class="step-label"><g:message code="kuorum.web.commands.profile.DomainUserPhoneValidationCommand.title"/></div></li></g:if>--}%
%{--        <g:if test="${_domainValidations.phone}"><li class=""><div class="step-label"><g:message code="kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand.title"/></div></li></g:if>--}%
        <li class="active"><div class="step-label"><g:message code="customRegister.step4.title"/></div></li>
    </ol>
    <div class="signup final">
            <h3><g:message code="customRegister.step4.congratulations"/> </h3>
            <fieldset class="row">
                <div class="form-group">
                    <g:link class="btn btn-lg" mapping="dashboard" params="[tour:false]">
                        <g:message code="customRegister.step4.goToDashboard"/>
                    </g:link>
                </div>
            </fieldset>

    </div>
</content>

