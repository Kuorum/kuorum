<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.login" args="[kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.name]"/> </title>
    <meta name="layout" content="centerColumn1Layout">
    <parameter name="showHeadSearch" value="false"/>
    <parameter name="showNavBar" value="false"/>
    <parameter name="extraHeadCss" value="landing"/>
</head>

<content tag="title">
    <g:message code="customRegister.step3.title"/>
</content>

<content tag="mainContent">
    <ol class="stepsSign">
        <li class="active">1</li>
        <li class="active">2</li>
        <li class="active">3</li>
    </ol>
    <div class="signup final">
            <h3><g:message code="customRegister.step3.congratulations"/> </h3>
            <fieldset class="row">
                <div class="form-group">
                    <g:link class="btn btn-lg" mapping="dashboard" params="[tour:true]">
                        <g:message code="customRegister.step3.goToDashboard"/>
                    </g:link>
                </div>
            </fieldset>

    </div>
</content>

