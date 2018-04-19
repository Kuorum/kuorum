<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.login" args="[kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.name]"/> </title>
    <meta name="layout" content="register1ColumnLayout">
    <parameter name="showHeadSearch" value="false"/>
    <parameter name="showNavBar" value="false"/>
    <parameter name="extraHeadCss" value="landing"/>
</head>

<content tag="title">
    <g:message code="customRegister.step3.title"/>
</content>

<content tag="mainContent">
    <ol class="stepsSign">
        <li>1</li>
        <li>2</li>
        <li>3</li>
    </ol>
    <div class="signup final">
        %{--<g:form mapping="customProcessRegisterStep3" method="POST">--}%
            <h3><g:message code="customRegister.step3.congratulations"/> </h3>
            %{--<fieldset class="row">--}%
                %{--<div class="form-group col-md-6 col-md-offset-3 promotional-code">--}%
                    %{--<formUtil:input command="${command}" field="promotionalCode" showLabel="true"/>--}%
                %{--</div>--}%
            %{--</fieldset>--}%
            <fieldset class="row">
                <div class="form-group">
                    %{--<input type="submit" class="btn btn-lg" value="${g.message(code:'customRegister.step3.goToDashboard')}"/>--}%
                    <g:link class="btn btn-lg" mapping="dashboard" params="[tour:true]">
                        <g:message code="customRegister.step3.goToDashboard"/>
                    </g:link>
                </div>
            </fieldset>

    </div>
</content>

