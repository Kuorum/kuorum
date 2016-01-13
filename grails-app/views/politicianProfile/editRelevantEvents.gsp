<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.knownFor"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
    <r:require modules="forms"/>
</head>

<content tag="leftMenu">
    <g:render template="/profile/leftMenu" model="[user:user, activeMapping:'profilePoliticianRelevantEvents', menu:menu]"/>

</content>
<content tag="titleContent">
    <h1><g:message code="profile.menu.profilePoliticianKnownFor"/></h1>
    <h3><g:message code="profile.menu.profilePoliticianKnownFor.subtitle"/></h3>
</content>
<content tag="mainContent">
    <formUtil:validateForm form="relevantEventsForm" bean="${command}" dirtyControl="true"/>
    <g:form method="POST" mapping="profilePoliticianRelevantEvents" name="relevantEventsForm" role="form">
        <g:render template="formRelevantEvents" model="[command:command]"/>
    </g:form>
</content>
