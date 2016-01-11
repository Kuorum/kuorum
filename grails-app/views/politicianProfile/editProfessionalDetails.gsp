<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.profilePoliticianProfessionalDetails"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
    <r:require modules="forms"/>
</head>

<content tag="leftMenu">
    <g:render template="/profile/leftMenu" model="[user:user, activeMapping:'profilePoliticianProfessionalDetails', menu:menu]"/>

</content>
<content tag="titleContent">
    <h1><g:message code="profile.menu.profilePoliticianProfessionalDetails"/></h1>
    <h3><g:message code="profile.menu.profilePoliticianProfessionalDetails.subtitle"/></h3>
</content>
<content tag="mainContent">
    <formUtil:validateForm form="professionalDetailsForm" bean="${command}"/>
    <g:form method="POST" mapping="profilePoliticianProfessionalDetails" name="professionalDetailsForm" role="form">
        <g:render template="formProfessionalDetails" model="[command:command]"/>
    </g:form>
</content>
