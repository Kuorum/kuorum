<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title> <g:message code="domain.config.firstConfig.steps.step2.title"/> </title>
    <meta name="layout" content="columnCLayout">
    <r:require modules="forms"/>
</head>

<content tag="mainContent">
    <ol class="stepsSign stepsSignSecondaryColor">
        <li class="active">1<span class="signStepDescription"><g:message code="domain.config.firstConfig.steps.step1.title"/></span></li>
        <li class="active">2<span class="signStepDescription"><g:message code="domain.config.firstConfig.steps.step2.title"/></span></li>
    </ol>
    <form role="form" class="signup-custom-site"  name="signup-custom-site" method="post" action="#">
        <p><g:message code="domain.config.firstConfig.steps.step2.intro"/> </p>
        <g:render template="inputUserRights" model="[campaignRoles:campaignRoles]"/>
        <fieldset>
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