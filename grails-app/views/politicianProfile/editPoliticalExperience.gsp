<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.profilePoliticianExperience"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <g:render template="/profile/leftMenu" model="[user:user, activeMapping:'profilePoliticianExperience', menu:menu]"/>

</content>
<content tag="titleContent">
    <h1><g:message code="profile.menu.profilePoliticianExperience"/></h1>
    <h3><g:message code="profile.menu.profilePoliticianExperience.subtitle"/></h3>
</content>

<content tag="mainContent">
    <formUtil:validateForm form="politicianExperienceForm" bean="${command}" dirtyControl="true"/>
    <g:form method="POST" mapping="profilePoliticianExperience" name="politicianExperienceForm" role="form">
        <g:render template="formPoliticianExperience" model="[command:command]"/>
    </g:form>
</content>
