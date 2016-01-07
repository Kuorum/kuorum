<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="admin.editUser.title" args="[user.name]"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <h1>
        <g:message code="admin.editUser.title" args="[user.name]"/>,
    </h1>
    <g:render template="adminUserMenu" model="[user:user]"/>

    %{--<p><g:message code="profile.changeEmail.description"/></p>--}%
    <g:render template="/admin/adminMenu" model="[activeMapping:'', menu:menu]"/>
</content>

<content tag="titleContent">
    <h1><g:message code="admin.editUser.title" args="[user.name]"/></h1>
    <h3>Account</h3>
</content>

<content tag="mainContent">
    <formUtil:validateForm bean="${command}" form="accountForm"/>
    <g:form method="POST" mapping="adminKuorumAccountEdit" params="${user.encodeAsLinkProperties()}" name="accountForm" role="form" class="submitOrangeButton">
        <div class="box-ppal-section">
            <g:render template="/profile/accountDetailsForm" model="[command:command]"/>
        </div>
        <div class="box-ppal-section">
            <h4 class="box-ppal-section-title">
                Admin
            </h4>
            <fieldset class="row">
                <div class="form-group col-md-6">
                    <formUtil:checkBox command="${command}" field="active" showLabel="true"/>
                </div>
                <div class="form-group col-md-6">
                    <formUtil:checkBox command="${command}" field="emailAccountActive" showLabel="true"/>
                </div>
            </fieldset>
            <fieldset class="row">
                <div class="form-group col-md-6">
                    <formUtil:radioEnum command="${command}" field="userType" showLabel="true"/>
                </div>
            </fieldset>
        </div>
        <div class="box-ppal-section">
            <fieldset class="form-group text-center">
                <input type="submit" value="${g.message(code:'profile.emailNotifications.save')}" class="btn btn-orange btn-lg">
            </fieldset>
        </div>
    </g:form>
</content>
