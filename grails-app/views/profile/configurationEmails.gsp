<%@ page import="kuorum.mail.MailType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.configurationEmails"/> </title>
    <meta name="layout" content="normalLayout">
</head>


<content tag="mainContent">
    <H1> Notificaciones por email ${user.name}</H1>

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
