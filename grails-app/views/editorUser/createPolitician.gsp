<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="admin.createPolitician.title"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <h1>
        <g:message code="admin.createPolitician.title" />
    </h1>
</content>

<content tag="mainContent">
    <h1><g:message code="admin.createPolitician.title"/></h1>
    <formUtil:validateForm form="createPolitician" bean="${command}"/>
    <g:form method="POST" mapping="editorCreatePolitician" name="createPolitician" role="form">
        <g:render template="/profile/accountDetailsForm" model="[command:command]"/>
        <div class="box-ppal-section">
            <fieldset>
                <div class="form-group text-center">
                    <button type="submit" class="btn btn-default btn-lg"><g:message code="admin.createUser.submit"/></button>
                </div>
            </fieldset>
        </div>
    </g:form>
</content>
