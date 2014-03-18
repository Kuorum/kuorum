<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/> </title>
    <meta name="layout" content="kuorumLayout">
</head>


<content tag="mainContent">


    <H1> PASO 4</H1>

FOTO
<g:form method="POST" mapping="customRegisterStep4">

    <div class="fieldcontain ${hasErrors(bean: command, field: 'userIds', 'error')} ">
        <label for="userIds">
            <g:message code="step1.userIds.label" default="userIds"/>

        </label>
        <g:select name="userIds" from="${recommendedUsers}" optionKey="id" multiple="true"/>
    </div>

    <g:link mapping="customRegisterStep5">skip step</g:link>
    <g:submitButton name="Continuar"/>
</g:form>
</content>

<content tag="cColumn">
    Columna C de dashboard
</content>
