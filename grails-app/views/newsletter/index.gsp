<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="head.logged.account.tools.massMailing"/></title>
    <meta name="layout" content="basicPlainLayout">
    <!-- Schema.org markup for Google+ -->
    <meta itemprop="name" content="${_domainName}">
    <r:require modules="campaignList"/>
</head>

<content tag="mainContent">
    <ol class="breadcrumb">
        <li  class="active"><g:message code="head.logged.account.tools.massMailing"/> </li>
    </ol>

    <g:if test="${campaigns || newsletters}">
        <div id="listCampaigns">
            <g:render template="searchCampaigns" model="[campaigns: campaigns, newsletters: newsletters, user: user]"/>
        </div>
    </g:if>
    <g:else>
        <div class="container-fluid box-ppal choose-campaign">
            <g:render template="/newsletter/chooseCampaign" model="[chooseCampaignTitle:g.message(code:'dashboard.payment.noCampaigns')]"/>
        </div>
    </g:else>

</content>