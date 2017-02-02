<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.dashboard.crmUser.normal"/></title>
    <meta name="layout" content="paymentPlainLayout">
    <!-- Schema.org markup for Google+ -->
    <meta itemprop="name" content="${g.message(code:"kuorum.name")}">
    <meta itemprop="description" content="${g.message(code:"layout.head.meta.description")}">
    <meta itemprop="image" content="${resource(dir: 'images', file:'landingSearch-rrss.png', absolute:true)}" />
    <meta itemprop="image" content="${resource(dir: 'images', file: 'logo@2x.png')}" />
</head>

<content tag="mainContent">
    <div class="row dashboard">
        <div class="col-md-4">
            <g:render template="/dashboard/payment/dashboardModules/campaignNewCampaign" model="[lastCampaign:lastCampaign,durationDays:durationDays,contacts:contacts]"/>
        </div>

        <div class="col-md-4">
            <g:render template="/dashboard/payment/dashboardModules/followOtherPoliticians" model="[recommendedUsers:recommendedUsers]"/>
        </div>

        <div class="col-md-4">
            <g:render template="/dashboard/payment/dashboardModules/dashboardPoliticianProfile" model="[user:user, emptyEditableData:emptyEditableData, numberCampaigns:numberCampaigns]"/>
        </div>
    </div>
</content>