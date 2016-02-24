<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="admin.menu.user.editKnownFor"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <g:render template="/editorUser/editorUserMenu" model="[user:command.politician]"/>
</content>

<content tag="mainContent">
    <h1><g:message code="admin.menu.user.editKnownFor" args="[command.politician.name]"/></h1>
    <formUtil:validateForm form="relevantEventsForm" bean="${command}"/>
    <g:form method="POST" mapping="editorEditPoliticianRelevantEvents" params="${command.politician.encodeAsLinkProperties()}" name="relevantEventsForm" role="form">
        <g:render template="/politicianProfile/formRelevantEvents" model="[command:command]"/>
    </g:form>
</content>
