<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="admin.createProject.title"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <h1>
        <g:message code="admin.createProject.title"/>,
    </h1>
    %{--<p><g:message code="profile.changeEmail.description"/></p>--}%
    <g:render template="/admin/adminMenu" model="[activeMapping:'adminCreateRegion', menu:menu]"/>

</content>

<content tag="mainContent">
    <h1><g:message code="admin.createRegion.title"/></h1>
    <formUtil:validateForm bean="${command}" form="createProject"/>
    <g:form method="POST" mapping="adminCreateRegion" name="createProject" role="form">
        <g:render template="formRegion" model="[command:command, regions:regions]"/>
        <div class="form-group">
            <input type="submit" value="${message(code:'admin.createProject.submit')}" class="btn btn-grey btn-lg">
            <a href="#" class="cancel" tabindex="19">Cancelar</a>
        </div>
    </g:form>
</content>
