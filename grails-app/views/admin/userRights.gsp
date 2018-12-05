<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title> CHOOSE DOMAIN </title>
    <meta name="layout" content="columnCLayout">
    <r:require modules="forms"/>
</head>

<content tag="mainContent">
    <ol class="stepsSign stepsSignSecondaryColor">
        <li class="active">1<span class="signStepDescription"><g:message code="domain.config.firstConfig.steps.step1.title"/></span></li>
        <li class="active">2<span class="signStepDescription"><g:message code="domain.config.firstConfig.steps.step2.title"/></span></li>
    </ol>
    <form role="form" class="signup-custom-site"  name="signup-custom-site" method="post" action="#">
        <p>Choose what kind of content will <b>your users</b> produce: </p>
        <g:render template="inputUserRights" model="[campaignRoles:campaignRoles]"/>
        <fieldset>
            <div class="form-group center">
                <input type="submit" value="Complete set up process" class="btn btn-lg btn-blue">
            </div>
        </fieldset>
    </form>

</content>



<content tag="cColumn">
    <div class="custom-url-info-box">
        <h3>What can do what?</h3>
        <p>
            As admin, you will be able to produce any kind of content. But you choose what your users can
            and cannot do. User generated content will sustain the growth of your community but it will
            also make it more difficult to manage.
        </p>
    </div>
</content>