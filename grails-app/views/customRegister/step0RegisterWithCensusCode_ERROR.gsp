<%@ page import="kuorum.core.model.UserType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.login" args="[kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.name]"/> </title>
    <meta name="layout" content="centerColumn1Layout">
    <parameter name="showHeadSearch" value="false"/>
    <parameter name="showNavBar" value="false"/>
    <parameter name="extraHeadCss" value="landing"/>
    <parameter name="disableLogoLink" value="true"/>
    <r:require modules="forms"/>
</head>

<content tag="title">
    <g:message code="kuorum.web.commands.profile.directCensusLogin.wrongCode.title"/>
</content>

<content tag="mainContent">
    <ol class="stepsSign">
        <li class=""><div class="step-label"><g:message code="kuorum.web.commands.profile.directCensusLogin.title"/></div></li>
        <g:if test="${_domainValidations.census}"><li class=""><div class="step-label"><g:message code="kuorum.web.commands.profile.DomainValidationCommand.title"/></div></li></g:if>
        <g:if test="${_domainValidations.customCode}"><li class=""><div class="step-label"><g:message code="kuorum.web.commands.profile.DomainUserCustomCodeValidationCommand.title"/></div></li></g:if>
        <g:if test="${_domainValidations.phone}"><li class=""><div class="step-label"><g:message code="kuorum.web.commands.profile.DomainUserPhoneValidationCommand.title"/></div></li></g:if>
        <g:if test="${_domainValidations.phone}"><li class=""><div class="step-label"><g:message code="kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand.title"/></div></li></g:if>
        <li class="active"><div class="step-label"><g:message code="customRegister.step4.title"/></div></li>
    </ol>
%{--    <formUtil:validateForm bean="${command}" form="step0" autocomplete="off"/>--}%
    <g:form mapping="customRegisterCensus" name="step0" role="form" method="POST" autocomplete="off"  class="signup step0">

        <fieldset class="row">
            <div class="form-group col-xs-12 center">
                <p><g:message code="kuorum.web.commands.profile.directCensusLogin.wrongCode.text" args="['info@kuorum.og']" encodeAs="raw"/></p>
            </div>
        </fieldset>

        <fieldset class="row">
            <div class="form-group text-center option-buttons">
                <a href="${redirectUrl}"  class="btn btn-lg">${g.message(code:'customRegister.step2.submit')}</a>
            </div>
        </fieldset>
    </g:form>
</content>

