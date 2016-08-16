<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="head.logged.account.tools.massMailing"/></title>
    <meta name="layout" content="paymentPlainLayout">
    <!-- Schema.org markup for Google+ -->
    <meta itemprop="name" content="${g.message(code:"kuorum.name")}">
    <meta itemprop="description" content="${g.message(code:"layout.head.meta.description")}">
    <meta itemprop="image" content="${resource(dir: 'images', file:'landingSearch-rrss.png', absolute:true)}" />
    <meta itemprop="image" content="${resource(dir: 'images', file: 'logo@2x.png')}" />
</head>

<content tag="mainContent">
    <ol class="breadcrumb">
        <li  class="active"><g:message code="head.logged.account.tools.massMailing"/> </li>
    </ol>
    <div class="box-ppal">
        <g:link mapping="politicianMassMailingNew">CREATE NEW CAMPAIGN</g:link>
    </div>
</content>