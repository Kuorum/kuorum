<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/> </title>
    <meta name="layout" content="noHeadLinksLayout">
</head>


<content tag="mainContent">


    <H1> PASO 2</H1>

FOTO
<g:form method="POST" mapping="customRegisterStep2">

    <div class="fieldcontain ${hasErrors(bean: command, field: 'workingSector', 'error')} ">
        <label for="workingSector">
            <g:message code="step1.workingSector.label" default="workingSector"/>

        </label>
        <g:select name="workingSector" from="${kuorum.core.model.WorkingSector?.values()}"
                  keys="${kuorum.core.model.WorkingSector.values()*.name()}" required="true"
                  value="${command?.workingSector?.name()}"/>
    </div>
    <div class="fieldcontain ${hasErrors(bean: command, field: 'studies', 'error')} ">
        <label for="studies">
            <g:message code="step1.studies.label" default="studies"/>

        </label>
        <g:select name="studies" from="${kuorum.core.model.Studies?.values()}"
                  keys="${kuorum.core.model.Studies.values()*.name()}" required="true"
                  value="${command?.studies?.name()}"/>
    </div>

    <g:link mapping="customRegisterStep3">skip step</g:link>
    <g:submitButton name="paso2">Continuar</g:submitButton>
</g:form>
</content>

<content tag="cColumn">
    Columna C de dashboard
</content>
