<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="admin.menu.adminEditPoliticianExperience.link"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <h1>
        <g:message code="admin.editUser.title" args="[command.politician.name]"/>,
    </h1>
    <g:render template="/adminUser/adminUserMenu" model="[user:command.politician]"/>
    <g:render template="/admin/adminMenu" model="[activeMapping:'adminEditPoliticianExperience', menu:menu]"/>

</content>

<content tag="mainContent">
    <h1>External Activity ${command.politician.name}</h1>
    <formUtil:validateForm form="politicianExperienceForm" bean="${command}"/>
    <g:form method="POST" mapping="adminEditPoliticianExperience" params="${command.politician.encodeAsLinkProperties()}" name="politicianExperienceForm" role="form" class="box-ppal">
        <g:render template="/politicianProfile/formPoliticianExperience" model="[command:command]"/>
    </g:form>
</content>
