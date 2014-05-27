<%@ page import="kuorum.core.FileGroup" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/> </title>
    <meta name="layout" content="editPostLayout">
    <parameter name="extraCssContainer" value="edit-post" />
</head>

<content tag="intro">
    <h1><g:message code="post.edit.step1.intro.head"/></h1>
    <p><g:message code="post.edit.step1.intro.subHead"/></p>
</content>

<content tag="mainContent">
    <formUtil:validateForm bean="${command}" form="createPost"/>
    <g:form mapping="postCreate" params="${law.encodeAsLinkProperties()}" role="form" name="createPost">
        <g:render template="form" model="[command:command,law:law]"/>
        <fieldset class="btns">
            <div class="form-group">
                <input type="submit" class="btn btn-grey btn-lg" tabindex="18" value="Guardar y continuar"/>
                <a href="#" class="cancel" tabindex="19">Cancelar</a>
            </div>
        </fieldset>
    </g:form>
</content>

<content tag="cColumn">
    <g:render template="/post/editPostColumnC" model="[law:law]"/>
</content>
