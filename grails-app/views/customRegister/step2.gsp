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
    <g:message code="customRegister.step2.title"/>
</content>

<content tag="mainContent">
    <ol class="stepsSign">
        <li>1</li>
        <li>2</li>
        <li class="active">3</li>
    </ol>
    <formUtil:validateForm bean="${command}" form="step2" autocomplete="off"/>
    <g:form mapping="customProcessRegisterStep2" name="step2" role="form" method="POST" autocomplete="off"  class="signup step2">
        <input type="hidden" name="user.id" value="${command?.user?.id}"/>
        <fieldset class="row">
            <div class="form-group col-md-6">
                <formUtil:input
                        command="${command}"
                        field="alias"
                        cssClass="form-control input-lg"
                        showLabel="true"
                        showCharCounter="false"
                        required="true"/>
            </div>
            <div class="form-group col-md-6">
                <input type="text" name="autocompleteNameOff" style="display:none" data-ays-ignore="true"/>
                <formUtil:selectEnum
                        command="${command}"
                        field="gender"
                        showLabel="true"
                        cssClass="form-control input-lg"
                        defaultEmpty="true"
                        required="true"/>
            </div>
        </fieldset>

        <fieldset class="row">
            <div class="form-group col-md-6">
                <formUtil:regionInput
                        command="${command}"
                        field="homeRegion"
                        showLabel="true"/>
            </div>
            <div class="form-group col-md-6">
                <formUtil:selectEnum
                        command="${command}"
                        field="language"
                        showLabel="true"/>
            </div>
            %{--<div class="form-group col-md-6">--}%
                %{--<div class="pull-left prefix">--}%
                    %{--<formUtil:input command="${command}" field="phonePrefix" showLabel="true"/>--}%
                %{--</div>--}%
                %{--<div class="pull-left phone">--}%
                    %{--<formUtil:input command="${command}" field="phone" showLabel="true"/>--}%
                %{--</div>--}%
                %{--<span class="help-block">--}%
                    %{--<g:message code="kuorum.web.commands.customRegister.Step2Command.phone-phonePrefix.helpBlock"/>--}%
                %{--</span>--}%
            %{--</div>--}%
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

