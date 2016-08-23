<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.contacts.import"/></title>
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
            <g:render template="/dashboard/payment/dashboardModules/dashboardPoliticianProfile" model="[user:user, emptyEditableData:emptyEditableData, campaigns:campaigns]"/>
        </div>
    </div>

    <div class="row">
        <div class="col-md-12">
            <div class="box-ppal" id="dashboardGraphs">
                <ul class="nav nav-tabs simple" data-tabs="tabs">
                    <li role="presentation" class="active"><a href="#engagement" data-toggle="tab">Engagement stats</a></li>
                    <li role="presentation"><a href="#valuation" data-toggle="tab">Valuation stats</a></li>
                    <li role="presentation"><a href="#profile" data-toggle="tab">Profile stats</a></li>
                    <li role="presentation"><a href="#socialData" data-toggle="tab">Social Data Analytics</a></li>
                </ul>
                <div id="tabs-dashboard" class="tab-content">
                    <div class="tab-pane active" id="engagement">
                        engagement content
                    </div>
                    <div class="tab-pane" id="valuation">
                        valuation content
                    </div>
                    <div class="tab-pane" id="profile">
                        profile content
                    </div>
                    <div class="tab-pane" id="socialData">
                        social data content
                    </div>
                </div>
            </div>
        </div>
    </div>
</content>