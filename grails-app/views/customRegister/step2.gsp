<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/> </title>
    <meta name="layout" content="customRegisterLayout">
    <parameter name="actualStep" value="2" />
</head>

<content tag="intro">
    <h1><g:message code="customRegister.step2.intro.title"/> </h1>
    <p><g:message code="customRegister.step2.intro.description"/></p>
</content>

<content tag="mainContent">
    <g:form method="POST" mapping="customRegisterStep2" name="sign2" role="form">
        <div class="form-group">
            <label for="photoId">Añade una foto</label>
            <formUtil:editImage command="${command}" field="photoId" fileGroup="${kuorum.core.FileGroup.USER_AVATAR}"/>

            %{--<input type="file" class="form-control" id="addphoto">--}%
        </div>
        <div class="form-group">
            <formUtil:selectEnum
                    command="${command}"
                    field="workingSector"/>

        </div>
        <div class="form-group">
            <formUtil:selectEnum
                    command="${command}"
                    field="studies"/>
        </div>
        <div class="form-group">
            <p class="help-block"><g:message code="customRegister.step2.form.helpBlock"/> </p>
        </div>
        <div class="form-group">
            <label for="bio">Bio</label>
            <textarea class="form-control counted" rows="5" id="bio" placeholder="Escribe algo sobre ti"></textarea>
            <div id="charInit" class="hidden">Tienes un límite de caracteres de <span>500</span></div>
            <div id="charNum" class="help-block">Te quedan <span></span> caracteres</div>
        </div>
        <div class="form-group">
            <a class="cancel" href="#">Saltar este paso Continuar</a>
            <input type="submit" class="btn btn-lg" value="Continuar"/>
        </div>
    </g:form>

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

<content tag="boxes">
    <div class="boxes">
        <h2>Cuéntanos algo más</h2>
        <p>Haz llegar tus historias sobre las leyes. Cosas en las que te afectan las leyes, cómo era antes, cómo será después de la entrada en vigor de la ley.</p>
        <p>Cuéntanos tu historia. En qué te afecta esta ley. Nos comprometemos a hacerla llegar a los políticos para que la tengan en cuenta.</p>
    </div>
</content>
