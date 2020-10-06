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
    <g:message code="customRegister.stepDomainValidation.title"/>
</content>

<content tag="mainContent">
    <ol class="stepsSign">
        <li class=""><div class="step-label"><g:message code="kuorum.web.commands.profile.directCensusLogin.title"/></div></li>
        <g:if test="${_domainValidations.census}"><li class=""><div class="step-label"><g:message code="kuorum.web.commands.profile.DomainValidationCommand.title"/></div></li></g:if>
        <g:if test="${_domainValidations.customCode}"><li class=""><div class="step-label"><g:message code="kuorum.web.commands.profile.DomainUserCustomCodeValidationCommand.title"/></div></li></g:if>
        <g:if test="${_domainValidations.phone}"><li class="active"><div class="step-label"><g:message code="kuorum.web.commands.profile.DomainUserPhoneValidationCommand.title"/></div></li></g:if>
        <g:if test="${_domainValidations.phone}"><li class=""><div class="step-label"><g:message code="kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand.title"/></div></li></g:if>
        <li class=""><div class="step-label"><g:message code="customRegister.step4.title"/></div></li>
    </ol>
    <formUtil:validateForm bean="${command}" form="stepDomainValidationPhoneNumber" autocomplete="off"/>
    <g:form mapping="campaignValidationPhoneNumberCode" params="${campaign.encodeAsLinkProperties()}" name="stepDomainValidationPhoneNumber" role="form" method="GET" autocomplete="off"  class="signup stepDomainValidationPhoneNumber">
        <fieldset class="row">
            <g:if test="${predefinedPhone}">
                <input type="hidden" value="000" name="phoneNumber"/>
                <input type="hidden" value="0034" name="phoneNumberPrefix"/>
                <p class="center">
                    <g:message code="kuorum.web.commands.profile.DomainUserPhoneValidationCommand.modal.predefinedPhone"/>:
                    <span class="modal-domain-validation-phone-step1-predefinedPhone-phone">${phone}</span>
                </p>

            </g:if>
            <g:else>
                <div class="form-group form-group-phone col-md-offset-3 col-md-6">
                    <formUtil:selectPhonePrefix
                            command="${command}"
                            field="phoneNumberPrefix"
                            showLabel="false"
                            placeHolder=""
                            cssClass="form-control input-lg"
                            required="true"
                    />
                    <formUtil:input
                            command="${command}"
                            field="phoneNumber"
                            showLabel="false"
                            placeHolder=""
                            cssClass="form-control input-lg"
                            type="number"
                            required="true"/>
                </div>
            </g:else>
        </fieldset>

        <fieldset class="row">
            <div class="form-group col-md-offset-3 col-md-6 center option-buttons">
                %{--<label><g:message code="customRegister.step2.choseUserType.label"/> </label>--}%
%{--                <g:link mapping="customProcessRegisterStep3" class="btn btn-light"><g:message code="customRegister.step4.validation.omit"/></g:link>--}%
                <input type="submit"value="${g.message(code:'customRegister.step2.submit')}" class="btn btn-lg">
                %{--<input type="submit" id="submitCitizen" value="${g.message(code:'customRegister.step2.choseUserType.citizen')}" class="btn btn-blue btn-lg">--}%
            </div>
        </fieldset>
    </g:form>
</content>

