<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <g:set var="breadCrumbName">
            <g:message code="head.logged.account.tools.massMailing.show" args="[campaign.name]"/>
    </g:set>

    <title>${breadCrumbName}</title>
    <meta name="layout" content="paymentPlainLayout">
    <!-- Schema.org markup for Google+ -->
    <meta itemprop="name" content="${g.message(code:"kuorum.name")}">
    <meta itemprop="description" content="${g.message(code:"layout.head.meta.description")}">
    <meta itemprop="image" content="${resource(dir: 'images', file:'landingSearch-rrss.png', absolute:true)}" />
    <meta itemprop="image" content="${resource(dir: 'images', file: 'logo@2x.png')}" />

</head>

<content tag="mainContent">
    <ol class="breadcrumb">
        <li><g:link mapping="politicianCampaigns"><g:message code="head.logged.account.tools.massMailing"/></g:link></li>
        <li class="active">${breadCrumbName}</li>
    </ol>
    <div class="box-ppal campaign-stats">
        <ul class="nav nav-tabs simple" data-tabs="tabs">
            <li role="presentation" class="active"><a href="#stats" data-toggle="tab"><g:message code="tools.massMailing.view.stats"/></a></li>
            <li role="presentation"><a href="#recipients" data-toggle="tab"><g:message code="tools.massMailing.list.recipients"/></a></li>
            <li role="presentation"><a href="#viewemail" data-toggle="tab"><g:message code="tools.massMailing.view.viewMail"/></a></li>
        </ul>
        <div id="tabs-stats-campaign" class="tab-content">
            <div class="tab-pane active" id="stats">
                <g:render template="/massMailing/campaignTabs/campaignStats" model="[campaign:campaign]"/>
            </div>
            <div class="tab-pane" id="recipients">
                <g:render template="/massMailing/campaignTabs/campaignRecipeints" model="[trackingPage:trackingPage, campaignId:campaign.id]"/>
            </div>
            <div class="tab-pane" id="viewemail">
                <g:render template="campaignTabs/campaignViewMail" model="[campaign: campaign]"/>
            </div>
        </div>
    </div>
</content>