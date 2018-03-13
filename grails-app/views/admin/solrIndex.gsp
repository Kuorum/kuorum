<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title>Admin - Solr Index</title>
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
    <p>Reciviras un email cuando termine</p>
    <h1>Reenviar info a MailChimp </h1>
    <p>NO USAR SALVO NECESIDAD QUE FUNDE A MAILCHIMP</p>
    <g:link controller="admin" action="updateMailChimp"> Actualizar Mailchimp </g:link>

    <H1> Subir politicos usando csv </H1>
    <p> Sube los politicos leyendolos desde el fichero csv scrapeado </p>
    <g:form action="uploadPoliticianCsv" controller="admin" enctype="multipart/form-data" useToken="true">
        <span class="button">
            <input type="file" name="filecsv"/>
            <input type="submit" value="upload"/>
        </span>
    </g:form>
</content>
