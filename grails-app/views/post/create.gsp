<%@ page import="org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <g:set var="breadCrumbName">
        <g:message code="tools.campaign.new.POST"/>
    </g:set>

    <title>${breadCrumbName}</title>
    <meta name="layout" content="basicPlainLayout">
</head>

<content tag="mainContent">
    <ol class="breadcrumb">
        <li><g:link mapping="politicianCampaigns"><g:message code="head.logged.account.tools.massMailing"/></g:link></li>
        <li><g:link mapping="politicianCampaignsNew"><g:message code="tools.campaign.new.title"/></g:link></li>
        <li class="active">${breadCrumbName}</li>
    </ol>
    <g:render template="/campaigns/edit/settingsStep" model="[
            attachEvent:false,
            domainValidation:domainValidation,
            command: command,
            filters: filters,
            totalContacts: totalContacts,
            anonymousFilter: anonymousFilter,
            events:[TrackingMailStatusRSDTO.CLICK,TrackingMailStatusRSDTO.POST_LIKE],
            mappings:[
                    step:'settings',
                    settings:'postEdit',
                    content:'postEditContent',
                    showResult: 'postShow',
                    next: 'postEditContent'
            ]]"/>
    <g:render template="/newsletter/timeZoneSelectorPopUp"/>
</content>