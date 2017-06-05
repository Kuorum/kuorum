<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title>${debate.title}</title>
    <meta name="layout" content="columnCLayout">
    <g:render template="debateMetaTags" model="[debate: debate]"/>
    <r:require modules="debate"/>


</head>

<content tag="mainContent">
    <g:render template="showModules/mainContent" model="[debate: debate, debateUser: debateUser,proposalPage:proposalPage]" />
</content>

<content tag="cColumn">
    <g:render template="showModules/cColumn" model="[debate: debate, debateUser: debateUser, proposalPage:proposalPage]" />
</content>

