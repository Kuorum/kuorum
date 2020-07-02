<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="admin.editUser.title" args="[user.name]"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <g:render template="/editorUser/editorUserMenu" model="[user:user]"/>
</content>

<content tag="titleContent">
    <h1><g:message code="admin.editUser.title" args="[user.name]"/></h1>
    <h3><g:message code="admin.menu.user.editAccount" args="[user.name]"/></h3>
</content>

<content tag="mainContent">
    <formUtil:validateForm bean="${command}" form="userRightsForm"/>
    <g:form method="POST" mapping="editorAdminUserRights" params="${user.encodeAsLinkProperties()}" name="userRightsForm" role="form" class="submitOrangeButton">
        <input type="hidden" name="userId" value="${command.userId}"/>
        <div class="box-ppal-section">
            <fieldset class="row">
                <div class="form-group col-md-6">
                    <formUtil:checkBox command="${command}" field="active" showLabel="true"/>
                </div>
            </fieldset>
            <fieldset class="row">
                <div class="form-group col-md-6">
                    <formUtil:password command="${command}" field="password" showLabel="true"/>
                </div>
            </fieldset>
        </div>
        <div class="box-ppal-section">
            <fieldset class="form-group text-center">
                <input type="submit" value="${g.message(code:'default.save')}" class="btn btn-orange btn-lg">
            </fieldset>
        </div>
        <h1>Eliminar validacion</h1>
        <div class="box-ppal-section">
            <fieldset class="row">
                <div class="form-group text-center">
                    <g:link mapping="editorAdminUserInvalidate" params="${user.encodeAsLinkProperties()}" class="btn btn-orange btn-lg"> Eliminar validacion</g:link>
                </div>
            </fieldset>
        </div>
    </g:form>
</content>
