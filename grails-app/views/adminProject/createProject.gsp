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
    <g:render template="/admin/adminMenu" model="[activeMapping:'adminCreateProject', menu:menu]"/>

</content>

<content tag="mainContent">
    <h1><g:message code="admin.createProject.title"/></h1>
    <formUtil:validateForm bean="${command}" form="createProject"/>
    <g:form method="POST" mapping="adminCreateProject" name="createProject" role="form">
        <h1><g:message code='admin.createProject.region.label'/><span>${command.region}</span> <span class="hashtag pull-right">#</span></h1>
        <g:render template="/adminProject/formProject" model="[command:command, regions:regions, institutions:institutions]"/>
        <fieldset class="btns text-right">
            <div class="form-group">
                <a href="#" class="cancel">${message(code:'admin.createProject.saveDraft')}</a>
                <input type="submit" class="btn btn-lg" value="${message(code:'admin.createProject.publish')}">
            </div>
        </fieldset>

    </g:form>
</content>
