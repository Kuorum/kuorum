<%@ page import="kuorum.core.model.UserType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.login" args="[kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.name]"/> </title>
    <meta name="layout" content="register1ColumnLayout">
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
        <li>1</li>
        <li>2</li>
        <li>3</li>
    </ol>
    <formUtil:validateForm bean="${command}" form="stepDomainValidation" autocomplete="off"/>
    <g:form mapping="customProcessRegisterDomainValidation" name="stepDomainValidation" role="form" method="POST" autocomplete="off"  class="signup stepDomainValidation">
        <fieldset class="row">
            <div class="form-group col-md-offset-3  col-md-6">
                <formUtil:input
                        command="${command}"
                        field="ndi"
                        cssClass="form-control input-lg"
                        showLabel="true"
                        showCharCounter="false"
                        required="true"/>
            </div>
        </fieldset>

        <fieldset class="row">
            <div class="form-group col-md-offset-3 col-md-6">
                <input type="text" name="autocompleteNameOff" style="display:none" data-ays-ignore="true"/>
                <formUtil:input
                        command="${command}"
                        field="postalCode"
                        showLabel="true"
                        cssClass="form-control input-lg"
                        printValue="true"
                        required="true"/>
            </div>
        </fieldset>

        <fieldset class="row">
            <div class="form-group col-md-offset-3 col-md-6">
                <formUtil:date
                        datePickerType="birthDate"
                        command="${command}"
                        field="birthDate"
                        showLabel="true"/>
            </div>

        </fieldset>

        <fieldset class="row">
            <div class="form-group col-md-offset-3 col-md-6 center option-buttons">
                %{--<label><g:message code="customRegister.step2.choseUserType.label"/> </label>--}%
                <g:link mapping="customProcessRegisterStep3" class="btn btn-light"> Omit</g:link>
                <input type="submit"value="${g.message(code:'customRegister.step2.submit')}" class="btn btn-lg">
                %{--<input type="submit" id="submitCitizen" value="${g.message(code:'customRegister.step2.choseUserType.citizen')}" class="btn btn-blue btn-lg">--}%
            </div>
        </fieldset>
    </g:form>
</content>
