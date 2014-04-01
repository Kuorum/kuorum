<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/> </title>
    <meta name="layout" content="noHeadLinksLayout">
</head>


<content tag="mainContent">


    <H1> PASO 1</H1>

    <g:form method="POST" mapping="customRegisterStep1">



        <div class="fieldcontain ${hasErrors(bean: command, field: 'gender', 'error')} ">
            <label for="gender">
                <g:message code="step1.gender.label" default="Post Type"/>

            </label>
            <g:select name="gender" from="${kuorum.core.model.Gender?.values()}"
                      keys="${kuorum.core.model.Gender.values()*.name()}" required="true"
                      value="${command?.gender?.name()}"/>
        </div>

        <div class="fieldcontain ${hasErrors(bean: command, field: 'postalCode', 'error')} ">
            <label for="postalCode">
                <g:message code="step1.postalCode.label" default="postalCode"/>

            </label>
            <g:textField name="postalCode" value="${command?.postalCode}"/>
        </div>
        <div class="fieldcontain ${hasErrors(bean: command, field: 'year', 'error')} ">
            <label for="year">
                <g:message code="step1.year.label" default="year"/>

            </label>
            <g:textField name="year" value="${command?.year}"/>
        </div>
        <div class="fieldcontain ${hasErrors(bean: command, field: 'month', 'error')} ">
            <label for="month">
                <g:message code="step1.month.label" default="month"/>

            </label>
            <g:textField name="month" value="${command?.month}"/>
        </div>
        <div class="fieldcontain ${hasErrors(bean: command, field: 'day', 'error')} ">
            <label for="day">
                <g:message code="step1.day.label" default="day"/>

            </label>
            <g:textField name="day" value="${command?.day}"/>
        </div>


        <g:submitButton name="paso2">Continuar</g:submitButton>
    </g:form>



</content>
