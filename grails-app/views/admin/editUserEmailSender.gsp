<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="admin.editUser.title" args="[command.user.name]"/> </title>
    <meta name="layout" content="leftMenuConfigLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <g:render template="/editorUser/editorUserMenu" model="[user:command.user]"/>
</content>

<content tag="titleContent">
    <h1><g:message code="admin.editUser.title" args="[command.user.name]"/></h1>
    <h3><g:message code="admin.menu.user.editAccount" args="[command.user.name]"/></h3>
</content>

<g:if test="${requestState}"><g:set var="icon" value="check"/></g:if>
<g:else><g:set var="icon" value="close"/></g:else>

<content tag="mainContent">
    <formUtil:validateForm bean="${command}" form="userRightsForm"/>
    <g:form method="POST" mapping="editorAdminEmailSender" params="${command.user.encodeAsLinkProperties()}" name="userRightsForm" role="form" class="submitOrangeButton">
        <input type="hidden" name="user.id" value="${command.user.id}"/>
        <div class="box-ppal-section">
            <h4 class="box-ppal-section-title">
                Custom email sender
            </h4>
        </div>
        <div class="box-ppal-section">
            <fieldset class="row validate" style="text-align: left">
                <div class="col-xs-12 valid">
                    <strong><span>Requested: </span></strong><i class="fas fa-${icon} fa-2x"></i>
                </div>
            </fieldset>
            <fieldset class="row">
                <div class="form-group col-md-6">
                    <formUtil:input type="text" command="${command}" field="emailSender" showLabel="true"/>
                </div>
            </fieldset>
        </div>
        <div class="box-ppal-section">
            <fieldset class="form-group text-center">
                <input type="submit" value="${g.message(code:'default.save')}" class="btn btn-orange btn-lg">
            </fieldset>
        </div>
    </g:form>
</content>