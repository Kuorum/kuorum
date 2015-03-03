<%@ page import="kuorum.core.model.UserType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="funnel.successfulStories.title"/></title>
    <meta name="layout" content="funnelLayout">
    <parameter name="extraCssContainer" value="embudo" />
</head>

<content tag="mainContent">
    <g:render template="leftColumn"/>
    <g:render template="successfulStories"/>
</content>

<content tag="preFooter">
    <g:render template="offersNoPrice"/>
</content>

