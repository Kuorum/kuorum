<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title>Admin - Solr Index</title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <h1>
        <g:message code="admin.solrIndex.title"/>
    </h1>
    <g:render template="/admin/adminMenu" model="[activeMapping:'adminSearcherIndex', menu:menu]"/>
</content>

<content tag="mainContent">
    <h1><g:message code="admin.solrIndex.title"/></h1>
    <p><g:message code="admin.solrIndex.warn"/></p>
    <g:link mapping="adminSearcherFullIndex" > Indexar </g:link>
    <p>Recibiras un email cuando termine</p>
    <h1>Reenviar info a MailChimp </h1>
    <p>NO USAR SALVO NECESIDAD QUE FUNDE A MAILCHIMP</p>
    <g:link controller="admin" action="updateMailChimp"> Actualizar Mailchimp </g:link>
</content>
