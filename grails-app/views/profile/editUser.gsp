<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.editUser"/> </title>
    <meta name="layout" content="normalLayout">
</head>


<content tag="mainContent">
    <H1> Perfil de ${user.name}</H1>
    <g:form method="POST" mapping="profileEditUser">

        <div class="fieldcontain ${hasErrors(bean: command, field: 'name', 'error')} ">
            <label for="name">
                <g:message code="step1.postalCode.label" default="name"/>

            </label>
            <g:textField name="name" value="${command?.name}"/>
        </div>


        <div class="fieldcontain ${hasErrors(bean: command, field: 'gender', 'error')} ">
            <label for="gender">
                <g:message code="step1.gender.label" default="Post Type"/>

            </label>
            <g:select name="gender" from="${kuorum.core.model.Gender?.values()}"
                      keys="${kuorum.core.model.Gender.values()*.name()}" required="true"
                      value="${command?.gender?.name()}"/>
        </div>

        <div class="fieldcontain ${hasErrors(bean: command, field: 'year', 'error')} ">
            <label for="year">
                <g:message code="step1.year.label" default="year"/>
                <g:if test="${hasErrors(bean: command, field: 'year', 'error')}">
                    <g:fieldError field="year" bean="${command}"/>
                </g:if>

            </label>
            <g:textField name="year" value="${command?.year}"/>
        </div>
        <div class="fieldcontain ${hasErrors(bean: command, field: 'month', 'error')} ">
            <label for="month">
                <g:message code="step1.month.label" default="month"/>
                <g:if test="${hasErrors(bean: command, field: 'month', 'error')}">
                    <g:fieldError field="month" bean="${command}"/>
                </g:if>
            </label>
            <g:textField name="month" value="${command?.month}"/>
        </div>
        <div class="fieldcontain ${hasErrors(bean: command, field: 'day', 'error')} ">
            <label for="day">
                <g:message code="step1.day.label" default="day"/>
                <g:if test="${hasErrors(bean: command, field: 'day', 'error')}">
                    <g:fieldError field="day" bean="${command}"/>
                </g:if>
            </label>
            <g:textField name="day" value="${command?.day}"/>
        </div>

        <div class="fieldcontain ${hasErrors(bean: command, field: 'postalCode', 'error')} ">
            <label for="postalCode">
                <g:message code="step1.postalCode.label" default="postalCode"/>

            </label>
            <g:textField name="postalCode" value="${command?.postalCode}"/>
        </div>

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

        <div class="fieldcontain ${hasErrors(bean: command, field: 'bio', 'error')} ">
            <label for="bio">
                <g:message code="step1.postalCode.label" default="bio"/>

            </label>
            <g:textArea name="bio" value="${command?.bio}"/>
        </div>

        <g:submitButton name="guardar">guardar</g:submitButton>
    </g:form>
</content>
