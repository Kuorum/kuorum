<%@ page import="kuorum.mail.MailType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.configurationEmails"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <h1>
        <g:message code="profile.emailNotifications.salutation"/>,
        <br>
        <span class="user-name">${user.name}</span>
    </h1>
    <p><g:message code="profile.emailNotifications.description"/></p>
    <g:render template="leftMenu" model="[user:user, activeMapping:'profileEmailNotifications', menu:menu]"/>

</content>

<content tag="mainContent">
    <h1><g:message code="profile.emailNotifications.title"/></h1>

<g:form method="POST" mapping="profileEmailNotifications">
    <g:each in="${MailType.values()}" var="mailType">
        <g:checkBox name="availableMails" value="${mailType}" checked="${command.availableMails.contains(mailType)}" disabled="${!mailType.configurable}"/>
        <span class="${hasErrors(bean: command, field: 'availableMails', 'error')}">
            <g:message code="kuorum.mail.MailType.${mailType}" default="${mailType}"/>
        </span>
        <br/>
    </g:each>

    <g:submitButton name="Continuar"/>
</g:form>
</content>
