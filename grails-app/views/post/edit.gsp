<%@ page import="kuorum.core.FileGroup" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/> </title>
    <meta name="layout" content="columnCLayout">
    <parameter name="specialContainerCssClass" value="edit-post" />
</head>

<content tag="mainContent">
    <formUtil:validateForm bean="${command}" form="editPost"/>
    <g:form mapping="postEdit" params="${post.encodeAsLinkProperties()}" role="form" name="editPost" class="box-ppal">
        <g:render template="form" model="[command:command, project:post.project]"/>
    </g:form>
</content>

<content tag="cColumn">
    <g:render template="/post/editPostColumnC" model="[project:post.project]"/>
</content>
