<%@ page import="kuorum.web.commands.profile.TimeZoneCommand" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="tools.campaign.new.title"/></title>
    <meta name="layout" content="paymentPlainLayout">
    <!-- Schema.org markup for Google+ -->
    <meta itemprop="name" content="${g.message(code:"kuorum.name")}">
    <meta itemprop="image" content="${resource(dir: 'images', file:'landingSearch-rrss.png', absolute:true)}" />
    <meta itemprop="image" content="${resource(dir: 'images', file: 'logo@2x.png')}" />
</head>

<content tag="mainContent">
    <ol class="breadcrumb">
        <li><g:link mapping="politicianCampaigns"><g:message code="head.logged.account.tools.massMailing"/></g:link></li>
        <li class="active"><g:message code="tools.campaign.new.title"/></li>
    </ol>
    <div class="container-fluid box-ppal choose-campaign">
        <g:render template="chooseCampaign"/>
    </div>
    <g:render template="timeZoneSelectorPopUp"/>
</content>