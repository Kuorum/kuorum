<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.changePassword"/> </title>
    <meta name="layout" content="normalLayout">
</head>


<content tag="mainContent">
    <H1> Cambiar pass de ${user.name}</H1>
<g:form method="POST" mapping="profileChangePass">

    <div class="fieldcontain ${hasErrors(bean: command, field: 'originalPassword', 'error')} ">
        <label for="originalPassword">
            <g:message code="step1.postalCode.label" default="originalPassword"/>
            <g:if test="${hasErrors(bean: command, field: 'originalPassword', 'error')}">
                <g:fieldError field="originalPassword" bean="${command}"/>
            </g:if>
        </label>
        <g:passwordField name="originalPassword" value="${command?.originalPassword}"/>
    </div>
    <div class="fieldcontain ${hasErrors(bean: command, field: 'password', 'error')} ">
        <label for="password">
            <g:message code="step1.postalCode.label" default="password"/>
            <g:if test="${hasErrors(bean: command, field: 'password', 'error')}">
                <g:fieldError field="password" bean="${command}"/>
            </g:if>
        </label>
        <g:passwordField name="password" value="${command?.password}"/>
    </div>
    <div class="fieldcontain ${hasErrors(bean: command, field: 'password2', 'error')} ">
        <label for="password2">
            <g:message code="step1.postalCode.label" default="password2"/>
            <g:if test="${hasErrors(bean: command, field: 'password2', 'error')}">
                <g:fieldError field="password2" bean="${command}"/>
            </g:if>
        </label>
        <g:passwordField name="password2" value="${command?.password2}"/>
    </div>

    <g:submitButton name="guardar">guardar</g:submitButton>
</g:form>
</content>
