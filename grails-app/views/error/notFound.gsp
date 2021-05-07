<%@ page import="kuorum.core.model.CommissionType; kuorum.core.model.VoteType; kuorum.core.model.PostType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="error.notFound.title"/></title>
    <meta name="layout" content="errorLayout">
    <parameter name="extraCssContainer" value="onecol error" />
    <parameter name="showNavBar" value="false" />
</head>

<content tag="mainContent">
    <div class="col-xs-offset-3 col-xs-6 col-sm-offset-4 col-sm-4">
        <div class="broken-link">
            <img src="${resource(dir: 'images', file: 'broken-link.png')}">
        </div>
    </div>
</content>

<content tag="preFooter">
    <g:render template="/error/errorFooter" model="[title:message(code: 'error.notFound.description')]"/>
    <div class="row"></div>
    <br/>
</content>