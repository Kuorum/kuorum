<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    %{--<title><g:message code="page.title.contacts.import"/></title>--}%
    <title><g:message code="page.title.dashboard.crmUser.noContacts"/></title>
    <meta name="layout" content="basicPlainLayout">

    <g:if test="${tour}">
        <r:require module="tour"/>
    </g:if>
</head>

<content tag="mainContent">
    <div class="container-fluid box-ppal choose-campaign">


        <p><g:message code="dashboard.payment.noContacts.sendTestCampaign" args="[g.createLink(mapping: 'politicianMassMailingNew', params: [testFilter:true])]"/>:</p>
        <g:render template="/contacts/importOptions"/>

        <div class="contacts-tooltip icons pull-right">
            <g:render template="/contacts/tools/tooltipContactsImport"/>
        </div>
        <div class="skip-contacts"><g:link mapping="dashboardSkipUploadContacts"><g:message code="dashboard.payment.noContacts.skipImport"/></g:link></div>
    </div>
</content>