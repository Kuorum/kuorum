<%@ page import="kuorum.core.FileGroup" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/> </title>
    <meta name="layout" content="editPostLayout">
</head>

<content tag="intro">
    <h1><g:message code="post.edit.step1.intro.head"/></h1>
    <p><g:message code="post.edit.step1.intro.subHead"/></p>
</content>

<content tag="mainContent">
    <formUtil:validateForm bean="${command}" form="editPost"/>
    <g:form mapping="postEdit" params="${post.encodeAsLinkProperties()}" role="form" name="editPost">
    %{--<form action="#" method="POST" role="form" id="editPost">--}%
        <g:render template="form" model="[command:command, post:post]"/>
        <fieldset class="btns">
            <div class="form-group">
                <input type="submit" class="btn btn-grey btn-lg" tabindex="18" value="Guardar y continuar"/>
                <a href="#" class="cancel" tabindex="19">Cancelar</a>
            </div>
        </fieldset>
    %{--</form>--}%
    </g:form>
</content>

<content tag="cColumn">
    <section class="boxes noted">
        <a href="#">#loquesea</a>
        <h1>Proyecto de ley del aborto</h1>
        <p>Ley Orgánica de protección de la vida del concebido y derechos de la mujer embarazada</p>
    </section>
    <section class="boxes">
        <h1>Consejos de publicación</h1>
        <h2><span class="fa fa-caret-right"></span> El título</h2>
        <p>Elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>
        <h2><span class="fa fa-caret-right"></span> Tu propuesta</h2>
        <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.</p>
        <p>Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.</p>
    </section>
</content>
