<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/> </title>
    <meta name="layout" content="noHeadLinksLayout">
</head>


<content tag="mainContent">


    <H1> PASO 3</H1>

FOTO
<g:form method="POST" mapping="customRegisterStep3">

    <div class="fieldcontain ${hasErrors(bean: command, field: 'relevantCommissions', 'error')} ">
        <label for="relevantCommissions">
            <g:message code="step1.relevantCommissions.label" default="relevantCommissions"/>

        </label>

        <g:each in="${kuorum.core.model.CommissionType.values()}" var="commissionType">
            <g:checkBox name="relevantCommissions" value="${commissionType}" checked="${command.relevantCommissions.contains(commissionType)}"/>
            <span class="${hasErrors(bean: command, field: 'relevantCommissions', 'error')}">
                <g:message code="kuorum.core.model.CommissionType.${commissionType}" default="${commissionType}"/>
            </span>
            <br/>
        </g:each>
    </div>

    <g:link mapping="customRegisterStep4">skip step</g:link>
    <g:submitButton name="Continuar"/>
</g:form>
</content>

<content tag="cColumn">
    Columna C de dashboard
</content>
