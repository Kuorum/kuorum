<%@ page import="org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <g:set var="breadCrumbName">
        <g:message code="admin.createDistrictProposal.title"/>
    </g:set>

    <title>${breadCrumbName}</title>
    <meta name="layout" content="paymentPlainLayout">
    <!-- Schema.org markup for Google+ -->
</head>

<content tag="mainContent">
    <ol class="breadcrumb">
        <li><g:link mapping="politicianCampaigns"><g:message code="head.logged.account.tools.massMailing"/></g:link></li>
        <li><g:link mapping="politicianCampaignsNew"><g:message code="tools.campaign.new.title"/></g:link></li>
        <li><g:link mapping="participatoryBudgetShow" params="${participatoryBudget.encodeAsLinkProperties()}">${participatoryBudget.title}</g:link></li>
        <li class="active">${breadCrumbName}</li>
    </ol>

    <g:set var="mappings" value="${
        [
                saveAndSentButtons:false,
                step:'district',
                settings:'districtProposalCreateSettings',
                content:'districtProposalCreateContent',
                district:'districtProposalCreate',
                showResult: 'campaignShow',
                next: 'districtProposalCreateContent'
        ]}"/>

    <g:render template="/districtProposal/editModules/editDistrict" model="[
            command: command,
            campaign:campaign,
            participatoryBudget:participatoryBudget,
            numberRecipients:numberRecipients,
            mappings:mappings
    ]"/>

    <g:render template="/newsletter/timeZoneSelectorPopUp"/>
</content>