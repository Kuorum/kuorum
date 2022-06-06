<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="domain.config.starting.steps.step4.header"/></title>
    <meta name="layout" content="centerColumn1Layout">
    <parameter name="showHeadSearch" value="false"/>
    <parameter name="showNavBar" value="false"/>
    <parameter name="extraHeadCss" value="landing"/>
</head>

<content tag="title">
    <g:message code="domain.config.starting.steps.step4.title"/>
</content>

<content tag="mainContent">
    <div class="signup final">
        <h3><g:message code="domain.config.starting.steps.step4.text.1"/> <g:message
                code="domain.config.starting.steps.step4.text.2"/></h3>
        <fieldset aria-live="polite" class="row">
            <div class="form-group">
                <g:link class="btn btn-lg" mapping="politicianCampaigns" params="[tour: true]">
                    <g:message code="domain.config.starting.steps.step4.button"/>
                </g:link>
            </div>
        </fieldset>
    </div>
</content>

