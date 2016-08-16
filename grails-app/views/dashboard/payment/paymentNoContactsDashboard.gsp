<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    %{--<title><g:message code="page.title.contacts.import"/></title>--}%
    <title>NO CONTACTS</title>
    <meta name="layout" content="paymentPlainLayout">
    <!-- Schema.org markup for Google+ -->
    <meta itemprop="name" content="${g.message(code:"kuorum.name")}">
    <meta itemprop="description" content="${g.message(code:"layout.head.meta.description")}">
    <meta itemprop="image" content="${resource(dir: 'images', file:'landingSearch-rrss.png', absolute:true)}" />
    <meta itemprop="image" content="${resource(dir: 'images', file: 'logo@2x.png')}" />
</head>

<content tag="mainContent">
    <div class="container-fluid box-ppal dashboard">


        <p><g:message code="dashboard.payment.noContacts.sendTestCampaign" args="[g.createLink(mapping: 'politicianMassMailingNew', params: [testFilter:true])]"/>:</p>
        %{--<ul id="mails">--}%
            %{--<li><a href="#" role="button" class="mail" id="gmail">Gmail</a></li>--}%
            %{--<li><a href="#" role="button" class="mail" id="yahoo">Yahoo!</a></li>--}%
            %{--<li><a href="#" role="button" class="mail" id="outlook">Outlook</a></li>--}%
            %{--<li><a href="#" role="button" class="mail" id="uploadFile">Upload file</a></li>--}%
        %{--</ul>--}%
        <p>
            <g:link mapping="politicianContactImport" class="btn inverted btn-lg">
                <g:message code="dashboard.payment.importContact"/>
            </g:link>
        </p>
    </div>
</content>