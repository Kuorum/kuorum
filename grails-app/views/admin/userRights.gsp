<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title> <g:message code="domain.config.firstConfig.steps.step2.title"/> </title>
    <meta name="layout" content="columnCLayout">
    <parameter name="bodyCss" value="configDomainProcess"/>
    <r:require modules="forms"/>
</head>

<content tag="mainContent">
    <ol class="stepsSign stepsSignSecondaryColor">
        <li class=""><div class="step-label"><g:message code="domain.config.firstConfig.steps.step1.title"/></div></li>
        <li class="active"><div class="step-label"><g:message code="domain.config.firstConfig.steps.step2.title"/></div></li>
    </ol>
    <form role="form" class="signup-custom-site"  name="signup-custom-site" method="post" action="#">
        <p><g:message code="domain.config.firstConfig.steps.step2.intro"/> </p>
        <g:render template="inputUserRights" model="[campaignRoles:campaignRoles]"/>
        <fieldset aria-live="polite">
            <div class="form-group center">
                <input type="submit" value="${g.message(code:'domain.config.firstConfig.steps.step2.submit')}" class="btn btn-lg btn-blue">
            </div>
        </fieldset>
    </form>

</content>

<content tag="cColumn">
    <div class="custom-url-info-box">
        <h3><g:message code="domain.config.firstConfig.steps.step2.columnc.title"/></h3>
        <p><g:message code="domain.config.firstConfig.steps.step2.columnc.explanation"/></p>
    </div>
</content>