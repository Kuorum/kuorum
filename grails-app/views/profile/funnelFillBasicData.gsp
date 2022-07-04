<%@ page import="kuorum.core.model.UserType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.login"
                      args="[kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.name]"/></title>
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
        <li class="active"><div class="step-label"><g:message code="customRegister.fillProfile.basicData"/></div></li>
        <li class=""><div class="step-label"><g:message code="customRegister.fillProfile.images"/></div></li>
        <li class=""><div class="step-label"><g:message code="customRegister.fillProfile.files"/></div></li>
        <li class=""><div class="step-label"><g:message code="customRegister.fillProfile.social"/></div></li>
    </ol>
    <formUtil:validateForm bean="${command}" form="stepBasicData" autocomplete="off"/>
    <g:form mapping="funnelFillBasicData" name="stepBasicData" role="form" method="POST" autocomplete="off"
            class="signup">
        <fieldset aria-live="polite" class="row">
            <div class="form-group col-md-6">
                <formUtil:input
                        command="${command}"
                        field="name"
                        cssClass="form-control input-lg"
                        showLabel="true"
                        showCharCounter="false"/>
            </div>

            <div class="form-group col-md-6">
                <formUtil:input
                        command="${command}"
                        field="email"
                        cssClass="form-control input-lg"
                        showLabel="true"
                        showCharCounter="false"
                        disabled="true"/>
            </div>
        </fieldset>
        <fieldset aria-live="polite" class="row">
            <div class="form-group col-md-6">
                <formUtil:input
                        command="${command}"
                        field="nid"
                        cssClass="form-control input-lg"
                        showLabel="true"
                        showCharCounter="false"/>
            </div>

            <div class="form-group form-group-phone col-md-6">
                <formUtil:selectPhonePrefix command="${command}" field="phonePrefix" showLabel="true"
                                            label="${g.message(code: 'kuorum.web.commands.payment.contact.ContactCommand.phone.label')}"/>
                <formUtil:input command="${command}" field="phone" showLabel="true" type="number"/>
            </div>
        </fieldset>
        <fieldset aria-live="polite" class="row">
            <div class="form-group col-xs-12">
                <formUtil:textArea command="${command}" field="bio" showLabel="true"/>
            </div>
        </fieldset>

        <fieldset aria-live="polite" class="row">
            <div class="form-group text-center option-buttons">
                %{--<label><g:message code="customRegister.step2.choseUserType.label"/> </label>--}%
                <input type="submit" value="${g.message(code: 'customRegister.step2.submit')}" class="btn btn-lg">
                %{--<input type="submit" id="submitCitizen" value="${g.message(code:'customRegister.step2.choseUserType.citizen')}" class="btn btn-blue btn-lg">--}%
            </div>
        </fieldset>
    </g:form>
</content>

