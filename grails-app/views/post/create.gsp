<%@ page import="kuorum.core.FileGroup" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/> </title>
    <meta name="layout" content="columnCLayout">
</head>

<content tag="mainContent">
    <formUtil:validateForm bean="${command}" form="createPost"/>
    <g:form mapping="postCreate" params="${project.encodeAsLinkProperties()}" role="form" name="createPost" class="box-ppal">
        <g:render template="form" model="[command:command,project:project]"/>
    </g:form>
</content>

<content tag="cColumn">
    <g:render template="/post/editPostColumnC" model="[project:project]"/>
</content>
