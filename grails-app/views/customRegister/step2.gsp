<%@ page import="kuorum.core.model.UserType" %>
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
            <formUtil:editImage command="${command}" field="photoId" fileGroup="${kuorum.core.FileGroup.USER_AVATAR}"/>
        </div>
        <g:if test="${user.userType != UserType.ORGANIZATION}">
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
        </g:if>
        <g:else>
            <div class="form-group">
                <formUtil:selectEnum
                        command="${command}"
                        field="enterpriseSector"/>
            </div>
        </g:else>
        <div class="form-group">
            <p class="help-block"><g:message code="customRegister.step2.form.helpBlock"/> </p>
        </div>
        <div class="form-group">
            <formUtil:textArea command="${command}" field="bio" rows="5"/>
            %{--<label for="bio">Bio</label>--}%
            %{--<textarea class="form-control counted" rows="5" id="bio" placeholder="Escribe algo sobre ti"></textarea>--}%
            %{--<div id="charInit" class="hidden">Tienes un límite de caracteres de <span>500</span></div>--}%
            %{--<div id="charNum" class="help-block">Te quedan <span></span> caracteres</div>--}%
        </div>
        <div class="form-group">
            <g:link class="cancel" mapping="customRegisterStep3"><g:message code="customRegister.step2.form.cancel"/></g:link>
            <input type="submit" class="btn btn-lg" value="${message(code:'customRegister.step2.form.submit')}"/>
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
