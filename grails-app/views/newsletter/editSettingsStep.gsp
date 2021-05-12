<%@ page import="org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <g:set var="breadCrumbName">
        <g:if test="${command?.campaignName}">
            <g:message code="head.logged.account.tools.massMailing.edit" args="[command.campaignName]"/>
        </g:if>
        <g:else>
            <g:message code="head.logged.account.tools.massMailing.new"/>
        </g:else>
    </g:set>

    <title>${breadCrumbName}</title>
    <meta name="layout" content="basicPlainLayout">
    <!-- Schema.org markup for Google+ -->
    <meta itemprop="name" content="${_domainName}">
</head>

<content tag="mainContent">
    <ol class="breadcrumb">
        <li><g:link mapping="politicianCampaigns"><g:message code="head.logged.account.tools.massMailing"/></g:link></li>
        <li><g:link mapping="politicianCampaignsNew"><g:message code="tools.campaign.new.title"/></g:link></li>
        <li class="active">${breadCrumbName}</li>
    </ol>
    <g:render template="/newsletter/types/settingsStep" model="[
            campaign:campaign,
            attachEvent:false,
            command: command,
            domainValidation:domainValidation,
            filters: filters,
            totalContacts: totalContacts,
            anonymousFilter: anonymousFilter,
            events:[TrackingMailStatusRSDTO.OPEN,TrackingMailStatusRSDTO.CLICK,TrackingMailStatusRSDTO.ANSWERED, TrackingMailStatusRSDTO.TRACK_LINK],
            mappings:[
                    step:'settings',
                    settings:'politicianMassMailingSettings',
                    template:'politicianMassMailingTemplate',
                    content:'politicianMassMailingContent',
                    showResult: 'politicianCampaigns',
                    next: 'politicianMassMailingTemplate']]"/>
</content>