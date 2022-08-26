<%@ page import="kuorum.core.model.UserType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="customRegister.stepDomainValidation.title"/></title>
    <meta name="layout" content="centerColumn1Layout">
    <parameter name="showHeadSearch" value="false"/>
    <parameter name="showNavBar" value="false"/>
    <parameter name="extraHeadCss" value="landing"/>
    <parameter name="disableLogoLink" value="true"/>
    <r:require modules="forms"/>
</head>

<content tag="title">
    <g:message code="customRegister.step2.title"/>
</content>

<content tag="mainContent">
    <ol class="stepsSign">
        <li class="active"><div class="step-label"><g:message code="kuorum.web.commands.profile.directCensusLogin.title"/></div></li>
        <g:if test="${_domainValidations.census}"><li class=""><div class="step-label"><g:message code="kuorum.web.commands.profile.DomainValidationCommand.title"/></div></li></g:if>
        <g:if test="${_domainValidations.customCode}"><li class=""><div class="step-label"><g:message code="kuorum.web.commands.profile.DomainUserCustomCodeValidationCommand.title"/></div></li></g:if>
        <g:if test="${_domainValidations.phone}"><li class=""><div class="step-label"><g:message code="kuorum.web.commands.profile.DomainUserPhoneValidationCommand.title"/></div></li></g:if>
        <g:if test="${_domainValidations.phone}"><li class=""><div class="step-label"><g:message code="kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand.title"/></div></li></g:if>
        <li class=""><div class="step-label"><g:message code="customRegister.step4.title"/></div></li>
    </ol>
    <formUtil:validateForm bean="${command}" form="step0" autocomplete="off"/>
    <g:form mapping="campaignValidationLinkCheck" name="step0" role="form" method="POST" autocomplete="off"  class="signup step0">
        <input type="hidden" name="censusLogin" value="${censusLogin}"/>
        <input type="hidden" name="email" value="${contact.email}"/>
        <input type="hidden" name="name" value="${contact.name}"/>
        <input type="hidden" name="password" value="XXXX"/>
        <fieldset aria-live="polite" class="row">
            <div class="form-group col-xs-12 center">
                <p><g:message code="kuorum.web.commands.profile.directCensusLogin.intro" args="[contact.name]" encodeAs="raw"/></p>
                <g:render template="step0RegisterWithCensusCode_CodeData" model="[contact:contact, campaign:campaign]"/>
            </div>
        </fieldset>
        <fieldset aria-live="polite" class="row">
            <div class="form-group col-xs-12 center">
                <formUtil:checkBox
                        command="${command}"
                        field="conditions"
                        label="${g.message(code: 'register.conditions', args: [g.createLink(mapping: 'footerPrivacyPolicy')], encodeAs: 'raw')}"/>
            </div>
        </fieldset>


        <fieldset aria-live="polite" class="row">
            <div class="form-group text-center option-buttons">
                %{--<label><g:message code="customRegister.step2.choseUserType.label"/> </label>--}%
                <button type="submit" class="btn btn-lg">${g.message(code: 'customRegister.step2.submit')}</button>
                %{--<input type="submit" id="submitCitizen" value="${g.message(code:'customRegister.step2.choseUserType.citizen')}" class="btn btn-blue btn-lg">--}%
            </div>
        </fieldset>
    </g:form>
</content>

