<%@ page import="kuorum.core.model.UserType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.login" args="[kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.name]"/> </title>
    <meta name="layout" content="centerColumn1Layout">
    <parameter name="showHeadSearch" value="false"/>
    <parameter name="showNavBar" value="false"/>
    <parameter name="extraHeadCss" value="landing"/>
    <r:require modules="forms"/>
</head>

<content tag="title">
    <g:message code="customRegister.stepDomainValidation.title"/>
</content>

<content tag="mainContent">
    <ol class="stepsSign">
        <li class=""><div class="step-label"><g:message code="customRegister.step2.title"/></div></li>
        <li class=""><div class="step-label"><g:message code="customRegister.step3.title"/></div></li>
%{--        <g:if test="${_domainValidations.census}"><li class=""><div class="step-label"><g:message code="kuorum.web.commands.profile.DomainValidationCommand.title"/></div></li></g:if>--}%
%{--        <g:if test="${_domainValidations.customCode}"><li class=""><div class="step-label"><g:message code="kuorum.web.commands.profile.DomainUserCustomCodeValidationCommand.title"/></div></li></g:if>--}%
%{--        <g:if test="${_domainValidations.phone}"><li class=""><div class="step-label"><g:message code="kuorum.web.commands.profile.DomainUserPhoneValidationCommand.title"/></div></li></g:if>--}%
%{--        <g:if test="${_domainValidations.phone}"><li class="active"><div class="step-label"><g:message code="kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand.title"/></div></li></g:if>--}%
        <li class=""><div class="step-label"><g:message code="customRegister.step4.title"/></div></li>
    </ol>
    <formUtil:validateForm bean="${command}" form="stepDomainValidation" autocomplete="off"/>
    <g:form mapping="customProcessRegisterPoneValidationCode" name="stepDomainValidation" role="form" method="POST" autocomplete="off"  class="signup stepDomainValidation">
        <input type="hidden" name="phoneHash" id="phoneHash" required="" value="${command.phoneHash}">
        <fieldset class="row">
            <div class="form-group col-md-offset-3  col-md-6">
                <formUtil:input
                        command="${command}"
                        field="phoneCode"
                        showLabel="true"
                        cssClass="form-control input-lg"
                        required="true"/>
            </div>
        </fieldset>


        <fieldset class="row">
            <div class="form-group col-md-offset-3 col-md-6 center option-buttons">
                %{--<label><g:message code="customRegister.step2.choseUserType.label"/> </label>--}%
                <g:link mapping="customProcessRegisterStep3" class="btn btn-light"><g:message code="customRegister.step4.validation.omit"/></g:link>
                <input type="submit"value="${g.message(code:'customRegister.step2.submit')}" class="btn btn-lg">
                %{--<input type="submit" id="submitCitizen" value="${g.message(code:'customRegister.step2.choseUserType.citizen')}" class="btn btn-blue btn-lg">--}%
            </div>
        </fieldset>
    </g:form>
</content>

