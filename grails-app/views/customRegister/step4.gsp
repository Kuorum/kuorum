<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/> </title>
    <meta name="layout" content="noHeadLinksLayout">
</head>


<content tag="mainContent">


    <H1> PASO 4</H1>

FOTO
<g:form method="POST" mapping="customRegisterStep4">

    <div class="fieldcontain ${hasErrors(bean: command, field: 'recommendedUsers', 'error')} ">
        <label for="recommendedUsers">
            <g:message code="step1.recommendedUsers.label" default="recommendedUsers"/>

        </label>
        <g:each in="${recommendedUsers}" var="recommendedUser">
            <g:checkBox name="recommendedUsers" value="${recommendedUser.id}" checked="${command.recommendedUsers.contains(recommendedUser.id.toString())}"/>
            <span class="${hasErrors(bean: command, field: 'recommendedUsers', 'error')}">
                ${recommendedUser.name}
            </span>
            <br/>
        </g:each>

    </div>

    <g:link mapping="customRegisterStep5">skip step</g:link>
    <g:submitButton name="Continuar"/>
</g:form>
</content>

<content tag="cColumn">
    Columna C de dashboard
</content>
