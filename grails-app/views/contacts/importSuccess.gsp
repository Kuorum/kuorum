<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="tools.contact.import.title.success"/></title>
    <meta name="layout" content="basicPlainLayout">

</head>

<content tag="mainContent">
    <ol class="breadcrumb">
        <li><g:link mapping="politicianContacts"> <g:message code="tools.contact.title"/> </g:link></li>
        <li><g:link mapping="politicianContactImport"> <g:message code="tools.contact.import.title"/> </g:link></li>
        <li class="active"><g:message code="tools.contact.import.title.success"/></li>
    </ol>

    <div class="container-fluid box-ppal import-contacts">
        <p><g:message code="tools.contact.import.success.congrats"/> </p>
        <p>
            <g:link mapping="politicianCampaignsNew" class="btn btn-blue inverted btn-lg">
                <g:message code="tools.contact.import.success.newCampaign"/>
            </g:link>
        </p>
    </div>

</content>