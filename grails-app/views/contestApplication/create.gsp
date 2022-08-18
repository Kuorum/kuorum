<%@ page import="org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <g:set var="breadCrumbName">
        <g:message code="admin.contestApplication.title"/>
    </g:set>

    <title>${breadCrumbName}</title>
    <meta name="layout" content="basicPlainLayout">

</head>

<content tag="mainContent">
    <ol class="breadcrumb">
        <li><g:link mapping="politicianCampaigns"><g:message
                code="head.logged.account.tools.massMailing"/></g:link></li>
        <li><g:link mapping="politicianCampaignsNew"><g:message code="tools.campaign.new.title"/></g:link></li>
        <li><g:link mapping="contestShow" params="${contest.encodeAsLinkProperties()}">${contest.title}</g:link></li>
        <li class="active">${breadCrumbName}</li>
    </ol>

    <g:set var="mappings" value="${
        [
        saveAndSentButtons: false,
        step: 'environment' ,
        content: 'contestApplicationEditContent',
        environment: 'contestApplicationCreate' ,
        showResult: 'campaignShow',
        next: 'contestApplicationEditContent',
        hideDraftButton: true
        ]}"/>

    <g:render template="/contestApplication/editModules/editContestApplicationEnvironment" model="[
            command : command,
            contest : contest,
            mappings: mappings
    ]"/>

    <g:render template="/newsletter/timeZoneSelectorPopUp"/>
</content>