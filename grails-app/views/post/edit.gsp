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
    <g:render template="/post/editPostColumnC" model="[law:post.law]"/>
</content>
