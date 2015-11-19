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
    <h1>CSV politicians loaded from ${fileName}</h1>
    <h2>Wrong politicians</h2>
    <ul>
        <g:each in="${politiciansWrong}" var="data">
            <li>
                ${data.name} (${data.id}): <i>${data.error}</i>
            </li>
        </g:each>
    </ul>
    <h2>Correct politicians</h2>
    <ul>
    <g:each in="${politiciansOk}" var="politician">
        <li>
            <a href="${politician.link}">${politician.name}</a>
        </li>
    </g:each>
    </ul>
</content>
