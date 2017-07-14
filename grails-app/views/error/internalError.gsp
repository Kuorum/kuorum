<%@ page import="kuorum.core.model.CommissionType; kuorum.core.model.VoteType; kuorum.core.model.PostType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="error.internalError.title"/> </title>
    <meta name="layout" content="errorLayout">
    <parameter name="extraCssContainer" value="onecol error" />
</head>

<content tag="mainContent">
    <div class="intro">
        <h1><g:message code="error.internalError.title"/> </h1>
    </div>
</content>

<content tag="preFooter">
    <div class="container-fluid onecol">
        <g:render template="/error/errorFooter" model="[title:message(code:'error.internalError.description')]"/>
        <div class="row">
            <div class="col-xs-12 col-sm-5 col-md-5">
                <h3><g:message code="error.internalError.h3.1.title"/></h3>
                <p><g:message code="error.internalError.h3.1.p1"/></p>
            </div>
            <div class="col-xs-12 col-sm-5 col-sm-offset-1 col-md-5 col-md-offset-1">
                <h3><g:message code="error.internalError.h3.2.title"/></h3>
                <p><g:message code="error.internalError.h3.2.p1" args="[email]"/></p>
            </div>
        </div>
    </div>
    <br/>
</content>