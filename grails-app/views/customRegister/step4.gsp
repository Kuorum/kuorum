<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/> </title>
    <meta name="layout" content="customRegisterLayout">
    <parameter name="actualStep" value="4" />
</head>

<content tag="intro">
    <h1><g:message code="customRegister.step4.intro.title"/> </h1>
    <p><g:message code="customRegister.step4.intro.description"/></p>
</content>

<content tag="mainContent">
    <g:form method="POST" mapping="customRegisterStep4" name="sign4" role="form">
        <div class="form-group">
            <span class="span-label">Empieza a seguir a otros <label class="checkbox-inline pull-right"><input type="checkbox" id="selectAll" value="allUsers"> Seleccionar todos</label></span>
            <div class="usersContainer clearfix">
                <div class="all clearfix">
                    <g:each in="${recommendedUsers}" var="recommendedUser">
                        <input type="checkbox" name="recommendedUsers" id="${recommendedUser.id}" value="${recommendedUser.id}" class="check">
                        <label for="${recommendedUser.id}"><img class="user-img big" alt="${recommendedUser.name}" src="${image.userImgSrc(user:recommendedUser)}">
                            ${recommendedUser.name}
                            <span class="rol">
                                <g:message code="kuorum.core.model.gamification.GamificationAward.${recommendedUser.gamification.activeRole}.${recommendedUser.personalData.gender}"/>
                            </span>
                        </label>
                    </g:each>
                </div><!-- /.all -->
                <p class="help-block"><strong>Selecciona a un mínimo de 3 personas.</strong> Sigue a los usuarios que te interesen para estar al día de todo lo que hacen en Kuorum.</p>
            </div>
            <g:if test="${hasErrors(bean: command, field: 'recommendedUsers', 'error')}">
                <span for="recommendedUsers" class="error">${g.fieldError(bean: command, field: 'recommendedUsers')}</span>
            </g:if>
        </div>
        <div class="form-group">
            <span class="cancel" id="descNumSelect">Selecciona 3 usuarios más</span>
            <input type="submit" class="btn btn-lg pull-right" value="Continuar"/>
        </div>
        <script>

        </script>
    </g:form>
</content>

<content tag="boxes">
    <div class="boxes">
        <h2><g:message code="customRegister.step4.lateral.title"/></h2>
        <p><g:message code="customRegister.step4.lateral.content"/></p>
    </div>
</content>
