<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.changeEmail"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <h1>
        <g:message code="admin.solrIndex.title"/>,
    </h1>
    <g:render template="/admin/adminMenu" model="[activeMapping:'adminSearcherIndex', menu:menu]"/>
</content>

<content tag="mainContent">
    <h1><g:message code="admin.solrIndex.title"/></h1>
    <p><g:message code="admin.solrIndex.warn"/></p>
    <g:link mapping="adminSearcherFullIndex" > Indexar </g:link>
    <hr>
    <ul>
    <g:each in="${res}" var="result">
        <li><strong>${result.key}</strong>: ${result.value}</li>
    </g:each>
    </ul>
</content>
