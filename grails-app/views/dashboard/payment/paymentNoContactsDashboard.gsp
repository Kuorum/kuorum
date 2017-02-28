<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    %{--<title><g:message code="page.title.contacts.import"/></title>--}%
    <title><g:message code="page.title.dashboard.crmUser.noContacts"/></title>
    <meta name="layout" content="paymentPlainLayout">
    <!-- Schema.org markup for Google+ -->
    <meta itemprop="name" content="${g.message(code:"kuorum.name")}">
    <meta itemprop="description" content="${g.message(code:"layout.head.meta.description")}">
    <meta itemprop="image" content="${resource(dir: 'images', file:'landingSearch-rrss.png', absolute:true)}" />
    <meta itemprop="image" content="${resource(dir: 'images', file: 'logo@2x.png')}" />
</head>

<content tag="mainContent">
    <div class="container-fluid box-ppal choose-campaign">


        <p><g:message code="dashboard.payment.noContacts.sendTestCampaign" args="[g.createLink(mapping: 'politicianMassMailingNew', params: [testFilter:true])]"/>:</p>
        <g:render template="/contacts/importOptions"/>
    </div>
</content>