<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="admin.editUser.title" args="[user.name]"/></title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config"/>
    <r:require module="forms"/>
</head>

<content tag="leftMenu">
    <g:render template="editorUserMenu" model="[user: user]"/>
</content>

<content tag="titleContent">
    <h1><g:message code="admin.editUser.title" args="[user.name]"/></h1>

    <h3><g:message code="admin.menu.user.editAccount" args="[user.name]"/></h3>
</content>

<content tag="mainContent">
    <formUtil:validateForm bean="${command}" form="profilePictures" dirtyControl="true"/>
    <g:form method="POST" mapping="editorKuorumAccountPicturesEdit" params="${user.encodeAsLinkProperties()}"
            name="profilePictures" role="form">
        <div class="box-ppal-section">
            <g:render template="/profile/formEditPictures" model="[command: command]"/>
        </div>

        <div class="box-ppal-section">
            <fieldset aria-live="polite" class="form-group text-center">
                <input type="submit" value="${g.message(code: 'default.save')}" class="btn btn-orange btn-lg">
            </fieldset>

        </div>
    </g:form>
</content>
