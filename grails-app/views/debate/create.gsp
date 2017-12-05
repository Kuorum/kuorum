<%@ page import="org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <g:set var="breadCrumbName">
        <g:message code="admin.createDebate.title"/>
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
        <li><g:link mapping="politicianCampaignsNew"><g:message code="tools.campaign.new.title"/></g:link></li>
        <li class="active">${breadCrumbName}</li>
    </ol>
    <g:render template="/campaigns/edit/settingsStep" model="[
            command: command,
            filters: filters,
            totalContacts: totalContacts,
            debate: debate,
            anonymousFilter: anonymousFilter,
            events:[TrackingMailStatusRSDTO.OPEN,TrackingMailStatusRSDTO.CLICK,TrackingMailStatusRSDTO.DEBATE_PROPOSAL_NEW,TrackingMailStatusRSDTO.DEBATE_PROPOSAL_COMMENT,TrackingMailStatusRSDTO.DEBATE_PROPOSAL_LIKE],
            mappings:[
                                                            step:'settings',
                                                            settings:'debateEdit',
                                                            content:'debateEditContent',
                                                            showResult: 'debateShow',
                                                            next: 'debateEditContent']]"/>

    <g:render template="/newsletter/timeZoneSelectorPopUp"/>
</content>