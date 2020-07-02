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
    <g:form mapping="customRegisterCensus" name="step0" role="form" method="POST" autocomplete="off"  class="signup step0">
        <input type="hidden" name="censusLogin" value="${censusLogin}"/>
        <input type="hidden" name="email" value="${contact.email}"/>
        <input type="hidden" name="name" value="${contact.name}"/>
        <input type="hidden" name="password" value="XXXX"/>
        <fieldset class="row">
            <div class="form-group col-xs-12 center">
                <p><g:message code="kuorum.web.commands.profile.directCensusLogin.intro" args="[contact.name]" encodeAs="raw"/></p>
                <table class="autologin-contact-data">
                    <tr><td><g:message code="kuorum.web.commands.payment.contact.ContactCommand.name.label"/>:</td><td>${contact.name}</td></tr>
                    <g:if test="${contact.surname}">
                        <tr><td><g:message code="kuorum.web.commands.payment.contact.ContactCommand.surname.label"/>:</td><td>${contact.surname}</td></tr>
                    </g:if>
                    <g:if test="${contact.phone}">
                        <tr><td><g:message code="kuorum.web.commands.profile.DomainUserPhoneValidationCommand.phoneNumber.placeholder"/>:</td><td>${contact.phone.encodeAsHiddenPhone()}</td></tr>
                    </g:if>
                    <tr><td>email:</td><td>${contact.email}</td></tr>
                    <g:if test="${contact.surveyVoteWeight != 1}">
                        <tr><td><g:message code="kuorum.web.commands.payment.contact.ContactCommand.surveyVoteWeight.label"/>:</td><td>${contact.surveyVoteWeight}</td></tr>
                    </g:if>
                </table>
            </div>
        </fieldset>
        <fieldset class="row">
            <div class="form-group col-xs-12 center">
                <formUtil:checkBox
                        command="${command}"
                        field="conditions"
                        label="${g.message(code:'register.conditions', args:[g.createLink(mapping: 'footerPrivacyPolicy')], encodeAs: 'raw')}"/>
            </div>
        </fieldset>


        <fieldset class="row">
            <div class="form-group text-center option-buttons">
                %{--<label><g:message code="customRegister.step2.choseUserType.label"/> </label>--}%
                <input type="submit"value="${g.message(code:'customRegister.step2.submit')}" class="btn btn-lg">
                %{--<input type="submit" id="submitCitizen" value="${g.message(code:'customRegister.step2.choseUserType.citizen')}" class="btn btn-blue btn-lg">--}%
            </div>
        </fieldset>
    </g:form>
</content>

