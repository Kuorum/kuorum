<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.login"/> </title>
    <meta name="layout" content="normalLayout">
</head>



<content tag="mainContent">
    <g:form mapping="register" name="registerForm">

        <div class="fieldcontain ${hasErrors(bean: command, field: 'name', 'error')} ">
            <label for="name">
                <g:message code="post.name.label" default="name"/>
                <g:if test="${hasErrors(bean: command, field: 'name', 'error')}">
                    <g:fieldError field="name" bean="${command}"/>
                </g:if>

            </label>
            <g:textField name="name" value="${command?.name}"/>
        </div>

        <div class="fieldcontain ${hasErrors(bean: command, field: 'email', 'error')} ">
            <label for="email">
                <g:message code="post.email.label" default="email"/>
                <g:if test="${hasErrors(bean: command, field: 'email', 'error')}">
                    <g:fieldError field="email" bean="${command}"/>
                </g:if>
            </label>
            <g:textField name="email" value="${command?.email}"/>
        </div>

        <div class="fieldcontain ${hasErrors(bean: command, field: 'password', 'error')} ">
            <label for="password">
                <g:message code="post.password.label" default="password"/>
                <g:if test="${hasErrors(bean: command, field: 'password', 'error')}">
                    <g:fieldError field="password" bean="${command}"/>
                </g:if>
            </label>
            <g:passwordField name="password" value="${command?.password}"/>
        </div>

        %{--<div class="fieldcontain ${hasErrors(bean: command, field: 'password2', 'error')} ">--}%
            %{--<label for="password2">--}%
                %{--<g:message code="post.password2.label" default="password2"/>--}%

            %{--</label>--}%
            %{--<g:textField name="text" value="${command?.password2}"/>--}%
        %{--</div>--}%

        <g:submitButton name="registrarse" form="registerForm"/>
    </g:form>

<script>
$(document).ready(function() {
	$('#username').focus();
});
</script>
</content>