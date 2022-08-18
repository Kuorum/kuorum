<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.contacts.import"/></title>
    <meta name="layout" content="basicPlainLayout">

    <meta itemprop="name" content="${_domainName}">
</head>

<content tag="mainContent">
    <ol class="breadcrumb">
        <li><g:link mapping="politicianContacts"> <g:message code="tools.contact.title"/> </g:link></li>
        <li class="active"><g:message code="tools.contact.import.title"/></li>
    </ol>
    <div class="container-fluid box-ppal dashboard">
        <g:render template="/contacts/importOptions"/>
        <div class="contacts-tooltip icons pull-right">
            <g:render template="tools/tooltipContactsImport"/>
        </div>
    </div>
</content>