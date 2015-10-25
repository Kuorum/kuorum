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
    <h1>Politicos cargados en el csv ${fileName}</h1>
    <h2>Políticos erróneos</h2>
    <ul>
        <g:each in="${politiciansWrong}" var="data">
            <li>
                ${data.name} (${data.id}): <i>${data.error}</i>
            </li>
        </g:each>
    </ul>
    <h2>Politicos cargados bien</h2>
    <ul>
    <g:each in="${politiciansOk}" var="politician">
        <li>
            <g:link mapping="userShow" params="${politician.encodeAsLinkProperties()}">
                ${politician.name}
            </g:link>
        </li>
    </g:each>
    </ul>
</content>
