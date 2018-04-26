<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.dashboard.crmUser.noCampaign"/></title>
    <meta name="layout" content="paymentPlainLayout">
    <!-- Schema.org markup for Google+ -->
    <meta itemprop="name" content="${_domainName}">
    <g:if test="${tour}">
        <r:require module="tour"/>
    </g:if>
</head>

<content tag="mainContent">
    <div class="container-fluid box-ppal choose-campaign">
        <p><g:message code="dashboard.payment.noCampaigns"/></p>
        %{--<p>--}%
            %{--<g:link mapping="politicianCampaignsNew" class="btn inverted btn-lg">--}%
                %{--<g:message code="dashboard.payment.newCampaign.sentNew"/>--}%
            %{--</g:link>--}%
        %{--</p>--}%
        <g:render template="/newsletter/chooseCampaign"/>
    </div>
</content>