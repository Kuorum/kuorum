<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/> </title>
    <meta name="layout" content="customRegisterLayout">
    <parameter name="actualStep" value="1" />
</head>

<content tag="intro">
    <h1><g:message code="customRegister.step1.intro.title"/> </h1>
    <p><g:message code="customRegister.step1.intro.description"/></p>
</content>

<content tag="mainContent">
    <formUtil:validateForm bean="${command}" form="sign1"/>
    <g:form method="POST" mapping="customRegisterStep1" name="sign1" role="form">
        <div class="form-group">
            <formUtil:input
                    command="${command}"
                    field="name"
                    required="true"/>
        </div>
        <div class="form-group">
            <span class="span-label"><g:message code="customRegister.step1.form.gender.placeholder"/> </span>
            <g:each in="${kuorum.core.model.Gender?.values()}" var="gender" status="i">
                <label class="radio-inline">
                    <input type="radio" name="gender" id="radio${i}" value="${gender}" ${command.gender==gender?'checked':''}>
                    <g:message code="kuorum.core.model.Gender.${gender}"/>
                </label>
            </g:each>
        </div>
        <div class="form-group">
            <span class="span-label"><g:message code="customRegister.step1.form.birthday.label"/></span>
            <div class="row">
                <div class="col-xs-4">
                    <formUtil:input
                            command="${command}"
                            field="day"
                            labelCssClass="sr-only"
                            required="true"
                            type="number"
                            maxlength="2"
                    />
                </div>
                <div class="col-xs-4">
                    <formUtil:input
                            command="${command}"
                            field="month"
                            labelCssClass="sr-only"
                            required="true"
                            type="number"
                            maxlength="2"
                    />
                </div>
                <div class="col-xs-4">
                    <formUtil:input
                            command="${command}"
                            field="year"
                            labelCssClass="sr-only"
                            required="true"
                            type="number"
                            maxlength="4"
                    />
                </div>
            </div>
        </div>
        <div class="form-group">
            <div class="row">
                <div class="col-xs-4">
                    <formUtil:input
                            command="${command}"
                            field="postalCode"
                            required="true"
                            type="number"
                            maxlength="5"
                    />
                </div>
            </div>
        </div>
        <div class="form-group clearfix">
            <p class="help-block">${message(code:'customRegister.step1.form.postalCode.helpBlock' )}</p>
            <input type="submit" class="btn btn-lg pull-right" value="${message(code: 'customRegister.step1.form.submit')}"/>
        </div>
    </g:form>
</content>

<content tag="boxes">
    <div class="boxes">
        <h2>Cuéntanos algo más</h2>
        <p>Haz llegar tus historias sobre las leyes. Cosas en las que te afectan las leyes, cómo era antes, cómo será después de la entrada en vigor de la ley.</p>
        <p>Cuéntanos tu historia. En qué te afecta esta ley. Nos comprometemos a hacerla llegar a los políticos para que la tengan en cuenta.</p>
    </div>
</content>