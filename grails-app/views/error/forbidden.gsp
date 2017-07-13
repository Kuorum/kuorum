<%@ page import="kuorum.core.model.CommissionType; kuorum.core.model.VoteType; kuorum.core.model.PostType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="error.forbidden.title"/></title>
    <meta name="layout" content="errorLayout">
    <parameter name="extraCssContainer" value="onecol error" />
</head>

<content tag="mainContent">
    <div class="intro">
        <h1><g:message code="error.forbidden.title"/> </h1>
    </div>
</content>

<content tag="preFooter">
    <g:render template="/error/errorFooter" model="[title:message(code: 'error.forbidden.description')]"/>
    <div class="row" style="min-height: 50px;"></div>
    <br/>
</content>