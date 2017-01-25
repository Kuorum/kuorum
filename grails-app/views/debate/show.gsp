<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <g:set var="breadCrumbName">
        <g:message code="admin.createDebate.title"/>
    </g:set>

    <title>${breadCrumbName}</title>
    <meta name="layout" content="columnCLayout">
    <r:require modules="debate"/>


</head>

<content tag="mainContent">
    <g:render template="showModules/mainContent" model="[debate: debate, debateUser: debateUser,proposalPage:proposalPage]" />
</content>

<content tag="cColumn">
    <g:render template="showModules/cColumn" model="[debate: debate, debateUser: debateUser]" />
</content>

