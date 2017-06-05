<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.socialNetworks"/> </title>
    <meta name="layout" content="leftMenuConfigLayout">
    <parameter name="extraCssContainer" value="config" />
    <r:require modules="forms"/>
</head>

<content tag="leftMenu">
    <g:render template="leftMenu" model="[user:user, activeMapping:'profileSocialNetworks', menu:menu]"/>

</content>

<content tag="titleContent">
    <h1><g:message code="profile.menu.profileSocialNetworks"/></h1>
    <h3><g:message code="profile.menu.profileSocialNetworks.subtitle"/></h3>
</content>
<content tag="mainContent">
    <formUtil:validateForm bean="${command}" form="socialForm" dirtyControl="true"/>
    <g:form mapping="profileSocialNetworks" role="form" dirtyControl="true" name="socialForm">
        <g:set var="showPoliticianFields" value="${false}"/>
        <sec:ifAnyGranted roles="ROLE_POLITICIAN">
            <g:set var="showPoliticianFields" value="${true}"/>
        </sec:ifAnyGranted>
        <g:render template="formSocialNetworks" model="[user:user, command:command,showPoliticianFields:showPoliticianFields]"/>
    </g:form>
</content>
